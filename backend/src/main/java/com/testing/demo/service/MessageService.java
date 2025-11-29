package com.testing.demo.service;

import com.testing.demo.dto.MessageResponseDto;
import com.testing.demo.entity.Conversation;
import com.testing.demo.entity.Message;
import com.testing.demo.entity.User; // <-- THÊM IMPORT
import com.testing.demo.repository.ConversationRepository;
import com.testing.demo.repository.MessageRepository;
import com.testing.demo.repository.UserRepository; // <-- THÊM IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- THÊM IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- THÊM IMPORT

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository; // <-- THÊM REPO NÀY

    /**
     * Lấy tất cả tin nhắn của một cuộc trò chuyện
     * (Hàm này bạn đã có)
     */
    public List<MessageResponseDto> getMessages(String conversationId, String currentUserId) {

        // (Bảo mật) Kiểm tra xem user này có quyền xem cuộc trò chuyện không
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));

        String user1Id = conversation.getMatch().getUser1().getUserId();
        String user2Id = conversation.getMatch().getUser2().getUserId();

        if (!user1Id.equals(currentUserId) && !user2Id.equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền xem cuộc trò chuyện này");
        }

        // (Logic) Lấy danh sách tin nhắn
        List<Message> messages = messageRepository
                .findAllByConversation_ConversationIdOrderByCreatedAtAsc(conversationId);

        // Chuyển đổi sang DTO
        return messages.stream()
                .map(MessageResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // --- BẮT ĐẦU PHẦN MỚI ---

    /**
     * Lưu một tin nhắn mới và trả về Entity đã lưu
     */
    @Transactional
    public Message saveNewMessage(String conversationId, String senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc trò chuyện"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người gửi"));

        // Kiểm tra bảo mật: Người gửi có thuộc cuộc trò chuyện không?
        User user1 = conversation.getMatch().getUser1();
        User user2 = conversation.getMatch().getUser2();

        if (!user1.getUserId().equals(senderId) && !user2.getUserId().equals(senderId)) {
            throw new AccessDeniedException("Người gửi không thuộc cuộc trò chuyện này");
        }

        User receiver = user1.getUserId().equals(senderId) ? user2 : user1;

        Message newMessage = new Message();
        newMessage.setConversation(conversation);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setContent(content);
        newMessage.setStatus("sent");

        // --- SỬA LỖI Ở DÒNG NÀY ---
        // SAI: MessageRepository.save(...) -> Gọi vào class tĩnh
        // ĐÚNG: messageRepository.save(...) -> Gọi vào biến instance
        Message savedMessage = messageRepository.save(newMessage);

        // Cập nhật thông tin chat
        conversation.setLastMessageId(savedMessage.getMessageId());
        conversation.setUpdatedAt(savedMessage.getCreatedAt());
        conversationRepository.save(conversation);

        return savedMessage;
    }
    public List<MessageResponseDto> getMessages(String chatRoomId, int page, int size) {
        // Tạo đối tượng Pageable (sắp xếp tin nhắn mới nhất lên đầu hoặc cuối tùy nhu cầu)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Message> messagePage = MessageRepository.findByConversation_ConversationId(chatRoomId, pageable);

        // Convert Page<Entity> sang List<DTO>
        return messagePage.getContent().stream()
                .map(message -> {
                    // Logic chuyển đổi viết trực tiếp ở đây
                    MessageResponseDto dto = new MessageResponseDto();
                    dto.setContent(message.getContent());
                    dto.setSenderId(message.getSender().getId());
                    dto.setCreatedAt(message.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}