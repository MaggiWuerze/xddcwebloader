package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.events.CustomSpringEventListener;
import de.maggiwuerze.xdccloader.events.DownloadDoneEvent;
import de.maggiwuerze.xdccloader.events.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.model.Download;
import org.apache.commons.io.FilenameUtils;
import org.pircbotx.dcc.FileTransfer;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FileTransferProgressWatcher {

    Logger logger = Logger.getLogger("Class FileTransferProgressWatcher");

    ReceiveFileTransfer fileTransfer;
    Download download;

    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    ScheduledFuture<?> schedulerResult;


    private ApplicationEventPublisher applicationEventPublisher;

    public FileTransferProgressWatcher(ReceiveFileTransfer fileTransfer, Download download) {
        this.fileTransfer = fileTransfer;
        this.download = download;

        try {

        applicationEventPublisher =
                new AnnotationConfigApplicationContext(
                        CustomSpringEventListener.class);

        }catch (Exception e){

            logger.log(Level.WARNING, "Error in progressWatcher", e);

        }

        logger.info("progress watcher for download tih id :" + download.getId());
    }

    public FileTransferProgressWatcher(Download download, ApplicationEventPublisher applicationEventPublisher) {

        this.download = download;
        this.applicationEventPublisher = applicationEventPublisher;



    }
    public FileTransferProgressWatcher(ReceiveFileTransfer fileTransfer, Download download, ApplicationEventPublisher applicationEventPublisher) {

        this.fileTransfer = fileTransfer;
        this.download = download;
        this.applicationEventPublisher = applicationEventPublisher;



    }


    public void run() {

        logger.info("starting progress watcher for download tih id :" + download.getId());

        download.setStatus(State.TRANSMITTING);
        schedulerResult = exec.scheduleAtFixedRate(() -> {

            try {
                long bytesTransfered = fileTransfer.getBytesTransfered();
                long fileSize = fileTransfer.getFileSize();

                Long newProgress = (long) Math.ceil(((float) bytesTransfered / fileSize) * 100);
                download.setProgress(newProgress);

                DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, download);
                applicationEventPublisher.publishEvent(downloadUpdateEvent);

                if(newProgress == 100 && fileTransfer.isFinished()){

                    download.setStatus(State.FINALIZING);
                    DownloadDoneEvent downloadDoneEvent = new DownloadDoneEvent(this, download);
                    applicationEventPublisher.publishEvent(downloadDoneEvent);

                    schedulerResult.cancel(false);

                }


            } catch (Exception e) {
                logger.log(Level.WARNING, "Error in progressWatcher", e);
            }

        }, 0, 5, TimeUnit.SECONDS);

    }

    public void setFileTransfer(ReceiveFileTransfer fileTransfer) {
        this.fileTransfer = fileTransfer;
    }

    public Download getDownload() {
        return download;
    }

    public void setDownload(Download download) {
        this.download = download;
    }
}
