package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String email;
    private String id;
    private String password;
    private String nickname;
    private String gender;
    private String phoneNumber;
    private String userImage;
}
