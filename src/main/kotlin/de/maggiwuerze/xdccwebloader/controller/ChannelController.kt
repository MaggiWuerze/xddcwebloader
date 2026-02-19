package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.entity.ChannelTO
import de.maggiwuerze.xdccwebloader.model.forms.ChannelFormTO
import de.maggiwuerze.xdccwebloader.service.ChannelService
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
@RequestMapping("channels/")
class ChannelController(val channelService: ChannelService) {

    /**
     * @return a list of all channels
     */
    @GetMapping()
    fun listChannels(): ResponseEntity<List<ChannelTO>> =
        ResponseEntity(channelService.list().map { it.toTO() }, HttpStatus.OK)

    @GetMapping("{id}")
    fun getChannel(@PathVariable id: UUID): ResponseEntity<ChannelTO> =
        channelService.findById(id)?.let { ResponseEntity(it.toTO(), HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)

    @PutMapping("{id}")
    fun updateChannel(@PathVariable id: UUID, @RequestBody channelFormTO: ChannelFormTO): ResponseEntity<ChannelTO> =
        ResponseEntity(channelService.update(id, channelFormTO).toTO(), HttpStatus.OK)

    @DeleteMapping("{id}")
    fun deleteChannel(@PathVariable id: UUID): ResponseEntity<HttpStatus> =
        ResponseEntity(channelService.delete(id))

    @PostMapping
    fun createChannel(@RequestBody channelFormTO: ChannelFormTO): ResponseEntity<ChannelTO> {
        channelService.save(Channel(name = channelFormTO.name)).let {
            return ResponseEntity(it.toTO(), HttpStatus.OK)
        }
    }


}