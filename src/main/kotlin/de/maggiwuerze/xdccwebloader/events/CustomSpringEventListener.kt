package de.maggiwuerze.xdccwebloader.events

import de.maggiwuerze.xdccwebloader.events.download.DownloadCreationEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadDeleteEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadDoneEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadUpdateEvent
import de.maggiwuerze.xdccwebloader.irc.IrcBot
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.service.DownloadService
import de.maggiwuerze.xdccwebloader.service.IrcBotService
import de.maggiwuerze.xdccwebloader.util.ProgressWatcherFactory
import org.pircbotx.exception.IrcException
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomSpringEventListener(
    val progressWatcherFactory: ProgressWatcherFactory,
    val eventPublisher: EventPublisher,
    val downloadService: DownloadService,
    val ircBotService: IrcBotService
) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    @EventListener
    fun onDownloadCreationEvent(event: DownloadCreationEvent) {
        downloadService.getOrThrow(event.payload).let { download ->
            eventPublisher.updateDownloadState(DownloadState.PREPARING, download)
            //Create our bot with the configuration
            download.progressWatcher = progressWatcherFactory.getProgressWatcher(download.id)

            val bot: IrcBot = ircBotService.getIrcBotForDownload(download)

            eventPublisher.updateDownloadState(DownloadState.PREPARED, download)
            val taskExecutor: TaskExecutor = SimpleAsyncTaskExecutor(bot.getNick() + "_bot")
            taskExecutor.execute {
                try {
                    bot.startBot()
                } catch (e: IOException) {
                    log.error(e.toString())
                } catch (e: IrcException) {
                    log.error(e.toString())
                }
            }

            eventPublisher.updateDownloadState(DownloadState.CONNECTING, download)
        }
    }

    @EventListener
    fun onDownloadUpdateEvent(event: DownloadUpdateEvent) {
        downloadService.getOrThrow(event.payload).let { download ->
            eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, download)
        }
    }

    @EventListener
    fun onDownloadDeleteEvent(event: DownloadDeleteEvent) {
        downloadService.getOrThrow(event.payload).let { download ->
            eventPublisher.sendWebsocketEvent(SocketEvents.DELETED_DOWNLOAD, download)
            downloadService.remove(event.payload)
        }
    }

    @EventListener
    fun onDownloadDoneEvent(event: DownloadDoneEvent) {
        //Do some stuff here before setting to done!

        downloadService.getOrThrow(event.payload).let { download ->
            download.status = DownloadState.DONE
            downloadService.update(download)

            eventPublisher.sendWebsocketEvent(SocketEvents.UPDATED_DOWNLOAD, download)
        }
    }
}