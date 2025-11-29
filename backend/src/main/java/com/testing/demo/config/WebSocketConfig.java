package com.testing.demo.config;

import com.testing.demo.filter.WebSocketAuthInterceptor; // <-- 1. IMPORT
import lombok.RequiredArgsConstructor; // <-- 2. IMPORT
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration; // <-- 3. IMPORT
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor // <-- 4. THÊM
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 5. Tiêm (Inject) "Người gác cổng"
    private final WebSocketAuthInterceptor authInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 6. HÀM MỚI: Đăng ký "người gác cổng" (Interceptor)
     * Nó sẽ chạy "preSend" (trước khi gửi) cho mọi tin nhắn "INBOUND" (đi vào)
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}