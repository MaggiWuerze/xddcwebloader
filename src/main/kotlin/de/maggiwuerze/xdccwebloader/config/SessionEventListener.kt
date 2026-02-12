package de.maggiwuerze.xdccwebloader.config

import de.maggiwuerze.xdccwebloader.events.EventPublisher
import de.maggiwuerze.xdccwebloader.events.SocketEvents
import jakarta.servlet.http.HttpSessionEvent
import jakarta.servlet.http.HttpSessionListener
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
class SessionEventListener(
    private val eventPublisher: EventPublisher
) {

    val log = LoggerFactory.getLogger(this.javaClass.name)

    @org.springframework.context.annotation.Bean
    fun httpSessionListener(): HttpSessionListener {
        return object : HttpSessionListener {
            // This method will be called when session is created
            override fun sessionCreated(se: HttpSessionEvent?) {
            }

            // This method will be automatically called when session is destroyed
            override fun sessionDestroyed(se: HttpSessionEvent) {
                log.info("Session Destroyed, Session id:" + se.getSession().getId())
                eventPublisher.sendWebsocketEvent(SocketEvents.SESSION_TIMEOUT, "Session timeout!")
            }
        }
    }
}