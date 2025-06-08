package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.Route_InfoDTO;

import java.util.*;

/**
 * 여러 경로를 GeoJSON FeatureCollection 형식으로 변환하는 유틸리티 클래스
 */
public class GeoJsonConverter {

    /**
     * 이름과 함께 경로 데이터를 받아 GeoJSON FeatureCollection 형태로 변환
     *
     * @param namedRoutes 이름 → 경로 좌표 리스트 (예: "Original Route" → List of RouteCoordinates)
     * @return GeoJSON 구조를 표현하는 Map 객체
     */
    public static Map<String, Object> convertNamedLineStrings(Map<String, List<Route_InfoDTO.RouteCoordinate>> namedRoutes) {
        Map<String, Object> geoJson = new LinkedHashMap<>();
        geoJson.put("type", "FeatureCollection");

        List<Map<String, Object>> features = new ArrayList<>();

        for (Map.Entry<String, List<Route_InfoDTO.RouteCoordinate>> entry : namedRoutes.entrySet()) {
            String name = entry.getKey();
            List<Route_InfoDTO.RouteCoordinate> coords = entry.getValue();

            // 유효한 좌표만 처리
            if (coords == null || coords.isEmpty()) continue;

            features.add(createLineFeature(coords, name));
        }

        geoJson.put("features", features);
        return geoJson;
    }

    /**
     * 주어진 좌표 리스트를 GeoJSON LineString Feature로 변환
     */
    private static Map<String, Object> createLineFeature(List<Route_InfoDTO.RouteCoordinate> RouteCoordinates, String name) {
        Map<String, Object> feature = new LinkedHashMap<>();
        feature.put("type", "Feature");

        // Feature 속성
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("name", name);
        feature.put("properties", properties);

        // Geometry 구성
        Map<String, Object> geometry = new LinkedHashMap<>();
        geometry.put("type", "LineString");

        List<List<Double>> coordList = new ArrayList<>();
        for (Route_InfoDTO.RouteCoordinate coord : RouteCoordinates) {
            coordList.add(List.of(coord.getLng(), coord.getLat()));
        }

        geometry.put("RouteCoordinates", coordList);
        feature.put("geometry", geometry);

        return feature;
    }
}
