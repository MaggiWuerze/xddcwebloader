package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.service.UserService;
import java.security.Principal;
import javax.servlet.http.HttpSession;
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

	private final UserService userService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

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

}