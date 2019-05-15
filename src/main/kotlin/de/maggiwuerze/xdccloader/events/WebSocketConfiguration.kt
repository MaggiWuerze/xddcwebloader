package de.maggiwuerze.xdccloader.events

import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.stereotype.Component
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Component
@EnableWebSocketMessageBroker
class WebSocketConfiguration : WebSocketMessageBrokerConfigurer{


    val MESSAGE_PREFIX : String = "/topic"


    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/downloads").withSockJS()
    }


    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(MESSAGE_PREFIX)
        registry.setApplicationDestinationPrefixes("/app")
    }

}