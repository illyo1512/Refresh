package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "danger_record")
public class DangerRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @ManyToOne
    @JoinColumn(name = "detail_id", nullable = false)
    private DangerDetail detail;

    private String dangerLocation;
    private Integer dangerRadius;
    private Integer dangerLevel;
}