package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class DmDTO {
    private Integer dmId;
    private Integer userId;
    private String content;
    private String sender;
    private String sentAt;
}