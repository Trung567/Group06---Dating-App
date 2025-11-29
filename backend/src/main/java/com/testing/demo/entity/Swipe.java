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
@Table(name = "swipe")
public class Swipe {

    @Id
    @UuidGenerator
    @Column(length = 36)
    private String swipeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swiper_id", nullable = false)
    private User swiper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swiped_id", nullable = false)
    private User swiped;

    @Column(nullable = false, length = 10)
    private String action; // "like", "dislike"

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
}