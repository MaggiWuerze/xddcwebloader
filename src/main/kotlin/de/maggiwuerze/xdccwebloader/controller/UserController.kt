package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.persistence.entity.UserSettings
import de.maggiwuerze.xdccwebloader.persistence.entity.UserSettingsTO
import de.maggiwuerze.xdccwebloader.model.forms.UserSettingsFormTO
import de.maggiwuerze.xdccwebloader.service.UserSettingsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/v1//")
class UserController(val userSettingsService: UserSettingsService) {

    @GetMapping(value = ["usersettings"])
    fun getSetting(): ResponseEntity<UserSettingsTO> =
        ResponseEntity(userSettingsService.getUserSettings().toTO(), HttpStatus.OK)


    @PostMapping(value = ["usersettings"])
    fun updateSetting(
        @RequestBody userSettingsFormTO: UserSettingsFormTO
    ): ResponseEntity<*> {
        val userSettingsById: UserSettings = userSettingsService.getUserSettings()
        userSettingsById.downloadSortBy = userSettingsFormTO.downloadSortBy
        userSettingsById.sessionTimeout = userSettingsFormTO.sessionTimeout
        userSettingsService.saveUserSettings(userSettingsById)

        return ResponseEntity("UserSettings updated successfully.", HttpStatus.OK)
    }
}