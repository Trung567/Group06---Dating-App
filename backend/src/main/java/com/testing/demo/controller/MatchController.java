package com.testing.demo.controller;

import com.testing.demo.dto.MatchResponseDto;
import com.testing.demo.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    /**
     * API để lấy danh sách tất cả các match
     */
    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getMyMatches() {

        // 1. Lấy ID của user đang đăng nhập (từ Token)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        // 2. Gọi Service để lấy danh sách DTO
        List<MatchResponseDto> matchDtos = matchService.getMatches(currentUserId);

        // 3. Trả về 200 OK và danh sách
        return ResponseEntity.ok(matchDtos);
    }
}