package com.refresh.refresh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteGeoJsonResponse {
    private Map<String, Object> originalRoute;  // GeoJSON 형식
    private Map<String, Object> safeRoute;      // GeoJSON 형식
    private boolean danger;
}
// 이 DTO는 경로 분석 결과를 GeoJSON 형식으로 반환하기 위해 사용됩니다.
// originalRoute와 safeRoute는 각각 원래 경로와 우회 경로를 GeoJSON 형식으로 표현한 맵입니다.