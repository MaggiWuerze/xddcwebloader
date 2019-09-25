package de.maggiwuerze.xdccloader.model.transport;

import de.maggiwuerze.xdccloader.model.DownloadSort;
import de.maggiwuerze.xdccloader.model.entity.Bot;
import de.maggiwuerze.xdccloader.model.entity.UserSettings;
import org.springframework.beans.BeanUtils;

import java.util.*;

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

    public Long getRefreshrateInSeconds() {
        return refreshrateInSeconds;
    }

    public void setRefreshrateInSeconds(Long refreshrateInSeconds) {
        this.refreshrateInSeconds = refreshrateInSeconds;
    }

    public DownloadSort getDownloadSortBy() {
        return downloadSortBy;
    }

    public void setDownloadSortBy(DownloadSort downloadSortBy) {
        this.downloadSortBy = downloadSortBy;
    }

    public Long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Boolean getShowAllBotsInQuickWindow() {
        return showAllBotsInQuickWindow;
    }

    public void setShowAllBotsInQuickWindow(Boolean showAllBotsInQuickWindow) {
        this.showAllBotsInQuickWindow = showAllBotsInQuickWindow;
    }

    public Map<Bot, Boolean> getBotsVisibleInQuickWindow() {
        return botsVisibleInQuickWindow;
    }

    public void setBotsVisibleInQuickWindow(Map<Bot, Boolean> botsVisibleInQuickWindow) {
        this.botsVisibleInQuickWindow = botsVisibleInQuickWindow;
    }

    public Boolean getShowAllItemsInDownloadCard() {
        return showAllItemsInDownloadCard;
    }

    public void setShowAllItemsInDownloadCard(Boolean showAllItemsInDownloadCard) {
        this.showAllItemsInDownloadCard = showAllItemsInDownloadCard;
    }

    public Map<String, Boolean> getItemsVisibleInDownloadCard() {
        return itemsVisibleInDownloadCard;
    }

    public void setItemsVisibleInDownloadCard(Map<String, Boolean> itemsVisibleInDownloadCard) {
        this.itemsVisibleInDownloadCard = itemsVisibleInDownloadCard;
    }
}
