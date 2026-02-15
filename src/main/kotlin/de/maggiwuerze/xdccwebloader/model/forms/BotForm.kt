package de.maggiwuerze.xdccwebloader.model.forms

import java.util.*

class BotForm(
    val name: String,
    val pattern: String,
    val serverId: UUID,
    val channelId: UUID,
    val maxParallelDownloads: Long? = 3L
)
