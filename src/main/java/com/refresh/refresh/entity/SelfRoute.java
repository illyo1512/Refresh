package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "self_route")
public class SelfRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer selfRouteId;

    @Column(nullable = false)
    private Integer userId;

    private String routeName;
    private String routeResult;
    private LocalDateTime createdAt;
}