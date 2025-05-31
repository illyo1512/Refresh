package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DmDTO {
    private Integer dmId;
    private Integer userId;
    private String content;
    private String sender;
    private LocalDateTime sentAt;
}