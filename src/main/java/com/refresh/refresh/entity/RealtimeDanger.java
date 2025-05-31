package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "realtime_danger")
public class RealtimeDanger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dangerId;

    private String locateName;

    @Column(nullable = false)
    private Integer dangerDetailId;

    private String placeLocation;
    private LocalDateTime occurredAt;
}