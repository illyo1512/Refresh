package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SavedRouteDTO {
    private Integer savedRouteId;
    private Integer userId;
    private String routeName;
    private String routeResult;
    private LocalDateTime createdAt;
}