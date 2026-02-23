package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.service.UserSettingsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class MainController(private val userSettingsService: UserSettingsService) {

    @GetMapping
    fun index(): String = "index"

    @get:GetMapping("/initialized")
    val initialized: ResponseEntity<Boolean>
        //USERDETAILS
        get() {
            return ResponseEntity<Boolean>(userSettingsService.getUserSettings().initialized, HttpStatus.OK)
        }

    @PostMapping("initialized/")
    fun setInitialized(): ResponseEntity<*> {
        userSettingsService.setInitialized()

        return ResponseEntity("ok", HttpStatus.OK)
    }
}