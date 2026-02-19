package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.entity.Server
import de.maggiwuerze.xdccwebloader.model.forms.ServerFormTO
import de.maggiwuerze.xdccwebloader.persistence.ServerRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ServerService(private val serverRepository: ServerRepository) {

    fun list(): List<Server> {
        return serverRepository.findAll()
    }

    fun save(server: Server): Server {
        return serverRepository.save(server)
    }

    fun findById(serverId: UUID): Server? {
        return serverRepository.findById(serverId).orElse(null)
    }

    fun delete(serverId: UUID) {
        serverRepository.deleteById(serverId)
    }

    fun update(id: UUID, serverFormTO: ServerFormTO): Server {
        return serverRepository.findById(id).getOrNull()?.let {
            it.name = serverFormTO.name
            it.serverUrl = serverFormTO.serverUrl
            serverRepository.save(it)
        } ?: throw IllegalStateException("Server with id $id not found.")
    }
}
