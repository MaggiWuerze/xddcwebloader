package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.download.DownloadTO
import de.maggiwuerze.xdccwebloader.model.forms.DownloadFormTO
import de.maggiwuerze.xdccwebloader.model.search.SearchResultItem
import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.persistence.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.util.*
import java.util.Collections.synchronizedMap

/**
 * Service for adding Downloads to the queue and retrieving them
 */
@Service
class DownloadService(
    val botService: BotService, val eventService: EventService,
    private val channelService: ChannelService,
    private val serverService: ServerService
) {
    private val downloads: MutableMap<UUID, Download> = synchronizedMap(HashMap())
    private val bots: MutableMap<Bot, List<Download>> = synchronizedMap(HashMap())
    private var downloadFolderReady = false
    val log = LoggerFactory.getLogger(this.javaClass.name)

    fun addDownloadToBotQueue(download: Download) {
        downloads[download.id] = download
    }

    fun getOrThrow(id: UUID): Download {
        return downloads[id] ?: throw RuntimeException("Download not found")
    }

    fun getById(id: UUID): Download? {
        return downloads[id]
    }

    fun remove(id: UUID) {
        downloads.remove(id)
    }

    fun findAllByOrderByProgressDesc(): List<Download> {

        return downloads.values.sortedBy { it.progress }.toList()
    }

    fun findAllInactive(): List<DownloadTO> {
        return listOf(DownloadState.UNKNOWN, DownloadState.DONE).let {
            findAllByStatusInOrderByProgress(it).map { it.toTO() }
        }
    }

    fun findAllActive(): List<DownloadTO> {
        return listOf(
            DownloadState.PREPARING,
            DownloadState.PREPARED,
            DownloadState.READY,
            DownloadState.CONNECTING,
            DownloadState.WAITING,
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

    fun update(download: Download) = download.id.let {
        downloads.replace(it, download)
        eventService.publishEvent(SocketEvents.UPDATED_DOWNLOAD, download)
    }


    fun create(searchResultItem: SearchResultItem): Download {

        val channel = channelService.findByName(searchResultItem.channel) ?: channelService.save(
            Channel(name = searchResultItem.channel)
        )
        val server = serverService.findByName(searchResultItem.server) ?: serverService.save(
            Server(name = searchResultItem.server, serverUrl = searchResultItem.server)
        )
        val bot = botService.findByName(searchResultItem.bot) ?: botService.save(
            Bot(
                server = server,
                channel = channel,
                name = searchResultItem.bot,
                pattern = "xdcc send %s",
                creationDate = LocalDateTime.now(),
                maxParallelDownloads = 3
            )
        )

        return Download(bot, searchResultItem.fileRefId).also { download ->
            addDownloadToBotQueue(download)
            eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download)
        }
    }

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

    fun create(downloadFormTO: DownloadFormTO): List<Download> {
        botService.findById(downloadFormTO.targetBotId)?.let { bot ->
            val fileRefId: String = downloadFormTO.fileRefId
            val downloads: MutableList<Download> = ArrayList()
            if (fileRefId.contains(",")) {
                for (id in fileRefId.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                    Download(bot, id).let { download ->
                        downloads.add(download)
                        addDownloadToBotQueue(download)
                        eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download)
                    }
                }
            } else {
                Download(bot, fileRefId).let { download ->
                    downloads.add(download)
                    addDownloadToBotQueue(download)
                    eventService.publishEvent(SocketEvents.NEW_DOWNLOAD, download)
                }
            }

            return downloads
        }
        return emptyList()
    }
}
