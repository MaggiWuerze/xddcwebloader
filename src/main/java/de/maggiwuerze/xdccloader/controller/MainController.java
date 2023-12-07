package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j
@RequiredArgsConstructor
class MainController {

	private final UserSettingsService userSettingsService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	//USERDETAILS
	@GetMapping("/initialized")
	public ResponseEntity<Boolean> getInitialized() {

		if (userSettingsService.getUserSettings().getInitialized()) {
			return new ResponseEntity(true, HttpStatus.OK);
		} else {
			return new ResponseEntity(false, HttpStatus.OK);
		}
	}

	@PostMapping("/initialized/")
	public ResponseEntity<?> setInitialized() {

		userSettingsService.setInitialized(true);

		return new ResponseEntity("ok", HttpStatus.OK);
	}

}