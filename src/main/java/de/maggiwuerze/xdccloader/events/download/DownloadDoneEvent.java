package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;
import de.maggiwuerze.xdccloader.model.Download;

public class DownloadDoneEvent extends EntityDoneEvent<Download>{

    public DownloadDoneEvent(Object source, Download download) {
        super(source, download);
    }

}