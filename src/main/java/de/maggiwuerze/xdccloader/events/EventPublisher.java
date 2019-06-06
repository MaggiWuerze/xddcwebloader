package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.model.Download;
import de.maggiwuerze.xdccloader.persistency.DownloadRepository;
import de.maggiwuerze.xdccloader.util.SocketEvents;
import de.maggiwuerze.xdccloader.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    @Autowired
    DownloadRepository downloadRepository;

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final String MESSAGE_PREFIX = "/topic";

    public void updateDownloadState(State state, Download download) {

        download.setStatus(state);
        downloadRepository.save(download);

        DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, download);
        applicationEventPublisher.publishEvent(downloadUpdateEvent);

    }

    public void sendWebsocketEvent(SocketEvents event, Object payload) {

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + event.getRoute(), payload);

    }
}
