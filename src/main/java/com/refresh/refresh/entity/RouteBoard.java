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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "saved_route_id")
    private SavedRoute savedRoute;

    @ManyToOne
    @JoinColumn(name = "self_route_id")
    private SelfRoute selfRoute;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BoardCategory category;

    private String title;
    private String content;
    private LocalDateTime createdAt;
}