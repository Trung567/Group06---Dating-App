package com.testing.demo.repository;

import com.testing.demo.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // <-- Import dòng này

@Repository
public interface MatchRepository extends JpaRepository<Match, String> { // (Kiểu ID là String)

    /**
     * Tự động tạo câu lệnh SQL:
     * "SELECT * FROM `Match` WHERE user1_id = ? OR user2_id = ?"
     * Dùng để tìm tất cả các match của một user cụ thể.
     */
    List<Match> findByUser1_UserIdOrUser2_UserId(String userId1, String userId2);
}