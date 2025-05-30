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

    @ManyToOne
    @JoinColumn(name = "danger_detail_id", nullable = false)
    private DangerDetail dangerDetail;

    private String placeLocation;
    private LocalDateTime occurredAt;
}