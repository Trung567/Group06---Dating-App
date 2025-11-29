package com.testing.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hobby")
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hobbyId;

    @Column(unique = true, nullable = false)
    private String hobbyName;

    // Quan hệ M:N
    @ManyToMany(mappedBy = "hobbies", fetch = FetchType.LAZY)
    private Set<User> users;
}