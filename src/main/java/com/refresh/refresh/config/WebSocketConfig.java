package com.refresh.refresh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        // 클라이언트가 구독할 수 있는 토픽 prefix 설정
        config.enableSimpleBroker("/topic");
        
        // 클라이언트가 서버로 메시지를 보낼 때 사용할 prefix 설정 (필요시)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트 설정
        registry.addEndpoint("/ws-danger-alerts")
                .setAllowedOriginPatterns("*")  // CORS 설정
                .withSockJS();  // SockJS 폴백 지원
    }
}
