package de.maggiwuerze.xdccwebloader.events.channel.server

import de.maggiwuerze.xdccwebloader.events.EntityDeleteEvent
import de.maggiwuerze.xdccwebloader.model.entity.Channel

class ChannelDeleteEvent(source: Any, channel: Channel) : EntityDeleteEvent<Channel>(source, channel)