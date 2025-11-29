package com.testing.demo.service;

import com.testing.demo.dto.UserPromptRequest;
import com.testing.demo.entity.Hobby;
import com.testing.demo.entity.Prompt;
import com.testing.demo.entity.User;
import com.testing.demo.entity.UserPromptAnswer;
import com.testing.demo.repository.HobbyRepository;
import com.testing.demo.repository.PromptRepo;
import com.testing.demo.repository.UserPromptAnswerRepo;
import com.testing.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Lấy thông tin User bằng ID
     */
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với ID: " + userId));
    }
    public void updateLocation(String email, Double lat, Double lon) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setLatitude(lat);
        user.setLongitude(lon);
        userRepository.save(user);
    }
    // Trong ProfileService.java
    @Autowired
    private HobbyRepository hobbyRepository;
    @Autowired
    private PromptRepo promptRepository;
    @Autowired
    private UserPromptAnswerRepo userPromptAnswerRepository;

    // --- PUBLIC METHODS ---
    public List<Hobby> getAllHobbies() {
        return hobbyRepository.findAll();
    }

    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }

// --- UPDATE METHODS ---

    // Cập nhật sở thích
    public void updateHobbies(String email, List<String> hobbyIds) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Lấy danh sách Hobby từ DB dựa trên list ID gửi lên
        List<Hobby> selectedHobbies = hobbyRepository.findAllById(hobbyIds);

        // Update vào user
        // Lưu ý: Set sẽ tự động xử lý việc xóa cái cũ không có trong list mới
        user.setHobbies(new HashSet<>(selectedHobbies));

        userRepository.save(user);
    }

    // Cập nhật câu trả lời Prompt
// Cách đơn giản nhất: Xóa hết câu cũ của user, lưu câu mới
    @Transactional // Quan trọng để rollback nếu lỗi
    public void updateUserPrompts(String email, List<UserPromptRequest> requests) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 1. Xóa câu trả lời cũ (nếu muốn reset hoàn toàn)
        // user.getPromptAnswers().clear(); // Cách dùng orphanRemoval
        // Hoặc dùng Repository xóa thủ công nếu cần thiết

        // Cách an toàn với orphanRemoval=true trong User entity:
        if (user.getPromptAnswers() == null) {
            user.setPromptAnswers(new ArrayList<>());
        } else {
            user.getPromptAnswers().clear(); // JPA sẽ tự động xóa record trong DB
        }

        // 2. Tạo danh sách mới
        for (UserPromptRequest req : requests) {
            Prompt prompt = promptRepository.findById(req.getPromptId())
                    .orElseThrow(() -> new RuntimeException("Prompt not found ID: " + req.getPromptId()));

            UserPromptAnswer answer = new UserPromptAnswer();
            answer.setUser(user);
            answer.setPrompt(prompt);
            answer.setAnswer(req.getAnswer());

            user.getPromptAnswers().add(answer);
        }

        userRepository.save(user);
    }
}