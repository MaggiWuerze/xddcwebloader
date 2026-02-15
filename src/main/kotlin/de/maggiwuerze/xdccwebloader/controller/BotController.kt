package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.model.entity.BotTO
import de.maggiwuerze.xdccwebloader.model.forms.BotForm
import de.maggiwuerze.xdccwebloader.service.BotService
import de.maggiwuerze.xdccwebloader.service.EventService
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/bots/")
class BotController
    (
    private val botService: BotService,
    private val eventService: EventService
) {

    @PostMapping
    @Schema(name = "Create Bot", description = "Creates a new bot")
    fun createBot(@RequestBody form: BotForm): ResponseEntity<BotTO> {
        botService.save(form).let {
            eventService.publishEvent(SocketEvents.NEW_SERVER, it)
            return ResponseEntity(it.toTO(), HttpStatus.OK)
        }
    }

    @GetMapping
    @Schema(name = "List Bots", description = "Lists all bots")
    fun listBots(): ResponseEntity<List<BotTO>> {
        return ResponseEntity(botService.list().map { it.toTO() }, HttpStatus.OK)
    }

    @GetMapping("{id}")
    @Schema(name = "Get Bot", description = "Gets a bot by id")
    fun getBot(@PathVariable id: UUID): ResponseEntity<BotTO> =
        botService.findById(id)?.let { ResponseEntity(it.toTO(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @DeleteMapping("{id}")
    @Schema(name = "Delete Bot", description = "Deletes a bot by id")
    fun deleteBot(@PathVariable id: UUID): ResponseEntity<HttpStatus> =
        botService.findById(id)?.let { ResponseEntity(botService.delete(it.id), HttpStatus.OK) } ?: ResponseEntity(
            HttpStatus.NOT_FOUND
        )

    @PutMapping("{id}")
    fun updateBot(@PathVariable id: UUID, @RequestBody botForm: BotForm): ResponseEntity<BotTO> =
        ResponseEntity(botService.update(id, botForm).toTO(), HttpStatus.OK)


}