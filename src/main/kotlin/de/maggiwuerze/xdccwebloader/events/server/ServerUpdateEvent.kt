package de.maggiwuerze.xdccwebloader.events.server

import de.maggiwuerze.xdccwebloader.events.EntityUpdateEvent
import de.maggiwuerze.xdccwebloader.model.entity.Server

class ServerUpdateEvent(source: Any, server: Server) : EntityUpdateEvent<Server?>(source, server)