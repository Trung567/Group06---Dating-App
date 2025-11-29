package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @UuidGenerator
    @Column(length = 36) // 36 ký tự cho UUID
    private String userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String firstName;
    private String lastName;

    @Column(length = 20)
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(length = 10)
    private String gender;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    private Timestamp lastActive;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Preference preference;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Photo> photos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "User_Hobby",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "hobby_id")
    )
    private Set<Hobby> hobbies;

    public String getId() {
        return this.userId;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserPromptAnswer> promptAnswers;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // (Nếu chưa có) Thêm ngày sinh để tính tuổi cho bộ lọc V2
    @Column(name = "birth_date")
    private java.time.LocalDate birthDate;
}