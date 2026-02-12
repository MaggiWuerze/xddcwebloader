package de.maggiwuerze.xdccwebloader.model.transport

import de.maggiwuerze.xdccwebloader.model.entity.Server
import java.util.*

class ServerTO(server: Server) {
    var id: UUID? = server.id

    var name: String = server.name

    var serverUrl: String = server.serverUrl
}
