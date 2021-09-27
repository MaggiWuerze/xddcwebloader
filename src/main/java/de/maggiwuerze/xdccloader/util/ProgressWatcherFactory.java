package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.service.DownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressWatcherFactory {

	private final DownloadService downloadService;
	private final ApplicationEventPublisher applicationEventPublisher;

	public FileTransferProgressWatcher getProgressWatcher(Long downloadId) {
		FileTransferProgressWatcher watcher = new FileTransferProgressWatcher(downloadId, applicationEventPublisher, downloadService);
		return watcher;
	}
}
