package com.refresh.refresh.controller;

import com.refresh.refresh.dto.CoordinateDTO;
import com.refresh.refresh.dto.RouteAnalysisResult;
import com.refresh.refresh.dto.RouteCheckRequest;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.service.route_judge.*;
import com.refresh.refresh.service.DangerRecordService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 경로 분석 및 우회 경로 요청을 처리하는 API
 */
@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
public class RouteAnalysisController {

    private final TmapService tmapService;
    private final DangerRecordService dangerRecordService;
    private final DangerZoneLoaderService dangerZoneLoaderService;
    private final RouteIntersectionChecker routeIntersectionChecker;
    private final BypassPointGenerator bypassPointGenerator;

    @PostMapping("/analyze")
    public RouteAnalysisResult analyzeRoute(@RequestBody RouteCheckRequest request) {

        // 1. 기본 경로 요청
        List<CoordinateDTO> originalRoute = tmapService.getPedestrianRoute(request.getStart(), request.getEnd());

        // 2. 경로 BBOX 계산
        double[] pathBbox = DZFilter.computeBboxFromCoordinates(
                originalRoute.stream().map(CoordinateDTO::toArray).toList()
        );

        // 3. DB에서 BBOX가 겹치는 DangerRecord 조회
        List<DangerRecord> intersectingRecords = dangerRecordService.getIntersectingRecords(pathBbox);

        // 4. 실제 위험 Polygon들 로드
        List<Polygon> dangerZones = dangerZoneLoaderService.loadDangerZonesFromRecords(intersectingRecords);

        // 5. 경로와 위험구역 교차 여부 판단
        boolean isDanger = routeIntersectionChecker.isRouteIntersectingDangerZones(originalRoute, dangerZones);

        List<CoordinateDTO> safeRoute = null;

        // 6. 우회 경로 요청 (위험 경로일 경우만)
        if (isDanger) {
            List<CoordinateDTO> bypassPoints = bypassPointGenerator.getBypassPoints(dangerZones);
            safeRoute = tmapService.getPedestrianRoute(request.getStart(), request.getEnd(), bypassPoints);
        }

        // 7. GeoJSON으로 변환해서 DTO에 담기
        Map<String, Object> originalGeoJson = GeoJsonConverter.convertToLineStringFeatureCollection(originalRoute);
        Map<String, Object> safeGeoJson = (safeRoute != null)
                ? GeoJsonConverter.convertToLineStringFeatureCollection(safeRoute)
                : null;

        return new RouteAnalysisResult(isDanger, originalGeoJson, safeGeoJson);
    }
    
    @PostMapping("/analyze/test")
    public Map<String, Object> analyzeAndReturnBypassPoints(@RequestBody RouteCheckRequest request) {

        // 1. 기본 경로 요청
        List<CoordinateDTO> originalRoute = tmapService.getPedestrianRoute(request.getStart(), request.getEnd());

        // 2. 경로 BBOX 계산
        double[] pathBbox = DZFilter.computeBboxFromCoordinates(
                originalRoute.stream().map(CoordinateDTO::toArray).toList()
        );

        // 3. 위험 지역 데이터 필터링 및 로딩
        List<DangerRecord> intersectingRecords = dangerRecordService.getIntersectingRecords(pathBbox);
        List<Polygon> dangerZones = dangerZoneLoaderService.loadDangerZonesFromRecords(intersectingRecords);

        // 4. 위험 지역과의 교차 여부 확인
        boolean isDanger = routeIntersectionChecker.isRouteIntersectingDangerZones(originalRoute, dangerZones);

        // 5. 반환용 맵 구성
        Map<String, Object> result = new HashMap<>();
        result.put("originalRoute", originalRoute);
        result.put("isDanger", isDanger);

        if (isDanger) {
            List<CoordinateDTO> bypassPoints = bypassPointGenerator.getBypassPoints(dangerZones);
            result.put("bypassPoints", bypassPoints);
        } else {
            result.put("bypassPoints", List.of());
        }

        return result;
    }

}

