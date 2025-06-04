package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.CoordinateDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CoordinateDTO 리스트를 GeoJSON 형식의 LineString으로 변환하는 유틸리티 클래스
 */
public class GeoJsonConverter {

    /**
     * CoordinateDTO 리스트를 GeoJSON 형식의 LineString FeatureCollection으로 변환
     * @param coordinates 경로를 구성하는 좌표 리스트
     * @return GeoJSON 형태의 Map 객체 (FeatureCollection)
     */
    public static Map<String, Object> convertToLineStringFeatureCollection(List<CoordinateDTO> coordinates) {
        // 최상위 FeatureCollection 객체 생성
        Map<String, Object> geoJson = new LinkedHashMap<>();
        geoJson.put("type", "FeatureCollection");

        // Feature 객체 생성
        Map<String, Object> feature = new LinkedHashMap<>();
        feature.put("type", "Feature");

        // Geometry 객체 생성 (LineString)
        Map<String, Object> geometry = new LinkedHashMap<>();
        geometry.put("type", "LineString");

        // [ [lng, lat], [lng, lat], ... ] 형식으로 좌표 변환
        List<List<Double>> coordList = coordinates.stream()
                .map(coord -> List.of(coord.getLng(), coord.getLat()))
                .collect(Collectors.toList());
        geometry.put("coordinates", coordList);

        // geometry, properties 포함하여 feature 구성
        feature.put("geometry", geometry);
        feature.put("properties", new LinkedHashMap<>()); // 빈 속성값 포함

        // feature 리스트로 FeatureCollection 구성
        geoJson.put("features", List.of(feature));

        return geoJson;
    }
}
