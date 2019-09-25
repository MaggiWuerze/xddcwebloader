package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.events.EventPublisher;
import de.maggiwuerze.xdccloader.events.SocketEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class SessionEventListener {

    Logger logger = Logger.getLogger("Class SessionEndedListener");

    @Autowired
    private EventPublisher eventPublisher;

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {

            // This method will be called when session is created
            @Override
            public void sessionCreated(HttpSessionEvent se) {
//                logger.log(Level.INFO, "Session Created with session id+" + se.getSession().getId());
            }

            // This method will be automatically called when session is destroyed
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                logger.log(Level.INFO, "Session Destroyed, Session id:" + se.getSession().getId());
                eventPublisher.sendWebsocketEvent(SocketEvents.SESSION_TIMEOUT, "Session timeout!");
            }
        };
    }

}