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
@Table(name = "profile")
public class Profile {

    @Id
    @Column(length = 36)
    private String profileId;


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "profile_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String jobTitle;
    private String company;
    private String school;

    private Double longitude;
    private Double latitude;
}