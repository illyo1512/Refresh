package com.refresh.refresh.controller;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/navigation")
@CrossOrigin(origins = "*")
public class NavigationController {
    
    @Autowired
    private GraphHopper graphHopper;
    
    /**
     * 일반 경로 계산
     * 
     * @param startLat 출발지 위도
     * @param startLng 출발지 경도
     * @param endLat 도착지 위도
     * @param endLng 도착지 경도
     * @param profile 프로필 (기본값: "foot")
     * @return 계산된 경로 정보
     */
    @PostMapping("/route")
    public ResponseEntity<?> calculateRoute(
            @RequestParam double startLat,
            @RequestParam double startLng,
            @RequestParam double endLat,
            @RequestParam double endLng,
            @RequestParam(defaultValue = "foot") String profile) {
        
        try {
            log.info("경로 계산: profile={}, 출발지=({}, {}), 도착지=({}, {})", 
                profile, startLat, startLng, endLat, endLng);
            
            // 경로 요청 생성
            GHRequest request = new GHRequest(startLat, startLng, endLat, endLng);
            request.setProfile(profile);
            
            // 경로 계산
            GHResponse response = graphHopper.route(request);
            
            if (response.hasErrors()) {
                log.error("경로 계산 오류: {}", response.getErrors());
                return ResponseEntity.badRequest().body(
                    Map.of("error", "경로 계산 오류", "details", response.getErrors().toString(), "success", false)
                );
            }
            
            ResponsePath path = response.getBest();
            PointList pointList = path.getPoints();
            
            Map<String, Object> result = new HashMap<>();
            result.put("distance", path.getDistance());
            result.put("time", path.getTime());
            result.put("points", pointList.toLineString(false));
            result.put("profile", profile);
            result.put("success", true);
            
            log.info("경로 계산 완료: 거리={}m, 시간={}ms", path.getDistance(), path.getTime());
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("경로 계산 중 오류", e);
            return ResponseEntity.badRequest().body(
                Map.of("error", "오류 발생", "message", e.getMessage(), "success", false)
            );
        }
    }
}