package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private Integer reportId;
    private Integer userId;
    private Integer targetId;
    private String reportContent;
    private String reportType;
    private String reportDate;
}