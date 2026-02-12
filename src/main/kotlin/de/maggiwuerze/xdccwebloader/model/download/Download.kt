package de.maggiwuerze.xdccwebloader.model.download

import com.fasterxml.jackson.annotation.JsonIgnore
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.util.FileTransferProgressWatcher
import java.util.*


class Download(var bot: Bot, var fileRefId: String) {

    var id: UUID = UUID.randomUUID()

    var date: java.time.LocalDateTime = java.time.LocalDateTime.now()

    var progress: Double = 0.0

    var filename: String = "unknown"

    var filesize: String = "-"

    var averageSpeed: String = "0 Kb/s"

    var timeRemaining: String = "-"

    var status: DownloadState = DownloadState.UNKNOWN

    var statusMessage: String = ""

    @JsonIgnore
    var progressWatcher: FileTransferProgressWatcher? = null

}
