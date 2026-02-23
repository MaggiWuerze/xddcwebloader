package de.maggiwuerze.xdccwebloader.util

import de.maggiwuerze.xdccwebloader.events.download.DownloadDeleteEvent
import de.maggiwuerze.xdccwebloader.events.download.DownloadUpdateEvent
import de.maggiwuerze.xdccwebloader.model.download.Download
import de.maggiwuerze.xdccwebloader.model.download.DownloadState
import de.maggiwuerze.xdccwebloader.service.DownloadService
import org.pircbotx.dcc.DccState
import org.pircbotx.dcc.ReceiveFileTransfer
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class FileTransferProgressWatcher(
    var downloadId: UUID,
    val applicationEventPublisher: ApplicationEventPublisher,
    val downloadService: DownloadService
) {
    var fileTransfer: ReceiveFileTransfer? = null
    var exec: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    var schedulerResult: ScheduledFuture<*>? = null

    val log = LoggerFactory.getLogger(this.javaClass.name)

    fun run() {

        if (fileTransfer == null) throw IllegalStateException("FileTransfer is null for Download with id: $downloadId")

        log.info("starting progress watcher for download with id :" + downloadId)
        downloadService.getOrThrow(downloadId).let { download ->

            download.status = DownloadState.TRANSMITTING
            applicationEventPublisher.publishEvent(DownloadUpdateEvent(this, downloadId))
            schedulerResult = exec.scheduleAtFixedRate({
                try {
                    if (download.filesize == "-") {
                        updateFileSize(download)
                    }

                    val newProgress = BigDecimal.valueOf(fileTransfer!!.fileTransferStatus.percentageComplete)
                        .setScale(2, RoundingMode.HALF_UP).toDouble()

                    download.timeRemaining = formatRemainingTime()
                    checkDownloadProgress(download, newProgress)
                } catch (e: Exception) {
                    log.warn("Error in progressWatcher", e)
                }
            }, 0, 1, TimeUnit.SECONDS)
        }
    }

    private fun checkDownloadProgress(download: Download, newProgress: Double) {
        if (download.status == DownloadState.STOPPED) {
            applicationEventPublisher.publishEvent(DownloadDeleteEvent(this, download.id))
            schedulerResult?.cancel(true)
        } else if (fileTransfer!!.getFileTransferStatus().getDccState().equals(DccState.ERROR)) {
            updateDownloadStatus(
                download,
                DownloadState.ERROR,
                fileTransfer!!.getFileTransferStatus().getException().message ?: "Unknown Error"
            )
            log.debug(
                String.format(
                    "error on filetransfer for fileID %s",
                    download.fileRefId
                )
            )
            log.debug(
                fileTransfer!!.getFileTransferStatus().getException().message
            )
            schedulerResult?.cancel(true)
        } else if (newProgress == 100.0 && fileTransfer!!.getFileTransferStatus().isSuccessful()) {
            updateDownloadStatus(download, DownloadState.FINALIZING, "")
            //TODO: some stuff to finalize
            updateDownloadStatus(download, DownloadState.DONE, "")
            schedulerResult?.cancel(true)
        } else {
            download.progress = newProgress
            val averageSpeed: String = FilesizeFormatter.createAutoReadableString(
                fileTransfer!!.getFileTransferStatus().getAverageBytesPerSecond()
            )
            download.averageSpeed = "${averageSpeed}/s"
            applicationEventPublisher.publishEvent(DownloadUpdateEvent(this, download.id))
        }
    }

    private fun updateDownloadStatus(download: Download, state: DownloadState, statusMessage: String) {
        download.status = state
        download.statusMessage = statusMessage
        applicationEventPublisher.publishEvent(DownloadUpdateEvent(this, download.id))
    }

    fun cancel(mayInterrupt: Boolean) {
        schedulerResult?.cancel(mayInterrupt)
    }

    private fun formatRemainingTime(): String {
        var secondsRemaining: Long =
            fileTransfer!!.getFileTransferStatus().getFileSize() - fileTransfer!!.getFileTransferStatus()
                .getBytesTransfered()
        if (fileTransfer!!.getFileTransferStatus().getAverageBytesPerSecond().toInt() != 0) {
            secondsRemaining /= fileTransfer!!.getFileTransferStatus().getAverageBytesPerSecond()
        }

        var seconds = secondsRemaining
        var minutes: Long = 0
        var hours: Long = 0
        var days: Long = 0

        var result: String = String.format("%02dm:%02ds", minutes, seconds)

        if (secondsRemaining > 60) {
            minutes = seconds / 60
            seconds = seconds % 60
            result = String.format("%02dm:%02ds", minutes, seconds)

            if (minutes > 60) {
                hours = minutes / 60
                minutes = minutes % 60
                result = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds)

                if (hours > 24) {
                    days = hours / 24
                    hours = hours % 24
                    result = String.format("%02dd:%02dh:%02dm:%02ds", days, hours, minutes, seconds)
                }
            }
        }

        return result
    }

    private fun updateFileSize(download: Download) {
        val fileSize: Long = fileTransfer!!.getFileTransferStatus().getFileSize()
        val readableString: String = FilesizeFormatter.createAutoReadableString(fileSize)
        download.filesize = readableString
    }
}
