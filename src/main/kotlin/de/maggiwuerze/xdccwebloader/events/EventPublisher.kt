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

        val downloadUpdateEvent: DownloadUpdateEvent = DownloadUpdateEvent(this, download.id)
        applicationEventPublisher.publishEvent(downloadUpdateEvent)
    }

    fun sendWebsocketEvent(event: SocketEvents, payload: Any) {
        this.websocket.convertAndSend(
            de.maggiwuerze.xdccwebloader.events.EventPublisher.Companion.MESSAGE_PREFIX + event.route, payload
        )
    }

    fun handleError(bot: IrcBot, exception: java.lang.Exception) {
        bot.stopBotReconnect()
        val download: Download = downloadService.getById(bot.downloadId)
        val message: String = String.format(DownloadState.ERROR.externalString, exception.message)
        try {
            download.statusMessage = message
            updateDownloadState(DownloadState.ERROR, download)
        } catch (e: java.lang.Exception) {
            log.error(e.message)
        }
    }

    companion object {
        private const val MESSAGE_PREFIX = "/topic"
    }
}
