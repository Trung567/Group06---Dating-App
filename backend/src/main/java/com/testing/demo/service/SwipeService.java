package com.testing.demo.service;

import com.testing.demo.entity.Conversation;
import com.testing.demo.entity.Match;
import com.testing.demo.entity.Swipe;
import com.testing.demo.entity.User;
import com.testing.demo.repository.ConversationRepository;
import com.testing.demo.repository.MatchRepository;
import com.testing.demo.repository.SwipeRepository;
import com.testing.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SwipeService {

    // Tiêm (Inject) TẤT CẢ các Repository liên quan đến việc tạo Match
    private final SwipeRepository swipeRepository;
    private final MatchRepository matchRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository; // Cần để lấy đối tượng User

    /**
     * Xử lý logic quẹt.
     * Trả về 'true' nếu có match, 'false' nếu không.
     */
    @Transactional // Đảm bảo tất cả các thao tác CSDL cùng thành công hoặc thất bại
    public boolean handleSwipe(String currentUserId, String swipedUserId, String action) {

        // 1. Lấy thông tin 2 User
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy User (swiper)"));
        User swipedUser = userRepository.findById(swipedUserId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy User (swiped)"));

        // 2. Lưu hành động quẹt này vào CSDL
        Swipe swipe = new Swipe();
        swipe.setSwiper(currentUser);
        swipe.setSwiped(swipedUser);
        swipe.setAction(action);
        swipeRepository.save(swipe);

        // 3. Nếu hành động là "dislike", dừng lại ở đây
        if (!action.equals("like")) {
            return false; // Không có match
        }

        // 4. (Phần quan trọng) Nếu là "like", kiểm tra xem người kia (swipedUser)
        //    đã "like" mình (currentUser) TRƯỚC ĐÓ chưa?
        Optional<Swipe> reverseSwipe = swipeRepository
                .findBySwiper_UserIdAndSwiped_UserIdAndAction(swipedUserId, currentUserId, "like");

        // 5. Xử lý kết quả
        if (reverseSwipe.isPresent()) {
            // --- CÓ MATCH! ---

            // 6a. Tạo một bản ghi Match mới
            Match newMatch = new Match();
            newMatch.setUser1(currentUser);
            newMatch.setUser2(swipedUser);
            newMatch.setStatus("active");
            Match savedMatch = matchRepository.save(newMatch);

            // 6b. Tạo một Conversation (phòng chat) mới
            // (Nhờ @MapsId, chúng ta chỉ cần setMatch)
            Conversation newConversation = new Conversation();
            newConversation.setMatch(savedMatch);
            conversationRepository.save(newConversation);

            return true; // Báo cho Controller biết là đã MATCH
        }

        // 6c. Nếu không có "reverse swipe", chỉ là "like" 1 chiều
        return false; // Không có match
    }
}