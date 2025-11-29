package com.testing.demo.repository;

import com.testing.demo.entity.Swipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Nhớ import cái này
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwipeRepository extends JpaRepository<Swipe, String> {

    // Hàm cũ của bạn (Giữ nguyên)
    Optional<Swipe> findBySwiper_UserIdAndSwiped_UserIdAndAction(String swiperId, String swipedId, String action);

    // --- THÊM HÀM NÀY (Đã sửa tên biến cho khớp) ---
    /**
     * Lấy danh sách ID của tất cả những người mà 'swiperId' đã từng tương tác.
     * Chú ý: 's.swiped.id' giả định User entity của bạn có trường 'id'.
     * Nếu User dùng 'userId', hãy đổi thành 's.swiped.userId'
     */
    @Query("SELECT s.swiped.userId FROM Swipe s WHERE s.swiper.userId = :swiperId")
    List<String> findSwipedUserIds(@Param("swiperId") String swiperId);
}