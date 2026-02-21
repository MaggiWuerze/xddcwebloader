package de.maggiwuerze.xdccwebloader.service

import de.maggiwuerze.xdccwebloader.events.SocketEvents
import de.maggiwuerze.xdccwebloader.events.SocketEvents.DELETED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.SocketEvents.NEW_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.SocketEvents.UPDATED_DOWNLOAD
import de.maggiwuerze.xdccwebloader.events.download.DownloadCreationEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadDeleteEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadUpdateEvent
import de.maggiwuerze.xdccwebloader.model.download.Download
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
        (payload as? Download)?.let {
            val downloadId: UUID = payload.id
            when (destinationSuffix) {
                UPDATED_DOWNLOAD -> {
                    DownloadUpdateEvent(this, downloadId).let { downloadUpdateEvent ->
                        applicationEventPublisher.publishEvent(downloadUpdateEvent)
                    }
                }

                NEW_DOWNLOAD -> {
                    DownloadCreationEvent(this, downloadId).let { downloadCreationEvent ->
                        applicationEventPublisher.publishEvent(downloadCreationEvent)
                    }
                }

                DELETED_DOWNLOAD -> {
                    DownloadDeleteEvent(this, downloadId).let { downloadDeleteEvent ->
                        applicationEventPublisher.publishEvent(downloadDeleteEvent)
                    }
                }

                else -> {}
            }

            this.websocket.convertAndSend(
                MESSAGE_PREFIX + destinationSuffix.route, payload.toTO()
            )
        }

    }

    companion object {
        private const val MESSAGE_PREFIX = "/topic"
    }
}
