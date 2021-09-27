package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityDeleteEvent;

public class DownloadDeleteEvent extends EntityDeleteEvent<Long> {

	public DownloadDeleteEvent(Object source, Long downloadId) {
		super(source, downloadId);
	}
}