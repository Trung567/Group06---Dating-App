package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_prompt_answer") // Bảng lưu câu trả lời (M:N)
public class UserPromptAnswer {

    @Id
    @UuidGenerator
    @Column(length = 36)
    private String answerId;

    // Quan hệ N:1 (Nhiều câu trả lời thuộc 1 User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ N:1 (Nhiều câu trả lời cho 1 Câu hỏi)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    // Thuộc tính quan trọng bị thiếu trong file SQL: Câu trả lời
    @Column(columnDefinition = "TEXT")
    private String answer;
}