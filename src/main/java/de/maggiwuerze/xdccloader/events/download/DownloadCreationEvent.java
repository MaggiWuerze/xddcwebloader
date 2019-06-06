package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityCreationEvent;
import de.maggiwuerze.xdccloader.model.Download;

public class DownloadCreationEvent extends EntityCreationEvent<Download> {

    public DownloadCreationEvent(Object source, Download download) {
        super(source, download);
    }

}