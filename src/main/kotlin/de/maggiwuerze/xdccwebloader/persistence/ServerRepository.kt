package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.model.entity.Server
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServerRepository : CrudRepository<Server, UUID> {
    override fun findAll(): List<Server>
}