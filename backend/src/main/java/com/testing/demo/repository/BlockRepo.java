package com.testing.demo.repository;

import com.testing.demo.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface BlockRepo extends JpaRepository<Block, String> {

    Optional<Block> findByBlocker_UserIdAndBlocked_UserId(String blockerId, String blockedId);
    // Lấy danh sách ID của những người mà User này đã chặn
    @Query("SELECT b.blocked.userId FROM Block b WHERE b.blocker.userId = :userId")
    List<String> findBlockedUserIds(@Param("userId") String userId);

    // (Tùy chọn) Lấy danh sách ID của những người đã chặn User này (nếu muốn ẩn 2 chiều)
    @Query("SELECT b.blocker.userId FROM Block b WHERE b.blocked.userId = :userId")
    List<String> findBlockerUserIds(@Param("userId") String userId);
}