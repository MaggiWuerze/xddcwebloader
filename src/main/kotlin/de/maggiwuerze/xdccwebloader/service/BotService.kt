package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.forms.BotFormTO
import de.maggiwuerze.xdccwebloader.persistence.TargetBotRepository
import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.persistence.entity.Channel
import de.maggiwuerze.xdccwebloader.persistence.entity.Server
import org.springframework.http.HttpStatus
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

    fun save(bot: Bot): Bot {
        return targetBotRepository.save(bot)
    }

    fun save(form: BotFormTO): Bot {
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

    fun findByName(botName: String): Bot? {
        return targetBotRepository.findByName(botName)
    }

    fun delete(id: UUID): HttpStatus {
        return findById(id)?.let {
            targetBotRepository.delete(it)
            HttpStatus.OK
        } ?: HttpStatus.BAD_REQUEST
    }

    fun update(id: UUID, botFormTO: BotFormTO): Bot {
        return findById(id)?.let {
            it.name = botFormTO.name
            it.pattern = botFormTO.pattern
            it.maxParallelDownloads = botFormTO.maxParallelDownloads
            it.server = serverService.findById(botFormTO.serverId)
                ?: throw IllegalStateException("Server ${botFormTO.serverId} not found")
            it.channel = channelService.findById(botFormTO.channelId)
                ?: throw IllegalStateException("Channel ${botFormTO.channelId} not found")
            targetBotRepository.save(it)
        } ?: throw IllegalStateException("Bot with id $id not found.")
    }
}
