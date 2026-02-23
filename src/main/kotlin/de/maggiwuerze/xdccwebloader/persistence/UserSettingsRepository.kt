package de.maggiwuerze.xdccwebloader.persistence

import de.maggiwuerze.xdccwebloader.persistence.entity.UserSettings
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserSettingsRepository : CrudRepository<UserSettings, UUID> {
    override fun findAll(): List<UserSettings>
    fun getFirstById(id: UUID): UserSettings?
}