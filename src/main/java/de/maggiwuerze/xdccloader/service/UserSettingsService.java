package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.persistence.IrcUserRepository;
import de.maggiwuerze.xdccloader.persistence.SettingsRepository;
import de.maggiwuerze.xdccloader.persistence.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSettingsService {

	private final SettingsRepository settingsRepository;
	private final IrcUserRepository ircUserRepository;
	private final UserSettingsRepository userSettingsRepository;

	public UserSettings getUserSettings() {
		return settingsRepository.getFirstById(1L);
	}

	public void saveUserSettings(UserSettings userSettingsBy) {
		settingsRepository.save(userSettingsBy);
	}

	public void setInitialized(boolean b) {
		UserSettings userSettings = getUserSettings();
		userSettings.setInitialized(true);
		saveUserSettings(userSettings);
	}
}
