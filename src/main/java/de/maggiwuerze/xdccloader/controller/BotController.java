package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.service.BotService;
import de.maggiwuerze.xdccloader.service.ChannelService;
import de.maggiwuerze.xdccloader.service.EventService;
import de.maggiwuerze.xdccloader.service.ServerService;
import de.maggiwuerze.xdccloader.service.UserService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
class BotController {

	private final UserService userService;
	private final ChannelService channelService;
	private final BotService botService;
	private final ServerService serverService;
	private final EventService eventService;

	@PostMapping("/bots/")
	public ResponseEntity<?> addBot(@RequestBody TargetBotForm form, Principal principal) {
		eventService.publishEvent(SocketEvents.NEW_SERVER, botService.save(principal, form));
		return new ResponseEntity("Bot added succcessfully", HttpStatus.OK);
	}

	/**
	 * @return a list of all bots
	 */
	@GetMapping("/bots/")
	public ResponseEntity<List<Bot>> getAllBots(Principal principal) {
		return new ResponseEntity(userService.getBots(principal), HttpStatus.OK);
	}

	/**
	 * @return a list of all users
	 */
	@GetMapping("/ircUsers/")
	public ResponseEntity<List<Bot>> getAllIrcUsers() {
		return new ResponseEntity(userService.listIrcUsers(), HttpStatus.OK);
	}
}