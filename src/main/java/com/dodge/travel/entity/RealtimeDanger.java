package com.dodge.travel.entity;

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

    @ManyToOne
    @JoinColumn(name = "locate_info_id", nullable = false)
    private LocateInfo locateInfo;

    @ManyToOne
    @JoinColumn(name = "danger_detail_id", nullable = false)
    private DangerDetail dangerDetail;

    private String placeLocation;
    private LocalDateTime occurredAt;
}