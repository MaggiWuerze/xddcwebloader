package de.maggiwuerze.xdccloader.events;

import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.irc.IrcBot;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.download.DownloadState;
import de.maggiwuerze.xdccloader.service.DownloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

	private static final String MESSAGE_PREFIX = "/topic";
	private final DownloadService downloadService;
	private final SimpMessagingTemplate websocket;
	private final ApplicationEventPublisher applicationEventPublisher;

	public void updateDownloadState(DownloadState state, Download download) {

		download.setStatus(state);
		downloadService.update(download);

		DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, download.getId());
		applicationEventPublisher.publishEvent(downloadUpdateEvent);
	}

	public void sendWebsocketEvent(SocketEvents event, Object payload) {

		this.websocket.convertAndSend(
			MESSAGE_PREFIX + event.getRoute(), payload);
	}

	public void handleError(IrcBot bot, Exception exception) {

		bot.stopBotReconnect();
		Download download = downloadService.getById(bot.getDownloadId());
		String message = String.format(DownloadState.ERROR.getExternalString(), exception.getMessage());
		try {
			download.setStatusMessage(message);
			updateDownloadState(DownloadState.ERROR, download);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
