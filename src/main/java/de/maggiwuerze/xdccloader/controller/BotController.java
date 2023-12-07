package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.forms.TargetBotForm;
import de.maggiwuerze.xdccloader.service.BotService;
import de.maggiwuerze.xdccloader.service.ChannelService;
import de.maggiwuerze.xdccloader.service.EventService;
import de.maggiwuerze.xdccloader.service.ServerService;
import de.maggiwuerze.xdccloader.service.UserSettingsService;
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

	private final UserSettingsService userSettingsService;
	private final ChannelService channelService;
	private final BotService botService;
	private final ServerService serverService;
	private final EventService eventService;

	@PostMapping("/bots/")
	public ResponseEntity<?> addBot(@RequestBody TargetBotForm form, Principal principal) {
		eventService.publishEvent(SocketEvents.NEW_SERVER, botService.save(form));
		return new ResponseEntity("Bot added succcessfully", HttpStatus.OK);
	}

	/**
	 * @return a list of all bots
	 */
	@GetMapping("/bots/")
	public ResponseEntity<List<Bot>> getAllBots(Principal principal) {
		return new ResponseEntity(botService.list(), HttpStatus.OK);
	}

}