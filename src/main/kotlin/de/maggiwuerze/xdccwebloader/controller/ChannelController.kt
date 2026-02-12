package de.maggiwuerze.xdccwebloader.controller

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.forms.ChannelForm
import de.maggiwuerze.xdccwebloader.service.ChannelService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ChannelController(val channelService: ChannelService) {

    /**
     * @return a list of all channels
     */
    @GetMapping("/channels/")
    fun allChannels(): ResponseEntity<List<Channel>> = ResponseEntity(channelService.list(), HttpStatus.OK)

    @PostMapping("/channels/")
    fun addChannel(@RequestBody channelForm: ChannelForm): ResponseEntity<*> {
        val channel: Channel = channelService.save(Channel(name = channelForm.name))
        return ResponseEntity("Download added succcessfully. id=[" + channel.id + "]", HttpStatus.OK)
    }
}