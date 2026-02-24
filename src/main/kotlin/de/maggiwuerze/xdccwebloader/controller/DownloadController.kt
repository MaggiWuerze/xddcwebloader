package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.events.SocketEvents.CANCELLED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.SocketEvents.DELETED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.forms.DownloadFormTO
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.EventService
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/download/")
class DownloadController(
    val downloadService: DownloadService,
    val eventService: EventService
) {

    @GetMapping("{id}")
    @Schema(name = "Get Download", description = "Returns a single download")
    fun getDownload(@PathVariable id: UUID): ResponseEntity<DownloadTO> = downloadService.getById(id)?.let {
        ResponseEntity(
            it.toTO(), HttpStatus.OK
        )
    } ?: ResponseEntity(HttpStatus.NOT_FOUND)


    @GetMapping
    @Schema(name = "Get Downloads", description = "Returns a list of all downloads")
    fun listDownloads(): ResponseEntity<List<DownloadTO>> = ResponseEntity(
        downloadService.findAllByOrderByProgressDesc().map { it.toTO() },
        HttpStatus.OK
    )

    /**
     * @return a list of downloads. if active, then it return all that are still working. if not it returns all that have stopped, this includes errors
     */
    @GetMapping("active/")
    @Schema(name = "Get Active Downloads", description = "Returns a list of all active downloads")
    fun getActiveDownloads(active: Boolean): ResponseEntity<List<DownloadTO>> {
        return ResponseEntity(
            when (active) {
                true -> downloadService.findAllActive()
                false -> downloadService.findAllInactive()
            }, HttpStatus.OK
        )
    }

    @GetMapping("failed")
    fun failedDownloads(): ResponseEntity<List<DownloadTO>> {
        downloadService.findAllByStatusOrderByProgressDesc(DownloadState.ERROR).map { it.toTO() }.let {
            return ResponseEntity(it, HttpStatus.OK)
        }
    }

    @DeleteMapping("{id}")
    fun removeDownload(@PathVariable id: UUID): ResponseEntity<*> {
        return downloadService.getById(id)?.let { download ->
            download.progressWatcher?.cancel(true)
            download.status = DownloadState.STOPPED
            eventService.publishEvent(DELETED_DOWNLOAD, download)

            ResponseEntity("Download marked for deletion", HttpStatus.OK)
        } ?: ResponseEntity("Download not found", HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun addDownload(@RequestBody downloadFormTO: DownloadFormTO): ResponseEntity<List<DownloadTO>> {
        return ResponseEntity(downloadService.create(downloadFormTO).map(Download::toTO), HttpStatus.OK)
    }

    @PutMapping("{id}")
    fun cancelDownload(@PathVariable id: UUID): ResponseEntity<*> {
        return downloadService.getById(id)?.let { download ->
            download.progressWatcher?.cancel(true)
            download.status = DownloadState.STOPPED
            eventService.publishEvent(CANCELLED_DOWNLOAD, download)

            ResponseEntity("Download marked for deletion", HttpStatus.OK)
        } ?: ResponseEntity("Download not found", HttpStatus.NOT_FOUND)
    }
}