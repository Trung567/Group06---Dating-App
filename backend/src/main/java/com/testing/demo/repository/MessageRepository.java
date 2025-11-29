package com.testing.demo.repository;

import com.testing.demo.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    // Cách 1: Lấy list (dùng cho API get chat history cũ)
    List<Message> findAllByConversation_ConversationIdOrderByCreatedAtAsc(String conversationId);

    // Cách 2: Lấy phân trang (dùng cho API mới, sửa tên hàm cho đúng chuẩn JPA)
    // findBy + [Tên biến trong Message] + _ + [Tên biến ID trong Conversation]
    static Page<Message> findByConversation_ConversationId(String conversationId, Pageable pageable) {
        return null;
    }
}