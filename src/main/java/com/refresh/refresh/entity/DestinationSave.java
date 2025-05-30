package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "destination_save")
public class DestinationSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer destinationId;

    @ManyToOne
    @JoinColumn(name = "saved_route_id", nullable = false)
    private SavedRoute savedRoute;

    @ManyToOne
    @JoinColumn(name = "info_id", nullable = false)
    private LocateInfo locateInfo;

    private String destinationLocate;
    private Integer destinationOrder;
}