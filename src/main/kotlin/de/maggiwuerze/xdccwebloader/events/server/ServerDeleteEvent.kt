package de.maggiwuerze.xdccwebloader.events.server

import de.maggiwuerze.xdccwebloader.events.EntityDeleteEvent
import de.maggiwuerze.xdccwebloader.model.entity.Server

class ServerDeleteEvent(source: Any, server: Server) : EntityDeleteEvent<Server?>(source, server)