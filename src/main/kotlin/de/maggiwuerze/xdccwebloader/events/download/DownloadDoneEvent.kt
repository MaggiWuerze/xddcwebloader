package de.maggiwuerze.xdccwebloader.events.download

import de.maggiwuerze.xdccwebloader.events.EntityDoneEvent
import java.util.*

class DownloadDoneEvent(source: Any, downloadId: UUID) : EntityDoneEvent<UUID>(source, downloadId)