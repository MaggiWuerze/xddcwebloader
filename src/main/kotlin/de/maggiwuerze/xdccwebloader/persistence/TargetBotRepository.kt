package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TargetBotRepository : CrudRepository<Bot, UUID> {
    override fun findAll(): List<Bot>

    fun findByName(name: String): Bot?
}