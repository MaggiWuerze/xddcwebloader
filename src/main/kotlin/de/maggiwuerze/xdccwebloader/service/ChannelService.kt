package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.ChannelRepository
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
}
