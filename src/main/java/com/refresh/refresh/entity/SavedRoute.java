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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String routeName;
    private String routeResult;
    private LocalDateTime createdAt;
}