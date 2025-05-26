package com.dodge.travel.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "locate_review")
public class LocateReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "locate_info_id", nullable = false)
    private LocateInfo locateInfo;

    private String reviewDetail;
    private LocalDateTime createdAt;
}