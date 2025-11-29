package com.testing.demo.repository;

import com.testing.demo.entity.Preference;
import com.testing.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    // Tìm preference dựa trên User entity
    Optional<Preference> findByUser(User user);
}