package de.maggiwuerze.xdccwebloader.irc

import de.maggiwuerze.xdccwebloader.events.EventPublisher
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.persistence.entity.Bot
import de.maggiwuerze.xdccwebloader.service.DownloadService
import org.pircbotx.PircBotX
import org.pircbotx.dcc.DccState
import org.pircbotx.dcc.ReceiveFileTransfer
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.BanListEvent
import org.pircbotx.hooks.events.ConnectAttemptFailedEvent
import org.pircbotx.hooks.events.DisconnectEvent
import org.pircbotx.hooks.events.ExceptionEvent
import org.pircbotx.hooks.events.FileTransferCompleteEvent
import org.pircbotx.hooks.events.IncomingFileTransferEvent
import org.pircbotx.hooks.events.JoinEvent
import org.pircbotx.hooks.events.ListenerExceptionEvent
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.hooks.events.OutputEvent
import org.pircbotx.hooks.types.GenericMessageEvent
import org.slf4j.LoggerFactory
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component
import java.io.File
import java.io.Serializable
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

@Component
class IrcEventListener(val eventPublisher: EventPublisher, val downloadService: DownloadService) : ListenerAdapter() {

    val log = LoggerFactory.getLogger(this.javaClass.name)


    override fun onBanList(event: BanListEvent) {
        log.info(java.lang.String.format("Nick %s was banned", (event.getBot() as PircBotX).nick))
    }

    override fun onJoin(event: JoinEvent) {
        super.onJoin(event)
        (event.getBot() as IrcBot).let { bot ->
            //ignore if not own join
            if (event.user?.nick.equals(bot.nick).not()) return
            //TODO: handle second trigger after download completes
            //ignore if download is already done
            if (downloadService.getOrThrow(bot.downloadId).status == DownloadState.DONE) return

            log.info("Bot ${bot.nick} joined channel ${event.channel.name}")
            downloadService.getOrThrow(bot.downloadId).let { download ->
                val targetBot: Bot = download.bot
                val message: String? = java.lang.String.format(targetBot.pattern, download.fileRefId)
                bot.sendIRC().message(targetBot.name, message)

                download.status = DownloadState.WAITING
                downloadService.update(download)
            }
        }
    }

    override fun onDisconnect(event: DisconnectEvent) {
        super.onDisconnect(event)
    }

    override fun onGenericMessage(event: GenericMessageEvent) {
        event.message
    }

    override fun onMessage(event: MessageEvent) {
        event.message
    }

    override fun onConnectAttemptFailed(event: ConnectAttemptFailedEvent) {
        //TODO: handle errors better... if possible
        if (event.getRemainingAttempts() <= 0) {
            downloadService.getOrThrow((event.getBot() as IrcBot).downloadId).let { download ->
                download.status = DownloadState.ERROR
                download.statusMessage = event.connectExceptions.get<Serializable, Exception>(0)?.localizedMessage
                    ?: "Couldn't determine cause"

                event.getConnectExceptions()[event.getConnectExceptions().keys.first()]?.let {
                    eventPublisher.handleError(event.getBot(), it)
                }

            }
        } else {
            log.info("Connection failed, remaining attempts: " + event.getRemainingAttempts())
        }
    }

    override fun onException(event: ExceptionEvent) {
        eventPublisher.handleError(event.getBot(), event.getException())
        event.getBot<IrcBot>().sendIRC().quitServer()
    }

    override fun onIncomingFileTransfer(event: IncomingFileTransferEvent) {
        super.onIncomingFileTransfer(event)
        val bot: IrcBot = event.getBot()
        downloadService.getOrThrow(bot.downloadId).let { download ->

            download.filename = event.getSafeFilename()
            //		String path = DL_PATH + File.separatorChar + event.getSafeFilename();
            val path: Path = Paths.get(DL_PATH + File.separatorChar + event.getSafeFilename())

            //Receive the file from the user
            // If the file exists, resume from a position
            //		File downloadFile = new File(path);
            val fileTransfer: ReceiveFileTransfer = if (path.toFile().exists()) {
                // Use BasicFileAttributes to find position to resume
                event.acceptResume(
                    path.toFile(),
                    java.nio.file.Files.readAttributes(path, BasicFileAttributes::class.java).size()
                )
            } else {
                event.accept(path.toFile())
            }

            download.progressWatcher?.fileTransfer = fileTransfer
            val taskExecutor: TaskExecutor = SimpleAsyncTaskExecutor(event.getBot<PircBotX>().getNick() + " transfer")

            taskExecutor.execute {
                if (fileTransfer.getFileTransferStatus().getDccState() !== DccState.CONNECTING) {
                    fileTransfer.transfer()
                }
            }

            download.progressWatcher?.run()
        }
    }

    override fun onFileTransferComplete(event: FileTransferCompleteEvent) {
        (event.getBot() as? IrcBot)?.let { bot ->
            downloadService.getOrThrow(bot.downloadId).let { download ->
                if (event.getTransferStatus().getDccState().equals(DccState.ERROR)) {
                    download.statusMessage = event.getTransferStatus().getException().localizedMessage
                    eventPublisher.updateDownloadState(DownloadState.ERROR, download)
                    log.error(java.lang.String.format("error on filetransfer for fileID %s", download.fileRefId))
                    log.error(event.getTransferStatus().getException().toString())
                } else {
                    //TODO: should be in FINALIZING when entering here. will have to possibly do stuff set up in configuration before setting to done
                    eventPublisher.updateDownloadState(DownloadState.DONE, download)
                    log.info(java.lang.String.format("filetransfer completed for %s", download.fileRefId))
                }
            }
            bot.sendIRC().quitServer()
        }

    }

    override fun onListenerException(event: ListenerExceptionEvent) {
        eventPublisher.handleError(event.getBot(), event.getException())
    }

    override fun onOutput(event: OutputEvent) {
        event.rawLine
    }

    companion object {
        //    final String DL_PATH = File.separator + "opt" + File.separator + "xdcc" + File.separator + "data";
        private const val DL_PATH = "xdcc"
    }
}