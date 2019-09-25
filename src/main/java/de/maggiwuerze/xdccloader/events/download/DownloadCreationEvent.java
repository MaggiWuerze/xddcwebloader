package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;

public class DownloadCreationEvent extends EntityCreationEvent<Long> {

    public DownloadCreationEvent(Object source, Long downloadId) {
        super(source, downloadId);
    }

}