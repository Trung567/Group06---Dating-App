package com.testing.demo.repository;

import com.testing.demo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
    // Lấy tất cả ảnh của một user
    List<Photo> findAllByUser_UserId(String userId);
}