package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "real_time_notification")
public class RealTimeNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer dangerId;

    @Column(nullable = false)
    private Integer detailId;

    private String content;
    private LocalDateTime sentAt;
}