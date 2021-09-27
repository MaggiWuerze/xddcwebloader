package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.events.channel.server.ChannelCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.events.server.ServerCreationEvent;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.forms.ChannelForm;
import de.maggiwuerze.xdccloader.model.forms.DownloadForm;
import de.maggiwuerze.xdccloader.model.forms.ServerForm;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.model.transport.DownloadTO;
import de.maggiwuerze.xdccloader.model.transport.UserTO;
import de.maggiwuerze.xdccloader.service.BotService;
import de.maggiwuerze.xdccloader.service.ChannelService;
import de.maggiwuerze.xdccloader.service.DownloadService;
import de.maggiwuerze.xdccloader.service.ServerService;
import de.maggiwuerze.xdccloader.service.UserService;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
class DataController {

	private static final String MESSAGE_PREFIX = "/topic";
	private final UserService userService;
	private final ChannelService channelService;
	private final ServerService serverService;
	private final DownloadService downloadService;
	private final BotService botService;
	private final SimpMessagingTemplate websocket;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final EntityLinks entityLinks;

	//USERDETAILS
	@GetMapping("/initialized")
	public ResponseEntity<Boolean> getInitialized(Principal principal) {
		User user = userService.findUserByName(principal.getName());

		if (user != null) {
			return new ResponseEntity(user.getInitialized(), HttpStatus.OK);
		} else {
			return new ResponseEntity("user not found", HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping("/user")
	public ResponseEntity<UserTO> getuser(Principal principal) {
		User user = userService.findUserByName(principal.getName());

		if (user != null) {

			UserTO userTO = new UserTO(user);
			List<Bot> bots = getAllBots().getBody();
			if (user.getUserSettings().getBotsVisibleInQuickWindow().size() != bots.size()) {

				Map<Bot, Boolean> botMap = new HashMap<>();
				bots.stream().forEach(b -> botMap.put(b, false));
				userTO.getUserSettings().getBotsVisibleInQuickWindow().putAll(botMap);
			}

			return new ResponseEntity(userTO, HttpStatus.OK);
		} else {

			return new ResponseEntity("user not found", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/initialized/")
	public ResponseEntity<?> setInitialized(Principal principal) {
		User user = userService.findUserByName(principal.getName());

		if (user != null) {

			user.setInitialized(true);
			userService.saveUser(user);

			return new ResponseEntity("ok", HttpStatus.OK);
		} else {

			return new ResponseEntity("user not found", HttpStatus.NOT_FOUND);
		}
	}

	//DOWNLOADS

	/**
	 * @return a list of all downloads
	 */
	@GetMapping("/downloads/")
	public ResponseEntity<List<Object>> getAllDownloads() {
		return new ResponseEntity(DownloadTO.getListOfTOs(downloadService.findAllByOrderByProgressDesc()), HttpStatus.OK);
	}

	/**
	 * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
	 */
	@GetMapping("/downloads/active/")
	public ResponseEntity<List<Object>> getActiveDownloads(boolean active) {
		List<DownloadState> states;
		if (!active) {

			states = Arrays.asList(DownloadState.UNKNOWN, DownloadState.DONE);
		} else {

			states = Arrays.asList(DownloadState.PREPARING,
				DownloadState.PREPARED,
				DownloadState.READY,
				DownloadState.CONNECTING,
				DownloadState.TRANSMITTING,
				DownloadState.FINALIZING
			);
		}

		return new ResponseEntity(DownloadTO.getListOfTOs(downloadService.findAllByStatusInOrderByProgress(states)), HttpStatus.OK);
	}

	@GetMapping("/downloads/failed")
	public ResponseEntity<List<Object>> getFailedDownloads() {
		List<DownloadTO> failedDownloads = DownloadTO.getListOfTOs(downloadService.findAllByStatusOrderByProgressDesc(DownloadState.ERROR));

		return new ResponseEntity(failedDownloads, HttpStatus.OK);
	}

	@GetMapping("/downloads/remove")
	public ResponseEntity<?> removeDownloads(Long downloadId) {
		Download download = downloadService.getById(downloadId);

		if (download != null) {

			download.setStatus(DownloadState.STOPPED);
			download.getProgressWatcher().cancel(true);
			publishEvent(SocketEvents.DELETED_DOWNLOAD, download);

			return new ResponseEntity("Download marked for deletion", HttpStatus.OK);
		}

		return new ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/downloads/")
	public ResponseEntity<?> addDownload(@RequestBody DownloadForm downloadForm) {
		Bot bot = botService.findById(downloadForm.getTargetBotId());

		if (bot == null) {
			return new ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST);
		}

		String fileRefId = downloadForm.getFileRefId();
		if (fileRefId.contains(",")) {

			for (String id : fileRefId.split(",")) {

				Download download = new Download(bot, id);
				downloadService.addDownloadToBotQueue(download);
				publishEvent(SocketEvents.NEW_DOWNLOAD, download);
			}
		} else {
			Download download = new Download(bot, fileRefId);
			downloadService.addDownloadToBotQueue(download);
			publishEvent(SocketEvents.NEW_DOWNLOAD, download);
		}

		return new ResponseEntity("Download(s) added succcessfully.", HttpStatus.OK);
	}


	//SERVERS

	/**
	 * @return a list of all servers
	 */
	@GetMapping("/servers/")
	public ResponseEntity<List<Server>> getAllServers() {
		List<Server> servers = serverService.list();
		return new ResponseEntity(servers, HttpStatus.OK);
	}

	@PostMapping("/servers/")
	public ResponseEntity<?> addServer(@RequestBody ServerForm serverForm) {
		Server server = serverService.save(new Server(serverForm.getName(), serverForm.getServerUrl()));
		return new ResponseEntity("Download added succcessfully. id=[" + server.getId() + "]", HttpStatus.OK);
	}


	//CHANNELS

	/**
	 * @return a list of all channels
	 */
	@GetMapping("/channels/")
	public ResponseEntity<List<Channel>> getAllChannels() {
		List<Channel> channels = channelService.list();
		return new ResponseEntity(channels, HttpStatus.OK);
	}

	@PostMapping("/channels/")
	public ResponseEntity<?> addChannel(@RequestBody ChannelForm channelForm) {
		Channel channel = channelService.save(new Channel(channelForm.getName()));
		return new ResponseEntity("Download added succcessfully. id=[" + channel.getId() + "]", HttpStatus.OK);
	}


	//BOTS

	@PostMapping("/bots/")
	public ResponseEntity<?> addBot(@RequestBody TargetBotForm form) {
		Server server = serverService.findById(form.getServerId());
		Channel channel = channelService.findById(form.getChannelId());
		Bot bot = botService.save(new Bot(server, channel, form.getName(), form.getPattern(), form.getMaxParallelDownloads()));

		publishEvent(SocketEvents.NEW_SERVER, bot);

		return new ResponseEntity("Bot added succcessfully", HttpStatus.OK);
	}

	/**
	 * @return a list of all bots
	 */
	@GetMapping("/bots/")
	public ResponseEntity<List<Bot>> getAllBots() {
		List<Bot> users = botService.list();
		return new ResponseEntity(users, HttpStatus.OK);
	}

	//IRC_USERS

	/**
	 * @return a list of all users
	 */
	@GetMapping("/ircUsers/")
	public ResponseEntity<List<Bot>> getAllIrcUsers() {
		List<Bot> users = userService.listIrcUsers();
		return new ResponseEntity(users, HttpStatus.OK);
	}

	//UTIL
	private void publishEvent(SocketEvents destinationSuffix, Object payload) {
		if (payload instanceof Download) {
			Long downloadId = ((Download) payload).getId();
			switch (destinationSuffix) {
				case UPDATED_DOWNLOAD:
					DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadUpdateEvent);
					break;

				case NEW_DOWNLOAD:
					DownloadCreationEvent downloadCreationEvent = new DownloadCreationEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadCreationEvent);
					break;

				case DELETED_DOWNLOAD:
					DownloadDeleteEvent downloadDeleteEvent = new DownloadDeleteEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadDeleteEvent);
					break;

				default:
			}
		}
		if (payload instanceof Server) {
			ServerCreationEvent downloadCreationEvent = new ServerCreationEvent(this, (Server) payload);
			applicationEventPublisher.publishEvent(downloadCreationEvent);
		}
		if (payload instanceof Channel) {
			ChannelCreationEvent downloadCreationEvent = new ChannelCreationEvent(this, (Channel) payload);
			applicationEventPublisher.publishEvent(downloadCreationEvent);
		}

		this.websocket.convertAndSend(
			MESSAGE_PREFIX + destinationSuffix.getRoute(), payload);
	}

	private String getPath(Download download) {
		return this.entityLinks.linkForItemResource(
			download.getClass(),
			download.getId()
		).toUri().getPath();
	}
}