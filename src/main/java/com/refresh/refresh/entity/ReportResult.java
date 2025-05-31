package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@IdClass(ReportResultId.class) // 다중 키를 주요 키로 사용
@Table(name = "report_result")
public class ReportResult {
    @Id
    private Integer resultId;

    @Id
    private Integer reportId;

    private String resultContent;
    private Integer banPeriod;
    private LocalDateTime resultDate;
}