package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @Column(length = 36)
    private String conversationId; // Dùng chung ID với Match

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "conversation_id")
    private Match match;


    @Column(length = 36)
    private String lastMessageId;

    private Timestamp updatedAt;

    // Quan hệ 1:N với Message
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;
}