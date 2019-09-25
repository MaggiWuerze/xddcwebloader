package de.maggiwuerze.xdccloader.model.entity;

import de.maggiwuerze.xdccloader.model.DownloadSort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(nullable = false)
    LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    Long refreshrateInSeconds = 1l;

    @Column(nullable = false)
    Long sessionTimeout = sessionTimeout = 300l;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    DownloadSort downloadSortBy = DownloadSort.PROGRESS;

    @Column(nullable = false)
    Boolean showAllBotsInQuickWindow = false;

    @ElementCollection
    @CollectionTable(name = "BotsVisibleInQuickWindow", joinColumns = @JoinColumn(name = "usersettings_id"))
    @Column(name = "BotsVisibleInQuickWindow")
    Map<Bot, Boolean> botsVisibleInQuickWindow = new HashMap<>();

    @Column(nullable = false)
    Boolean showAllItemsInDownloadCard = true;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(name = "MAP")
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, Boolean> itemsVisibleInDownloadCard;

    public UserSettings(Long refreshrateInSeconds, Long sessionTimeout) {
        this.refreshrateInSeconds = refreshrateInSeconds;
        this.sessionTimeout = sessionTimeout;
    }

    public UserSettings() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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
