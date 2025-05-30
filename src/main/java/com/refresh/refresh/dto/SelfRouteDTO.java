package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class SelfRouteDTO {
    private Integer selfRouteId;
    private Integer userId;
    private String routeName;
    private String routeResult;
    private String createdAt;
}