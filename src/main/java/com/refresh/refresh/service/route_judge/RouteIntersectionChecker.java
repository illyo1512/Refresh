package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.Route_InfoDTO;
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
     * @param routeCoords 경로를 구성하는 좌표 리스트 (RouteDto.Coordinate)
     * @param dangerZones 위험구역 Polygon 리스트
     * @return true = 겹침 있음, false = 안전한 경로
     */
    public boolean isRouteIntersectingDangerZones(List<Route_InfoDTO.RouteCoordinate> routeCoords, List<Polygon> dangerZones) {
        if (routeCoords == null || routeCoords.size() < 2 || dangerZones == null || dangerZones.isEmpty()) {
            return false; // 경로가 비정상일 경우 안전하다고 간주
        }

        // RouteDto.Coordinate → JTS Coordinate로 변환
        Coordinate[] jtsCoordinates = routeCoords.stream()
                .map(c -> new Coordinate(c.getLng(), c.getLat())) // JTS: x=lng, y=lat
                .toArray(Coordinate[]::new);

        // LineString 객체 생성
        LineString routeLine = geometryFactory.createLineString(jtsCoordinates);

        // 각 위험구역 Polygon과 intersects 연산
        for (Polygon dangerZone : dangerZones) {
            if (routeLine.intersects(dangerZone)) {
                return true;
            }
        }

        return false; // 교차하는 Polygon 없음
    }
}
