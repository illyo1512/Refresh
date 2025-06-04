package com.refresh.refresh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 위도, 경도 좌표 정보를 담는 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateDTO {
    private double lat;
    private double lng;

    public double[] toArray() {
        return new double[]{lng, lat};
    }
}