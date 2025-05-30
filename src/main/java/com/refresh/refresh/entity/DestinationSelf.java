package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "destination_self")
public class DestinationSelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer destinationId;

    @ManyToOne
    @JoinColumn(name = "self_route_id", nullable = false)
    private SelfRoute selfRoute;

    @ManyToOne
    @JoinColumn(name = "info_id", nullable = false)
    private LocateInfo locateInfo;

    private String destinationLocate;
    private Integer destinationOrder;
}