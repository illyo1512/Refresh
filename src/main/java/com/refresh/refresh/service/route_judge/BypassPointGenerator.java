package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.CoordinateDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 위험구역 Polygon으로부터 우회 경유지 좌표를 생성하는 유틸리티
 */
@Component
@RequiredArgsConstructor
public class BypassPointGenerator {

    /**
     * 단일 Polygon의 중심점을 우회 경유지 후보로 반환
     *
     * @param polygon 위험지역 폴리곤
     * @return 중심점 DTO (lat, lng)
     */
    public CoordinateDTO getBypassPoint(Polygon polygon) {
        if (polygon == null) return null;

        Coordinate centroid = polygon.getCentroid().getCoordinate();
        return new CoordinateDTO(centroid.y, centroid.x); // y = 위도, x = 경도
    }

    /**
     * 다수의 Polygon 위험구역에서 경유지 후보 좌표들을 생성
     *
     * @param polygons Polygon 리스트
     * @return 중심점 좌표 리스트
     */
    public List<CoordinateDTO> getBypassPoints(List<Polygon> polygons) {
        List<CoordinateDTO> bypassPoints = new ArrayList<>();
        if (polygons == null || polygons.isEmpty()) return bypassPoints;

        for (Polygon polygon : polygons) {
            CoordinateDTO point = getBypassPoint(polygon);
            if (point != null) {
                bypassPoints.add(point);
            }
        }

        return bypassPoints;
    }
}
