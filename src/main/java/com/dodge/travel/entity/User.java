package com.dodge.travel.entity;

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

    private String email;
    private String nickname;
    private String gender;
    private String role;
    private String phoneNumber;
    private String password;
    private String userImage;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean userBan;

    private LocalDateTime createdAt;
}