package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.model.DownloadState;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.util.DownloadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    DownloadManager downloadManager = DownloadManager.getInstance();

    @Autowired
    private SimpMessagingTemplate websocket;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    final String MESSAGE_PREFIX = "/topic";

    public void updateDownloadState(DownloadState state, Download download) {

        download.setStatus(state);
        downloadManager.update(download);

        DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, download.getId());
        applicationEventPublisher.publishEvent(downloadUpdateEvent);
    }

    public void sendWebsocketEvent(SocketEvents event, Object payload) {

        this.websocket.convertAndSend(
                MESSAGE_PREFIX + event.getRoute(), payload);

    }

    public void handleError(IrcBot bot, Exception exception) {

        bot.stopBotReconnect();
        String message = String.format(DownloadState.ERROR.getExternalString(), exception.getMessage());
        bot.getDownload().setStatusMessage(message);
        updateDownloadState(DownloadState.ERROR, bot.getDownload());

    }

}
