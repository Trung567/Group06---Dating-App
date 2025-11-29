package com.testing.demo.controller;

import com.testing.demo.dto.UserResponseDto; // Import DTO an toàn
import com.testing.demo.entity.User;
import com.testing.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Import
import org.springframework.security.core.context.SecurityContextHolder; // Import
import org.springframework.web.bind.annotation.*;
import com.testing.demo.dto.UserPromptRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Tất cả API liên quan đến User
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;


    /**
     * API để lấy thông tin của người dùng đang đăng nhập (đã xác thực)
     */
    @GetMapping("/me") // -> /api/users/me
    public ResponseEntity<UserResponseDto> getMyProfile() {

        // --- Đây là cách lấy User ID từ Token đã được xác thực ---
        // JwtAuthFilter của bạn đã làm việc này và đặt nó vào "Context"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Đây chính là userId từ token
        // ---

        // 1. Gọi Service để lấy thông tin User
        User user = userService.getUserById(currentUserId);

        // 2. Chuyển sang DTO an toàn (file bạn đã tạo ở bước đăng ký)
        UserResponseDto responseDto = UserResponseDto.fromEntity(user);

        // 3. Trả về
        return ResponseEntity.ok(responseDto);
    }
    @PutMapping("/me/hobbies")
    public ResponseEntity<?> updateHobbies(@RequestBody List<String> hobbyIds,
                                           Authentication authentication) {
        userService.updateHobbies(authentication.getName(), hobbyIds);
        return ResponseEntity.ok("Hobbies updated successfully");
    }

    // PUT /api/profile/me/prompts
    @PutMapping("/me/prompts")
    public ResponseEntity<?> updatePrompts(@RequestBody List<UserPromptRequest> request,
                                           Authentication authentication) {
        userService.updateUserPrompts(authentication.getName(), request);
        return ResponseEntity.ok("Prompts updated successfully");
    }
}