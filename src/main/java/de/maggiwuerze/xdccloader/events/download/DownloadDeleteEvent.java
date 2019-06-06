package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.model.TargetBot;

public class DownloadDeleteEvent extends EntityDeleteEvent<Download> {

    public DownloadDeleteEvent(Object source, Download download) {
        super(source, download);
    }

}