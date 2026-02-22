package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.persistence.entity.UserSettings
import de.maggiwuerze.xdccwebloader.persistence.IrcUserRepository
import de.maggiwuerze.xdccwebloader.persistence.UserSettingsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserSettingsService
    (
    private val ircUserRepository: IrcUserRepository,
    private val settingsRepository: UserSettingsRepository,
) {

    fun getUserSettings(usersettingsId: UUID? = null): UserSettings =
        usersettingsId?.let {
            settingsRepository.getFirstById(usersettingsId)
        } ?: settingsRepository.findAll().firstOrNull() ?: throw IllegalStateException("UserSettings not found!")

    fun saveUserSettings(userSettingsBy: UserSettings) {
        settingsRepository.save(userSettingsBy)
    }

    fun setInitialized() {
        val usersettings = getUserSettings()
        usersettings.initialized = true
        saveUserSettings(usersettings)
    }
}
