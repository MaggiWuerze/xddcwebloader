package de.maggiwuerze.xdccwebloader.events.channel.server

import de.maggiwuerze.xdccwebloader.events.EntityDoneEvent
import de.maggiwuerze.xdccwebloader.model.entity.Channel

class ChannelDoneEvent(source: Any, channel: Channel) : EntityDoneEvent<Channel>(source, channel)