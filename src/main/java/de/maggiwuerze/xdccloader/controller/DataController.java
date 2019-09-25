package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.channel.server.ChannelCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.events.server.ServerCreationEvent;
import de.maggiwuerze.xdccloader.model.entity.*;
import de.maggiwuerze.xdccloader.model.forms.ChannelForm;
import de.maggiwuerze.xdccloader.model.forms.DownloadForm;
import de.maggiwuerze.xdccloader.model.forms.ServerForm;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.transport.DownloadTO;
import de.maggiwuerze.xdccloader.model.transport.UserTO;
import de.maggiwuerze.xdccloader.persistency.*;
import de.maggiwuerze.xdccloader.util.DownloadManager;
import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.model.DownloadState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;


@RestController
@RequestMapping("/data")
class DataController {


    Logger logger = Logger.getLogger("Class DataController");

    @Autowired
    UserRepository userRepository;
    @Autowired
    IrcUserRepository ircUserRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    TargetBotRepository targetBotRepository;

    DownloadManager downloadManager = DownloadManager.getInstance();

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final String MESSAGE_PREFIX = "/topic";

    //USERDETAILS
    @GetMapping("/initialized")
    public ResponseEntity<Boolean> getInitialized(Principal principal) {

        String username = principal.getName();
        User user = userRepository.findUserByName(username).orElse(null);


        if (user != null) {

            return new ResponseEntity(user.getInitialized(), HttpStatus.OK);

        } else {

            return new ResponseEntity("user not found", HttpStatus.UNAUTHORIZED);

        }

    }

    @GetMapping("/user")
    public ResponseEntity<UserTO> getuser(Principal principal) {

        User user = userRepository.findUserByName(principal.getName()).orElse(null);

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


        User user = userRepository.findUserByName(principal.getName()).orElse(null);

        if (user != null) {

            user.setInitialized(true);
            userRepository.save(user);

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

        return new ResponseEntity(DownloadTO.getListOfTOs(downloadManager.findAllByOrderByProgressDesc()), HttpStatus.OK);

    }

    /**
     * @param active
     * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
     */
    @GetMapping("/downloads/active/")
    public ResponseEntity<List<Object>> getActiveDownloads(boolean active) {

        List<DownloadState> states;
        if (!active) {

            states = Arrays.asList(DownloadState.UNKNOWN, DownloadState.DONE);

        } else {

            states = Arrays.asList(DownloadState.PREPARING, DownloadState.PREPARED, DownloadState.READY, DownloadState.CONNECTING, DownloadState.TRANSMITTING, DownloadState.FINALIZING);

        }

        return new ResponseEntity(DownloadTO.getListOfTOs(downloadManager.findAllByStatusInOrderByProgress(states)), HttpStatus.OK);

    }

    @GetMapping("/downloads/failed")
    public ResponseEntity<List<Object>> getFailedDownloads() {

        List<DownloadTO> failedDownloads = DownloadTO.getListOfTOs(downloadManager.findAllByStatusOrderByProgressDesc(DownloadState.ERROR));

        return new ResponseEntity(failedDownloads, HttpStatus.OK);

    }

    @GetMapping("/downloads/remove")
    public ResponseEntity<?> removeDownloads(Long downloadId) {

        Download download = downloadManager.getById(downloadId);

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

        Bot bot = targetBotRepository.findById(downloadForm.getTargetBotId()).orElse(null);

        if (bot == null) {

            return new ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST);

        }

        String fileRefId = downloadForm.getFileRefId();
        if (fileRefId.contains(",")) {

            for (String id : fileRefId.split(",")) {

                Download download = new Download(bot, id);
                downloadManager.addDownloadToBotQueue(download);
                publishEvent(SocketEvents.NEW_DOWNLOAD, download);

            }


        } else {

            Download download = new Download(bot, fileRefId);
            downloadManager.addDownloadToBotQueue(download);
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

        List<Server> servers = serverRepository.findAll();
        return new ResponseEntity(servers, HttpStatus.OK);

    }

    @PostMapping("/servers/")
    public ResponseEntity<?> addServer(@RequestBody ServerForm serverForm) {

        Server server = serverRepository.save(new Server(serverForm.getName(), serverForm.getServerUrl()));

        return new ResponseEntity("Download added succcessfully. id=[" + server.getId() + "]", HttpStatus.OK);
    }


    //CHANNELS

    /**
     * @return a list of all channels
     */
    @GetMapping("/channels/")
    public ResponseEntity<List<Channel>> getAllChannels() {

        List<Channel> channels = channelRepository.findAll();
        return new ResponseEntity(channels, HttpStatus.OK);

    }

    @PostMapping("/channels/")
    public ResponseEntity<?> addChannel(@RequestBody ChannelForm channelForm) {

        Channel channel = channelRepository.save(new Channel(channelForm.getName()));

        return new ResponseEntity("Download added succcessfully. id=[" + channel.getId() + "]", HttpStatus.OK);
    }


    //BOTS

    @PostMapping("/bots/")
    public ResponseEntity<?> addBot(@RequestBody TargetBotForm targetBotForm) {


        Optional<Server> server = serverRepository.findById(targetBotForm.getServerId());
        Optional<Channel> channel = channelRepository.findById(targetBotForm.getChannelId());
        Bot bot = targetBotRepository.save(new Bot(server.get(), channel.get(), targetBotForm.getName(), targetBotForm.getPattern()));

        publishEvent(SocketEvents.NEW_SERVER, bot);

        return new ResponseEntity("Bot added succcessfully", HttpStatus.OK);
    }

    /**
     * @return a list of all bots
     */
    @GetMapping("/bots/")
    public ResponseEntity<List<Bot>> getAllBots() {

        List<Bot> users = targetBotRepository.findAll();
        return new ResponseEntity(users, HttpStatus.OK);

    }

    //IRC_USERS

    /**
     * @return a list of all users
     */
    @GetMapping("/ircUsers/")
    public ResponseEntity<List<Bot>> getAllIrcUsers() {

        List<Bot> users = ircUserRepository.findAll();
        return new ResponseEntity(users, HttpStatus.OK);

    }

    //UTIL

    /**
     * @param destinationSuffix
     * @param payload
     */
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

}