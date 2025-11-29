package com.testing.demo.service;

import com.testing.demo.entity.Block;
import com.testing.demo.entity.Report;
import com.testing.demo.entity.User;
import com.testing.demo.repository.BlockRepo;
import com.testing.demo.repository.ReportRepo;
import com.testing.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ModerationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlockRepo blockRepository;
    @Autowired
    private ReportRepo reportRepository;

    public void blockUser(String blockerEmail, String blockedUserId) {
        User blocker = userRepository.findByEmail(blockerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User blocked = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new RuntimeException("User to block not found"));

        // Tạo Block entity
        Block block = new Block();
        block.setBlocker(blocker);
        block.setBlocked(blocked);
        block.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));

        blockRepository.save(block);
    }

    public void reportUser(String reporterEmail, String reportedUserId, String reason) {
        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User reported = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new RuntimeException("User to report not found"));

        // Tạo Report entity
        Report report = new Report();
        report.setReporter(reporter);
        report.setReported(reported);
        report.setReason(reason);
        report.setCreatedAt(Timestamp.valueOf(java.time.LocalDateTime.now()));

        reportRepository.save(report);
    }
}