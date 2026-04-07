package com.SmartAttendance.demo.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Configure message routing
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // Enables simple in-memory message broker
        // Messages sent to /topic will be broadcast to subscribers
        config.enableSimpleBroker("/topic");

        // Prefix for messages sent from client to server
        config.setApplicationDestinationPrefixes("/app");
    }

    // Register WebSocket endpoint
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")   // Endpoint for WebSocket connection
                .setAllowedOriginPatterns("*")  // Allow all origins (adjust in prod)
                .withSockJS();  // Fallback for browsers that don’t support WebSocket
    }
}
