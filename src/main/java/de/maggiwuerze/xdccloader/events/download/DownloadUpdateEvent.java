package de.maggiwuerze.xdccloader.events.download;

import de.maggiwuerze.xdccloader.events.EntityUpdateEvent;

public class DownloadUpdateEvent extends EntityUpdateEvent<Long> {

	public DownloadUpdateEvent(Object source, Long downloadId) {
		super(source, downloadId);
	}
}