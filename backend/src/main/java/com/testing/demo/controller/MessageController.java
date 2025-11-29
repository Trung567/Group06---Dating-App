package com.testing.demo.controller;

import com.testing.demo.dto.MessageResponseDto;
import com.testing.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Import
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations") // Tiền tố chung
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * API để lấy lịch sử tin nhắn của một cuộc trò chuyện
     * {conversationId} là một "biến đường dẫn" (Path Variable)
     */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageResponseDto>> getChatHistory(
            @PathVariable String conversationId
    ) {

        // 1. Lấy ID của user đang đăng nhập (từ Token)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        // 2. Gọi Service để lấy danh sách tin nhắn
        // (Service sẽ tự xử lý lỗi bảo mật nếu user cố xem trộm)
        List<MessageResponseDto> messageDtos = messageService.getMessages(conversationId, currentUserId);

        // 3. Trả về 200 OK và danh sách
        return ResponseEntity.ok(messageDtos);
    }
}