package com.refresh.refresh.controller;

import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.refresh.refresh.service.navigate.GraphHopperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.locationtech.jts.geom.LineString;

import java.util.*;

/**
 * GraphHopper 기반 네비게이션 컨트롤러 (동기 방식)
 * 
 * 사용 가능한 프로필:
 * - foot_fastest: 보행자 최단거리 (위험지역 고려 안함)
 * - foot_avoid: 보행자 위험지역 회피 (거리보다 안전성 우선)
 * - car_fastest: 자동차 최단거리 (위험지역 고려 안함)
 * - car_avoid: 자동차 위험지역 회피 (거리보다 안전성 우선)
 */
@Slf4j
@RestController
@RequestMapping("/api/navigation")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NavigationController {

    private final GraphHopperService service;

    /**
     * 사용 가능한 프로필 목록
     */
    public static final Map<String, String> AVAILABLE_PROFILES = Map.of(
        "foot_fastest", "보행자 최단거리",
        "foot_avoid", "보행자 위험지역 회피",
        "car_fastest", "자동차 최단거리", 
        "car_avoid", "자동차 위험지역 회피"
    );

    /* ───────────────────────── 기본 경로 계산 ───────────────────────── */
    @PostMapping("/route")
    public ResponseEntity<?> route(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng,
            @RequestParam(defaultValue = "foot_avoid") String profile) {

        // 프로필 유효성 검사
        if (!AVAILABLE_PROFILES.containsKey(profile)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false,
                                 "error", "지원되지 않는 프로필: " + profile,
                                 "availableProfiles", AVAILABLE_PROFILES));
        }

        try {
            ResponsePath path = service.calculatePath(startLat, startLng, endLat, endLng, profile);
            
            PointList pts = path.getPoints();
            LineString line = pts.toLineString(false);
            String lineWkt = line.toText();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("distance", path.getDistance());
            result.put("time", path.getTime());
            result.put("wkt", lineWkt);
            result.put("profile", profile);
            result.put("profileDescription", AVAILABLE_PROFILES.get(profile));
            result.put("distanceKm", Math.round(path.getDistance() / 1000.0 * 100.0) / 100.0);
            result.put("timeMinutes", Math.round(path.getTime() / 60000.0));

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("경로 계산 중 예외 [{}]", profile, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /* ───────────────────────── 고급 경로 분석 ───────────────────────── */
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng,
            @RequestParam(defaultValue = "foot_avoid") String profile) {

        // 프로필 유효성 검사
        if (!AVAILABLE_PROFILES.containsKey(profile)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false,
                                 "error", "지원되지 않는 프로필: " + profile,
                                 "availableProfiles", AVAILABLE_PROFILES));
        }

        try {
            ResponsePath path = service.calculatePath(startLat, startLng, endLat, endLng, profile);
            GraphHopperService.RouteAnalysis analysis = service.analyzeRoute(path);
            
            PointList pts = path.getPoints();
            LineString line = pts.toLineString(false);
            String lineWkt = line.toText();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("profile", profile);
            result.put("profileDescription", AVAILABLE_PROFILES.get(profile));
            result.put("wkt", lineWkt);
            
            // 기본 정보
            result.put("distance", analysis.getDistanceInMeters());
            result.put("distanceKm", analysis.getDistanceInKm());
            result.put("time", analysis.getTimeInMillis());
            result.put("timeMinutes", analysis.getTimeInMinutes());
            result.put("totalPoints", analysis.getTotalPoints());
            
            // 좌표 정보
            result.put("coordinates", analysis.getCoordinates());
            
            // 길찾기 안내
            result.put("directions", analysis.getDirections());
            result.put("directionsCount", analysis.getDirections().size());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("경로 분석 중 예외 [{}]", profile, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /* ───────────────────────── 경로 비교 ───────────────────────── */
    @PostMapping("/compare")
    public ResponseEntity<?> compareRoutes(
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
            String fastestProfile = transportMode.equals("car") ? "car_fastest" : "foot_fastest";
            String avoidProfile = transportMode.equals("car") ? "car_avoid" : "foot_avoid";

            // 두 경로 계산
            ResponsePath fastestPath = service.calculatePath(startLat, startLng, endLat, endLng, fastestProfile);
            ResponsePath avoidPath = service.calculatePath(startLat, startLng, endLat, endLng, avoidProfile);

            // 분석
            GraphHopperService.RouteAnalysis fastestAnalysis = service.analyzeRoute(fastestPath);
            GraphHopperService.RouteAnalysis avoidAnalysis = service.analyzeRoute(avoidPath);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("transportMode", transportMode);
            result.put("transportModeDescription", transportMode.equals("car") ? "자동차" : "보행자");

            // 최단 경로 정보
            Map<String, Object> fastestInfo = createRouteInfo(fastestPath, fastestAnalysis, fastestProfile, "최단 경로");
            result.put("fastestRoute", fastestInfo);

            // 회피 경로 정보
            Map<String, Object> avoidInfo = createRouteInfo(avoidPath, avoidAnalysis, avoidProfile, "회피 경로");
            result.put("avoidRoute", avoidInfo);

            // 비교 정보
            Map<String, Object> comparison = new LinkedHashMap<>();
            comparison.put("distanceDifference", avoidAnalysis.getDistanceInMeters() - fastestAnalysis.getDistanceInMeters());
            comparison.put("distanceDifferenceKm", Math.round((avoidAnalysis.getDistanceInKm() - fastestAnalysis.getDistanceInKm()) * 100.0) / 100.0);
            comparison.put("timeDifference", avoidAnalysis.getTimeInMillis() - fastestAnalysis.getTimeInMillis());
            comparison.put("timeDifferenceMinutes", avoidAnalysis.getTimeInMinutes() - fastestAnalysis.getTimeInMinutes());
            comparison.put("isAvoidLonger", avoidAnalysis.getDistanceInMeters() > fastestAnalysis.getDistanceInMeters());
            comparison.put("isAvoidSlower", avoidAnalysis.getTimeInMillis() > fastestAnalysis.getTimeInMillis());
            
            // 퍼센트 차이 계산
            double distancePercent = fastestAnalysis.getDistanceInMeters() > 0 ? 
                ((avoidAnalysis.getDistanceInMeters() - fastestAnalysis.getDistanceInMeters()) / fastestAnalysis.getDistanceInMeters()) * 100 : 0;
            double timePercent = fastestAnalysis.getTimeInMillis() > 0 ? 
                ((avoidAnalysis.getTimeInMillis() - fastestAnalysis.getTimeInMillis()) / (double)fastestAnalysis.getTimeInMillis()) * 100 : 0;
            
            comparison.put("distancePercentDifference", Math.round(distancePercent * 10.0) / 10.0);
            comparison.put("timePercentDifference", Math.round(timePercent * 10.0) / 10.0);
            
            result.put("comparison", comparison);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("경로 비교 중 예외 [{}]", transportMode, e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /* ───────────────────────── 편의 메서드들 ───────────────────────── */
    @PostMapping("/walking")
    public ResponseEntity<?> calculateWalkingRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng) {

        try {
            ResponsePath path = service.calculateWalkingPath(startLat, startLng, endLat, endLng);
            GraphHopperService.RouteAnalysis analysis = service.analyzeRoute(path);
            
            Map<String, Object> result = createRouteInfo(path, analysis, "foot_avoid", "보행자 회피 경로");
            result.put("success", true);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("보행자 경로 계산 중 예외", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    @PostMapping("/driving")
    public ResponseEntity<?> calculateDrivingRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng) {

        try {
            ResponsePath path = service.calculateDrivingPath(startLat, startLng, endLat, endLng);
            GraphHopperService.RouteAnalysis analysis = service.analyzeRoute(path);
            
            Map<String, Object> result = createRouteInfo(path, analysis, "car_avoid", "자동차 회피 경로");
            result.put("success", true);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("자동차 경로 계산 중 예외", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    /* ───────────────────────── 정보 조회 API ───────────────────────── */
    @GetMapping("/profiles")
    public ResponseEntity<?> getAvailableProfiles() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("profiles", AVAILABLE_PROFILES);
        result.put("profileCount", AVAILABLE_PROFILES.size());
        
        // 실제 GraphHopper에서 로드된 프로필과 비교
        try {
            List<String> actualProfiles = service.getAvailableProfiles();
            result.put("actualProfiles", actualProfiles);
            result.put("actualProfileCount", actualProfiles.size());
        } catch (Exception e) {
            log.warn("실제 프로필 목록 조회 실패", e);
        }
        
        return ResponseEntity.ok(result);
    }

    /* ───────────────────────── 헬스 체크 ───────────────────────── */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        Map<String, Object> result = new LinkedHashMap<>();
        
        try {
            boolean healthy = service.checkHealth();
            List<String> profiles = service.getAvailableProfiles();
            
            result.put("status", healthy ? "UP" : "DOWN");
            result.put("serviceHealthy", healthy);
            result.put("availableProfiles", profiles);
            result.put("profileCount", profiles.size());
            result.put("configuredProfiles", AVAILABLE_PROFILES);
            result.put("configuredProfileCount", AVAILABLE_PROFILES.size());
            result.put("timestamp", System.currentTimeMillis());
            result.put("message", healthy ? "모든 시스템 정상" : "시스템 점검 필요");
            
        } catch (Exception e) {
            log.error("헬스 체크 중 오류", e);
            result.put("status", "ERROR");
            result.put("serviceHealthy", false);
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            result.put("message", "시스템 오류 발생");
        }
        
        return ResponseEntity.ok(result);
    }

    /* ───────────────────────── 헬퍼 메서드 ───────────────────────── */
    private Map<String, Object> createRouteInfo(ResponsePath path, GraphHopperService.RouteAnalysis analysis, 
                                                String profile, String description) {
        PointList pts = path.getPoints();
        LineString line = pts.toLineString(false);
        String lineWkt = line.toText();

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("description", description);
        info.put("profile", profile);
        info.put("profileDescription", AVAILABLE_PROFILES.get(profile));
        info.put("distance", analysis.getDistanceInMeters());
        info.put("distanceKm", analysis.getDistanceInKm());
        info.put("time", analysis.getTimeInMillis());
        info.put("timeMinutes", analysis.getTimeInMinutes());
        info.put("totalPoints", analysis.getTotalPoints());
        info.put("directionsCount", analysis.getDirections().size());
        info.put("wkt", lineWkt);
        
        return info;
    }
}
