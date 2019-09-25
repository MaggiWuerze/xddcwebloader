package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.DownloadState;
import org.pircbotx.dcc.DccState;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTransferProgressWatcher {

    Logger logger = Logger.getLogger("Class FileTransferProgressWatcher");

    ReceiveFileTransfer fileTransfer;
    Long downloadId;
    DownloadManager downloadManager = DownloadManager.getInstance();
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> schedulerResult;
    final Logger LOG = Logger.getLogger("Class FileTransferProgressWatcher");

    private ApplicationEventPublisher applicationEventPublisher;


    public FileTransferProgressWatcher(Long downloadId, ApplicationEventPublisher applicationEventPublisher) {

        this.downloadId = downloadId;
        this.applicationEventPublisher = applicationEventPublisher;

    }

    public void run() {

        logger.info("starting progress watcher for download with id :" + downloadId);
        downloadManager.getById(downloadId).setStatus(DownloadState.TRANSMITTING);

        applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, downloadId));

        schedulerResult = exec.scheduleAtFixedRate(() -> {

            try {

                Download download = downloadManager.getById(downloadId);

                if (download.getFilesize().equals("-")) {
                    updateFileSize(download);
                }

                Double newProgress = fileTransfer.getFileTransferStatus().getPercentageComplete();
                newProgress = BigDecimal.valueOf(newProgress)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                download.setTimeRemaining(formatRemainingTime(fileTransfer));

                if (download.getStatus().equals(DownloadState.STOPPED)) {

                    applicationEventPublisher.publishEvent(new DownloadDeleteEvent(this, download.getId()));
                    schedulerResult.cancel(true);

                } else if (fileTransfer.getFileTransferStatus().getDccState().equals(DccState.ERROR)) {

                    download.setStatus(DownloadState.ERROR);
                    download.setStatusMessage(fileTransfer.getFileTransferStatus().getException().getLocalizedMessage());
                    applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, download.getId()));

                    LOG.log(Level.SEVERE, String.format("error on filetransfer for bot %s", download.getFileRefId()));
                    LOG.log(Level.SEVERE, fileTransfer.getFileTransferStatus().getException().getLocalizedMessage());

                    schedulerResult.cancel(true);

                } else if (newProgress == 100.0 && fileTransfer.getFileTransferStatus().isSuccessful()) {


                    download.setStatus(DownloadState.FINALIZING);
                    //do some stuff to finalize


                    download.setStatus(DownloadState.DONE);

                    applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, download.getId()));

                    schedulerResult.cancel(true);

                } else {

                    download.setProgress(newProgress);
                    String averageSpeed = FilesizeFormatter.createAutoReadableString(fileTransfer.getFileTransferStatus().getAverageBytesPerSecond());
                    download.setAverageSpeed(averageSpeed);
                    applicationEventPublisher.publishEvent(new DownloadUpdateEvent(this, download.getId()));

                }

            } catch (Exception e) {
                logger.log(Level.WARNING, "Error in progressWatcher", e);
            }

        }, 0, 1, TimeUnit.SECONDS);

    }

    private String formatRemainingTime(ReceiveFileTransfer fileTransfer) {

        long secondsRemaining = fileTransfer.getFileTransferStatus().getFileSize() - fileTransfer.getFileTransferStatus().getBytesTransfered();
        if (fileTransfer.getFileTransferStatus().getAverageBytesPerSecond() != 0) {

            secondsRemaining = secondsRemaining / fileTransfer.getFileTransferStatus().getAverageBytesPerSecond();

        }

        long seconds = secondsRemaining;
        long minutes = 0;
        long hours = 0;
        long days = 0;

        String result = String.format("%02dm:%02ds", minutes, seconds);

        if (secondsRemaining > 60) {

            minutes = seconds / 60;
            seconds = seconds % 60;
            result = String.format("%02dm:%02ds", minutes, seconds);

            if (minutes > 60) {

                hours = minutes / 60;
                minutes = minutes % 60;
                result = String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);

                if (hours > 24) {

                    days = hours / 24;
                    hours = hours % 24;
                    result = String.format("%02dd:%02dh:%02dm:%02ds", days, hours, minutes, seconds);

                }
            }
        }

        return result;

    }

    public void cancel(boolean mayInterrupt) {

        schedulerResult.cancel(mayInterrupt);

    }

    private void updateFileSize(Download download) {
        long fileSize = fileTransfer.getFileTransferStatus().getFileSize();
        String readableString = FilesizeFormatter.createAutoReadableString(fileSize);
        download.setFilesize(readableString);
    }

    public void setFileTransfer(ReceiveFileTransfer fileTransfer) {
        this.fileTransfer = fileTransfer;
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }
}
