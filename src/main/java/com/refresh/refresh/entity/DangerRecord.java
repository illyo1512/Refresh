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

    @Column(nullable = false)
    private Integer detailId;
    private String dangerJsonPath;
    private Double bboxMinLng;
    private Double bboxMinLat;
    private Double bboxMaxLng; 
    private Double bboxMaxLat;
}