package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.download.DownloadCardField;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.User;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserTO {

	String name;

	String userRole;

	Boolean initialized = false;

	UserSettingsTO userSettings;

	List<Bot> bots;

	public UserTO(String name, String password, String userRole, boolean initialized, UserSettingsTO userSettings) {
		this.name = name;
		this.userRole = userRole;
		this.initialized = initialized;
		this.userSettings = userSettings;
	}

	public UserTO(User user) {

		BeanUtils.copyProperties(user, this);
		userSettings = new UserSettingsTO(user.getUserSettings());

	}
}
