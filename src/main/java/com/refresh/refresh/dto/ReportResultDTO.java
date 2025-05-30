package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class ReportResultDTO {
    private Integer resultId;
    private Integer reportId;
    private String resultContent;
    private Integer banPeriod;
    private String resultDate;
}