package com.testing.demo.controller;

import com.testing.demo.dto.SwipeRequestDto;
import com.testing.demo.service.SwipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map; // Import Map

@RestController
@RequestMapping("/api/swipe")
@RequiredArgsConstructor
public class SwipeController {

    private final SwipeService swipeService;

    /**
     * API để xử lý hành động quẹt (like/dislike)
     */
    @PostMapping
    public ResponseEntity<?> swipe(@RequestBody SwipeRequestDto swipeRequest) {

        // 1. Lấy ID của user đang đăng nhập (từ Token)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        // 2. Gọi "bộ não" Service để xử lý
        boolean isMatch = swipeService.handleSwipe(
                currentUserId,
                swipeRequest.getSwipedUserId(),
                swipeRequest.getAction()
        );

        // 3. Trả về kết quả (để Front-end biết có hiển thị "It's a Match!" không)
        // Chúng ta dùng Map.of() để tạo một đối tượng JSON đơn giản
        return ResponseEntity.ok(Map.of("isMatch", isMatch));
    }
}