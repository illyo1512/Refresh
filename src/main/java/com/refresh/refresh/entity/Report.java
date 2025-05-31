package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer targetId;

    private String reportContent;
    private String reportType;
    private LocalDateTime reportDate;
}