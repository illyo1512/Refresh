package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {
    private Integer reportId;
    private Integer userId;
    private Integer targetId;
    private String reportContent;
    private String reportType;
    private LocalDateTime reportDate;
}