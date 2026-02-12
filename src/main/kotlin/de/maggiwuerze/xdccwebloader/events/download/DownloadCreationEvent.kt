package de.maggiwuerze.xdccwebloader.events.download

import de.maggiwuerze.xdccwebloader.events.EntityCreationEvent
import java.util.*

class DownloadCreationEvent(source: Any, downloadId: UUID) : EntityCreationEvent<UUID>(source, downloadId)