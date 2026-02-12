package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.model.forms.TargetBotForm
import de.maggiwuerze.xdccwebloader.service.BotService
import de.maggiwuerze.xdccwebloader.service.ChannelService
import de.maggiwuerze.xdccwebloader.service.EventService
import de.maggiwuerze.xdccwebloader.service.ServerService
import de.maggiwuerze.xdccwebloader.service.UserSettingsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class BotController
    (
    private val userSettingsService: UserSettingsService,
    private val channelService: ChannelService,
    private val botService: BotService,
    private val serverService: ServerService,
    private val eventService: EventService
) {

    @PostMapping("/bots/")
    fun addBot(@RequestBody form: TargetBotForm, principal: Principal): ResponseEntity<*> {
        eventService.publishEvent(SocketEvents.NEW_SERVER, botService.save(form))
        return ResponseEntity("Bot added succcessfully", org.springframework.http.HttpStatus.OK)
    }

    /**
     * @return a list of all bots
     */
    @GetMapping("/bots/")
    fun getAllBots(principal: Principal): ResponseEntity<List<Bot>> {
        return ResponseEntity(botService.list(), org.springframework.http.HttpStatus.OK)
    }
}