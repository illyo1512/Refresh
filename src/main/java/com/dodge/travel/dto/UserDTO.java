package com.dodge.travel.dto;

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
}