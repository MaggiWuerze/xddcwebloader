package de.maggiwuerze.xdccwebloader.events.channel.server

import de.maggiwuerze.xdccwebloader.events.EntityUpdateEvent
import de.maggiwuerze.xdccwebloader.model.entity.Channel

class ChannelUpdateEvent(source: Any, channel: Channel) : EntityUpdateEvent<Channel>(source, channel)