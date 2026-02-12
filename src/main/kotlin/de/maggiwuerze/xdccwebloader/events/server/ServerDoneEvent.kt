package de.maggiwuerze.xdccwebloader.events.server

import de.maggiwuerze.xdccwebloader.events.EntityDoneEvent
import de.maggiwuerze.xdccwebloader.model.entity.Server

class ServerDoneEvent(source: Any, server: Server) : EntityDoneEvent<Server?>(source, server)