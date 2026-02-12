package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.entity.UserSettings
import de.maggiwuerze.xdccwebloader.model.forms.UserSettingsForm
import de.maggiwuerze.xdccwebloader.service.UserSettingsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class UserController(val userSettingsService: UserSettingsService) {

    @get:GetMapping(value = ["/usersettings"])
    val userSettings: ResponseEntity<*>
        get() {
            val userSettings: UserSettings = userSettingsService.getUserSettings()

            return ResponseEntity(userSettings, HttpStatus.OK)
        }

    @PostMapping(value = ["/usersettings"])
    fun updateUserSettings(
        @RequestBody userSettingsForm: UserSettingsForm
    ): ResponseEntity<*> {
        val userSettingsById: UserSettings = userSettingsService.getUserSettings()
        userSettingsById.downloadSortBy = userSettingsForm.downloadSortBy
        userSettingsById.sessionTimeout = userSettingsForm.sessionTimeout
        userSettingsService.saveUserSettings(userSettingsById)

        return ResponseEntity("UserSettings updated successfully.", HttpStatus.OK)
    }
}