package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.CoordinateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

/**
 * Tmap API를 통해 경로를 요청하고 좌표 리스트로 반환하는 서비스
 */
@Service
@RequiredArgsConstructor
public class TmapService {

    private final WebClient webClient = WebClient.create("https://apis.openapi.sk.com");

    @Value("${tmap.appKey}")
    private String tmapAppKey;

    // ✅ 단일 경로용
    public List<CoordinateDTO> getPedestrianRoute(CoordinateDTO start, CoordinateDTO end) {
        return getPedestrianRoute(start, end, null);
    }

    // ✅ 경유지 포함 경로용
    public List<CoordinateDTO> getPedestrianRoute(CoordinateDTO start, CoordinateDTO end, List<CoordinateDTO> passList) {

        // 1. 요청 파라미터 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("startX", start.getLng());
        requestBody.put("startY", start.getLat());
        requestBody.put("endX", end.getLng());
        requestBody.put("endY", end.getLat());
        requestBody.put("reqCoordType", "WGS84GEO");
        requestBody.put("resCoordType", "WGS84GEO");
        requestBody.put("startName", "출발지");
        requestBody.put("endName", "도착지");

        // 2. passList가 있을 경우 문자열 생성
        if (passList != null && !passList.isEmpty()) {
            StringBuilder passListStr = new StringBuilder();
            for (CoordinateDTO c : passList) {
                passListStr.append(c.getLng()).append(",").append(c.getLat()).append("_");
            }
            passListStr.setLength(passListStr.length() - 1); // 마지막 '_' 제거
            requestBody.put("passList", passListStr.toString());
        }

        // 3. API 요청
        Map<String, Object> response = webClient.post()
                .uri("/tmap/routes/pedestrian?version=1")
                .header("appKey", tmapAppKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 4. 응답에서 좌표 추출
        List<CoordinateDTO> routePoints = new ArrayList<>();
        List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");

        for (Map<String, Object> feature : features) {
            Map<String, Object> geometry = (Map<String, Object>) feature.get("geometry");

            if ("LineString".equals(geometry.get("type"))) {
                List<List<Double>> coords = (List<List<Double>>) geometry.get("coordinates");

                for (List<Double> point : coords) {
                    routePoints.add(new CoordinateDTO(point.get(1), point.get(0))); // 위도, 경도 순서
                }
            }
        }
        System.out.println("🚩 Tmap 경로 요청:");
        System.out.println("Start: " + start);
        System.out.println("End: " + end);
        if (passList != null) {
            System.out.println("PassList:");
            for (CoordinateDTO coord : passList) {
                System.out.println(" - " + coord);
            }
        }

        return routePoints;
    }
}
