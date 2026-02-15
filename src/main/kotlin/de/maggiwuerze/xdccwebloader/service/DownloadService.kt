package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import java.util.Collections.synchronizedMap

/**
 * Service for adding Downloads to the queue and retrieving them
 */
@Service
class DownloadService {
    private val downloads: MutableMap<UUID, Download> = synchronizedMap(HashMap())
    private val bots: MutableMap<Bot, List<Download>> = synchronizedMap(HashMap())
    private var downloadFolderReady = false
    val log = LoggerFactory.getLogger(this.javaClass.name)

    fun addDownloadToBotQueue(download: Download) {
        downloads[download.id] = download
    }

    fun getById(id: UUID): Download {
        return downloads[id] ?: throw IllegalArgumentException("Download with id $id does not exist.")
    }

    fun remove(id: UUID) {
        downloads.remove(id)
    }

    fun findAllByOrderByProgressDesc(): List<Download> {

        return downloads.values.sortedBy { it.progress }.toList()
    }

    fun findAllActive(): List<DownloadTO> {
        return listOf(DownloadState.UNKNOWN, DownloadState.DONE).let {
            findAllByStatusInOrderByProgress(it).map { it.toTO() }
        }
    }

    fun findAllInactive(): List<DownloadTO> {
        return listOf(
            DownloadState.PREPARING,
            DownloadState.PREPARED,
            DownloadState.READY,
            DownloadState.CONNECTING,
            DownloadState.TRANSMITTING,
            DownloadState.FINALIZING
        ).let {
            findAllByStatusInOrderByProgress(it).map { it.toTO() }

        }
    }


    fun findAllByStatusInOrderByProgress(states: List<DownloadState>): List<Download> {
        return downloads.values
            .filter { states.contains(it.status) }
            .sortedBy { it.progress }.toList()
    }

    fun findAllByStatusOrderByProgressDesc(state: DownloadState): List<Download> {
        return downloads.values
            .filter { state == it.status }
            .sortedBy { it.progress }.toList()
    }

    fun update(download: Download) = download.id.let { downloads.replace(it, download) }


    /**
     * creating download folder if necessary
     */
    @PostConstruct
    private fun createDownloadFolderIfNecessary() {
        val path = "." + File.separator + "xdcc"
        val customDir = File(path)

        if (customDir.exists()) {
            log.info("download folder exists in " + path)
        } else if (customDir.mkdirs()) {
            log.info("download folder was created in " + path)
        } else {
            throw RuntimeException("target folder" + customDir + "could not be created")
        }
        downloadFolderReady = true
    }
}
