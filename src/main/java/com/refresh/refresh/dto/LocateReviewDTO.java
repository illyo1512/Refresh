package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocateReviewDTO {
    private Integer reviewId;
    private Integer userId;
    private Integer locateInfoId;
    private String reviewDetail;
    private LocalDateTime createdAt;
}