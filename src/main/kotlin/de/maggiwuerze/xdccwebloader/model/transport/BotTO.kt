package de.maggiwuerze.xdccwebloader.model.transport

import de.maggiwuerze.xdccwebloader.model.entity.Bot
import java.util.*

class BotTO(bot: Bot) {
    var id: UUID? = bot.id

    var channel: String? = bot.channel.name

    var server: String? = bot.server.name

    var name: String? = bot.name

    var pattern: String? = bot.pattern

    var maxParallelDownloads: Long? = bot.maxParallelDownloads
}
