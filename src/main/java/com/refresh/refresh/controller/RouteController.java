package com.refresh.refresh.controller;

import com.refresh.refresh.dto.Route_InfoDTO.*;
import com.refresh.refresh.service.navigate.GraphHopperService;
import com.refresh.refresh.service.route_judge.GeoJsonConverter;
import com.graphhopper.util.PointList;
import com.graphhopper.ResponsePath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/route")
public class RouteController {

    private final GraphHopperService graphHopperService;

    /**
     * 시작점과 도착점 사이의 보행자 경로를 GeoJSON으로 반환
     */
    @PostMapping("/find")
    public ResponseEntity<RouteGeoJsonResponse> findRoute(@RequestBody RouteCheckRequest request) {
        // 요청에서 좌표 추출
        RouteCoordinate start = request.getStart();
        RouteCoordinate end = request.getEnd();

        // 경로 계산 (foot_default 프로필 사용)
        ResponsePath path = graphHopperService.calculateWalkingPath(
                start.getLat(), start.getLng(),
                end.getLat(), end.getLng()
        );

        // PointList → List<RouteCoordinate> 변환
        PointList pointList = path.getPoints();
        List<RouteCoordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            coordinates.add(new RouteCoordinate(pointList.getLat(i), pointList.getLon(i)));
        }

        // GeoJSON 변환
        Map<String, List<RouteCoordinate>> namedRoutes = Map.of("Original Route", coordinates);
        RouteGeoJsonResponse geoJson = GeoJsonConverter.convertNamedLineStringsToDto(namedRoutes);

        // 응답 반환
        return ResponseEntity.ok(geoJson);
    }
}
