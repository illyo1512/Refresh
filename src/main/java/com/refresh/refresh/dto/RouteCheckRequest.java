package com.refresh.refresh.dto;

import lombok.Data;

/**
 * /api/route/analyze 요청 DTO
 */
@Data
public class RouteCheckRequest {
    private CoordinateDTO start;
    private CoordinateDTO end;
}
