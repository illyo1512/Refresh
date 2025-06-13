package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(unique = true, length = 50)
    private String id;

    @Column(length = 10)
    private String gender;

    @Column(length = 20)
    private String role;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String userImage;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean userBan;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}