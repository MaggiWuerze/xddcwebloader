package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserSettingsTO {

	Long refreshrateInSeconds = 1L;
	DownloadSort downloadSortBy = DownloadSort.PROGRESS;
	Long sessionTimeout = 300L;
	Boolean showAllBotsInQuickWindow = false;
	Map<Bot, Boolean> botsVisibleInQuickWindow = new HashMap<>();
	Boolean showAllItemsInDownloadCard = true;
	String downloadPath = "";
	private Map<String, Boolean> itemsVisibleInDownloadCard;

	public UserSettingsTO(Long refreshrateInSeconds, Long sessionTimeout) {
		this.refreshrateInSeconds = refreshrateInSeconds;
		this.sessionTimeout = sessionTimeout;
	}

	public UserSettingsTO(UserSettings userSettings) {
		BeanUtils.copyProperties(userSettings, this);
	}


}
