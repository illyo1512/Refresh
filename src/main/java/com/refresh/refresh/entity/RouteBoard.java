package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "route_board")
public class RouteBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer routeBoardId;

    @Column(nullable = false)
    private Integer userId;

    private Integer savedRouteId;
    private Integer selfRouteId;
    private String title;
    private String content;

    @Column(nullable = false)
    private Integer categoryId;

    private LocalDateTime createdAt;
}