package com.refresh.refresh.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String email;
    private String nickname;
    private String gender;
    private String role;
    private String phoneNumber;
    private String userImage;
    private String password;
    private String id;
    private Boolean userBan;
    private LocalDateTime createdAt;
}