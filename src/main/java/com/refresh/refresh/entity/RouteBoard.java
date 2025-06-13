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
    private Integer categoryId;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer viewCount;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer likeCount;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer boardStatus;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}