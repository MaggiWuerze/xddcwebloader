package de.maggiwuerze.xdccwebloader.events.channel.server

import de.maggiwuerze.xdccwebloader.events.EntityCreationEvent
import de.maggiwuerze.xdccwebloader.model.entity.Channel

class ChannelCreationEvent(source: Any, channel: Channel) : EntityCreationEvent<Channel>(source, channel)