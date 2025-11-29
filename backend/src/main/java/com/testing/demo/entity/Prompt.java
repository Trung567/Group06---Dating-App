package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prompt")
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promptId;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String questionText;

    // Quan hệ 1:N với các câu trả lời
    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL)
    private List<UserPromptAnswer> answers;

}