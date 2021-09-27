package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.download.DownloadSort;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;

@Data
public class UserSettingsTO {

    Long refreshrateInSeconds = 1l;

    Long sessionTimeout = sessionTimeout = 300l;

    DownloadSort downloadSortBy = DownloadSort.PROGRESS;

    Boolean showAllBotsInQuickWindow = false;

    Map<Bot, Boolean> botsVisibleInQuickWindow = new HashMap<>();

    Boolean showAllItemsInDownloadCard = true;

    private Map<String, Boolean> itemsVisibleInDownloadCard;

    public UserSettingsTO(Long refreshrateInSeconds, Long sessionTimeout) {
        this.refreshrateInSeconds = refreshrateInSeconds;
        this.sessionTimeout = sessionTimeout;
    }

    public UserSettingsTO(UserSettings userSettings) {
        BeanUtils.copyProperties(userSettings, this);
    }
}
