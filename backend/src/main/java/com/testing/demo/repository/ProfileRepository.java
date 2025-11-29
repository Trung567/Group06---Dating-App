package com.testing.demo.repository;

import com.testing.demo.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    // Tự động tìm Profile bằng cách tham chiếu qua User -> userId
    Optional<Profile> findByUser_UserId(String userId);
}