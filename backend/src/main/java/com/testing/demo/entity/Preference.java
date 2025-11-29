package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preference")
public class Preference {

    @Id
    @Column(length = 36)
    private String preferenceId; // Dùng chung ID với User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "preference_id")
    private User user;

    private String interestedInGender;
    private int minAge;
    private int maxAge;
    private int maxDistance;
    private boolean isVisible;
}