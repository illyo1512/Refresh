package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RealTimeNotificationDTO {
    private Integer notificationId;
    private Integer userId;
    private Integer dangerId;
    private Integer detailId;
    private String content;
    private LocalDateTime sentAt;
}