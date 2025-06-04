package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.CoordinateDTO;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 경로와 위험지역(Polygon) 간의 교차 여부를 검사하는 유틸리티
 */
@Component
public class RouteIntersectionChecker {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * 경로가 하나 이상의 위험구역(Polygon)과 교차하는지 판단
     *
     * @param routeDTOs   Tmap에서 받은 경로 (위도, 경도 DTO 리스트)
     * @param dangerZones 위험구역 Polygon 리스트
     * @return true = 겹침 있음, false = 안전한 경로
     */
    public boolean isRouteIntersectingDangerZones(List<CoordinateDTO> routeDTOs, List<Polygon> dangerZones) {
        if (routeDTOs == null || routeDTOs.size() < 2 || dangerZones == null || dangerZones.isEmpty()) {
            return false; // 경로가 비정상일 경우 안전하다고 간주
        }

        // 1. CoordinateDTO → JTS Coordinate로 변환
        Coordinate[] jtsCoordinates = routeDTOs.stream()
                .map(c -> new Coordinate(c.getLng(), c.getLat())) // JTS는 x=lng, y=lat
                .toArray(Coordinate[]::new);

        // 2. LineString 객체 생성
        LineString routeLine = geometryFactory.createLineString(jtsCoordinates);

        // 3. 각 위험구역 Polygon과 intersects 연산
        for (Polygon dangerZone : dangerZones) {
            if (routeLine.intersects(dangerZone)) {
                return true;
            }
        }

        return false; // 교차하는 Polygon 없음
    }
}
