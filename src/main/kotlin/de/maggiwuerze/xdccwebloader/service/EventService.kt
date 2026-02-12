package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.events.SocketEvents.DELETED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.SocketEvents.NEW_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.SocketEvents.UPDATED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.channel.server.ChannelCreationEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadCreationEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadDeleteEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadUpdateEvent
import de.maggiwuerze.xdccwebloader.events.server.ServerCreationEvent
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.entity.Channel
import de.maggiwuerze.xdccwebloader.model.entity.Server
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*


@Service
class EventService(
    val websocket: SimpMessagingTemplate,
    val applicationEventPublisher: ApplicationEventPublisher
) {

    fun publishEvent(destinationSuffix: SocketEvents, payload: Any) {
        if (payload is Download) {
            val downloadId: UUID = (payload as Download).id
            when (destinationSuffix) {
                UPDATED_DOWNLOAD -> {
                    val downloadUpdateEvent: DownloadUpdateEvent = DownloadUpdateEvent(this, downloadId)
                    applicationEventPublisher.publishEvent(downloadUpdateEvent)
                }

                NEW_DOWNLOAD -> {
                    val downloadCreationEvent: DownloadCreationEvent = DownloadCreationEvent(this, downloadId)
                    applicationEventPublisher.publishEvent(downloadCreationEvent)
                }

                DELETED_DOWNLOAD -> {
                    val downloadDeleteEvent: DownloadDeleteEvent = DownloadDeleteEvent(this, downloadId)
                    applicationEventPublisher.publishEvent(downloadDeleteEvent)
                }

                else -> {}
            }
        }
        if (payload is Server) {
            val downloadCreationEvent = ServerCreationEvent(this, payload)
            applicationEventPublisher.publishEvent(downloadCreationEvent)
        }
        if (payload is Channel) {
            val downloadCreationEvent = ChannelCreationEvent(this, payload)
            applicationEventPublisher.publishEvent(downloadCreationEvent)
        }

        this.websocket.convertAndSend(
            MESSAGE_PREFIX + destinationSuffix.route, payload
        )
    }

    companion object {
        private const val MESSAGE_PREFIX = "/topic"
    }
}
