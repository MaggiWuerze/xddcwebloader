package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.entity.Server
import de.maggiwuerze.xdccwebloader.model.forms.ServerForm
import de.maggiwuerze.xdccwebloader.model.transport.ServerTO
import de.maggiwuerze.xdccwebloader.service.ServerService
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
internal class ServerController(private val serverService: ServerService) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    @get:GetMapping("/servers/")
    val allServers: ResponseEntity<List<ServerTO>>
        /**
         * @return a list of all servers
         */
        get() {
            val servers: List<ServerTO> = serverService.list().map { ServerTO(it) }.toList()

            return ResponseEntity(servers, HttpStatus.OK)
        }

    @PostMapping("/servers/")
    fun addServer(@RequestBody serverForm: ServerForm): ResponseEntity<*> {
        try {
            val server: Server = serverService.save(Server(name = serverForm.name, serverUrl = serverForm.serverUrl))
            return ResponseEntity("Download added successfully. id=[${server.id}]", HttpStatus.OK)
        } catch (e: ConstraintViolationException) {
            log.error(e.message)
            val errormessage =
                e.constraintViolations.map { it.message }.joinToString("\n")
            return ResponseEntity(errormessage, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * @return an example server object to populate the attributes for server creation popover
     */
    @GetMapping("/servers/example")
    fun getExampleServer(): ResponseEntity<List<Server>> {
        return ResponseEntity(listOf(Server()), HttpStatus.OK)
    }

    @DeleteMapping("/servers/")
    fun delete(serverId: UUID): ResponseEntity<*> {
        try {
            serverService.delete(serverId)
            return ResponseEntity("Server deleted successfully.", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("Server could not be deleted", HttpStatus.CONFLICT)
        }
    }
}