package com.refresh.refresh.entity;

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

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer locateInfoId;

    private String reviewDetail;
    private LocalDateTime createdAt;
}