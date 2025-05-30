package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report_result")
public class ReportResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    private String resultContent;
    private Integer banPeriod;
    private LocalDateTime resultDate;
}