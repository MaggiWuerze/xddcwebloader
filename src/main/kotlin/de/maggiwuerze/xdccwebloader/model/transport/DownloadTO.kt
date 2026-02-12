package de.maggiwuerze.xdccwebloader.model.transport

import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import org.springframework.beans.BeanUtils
import java.time.LocalDateTime
import java.util.*

class DownloadTO(
    val download: Download,
    var id: UUID?,

    var bot: Bot,

    var fileRefId: String,

    var date: LocalDateTime,

    var progress: Double,

    var filename: String,

    var filesize: String? = "-",

    var averageSpeed: String? = "0 Kb/s",

    var timeRemaining: String? = "-",

    var status: DownloadState? = DownloadState.UNKNOWN,

    var statusMessage: String? = "",

    ) {
    init {
        BeanUtils.copyProperties(download, this)
    }

    companion object {
        fun getListOfTOs(downloads: List<Download>): List<DownloadTO> {
            return downloads.map { dl: Download ->
                DownloadTO(
                    dl,
                    id = dl.id,
                    bot = dl.bot,
                    fileRefId = dl.fileRefId,
                    date = dl.date,
                    progress = dl.progress,
                    filename = dl.filename,
                    filesize = dl.filesize,
                    averageSpeed = dl.averageSpeed,
                    timeRemaining = dl.timeRemaining,
                    status = dl.status,
                    statusMessage = dl.statusMessage,
                )
            }.toList()
        }
    }
}
