package com.refresh.refresh.controller;

import com.graphhopper.ResponsePath;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.service.navigate.GraphHopperService;
import com.refresh.refresh.service.route_judge.DangerPolygonLoader;
import com.refresh.refresh.service.route_judge.DangerZoneBboxChecker;
import com.refresh.refresh.service.route_judge.RouteIntersectionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import java.util.*;

/**
 * GraphHopper 기반 위험구역 고려 네비게이션 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/navigation")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NavigationController {

    private final GraphHopperService graphHopperService;
    private final DangerZoneBboxChecker dangerZoneBboxChecker;
    private final DangerPolygonLoader dangerPolygonLoader;
    private final RouteIntersectionChecker routeIntersectionChecker;

    @Value("${app.geojson.base-dir:content/geojson}")
    private String geojsonBaseDir;

    @Value("${app.geojson.city-name:광주광역시}")
    private String cityName;

    /**
     * 위험구역을 고려한 경로 계산
     */
    @PostMapping("/route-with-danger-check")
    public ResponseEntity<?> routeWithDangerCheck(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng,
            @RequestParam(defaultValue = "foot") String transportMode) {

        // 교통수단 유효성 검사
        if (!transportMode.equals("foot") && !transportMode.equals("car")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false,
                                 "error", "지원되지 않는 교통수단: " + transportMode,
                                 "availableTransportModes", List.of("foot", "car")));
        }

        try {
            // 1. 기본 경로 생성 (fastest 프로필 사용)
            String fastestProfile = transportMode.equals("car") ? "car_fastest" : "foot_fastest";
            ResponsePath basicPath = graphHopperService.calculatePath(startLat, startLng, endLat, endLng, fastestProfile);
            GraphHopperService.RouteAnalysis basicAnalysis = graphHopperService.analyzeRoute(basicPath);

            // 2. 위험구역 통과 여부 계산
            boolean passesThroughDangerZone = checkDangerZonePassage(basicPath);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("transportMode", transportMode);
            result.put("transportModeDescription", transportMode.equals("car") ? "자동차" : "보행자");

            // 기본 경로 정보
            Map<String, Object> basicRouteInfo = createRouteInfo(basicPath, basicAnalysis, fastestProfile, "기본 경로");
            result.put("basicRoute", basicRouteInfo);

            // 위험구역 통과 여부
            result.put("passesThroughDangerZone", passesThroughDangerZone);

            // 3. 위험구역을 통과할 경우 회피 경로 생성
            if (passesThroughDangerZone) {
                String avoidProfile = transportMode.equals("car") ? "car_avoid" : "foot_avoid";
                ResponsePath avoidPath = graphHopperService.calculatePath(startLat, startLng, endLat, endLng, avoidProfile);
                GraphHopperService.RouteAnalysis avoidAnalysis = graphHopperService.analyzeRoute(avoidPath);

                // 회피 경로 정보
                Map<String, Object> avoidRouteInfo = createRouteInfo(avoidPath, avoidAnalysis, avoidProfile, "회피 경로");
                result.put("avoidRoute", avoidRouteInfo);

                // 경로 비교 정보
                Map<String, Object> comparison = createRouteComparison(basicAnalysis, avoidAnalysis);
                result.put("routeComparison", comparison);
                result.put("recommendedRoute", "avoid");
            } else {
                result.put("avoidRoute", null);
                result.put("routeComparison", null);
                result.put("recommendedRoute", "basic");
            }

            // 4. 위험구역 상세 정보
            Map<String, Object> dangerZoneInfo = getDangerZoneInfo(basicPath);
            result.put("dangerZoneInfo", dangerZoneInfo);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("위험구역 고려 경로 계산 중 예외 [{}]", transportMode, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /**
     * 경로가 위험구역을 통과하는지 확인
     */
    private boolean checkDangerZonePassage(ResponsePath path) {
        try {
            // 1. bbox 교차 확인으로 후보 DangerRecord들 조회
            List<DangerRecord> intersectingRecords = dangerZoneBboxChecker.getIntersectingDangerRecords(path);
            
            log.info("bbox 교차 확인 결과: {}개의 DangerRecord 발견", intersectingRecords.size());
            
            if (intersectingRecords.isEmpty()) {
                log.info("bbox 교차하는 위험구역이 없음");
                return false; // bbox 교차하는 위험구역이 없음
            }

            // 2. 각 DangerRecord의 폴리곤과 정확한 교차 여부 확인
            for (DangerRecord record : intersectingRecords) {
                log.info("DangerRecord 처리 중: recordId={}, detailId={}, path={}", 
                    record.getRecordId(), record.getDetailId(), record.getDangerJsonPath());
                
                try {
                    List<Polygon> polygons = dangerPolygonLoader.loadPolygonsFromRecord(record, geojsonBaseDir, cityName);
                    log.info("폴리곤 로드 성공: {}개의 폴리곤", polygons.size());
                    
                    if (polygons.isEmpty()) {
                        log.warn("폴리곤이 비어있음: {}", record.getDangerJsonPath());
                        continue;
                    }
                    
                    boolean intersects = routeIntersectionChecker.isRouteIntersectingDangerZones(path, polygons);
                    log.info("교차 여부 확인 결과: {}", intersects);
                    
                    if (intersects) {
                        log.info("위험구역 통과 확인됨: recordId={}", record.getRecordId());
                        return true; // 하나라도 교차하면 위험구역 통과
                    }
                } catch (Exception e) {
                    log.error("DangerRecord 처리 중 오류: recordId={}, error={}", record.getRecordId(), e.getMessage());
                }
            }

            log.info("모든 폴리곤과 교차하지 않음");
            return false; // 모든 폴리곤과 교차하지 않음
            
        } catch (Exception e) {
            log.error("위험구역 통과 여부 확인 중 오류", e);
            return false; // 오류 시 안전하게 처리
        }
    }

    /**
     * 위험구역 상세 정보 조회
     */
    private Map<String, Object> getDangerZoneInfo(ResponsePath path) {
        Map<String, Object> info = new LinkedHashMap<>();
        
        try {
            List<DangerRecord> intersectingRecords = dangerZoneBboxChecker.getIntersectingDangerRecords(path);
            
            info.put("dangerZoneCount", intersectingRecords.size());
            info.put("dangerZones", intersectingRecords.stream()
                    .map(record -> Map.of(
                            "recordId", record.getRecordId(),
                            "detailId", record.getDetailId(),
                            "dangerJsonPath", record.getDangerJsonPath()
                    ))
                    .collect(Collectors.toList()));
            
            // 위험도 레벨 계산
            String riskLevel = intersectingRecords.isEmpty() ? "LOW" : 
                              intersectingRecords.size() > 2 ? "HIGH" : "MEDIUM";
            info.put("riskLevel", riskLevel);
            
        } catch (Exception e) {
            log.error("위험구역 정보 조회 중 오류", e);
            info.put("error", "위험구역 정보 조회 실패");
            info.put("dangerZoneCount", 0);
            info.put("riskLevel", "LOW");
        }
        
        return info;
    }

    /**
     * 경로 정보 생성
     */
    private Map<String, Object> createRouteInfo(ResponsePath path, GraphHopperService.RouteAnalysis analysis, 
                                               String profile, String description) {
        Map<String, Object> routeInfo = new LinkedHashMap<>();
        routeInfo.put("profile", profile);
        routeInfo.put("description", description);
        routeInfo.put("distance", analysis.getDistanceInMeters());
        routeInfo.put("distanceKm", analysis.getDistanceInKm());
        routeInfo.put("time", analysis.getTimeInMillis());
        routeInfo.put("timeMinutes", analysis.getTimeInMinutes());
        routeInfo.put("coordinates", path.getPoints().toLineString(false).getCoordinates());
        return routeInfo;
    }

    /**
     * 경로 비교 정보 생성
     */
    private Map<String, Object> createRouteComparison(GraphHopperService.RouteAnalysis basic, 
                                                     GraphHopperService.RouteAnalysis avoid) {
        Map<String, Object> comparison = new LinkedHashMap<>();
        comparison.put("distanceDifference", avoid.getDistanceInMeters() - basic.getDistanceInMeters());
        comparison.put("distanceDifferenceKm", Math.round((avoid.getDistanceInKm() - basic.getDistanceInKm()) * 100.0) / 100.0);
        comparison.put("timeDifference", avoid.getTimeInMillis() - basic.getTimeInMillis());
        comparison.put("timeDifferenceMinutes", avoid.getTimeInMinutes() - basic.getTimeInMinutes());

        // 퍼센트 차이 계산
        double distancePercent = basic.getDistanceInMeters() > 0 ? 
            ((avoid.getDistanceInMeters() - basic.getDistanceInMeters()) / basic.getDistanceInMeters()) * 100 : 0;
        double timePercent = basic.getTimeInMillis() > 0 ? 
            ((avoid.getTimeInMillis() - basic.getTimeInMillis()) / (double)basic.getTimeInMillis()) * 100 : 0;
        
        comparison.put("distancePercentDifference", Math.round(distancePercent * 10.0) / 10.0);
        comparison.put("timePercentDifference", Math.round(timePercent * 10.0) / 10.0);
        
        return comparison;
    }
}
