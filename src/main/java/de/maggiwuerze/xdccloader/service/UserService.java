package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import de.maggiwuerze.xdccloader.model.transport.UserTO;
import de.maggiwuerze.xdccloader.persistency.IrcUserRepository;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import de.maggiwuerze.xdccloader.persistency.UserSettingsRepository;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final IrcUserRepository ircUserRepository;
	private final UserSettingsRepository userSettingsRepository;

	@Getter
	private UserTO currentUser;

	public long getUserCount() {
		return userRepository.count();
	}

	public boolean usernameExists(String username) {
		return userRepository.findUserByName(username).isPresent();
	}

	public UserSettings findUserSettingsById(Long id) {
		return userSettingsRepository.findById(id).orElse(null);
	}
	public UserSettings saveUserSettings(UserSettings userSettings) {
		return userSettingsRepository.save(new UserSettings());
	}

	public void saveUser(User user) {
		this.userRepository.save(user);
	}

	public User findUserByName(String username) {
		return userRepository.findUserByName(username).orElse(null);
	}

	public List<Bot> listIrcUsers() {
		return ircUserRepository.findAll();
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = new UserTO(findUserByName(currentUser));
	}
}
