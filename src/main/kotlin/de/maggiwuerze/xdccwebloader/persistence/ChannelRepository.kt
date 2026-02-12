package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import org.springframework.data.repository.CrudRepository
import java.util.*

@org.springframework.stereotype.Repository
interface ChannelRepository : CrudRepository<Channel, UUID> {
    override fun findAll(): List<Channel>
}