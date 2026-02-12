package de.maggiwuerze.xdccwebloader.model.transport

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import java.util.*

class ChannelTO(channel: Channel) {
    var id: UUID? = channel.id
    var name: String = channel.name
}