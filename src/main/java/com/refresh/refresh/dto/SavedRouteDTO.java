package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class SavedRouteDTO {
    private Integer savedRouteId;
    private Integer userId;
    private String routeName;
    private String routeResult;
    private String createdAt;
}