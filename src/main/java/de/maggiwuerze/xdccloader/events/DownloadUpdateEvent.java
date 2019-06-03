package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.model.Download;
import org.springframework.context.ApplicationEvent;

public class DownloadUpdateEvent extends ApplicationEvent {
    private Download download;

    public DownloadUpdateEvent(Object source, Download download) {
        super(source);
        this.download = download;
    }

    public Download getDownload() {
        return download;
    }
}