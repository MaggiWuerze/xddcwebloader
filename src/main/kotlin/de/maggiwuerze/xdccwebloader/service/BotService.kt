package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.model.entity.Bot
import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.entity.Server
import de.maggiwuerze.xdccwebloader.model.forms.BotForm
import de.maggiwuerze.xdccwebloader.persistence.TargetBotRepository
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

    fun save(form: BotForm): Bot {
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

    fun delete(id: UUID): HttpStatus {
        return findById(id)?.let {
            targetBotRepository.delete(it)
            HttpStatus.OK
        } ?: HttpStatus.BAD_REQUEST
    }

    fun update(id: UUID, botForm: BotForm): Bot {
        return findById(id)?.let {
            it.name = botForm.name
            it.pattern = botForm.pattern
            it.maxParallelDownloads = botForm.maxParallelDownloads
            it.server = serverService.findById(botForm.serverId)
                ?: throw IllegalStateException("Server ${botForm.serverId} not found")
            it.channel = channelService.findById(botForm.channelId)
                ?: throw IllegalStateException("Channel ${botForm.channelId} not found")
            targetBotRepository.save(it)
        } ?: throw IllegalStateException("Bot with id $id not found.")
    }
}
