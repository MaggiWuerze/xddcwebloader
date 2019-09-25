package de.maggiwuerze.xdccloader.util;

import de.maggiwuerze.xdccloader.model.download.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ProgressWatcherFactory {


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public FileTransferProgressWatcher getProgressWatcher(Long downloadId){

            FileTransferProgressWatcher watcher = new FileTransferProgressWatcher(downloadId, applicationEventPublisher);

            return watcher;

    }

}
