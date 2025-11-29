package com.testing.demo.service;

import com.testing.demo.dto.MatchResponseDto;
import com.testing.demo.entity.Match;
import com.testing.demo.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    /**
     * Lấy tất cả các match của một user
     */
    public List<MatchResponseDto> getMatches(String currentUserId) {

        // 1. Gọi Repository để lấy danh sách Match (Entities)
        List<Match> matches = matchRepository.findByUser1_UserIdOrUser2_UserId(currentUserId, currentUserId);

        // 2. Dùng Stream để chuyển đổi List<Match> thành List<MatchResponseDto>
        return matches.stream()
                .map(match -> MatchResponseDto.fromEntity(match, currentUserId)) // Dùng converter
                .collect(Collectors.toList());
    }
}