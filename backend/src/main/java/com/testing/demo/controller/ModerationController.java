package com.testing.demo.controller;

import com.testing.demo.dto.ReportRequest;
import com.testing.demo.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    // POST /api/block/{userIdToBlock}
    @PostMapping("/block/{userId}")
    public ResponseEntity<?> blockUser(@PathVariable String userId, Authentication authentication) {
        moderationService.blockUser(authentication.getName(), userId);
        return ResponseEntity.ok("User blocked successfully");
    }

    // POST /api/report/{userIdToReport}
    @PostMapping("/report/{userId}")
    public ResponseEntity<?> reportUser(@PathVariable String userId,
                                        @RequestBody ReportRequest request,
                                        Authentication authentication) {
        moderationService.reportUser(authentication.getName(), userId, request.getReason());
        return ResponseEntity.ok("User reported successfully");
    }
}