package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.model.entity.Bot
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TargetBotRepository : CrudRepository<Bot, UUID> {
    override fun findAll(): List<Bot>
}