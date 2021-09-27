package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityDoneEvent;

public class DownloadDoneEvent extends EntityDoneEvent<Long> {

	public DownloadDoneEvent(Object source, Long downloadId) {
		super(source, downloadId);
	}
}