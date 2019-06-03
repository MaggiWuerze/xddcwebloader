package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.model.Download;
import org.pircbotx.dcc.FileTransfer;
import org.pircbotx.dcc.ReceiveFileTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProgressWatcherFactory {


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public FileTransferProgressWatcher getProgressWatcher(Download download){

            FileTransferProgressWatcher watcher = new FileTransferProgressWatcher(download, applicationEventPublisher);

            return watcher;

    }

    public FileTransferProgressWatcher getProgressWatcher(ReceiveFileTransfer fileTransfer, Download download){

            FileTransferProgressWatcher watcher = new FileTransferProgressWatcher(fileTransfer, download, applicationEventPublisher);

            return watcher;

    }

}
