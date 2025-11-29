package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`match`")
public class Match {

    @Id
    @UuidGenerator
    @Column(length = 36)
    private String matchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    @Column(length = 10)
    private String status; // "active", "unmatched"

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    // Quan hệ 1:1 với Conversation
    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Conversation conversation;
}