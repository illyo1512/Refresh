package com.refresh.refresh.controller.navigation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import com.graphhopper.ResponsePath;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.service.navigate.GraphHopperService;
import com.refresh.refresh.service.route_judge.DangerPolygonLoader;
import com.refresh.refresh.service.route_judge.DangerZoneBboxChecker;
import com.refresh.refresh.service.route_judge.RouteIntersectionChecker;
import com.refresh.refresh.service.SelfRouteService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final SelfRouteService selfRouteService;
    private final ObjectMapper objectMapper;

    @Value("${app.geojson.base-dir:content/geojson}")
    private String geojsonBaseDir;

    @Value("${app.geojson.city-name:광주광역시}")
    private String cityName;

    /**
     * 위험구역을 고려한 경로 계산
     */
    @Operation(summary = "위험구역 고려 경로 계산", description = "기본 경로와 위험구역 회피 경로를 계산합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "성공 응답 예시",
                    value = """
                    {
                      "success": true,
                      "transportMode": "foot",
                      "transportModeDescription": "보행자",
                      "basicRoute": {
                        "profile": "foot_fastest",
                        "description": "기본 경로",
                        "distance": 897.823888111766,
                        "distanceKm": 0.9,
                        "time": 646434,
                        "timeMinutes": 11,
                        "coordinates": [
                          {
                            "x": 126.896159,
                            "y": 35.111134,
                            "z": "NaN",
                            "m": "NaN",
                            "valid": true
                          }
                        ]
                      },
                      "passesThroughDangerZone": true,
                      "avoidRoute": {
                        "profile": "foot_avoid",
                        "description": "회피 경로",
                        "distance": 2128.3870594866466,
                        "distanceKm": 2.13,
                        "time": 1532439,
                        "timeMinutes": 26,
                        "coordinates": [
                          {
                            "x": 126.896159,
                            "y": 35.111134,
                            "z": "NaN",
                            "m": "NaN",
                            "valid": true
                          }
                        ]
                      },
                      "routeComparison": {
                        "distanceDifference": 1230.5631713748808,
                        "distanceDifferenceKm": 1.23,
                        "timeDifference": 886005,
                        "timeDifferenceMinutes": 15,
                        "distancePercentDifference": 137.1,
                        "timePercentDifference": 137.1
                      },
                      "recommendedRoute": "avoid",
                      "dangerZoneInfo": {
                        "dangerZoneCount": 2,
                        "dangerZones": [
                          {
                            "recordId": 310,
                            "detailId": 2,
                            "dangerJsonPath": "danger_area_126.8960_35.1040_126.9000_35.1080.geojson"
                          }
                        ],
                        "riskLevel": "MEDIUM"
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": false,
                      "error": "지원되지 않는 교통수단: bike",
                      "availableTransportModes": ["foot", "car"]
                    }
                    """
                )
            )
        )
    })
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

    /**
     * 경로 계산 결과 저장
     */
    @Operation(summary = "경로 계산 결과 저장", description = "사용자가 계산한 경로 결과를 저장합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "저장 성공",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": true,
                      "message": "경로가 성공적으로 저장되었습니다",
                      "selfRouteId": 1
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "저장 실패",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "success": false,
                      "error": "필수 파라미터가 누락되었습니다"
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/save-route")
    public ResponseEntity<?> saveRoute(@RequestBody Map<String, Object> request) {
        try {
            // 파라미터 추출
            Integer userId = (Integer) request.get("userId");
            String routeName = (String) request.get("routeName");
            Object routeResult = request.get("routeResult");

            // 입력값 검증
            if (userId == null || routeName == null || routeName.toString().trim().isEmpty() 
                || routeResult == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "필수 파라미터가 누락되었습니다"));
            }

            // routeResult를 JSON 문자열로 변환
            String routeResultJson;
            if (routeResult instanceof String) {
                routeResultJson = (String) routeResult;
            } else {
                routeResultJson = objectMapper.writeValueAsString(routeResult);
            }

            // JSON 유효성 검증
            try {
                objectMapper.readTree(routeResultJson);
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "error", "올바르지 않은 JSON 형식입니다"));
            }

            // 경로 저장
            Integer selfRouteId = selfRouteService.saveRoute(userId, routeName.toString().trim(), routeResultJson);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "경로가 성공적으로 저장되었습니다");
            response.put("selfRouteId", selfRouteId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("경로 저장 중 예외 발생", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "error", "서버 오류로 저장에 실패했습니다"));
        }
    }
}
