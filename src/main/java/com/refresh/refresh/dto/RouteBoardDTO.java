package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteBoardDTO {
    private Integer routeBoardId;
    private Integer userId;
    private Integer savedRouteId;
    private Integer selfRouteId;
    private String title;
    private String content;
    private Integer categoryId;
    private LocalDateTime createdAt;
}