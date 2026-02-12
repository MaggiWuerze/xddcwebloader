package de.maggiwuerze.xdccwebloader.events.download

import de.maggiwuerze.xdccwebloader.events.EntityDeleteEvent
import java.util.*

class DownloadDeleteEvent(source: Any, downloadId: UUID) : EntityDeleteEvent<UUID>(source, downloadId)