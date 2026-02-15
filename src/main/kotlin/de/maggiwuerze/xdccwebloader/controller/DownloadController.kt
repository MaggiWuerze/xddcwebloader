package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.events.SocketEvents.DELETED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.forms.DownloadForm
import de.maggiwuerze.xdccwebloader.service.BotService
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.EventService
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class DownloadController(
    val downloadService: DownloadService,
    val botService: BotService,
    val eventService: EventService
) {

    @GetMapping("/downloads/")
    @Schema(name = "Get Downloads", description = "Returns a list of all downloads")
    fun listDownloads(): ResponseEntity<List<DownloadTO>> = ResponseEntity(
        downloadService.findAllByOrderByProgressDesc().map { it.toTO() },
        HttpStatus.OK
    )

    /**
     * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
     */
    @GetMapping("/downloads/active/")
    @Schema(name = "Get Active Downloads", description = "Returns a list of all active downloads")
    fun getActiveDownloads(active: Boolean): ResponseEntity<List<DownloadTO>> {
        return ResponseEntity(
            when (active) {
                true -> downloadService.findAllActive()
                false -> downloadService.findAllInactive()
            }, HttpStatus.OK
        )
    }

    @GetMapping("/downloads/failed")
    fun failedDownloads(): ResponseEntity<List<DownloadTO>> {
        downloadService.findAllByStatusOrderByProgressDesc(DownloadState.ERROR).map { it.toTO() }.let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }

    @GetMapping("/downloads/remove")
    fun removeDownloads(downloadId: UUID): ResponseEntity<*> {
        downloadService.getById(downloadId).let { download ->
            download.status = DownloadState.STOPPED
            download.progressWatcher?.cancel(true)
            eventService.publishEvent(DELETED_DOWNLOAD, download)

            return ResponseEntity("Download marked for deletion", HttpStatus.OK)
        }
    }

    @PostMapping("/downloads/")
    fun addDownload(@RequestBody downloadForm: DownloadForm): ResponseEntity<*> {
        botService.findById(downloadForm.targetBotId)?.let { bot ->
            val fileRefId: String = downloadForm.fileRefId
            if (fileRefId.contains(",")) {
                for (id in fileRefId.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    val download: Download = Download(bot, id)
                    downloadService.addDownloadToBotQueue(download)
                    eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download)
                }
            } else {
                val download: Download = Download(bot, fileRefId)
                downloadService.addDownloadToBotQueue(download)
                eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download)
            }

            return ResponseEntity("Download(s) added succcessfully.", HttpStatus.OK)
        }

        return ResponseEntity(
            "Illegal Arguments in Request",
            HttpStatus.BAD_REQUEST
        )
    }
}