package de.maggiwuerze.xdccwebloader.events.server

import de.maggiwuerze.xdccwebloader.events.EntityCreationEvent
import de.maggiwuerze.xdccwebloader.model.entity.Server

class ServerCreationEvent(source: Any, server: Server) : EntityCreationEvent<Server?>(source, server)