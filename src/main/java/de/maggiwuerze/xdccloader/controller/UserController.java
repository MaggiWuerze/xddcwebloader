package de.maggiwuerze.xdccloader.controller;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.model.forms.UserSettingsForm;
import de.maggiwuerze.xdccloader.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@Slf4j
@RequiredArgsConstructor
class UserController {

	private final UserSettingsService userSettingsService;

	@PostMapping(value = "/usersettings")
	public ResponseEntity<?> updateUserSettings(
		@RequestBody UserSettingsForm userSettingsForm) {
		UserSettings userSettingsById = userSettingsService.getUserSettings();
		userSettingsById.setDownloadSortBy(userSettingsForm.getDownloadSortBy());
		userSettingsById.setSessionTimeout(userSettingsForm.getSessionTimeout());
		userSettingsService.saveUserSettings(userSettingsById);

		return new ResponseEntity<>("UserSettings updated successfully.", HttpStatus.OK);
	}
}