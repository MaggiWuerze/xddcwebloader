package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.forms.ChannelFormTO
import de.maggiwuerze.xdccwebloader.persistence.ChannelRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChannelService(val channelRepository: ChannelRepository) {

    fun list(): List<Channel> {
        return channelRepository.findAll()
    }

    fun save(channel: Channel): Channel {
        return channelRepository.save(channel)
    }

    fun findById(channelId: UUID): Channel? {
        return channelRepository.findById(channelId).get()
    }

    fun delete(id: UUID): HttpStatus {
        return findById(id)?.let {
            channelRepository.delete(it)
            HttpStatus.OK
        } ?: HttpStatus.BAD_REQUEST
    }

    fun update(id: UUID, channelFormTO: ChannelFormTO): Channel {
        return findById(id)?.let {
            it.name = channelFormTO.name
            channelRepository.save(it)
        } ?: throw IllegalStateException("Channel with id $id not found.")
    }
}
