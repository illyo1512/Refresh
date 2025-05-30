package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class RealTimeNotificationDTO {
    private Integer notificationId;
    private Integer userId;
    private Integer dangerId;
    private Integer detailId;
    private String content;
    private String sentAt;
}