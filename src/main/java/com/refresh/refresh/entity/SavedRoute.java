package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "saved_route")
public class SavedRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer savedRouteId;

    @Column(nullable = false)
    private Integer userId;

    private String routeName;
    private String routeResult;
    private LocalDateTime createdAt;
}