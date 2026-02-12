package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.events.SocketEvents.DELETED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.model.forms.DownloadForm
import de.maggiwuerze.xdccwebloader.model.transport.DownloadTO
import de.maggiwuerze.xdccwebloader.service.BotService
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
internal class DownloadController(
    val downloadService: DownloadService,
    val botService: BotService,
    val eventService: EventService
) {

    @get:GetMapping("/downloads/")
    val allDownloads: ResponseEntity<List<DownloadTO>>
        /**
         * @return a list of all downloads
         */
        get() = ResponseEntity(
            DownloadTO.getListOfTOs(downloadService.findAllByOrderByProgressDesc()),
            HttpStatus.OK
        )

    /**
     * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
     */
    @GetMapping("/downloads/active/")
    fun getActiveDownloads(active: Boolean): ResponseEntity<List<Any>> {
        val states: List<DownloadState> = if (!active) {
            listOf(DownloadState.UNKNOWN, DownloadState.DONE)
        } else {
            listOf(
                DownloadState.PREPARING,
                DownloadState.PREPARED,
                DownloadState.READY,
                DownloadState.CONNECTING,
                DownloadState.TRANSMITTING,
                DownloadState.FINALIZING
            )
        }

        return ResponseEntity(
            DownloadTO.getListOfTOs(downloadService.findAllByStatusInOrderByProgress(states)),
            HttpStatus.OK
        )
    }

    @get:GetMapping("/downloads/failed")
    val failedDownloads: ResponseEntity<List<Any>>
        get() {
            val failedDownloads: List<DownloadTO> =
                DownloadTO.getListOfTOs(downloadService.findAllByStatusOrderByProgressDesc(DownloadState.ERROR))

            return ResponseEntity(failedDownloads, HttpStatus.OK)
        }

    @GetMapping("/downloads/remove")
    fun removeDownloads(downloadId: UUID): ResponseEntity<*> {
        val download: Download? = downloadService.getById(downloadId)

        if (download != null) {
            download.status = DownloadState.STOPPED
            download.progressWatcher?.cancel(true)
            eventService.publishEvent(DELETED_DOWNLOAD, download)

            return ResponseEntity("Download marked for deletion", HttpStatus.OK)
        }

        return ResponseEntity("Illegal Arguments in Request", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("/downloads/")
    fun addDownload(@RequestBody downloadForm: DownloadForm): ResponseEntity<*> {
        val bot: Bot = botService.findById(downloadForm.targetBotId) ?: return ResponseEntity(
            "Illegal Arguments in Request",
            HttpStatus.BAD_REQUEST
        )

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
}