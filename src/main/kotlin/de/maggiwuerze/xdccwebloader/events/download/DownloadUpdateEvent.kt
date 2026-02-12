package de.maggiwuerze.xdccwebloader.events.download

import de.maggiwuerze.xdccwebloader.events.EntityUpdateEvent
import java.util.*

class DownloadUpdateEvent(source: Any, downloadId: UUID) : EntityUpdateEvent<UUID>(source, downloadId)