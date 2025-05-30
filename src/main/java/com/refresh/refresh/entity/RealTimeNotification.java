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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "danger_id", nullable = false)
    private RealtimeDanger danger;

    @ManyToOne
    @JoinColumn(name = "detail_id", nullable = false)
    private DangerDetail detail;

    private String content;
    private LocalDateTime sentAt;
}