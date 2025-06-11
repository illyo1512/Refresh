package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.Route_InfoDTO.RouteCoordinate;
import com.refresh.refresh.dto.Route_InfoDTO.RouteGeoJsonResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 여러 경로를 GeoJSON DTO 형태로 변환하는 유틸리티 클래스
 */
@Component
public class GeoJsonConverter {

    /**
     * 이름과 좌표 리스트로 구성된 경로 데이터를 GeoJSON DTO로 변환
     *
     * @param namedRoutes 이름 → 경로 좌표 리스트 (예: "Original Route" → List of RouteCoordinate)
     * @return GeoJSON 형식의 DTO 객체 (RouteGeoJsonResponse)
     */
    public static RouteGeoJsonResponse convertNamedLineStringsToDto(Map<String, List<RouteCoordinate>> namedRoutes) {
        List<RouteGeoJsonResponse.Feature> features = new ArrayList<>();

        for (Map.Entry<String, List<RouteCoordinate>> entry : namedRoutes.entrySet()) {
            String name = entry.getKey();
            List<RouteCoordinate> coords = entry.getValue();

            if (coords == null || coords.isEmpty()) continue;

            // 좌표 변환: [ [lng, lat], ... ]
            List<List<Double>> coordinatePairs = new ArrayList<>();
            for (RouteCoordinate coord : coords) {
                coordinatePairs.add(List.of(coord.getLng(), coord.getLat()));
            }

            RouteGeoJsonResponse.Geometry geometry = new RouteGeoJsonResponse.Geometry("LineString", coordinatePairs);
            RouteGeoJsonResponse.Properties properties = new RouteGeoJsonResponse.Properties(name);
            RouteGeoJsonResponse.Feature feature = new RouteGeoJsonResponse.Feature("Feature", geometry, properties);

            features.add(feature);
        }

        return new RouteGeoJsonResponse("FeatureCollection", features);
    }
}
