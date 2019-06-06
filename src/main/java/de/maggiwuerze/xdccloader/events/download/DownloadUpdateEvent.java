package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;
import de.maggiwuerze.xdccloader.model.Download;
import org.springframework.context.ApplicationEvent;

public class DownloadUpdateEvent extends EntityUpdateEvent<Download> {

    public DownloadUpdateEvent(Object source, Download download) {
        super(source, download);
    }

}