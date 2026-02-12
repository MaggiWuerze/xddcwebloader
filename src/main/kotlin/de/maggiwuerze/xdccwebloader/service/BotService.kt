package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.entity.Server
import de.maggiwuerze.xdccwebloader.model.forms.TargetBotForm
import de.maggiwuerze.xdccwebloader.persistence.TargetBotRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BotService(
    private val targetBotRepository: TargetBotRepository,
    private val userSettingsService: UserSettingsService,
    private val serverService: ServerService,
    private val channelService: ChannelService
) {

    fun list(): List<Bot> {
        return targetBotRepository.findAll()
    }

    fun save(form: TargetBotForm): Bot {
        val server: Server =
            serverService.findById(form.serverId) ?: throw IllegalStateException("Server ${form.serverId} not found")
        val channel: Channel = channelService.findById(form.channelId)
            ?: throw IllegalStateException("Channel ${form.channelId} not found")
        val bot = Bot(
            server = server,
            channel = channel,
            name = form.name,
            pattern = form.pattern,
            maxParallelDownloads = form.maxParallelDownloads
        )

        return targetBotRepository.save(bot)
    }

    fun findById(botId: UUID): Bot? {
        return targetBotRepository.findById(botId).orElse(null)
    }

    fun delete(channelId: UUID) {
        targetBotRepository.deleteById(channelId)
    }
}
