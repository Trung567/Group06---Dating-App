package com.testing.demo.filter;

import com.testing.demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull; // <-- 1. THÊM IMPORT NÀY
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // (ApplicationUserDetailsService)

    @Override
    public Message<?> preSend(
            @NonNull Message<?> message, // <-- 2. THÊM @NonNull
            @NonNull MessageChannel channel // <-- 3. THÊM @NonNull
    ) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 4. THÊM KIỂM TRA NULL (SỬA LỖI NPE)
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                try {
                    String userId = jwtService.extractUserId(jwt);

                    if (userId != null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                        if (jwtService.isTokenValid(jwt, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                            accessor.setUser(authToken);
                            log.info("WebSocket: Đã xác thực User ID: {}", userId);
                        }
                    }
                } catch (Exception e) {
                    log.error("WebSocket: Xác thực thất bại - {}", e.getMessage());
                }
            }
        }
        return message;
    }
}