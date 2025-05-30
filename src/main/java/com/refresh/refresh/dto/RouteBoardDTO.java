package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class RouteBoardDTO {
    private Integer routeBoardId;
    private Integer userId;
    private Integer savedRouteId;
    private Integer selfRouteId;
    private String title;
    private String content;
    private Integer categoryId;
    private String createdAt;
}