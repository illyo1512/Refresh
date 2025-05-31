package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResultDTO {
    private Integer resultId;
    private Integer reportId;
    private String resultContent;
    private Integer banPeriod;
    private LocalDateTime resultDate;
}