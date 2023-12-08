package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.events.EventPublisher;
import de.maggiwuerze.xdccloader.events.SocketEvents;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SessionEventListener {

	@Autowired
	private EventPublisher eventPublisher;

	@Bean
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {

			// This method will be called when session is created
			@Override
			public void sessionCreated(HttpSessionEvent se) {
			}

			// This method will be automatically called when session is destroyed
			@Override
			public void sessionDestroyed(HttpSessionEvent se) {
				log.info("Session Destroyed, Session id:" + se.getSession().getId());
				eventPublisher.sendWebsocketEvent(SocketEvents.SESSION_TIMEOUT, "Session timeout!");
			}
		};
	}
}