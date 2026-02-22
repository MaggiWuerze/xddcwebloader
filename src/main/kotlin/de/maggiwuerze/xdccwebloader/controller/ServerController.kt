package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import de.maggiwuerze.xdccwebloader.persistence.entity.ServerTO
import de.maggiwuerze.xdccwebloader.model.forms.ServerFormTO
import de.maggiwuerze.xdccwebloader.service.ServerService
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
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
@RequestMapping("server/")
internal class ServerController(private val serverService: ServerService) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    @GetMapping
    fun listServers(): ResponseEntity<List<ServerTO>> {
        return ResponseEntity(serverService.list().map { it.toTO() }.toList(), HttpStatus.OK)
    }

    @GetMapping("{id}")
    fun getServer(@PathVariable id: UUID): ResponseEntity<ServerTO> {
        serverService.findById(id)?.let {
            return ResponseEntity(it.toTO(), HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun createServer(@RequestBody serverFormTO: ServerFormTO): ResponseEntity<ServerTO> {
        try {
            serverService.save(Server(name = serverFormTO.name, serverUrl = serverFormTO.serverUrl)).let { server ->
                return ResponseEntity(server.toTO(), HttpStatus.OK)
            }
        } catch (e: ConstraintViolationException) {
            log.error(e.message)
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("{id}")
    fun updateServer(@PathVariable id: UUID, @RequestBody serverFormTO: ServerFormTO): ResponseEntity<ServerTO> =
        serverService.update(id, serverFormTO).toTO().let { ResponseEntity(it, HttpStatus.OK) }

    @DeleteMapping("{id}")
    fun deleteServer(@PathVariable id: UUID): ResponseEntity<*> {
        try {
            serverService.delete(id)
            return ResponseEntity("Server deleted successfully.", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Server could not be deleted", HttpStatus.CONFLICT)
        }
    }
}