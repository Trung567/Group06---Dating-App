package com.testing.demo.dto;

import com.testing.demo.entity.Match;
import com.testing.demo.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class MatchResponseDto {

    private String matchId;
    private String conversationId; // ID của phòng chat

    // Thông tin về NGƯỜI KIA
    private String otherUserId;
    private String otherUserFirstName;
    private String otherUserLastName;
    private String otherUserMainPhotoUrl; // (Chúng ta sẽ thêm logic này sau)

    // Thông tin phi chuẩn hóa (từ bảng Conversation)
    private String lastMessagePreview; // (Chưa có, sẽ thêm sau)
    private Timestamp lastMessageTimestamp; // (Chưa có, sẽ thêm sau)

    /**
     * Hàm chuyển đổi (Converter)
     * Biến 1 Match Entity thành 1 DTO
     * @param match Đối tượng Match lấy từ CSDL
     * @param currentUserId ID của người dùng đang xem
     */
    public static MatchResponseDto fromEntity(Match match, String currentUserId) {
        MatchResponseDto dto = new MatchResponseDto();
        dto.setMatchId(match.getMatchId());

        if (match.getConversation() != null) {
            dto.setConversationId(match.getConversation().getConversationId());
            // (Sau này sẽ lấy lastMessage từ đây)
        }

        // --- Logic quan trọng: Xác định "Người kia" ---
        User user1 = match.getUser1();
        User user2 = match.getUser2();

        if (user1.getUserId().equals(currentUserId)) {
            // "Người kia" là User 2
            dto.setOtherUserId(user2.getUserId());
            dto.setOtherUserFirstName(user2.getFirstName());
            dto.setOtherUserLastName(user2.getLastName());
            // (Thêm logic lấy ảnh)
        } else {
            // "Người kia" là User 1
            dto.setOtherUserId(user1.getUserId());
            dto.setOtherUserFirstName(user1.getFirstName());
            dto.setOtherUserLastName(user1.getLastName());
            // (Thêm logic lấy ảnh)
        }

        return dto;
    }
}