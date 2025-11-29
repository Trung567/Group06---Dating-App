package com.testing.demo.dto;

import com.testing.demo.entity.Message;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class MessageResponseDto {

    private String messageId;
    private String conversationId;
    private String senderId;
    private String content;
    private Timestamp createdAt;

    /**
     * Hàm chuyển đổi (Converter)
     * Biến 1 Message Entity thành 1 DTO
     */
    public static MessageResponseDto fromEntity(Message message) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setMessageId(message.getMessageId());
        dto.setConversationId(message.getConversation().getConversationId());
        dto.setSenderId(message.getSender().getUserId());
        dto.setContent(message.getContent());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
}