package de.maggiwuerze.xdccloader.service;

import de.maggiwuerze.xdccloader.events.SocketEvents;
import de.maggiwuerze.xdccloader.events.channel.server.ChannelCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadCreationEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadDeleteEvent;
import de.maggiwuerze.xdccloader.events.download.DownloadUpdateEvent;
import de.maggiwuerze.xdccloader.events.server.ServerCreationEvent;
import de.maggiwuerze.xdccloader.model.download.Download;
import de.maggiwuerze.xdccloader.model.entity.Channel;
import de.maggiwuerze.xdccloader.model.entity.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventService {

	private static final String MESSAGE_PREFIX = "/topic";
	private final SimpMessagingTemplate websocket;
	private final ApplicationEventPublisher applicationEventPublisher;
	private final EntityLinks entityLinks;

	public void publishEvent(SocketEvents destinationSuffix, Object payload) {
		if (payload instanceof Download) {
			Long downloadId = ((Download) payload).getId();
			switch (destinationSuffix) {
				case UPDATED_DOWNLOAD:
					DownloadUpdateEvent downloadUpdateEvent = new DownloadUpdateEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadUpdateEvent);
					break;

				case NEW_DOWNLOAD:
					DownloadCreationEvent downloadCreationEvent = new DownloadCreationEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadCreationEvent);
					break;

				case DELETED_DOWNLOAD:
					DownloadDeleteEvent downloadDeleteEvent = new DownloadDeleteEvent(this, downloadId);
					applicationEventPublisher.publishEvent(downloadDeleteEvent);
					break;

				default:
			}
		}
		if (payload instanceof Server) {
			ServerCreationEvent downloadCreationEvent = new ServerCreationEvent(this, (Server) payload);
			applicationEventPublisher.publishEvent(downloadCreationEvent);
		}
		if (payload instanceof Channel) {
			ChannelCreationEvent downloadCreationEvent = new ChannelCreationEvent(this, (Channel) payload);
			applicationEventPublisher.publishEvent(downloadCreationEvent);
		}

		this.websocket.convertAndSend(
			MESSAGE_PREFIX + destinationSuffix.getRoute(), payload);
	}
}
