package com.testing.demo.repository;

import com.testing.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // <-- Thêm import này
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Các phương thức tìm kiếm cho Auth (Đăng nhập/Đăng ký)
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    // --- PHƯƠNG THỨC MỚI CHO RECOMMENDATION ---
    /**
     * Tự động tạo câu lệnh SQL: "SELECT * FROM User WHERE user_id != ?"
     * (Lọc ở tầng CSDL, hiệu năng cao)
     */
    List<User> findAllByUserIdNot(String userId);
    List<User> findAllByIdNotIn(List<String> ids);
}