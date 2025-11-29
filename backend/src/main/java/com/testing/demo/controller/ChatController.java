package com.testing.demo.controller;

import com.testing.demo.dto.MessageResponseDto;
import com.testing.demo.dto.NewMessageRequestDto; // (Sửa DTO ở bước 3)
import com.testing.demo.entity.Message;
import com.testing.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal; // <-- IMPORT LẠI

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void processMessage(
            @Payload NewMessageRequestDto messageRequest,
            Principal principal // <-- LẤY USER TỪ TOKEN
    ) {

        // 1. Lấy ID của người gửi (đã được xác thực 100%)
        String senderId = principal.getName();

        // 2. Gọi Service (bộ não) để lưu tin nhắn
        Message savedMessage = messageService.saveNewMessage(
                messageRequest.getConversationId(),
                senderId, // Dùng senderId bảo mật
                messageRequest.getContent()
        );

        // 3. Chuyển đổi Entity sang DTO
        MessageResponseDto responseDto = MessageResponseDto.fromEntity(savedMessage);

        // 4. Lấy ID của người nhận
        String receiverId = savedMessage.getReceiver().getUserId();

        // 5. Đẩy (Push) tin nhắn đến KÊNH CÔNG KHAI CỦA PHÒNG CHAT
        // (Chúng ta sẽ chuyển sang kênh cá nhân /user/... ở bước nâng cao sau)
        String destination = "/topic/conversation/" + savedMessage.getConversation().getConversationId();
        messagingTemplate.convertAndSend(destination, responseDto);
    }
}