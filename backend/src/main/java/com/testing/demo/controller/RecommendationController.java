package com.testing.demo.controller;

import com.testing.demo.dto.ProfileResponseDto;
import com.testing.demo.entity.User;
import com.testing.demo.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations") // API được bảo vệ
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * API để lấy danh sách gợi ý hồ sơ
     */
    @GetMapping
    public ResponseEntity<List<ProfileResponseDto>> getRecommendations() {

        // 1. Lấy user ID từ token (Nhờ JwtAuthFilter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Đây chính là userId

        // 2. Gọi Service để lấy danh sách User (Entities)
        List<User> userEntities = recommendationService.getRecommendations(currentUserId);

        // 3. Chuyển đổi danh sách User (Entities) sang DTO (an toàn)
        List<ProfileResponseDto> responseDtos = userEntities.stream()
                .map(ProfileResponseDto::fromEntity) // Dùng hàm converter ta đã viết
                .collect(Collectors.toList());

        // 4. Trả về 200 OK và danh sách DTO
        return ResponseEntity.ok(responseDtos);
    }
}