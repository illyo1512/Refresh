package com.refresh.refresh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteAnalysisResult {
    private boolean isDanger;
    private Map<String, Object> originalRoute;  // GeoJSON
    private Map<String, Object> safeRoute;      // GeoJSON (nullable)
}
