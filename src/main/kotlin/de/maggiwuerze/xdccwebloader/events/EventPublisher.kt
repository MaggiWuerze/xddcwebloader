package de.maggiwuerze.xdccwebloader.events

import de.maggiwuerze.xdccwebloader.events.download.DownloadUpdateEvent
import de.maggiwuerze.xdccwebloader.irc.IrcBot
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.service.DownloadService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val downloadService: DownloadService,
    private val websocket: SimpMessagingTemplate,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    fun updateDownloadState(state: DownloadState, download: Download) {
        download.status = state
        downloadService.update(download)

        DownloadUpdateEvent(this, download.id).also { downloadUpdateEvent ->
            applicationEventPublisher.publishEvent(downloadUpdateEvent)
        }
    }

    fun sendWebsocketEvent(event: SocketEvents, payload: Any) {
        this.websocket.convertAndSend(
            MESSAGE_PREFIX + event.route, payload
        )
    }

    fun handleError(bot: IrcBot, exception: Exception) {
        bot.stopBotReconnect()
        bot.sendIRC().quitServer()
        downloadService.getOrThrow(bot.downloadId).let { download ->

            try {
                download.statusMessage = exception.message ?: "Unknown error"
                updateDownloadState(DownloadState.ERROR, download)
            } catch (e: Exception) {
                log.error(e.message)
            }

        }
    }

    companion object {
        private const val MESSAGE_PREFIX = "/topic"
    }
}
