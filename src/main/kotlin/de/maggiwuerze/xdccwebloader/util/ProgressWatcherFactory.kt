package de.maggiwuerze.xdccwebloader.util

import de.maggiwuerze.xdccwebloader.service.DownloadService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProgressWatcherFactory(
    private val downloadService: DownloadService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun getProgressWatcher(downloadId: UUID): FileTransferProgressWatcher {
        val watcher: FileTransferProgressWatcher =
            FileTransferProgressWatcher(downloadId, applicationEventPublisher, downloadService)
        return watcher
    }
}
