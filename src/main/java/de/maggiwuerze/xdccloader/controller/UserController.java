package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.model.forms.UserForm;
import de.maggiwuerze.xdccloader.model.forms.UserSettingsForm;
import de.maggiwuerze.xdccloader.model.transport.UserTO;
import de.maggiwuerze.xdccloader.security.UserRole;
import de.maggiwuerze.xdccloader.service.UserService;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
@RequiredArgsConstructor
class UserController {

	private final UserService userService;

	@GetMapping("/register")
	public ModelAndView register(ModelMap model) {
		model.addAttribute("user", new UserForm());
		return new ModelAndView("register", model);
	}

	@GetMapping("/login")
	public String login(Model model) {
		if (userService.getUserCount() <= 0) {
			model.addAttribute("user", new UserForm());
			model.addAttribute("noLogin", true);
			return "register";
		}
		return "login";
	}

	@PostMapping(value = "/register")
	public ModelAndView registerUserAccount(
		@ModelAttribute("user") @Valid UserForm userForm,
		BindingResult result, HttpServletRequest request, Errors errors, ModelMap model) {
		model.addAttribute("user", userForm);

		if (errors.hasErrors()) {
			return new ModelAndView("register", model);
		} else if (userService.usernameExists(userForm.getUsername())) {
			result.rejectValue("username", "username already exists");
			return new ModelAndView("register", model);
		} else {
			String username = userForm.getUsername();
			String password = new BCryptPasswordEncoder(11).encode(userForm.getPassword());
			UserSettings userSettings = userService.saveUserSettings(new UserSettings());
			userService.saveUser(new User(username, password, UserRole.USER.getExternalString(), true, userSettings));

			try {
				request.login(username, userForm.getPassword());
			} catch (ServletException e) {
				log.error(e.getLocalizedMessage(), e);
			}

			return new ModelAndView("redirect:/", model);
		}
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUser(Principal principal) {
		User user = userService.findUserByName(principal.getName());

		if (user != null) {
			UserTO userTO = new UserTO(user);
			return new ResponseEntity<>(userTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("user not found", HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping(value = "/usersettings")
	public ResponseEntity<?> updateUserSettings(
		@RequestBody UserSettingsForm userSettingsForm, Principal principal) {
		User user = userService.findUserByName(principal.getName());
		UserSettings userSettingsById = user.getUserSettings();
		userSettingsById.setDownloadSortBy(userSettingsForm.getDownloadSortBy());
		userSettingsById.setSessionTimeout(userSettingsForm.getSessionTimeout());
		userService.saveUserSettings(userSettingsById);
		user.setUserSettings(userSettingsById);
		userService.saveUser(user);

		return new ResponseEntity<>("UserSettings updated successfully.", HttpStatus.OK);
	}
}