package com.refresh.refresh.service.navigate;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.TranslationMap;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;
import lombok.Getter;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

/**
 * GraphHopper 8.0 전용 경로 계산 서비스
 */
@Slf4j
@Service
public class GraphHopperService {

    private final GraphHopper hopper;
    private final Translation koreanTranslation;

    public GraphHopperService(GraphHopper hopper) {
        this.hopper = hopper;
        
        // GraphHopper 8.0 방식의 Translation 객체 생성
        try {
            TranslationMap translationMap = new TranslationMap().doImport();
            this.koreanTranslation = translationMap.getWithFallBack(Locale.KOREA);
            log.info("GraphHopperService 초기화 완료 - 한국어 번역 지원");
        } catch (Exception e) {
            log.error("Translation 초기화 실패", e);
            throw new RuntimeException("Translation 초기화 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 기본 경로 계산 (하위 호환성)
     */
    public ResponsePath calculatePath(double startLat, double startLng, double endLat, double endLng) {
        log.debug("기본 경로 계산 요청 - 보행자 모드로 처리");
        return calculateWalkingPath(startLat, startLng, endLat, endLng);
    }

    /**
     * 프로필별 경로 계산
     */
    public ResponsePath calculatePath(double startLat, double startLng, double endLat, double endLng, String profile) {
        log.debug("프로필별 경로 계산 - 프로필: {}", profile);

        return switch (profile.toLowerCase()) {
            case "foot", "walking" -> calculateWalkingPath(startLat, startLng, endLat, endLng);
            case "car", "driving" -> calculateDrivingPath(startLat, startLng, endLat, endLng);
            default -> {
                log.warn("지원하지 않는 프로필: {}. 기본 보행자 모드로 처리", profile);
                yield calculateWalkingPath(startLat, startLng, endLat, endLng);
            }
        };
    }

    /**
     * 보행자 경로 계산
     */
    public ResponsePath calculateWalkingPath(double startLat, double startLng, double endLat, double endLng) {
        log.debug("보행자 경로 계산 시작 - 시작점: [{}, {}], 도착점: [{}, {}]", 
                 startLat, startLng, endLat, endLng);

        GHPoint start = new GHPoint(startLat, startLng);
        GHPoint end = new GHPoint(endLat, endLng);

        GHRequest request = new GHRequest(start, end)
                .setProfile("foot")
                .setLocale(Locale.KOREA);

        try {
            GHResponse response = hopper.route(request);

            if (response.hasErrors()) {
                String errorMsg = "보행자 경로 계산 오류: " + response.getErrors().toString();
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            if (response.getAll().isEmpty()) {
                throw new RuntimeException("보행자 경로를 찾을 수 없습니다.");
            }

            ResponsePath bestPath = response.getBest();
            log.info("보행자 경로 계산 완료 - 거리: {}m, 예상 시간: {}분", 
                    bestPath.getDistance(), bestPath.getTime() / 60000);
            
            return bestPath;

        } catch (Exception e) {
            log.error("보행자 경로 계산 중 예외 발생", e);
            throw new RuntimeException("경로 계산 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 자동차 경로 계산
     */
    public ResponsePath calculateDrivingPath(double startLat, double startLng, double endLat, double endLng) {
        log.debug("자동차 경로 계산 시작 - 시작점: [{}, {}], 도착점: [{}, {}]", 
                 startLat, startLng, endLat, endLng);

        GHPoint start = new GHPoint(startLat, startLng);
        GHPoint end = new GHPoint(endLat, endLng);

        GHRequest request = new GHRequest(start, end)
                .setProfile("car")
                .setLocale(Locale.KOREA);

        try {
            GHResponse response = hopper.route(request);

            if (response.hasErrors()) {
                String errorMsg = "자동차 경로 계산 오류: " + response.getErrors().toString();
                log.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            if (response.getAll().isEmpty()) {
                throw new RuntimeException("자동차 경로를 찾을 수 없습니다.");
            }

            ResponsePath bestPath = response.getBest();
            log.info("자동차 경로 계산 완료 - 거리: {}m, 예상 시간: {}분", 
                    bestPath.getDistance(), bestPath.getTime() / 60000);
            
            return bestPath;

        } catch (Exception e) {
            log.error("자동차 경로 계산 중 예외 발생", e);
            throw new RuntimeException("경로 계산 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 경로 분석 결과 추출 (Translation 객체 사용)
     */
    public RouteAnalysis analyzeRoute(ResponsePath path) {
        if (path == null) {
            throw new IllegalArgumentException("분석할 경로가 null입니다.");
        }

        double distanceInMeters = path.getDistance();
        long timeInMillis = path.getTime();
        double distanceInKm = distanceInMeters / 1000.0;
        int timeInMinutes = (int) (timeInMillis / 60000);

        // 좌표 리스트 추출
        PointList pointList = path.getPoints();
        List<RoutePoint> coordinates = new ArrayList<>();
        
        for (int i = 0; i < pointList.size(); i++) {
            coordinates.add(RoutePoint.builder()
                .latitude(pointList.getLat(i))
                .longitude(pointList.getLon(i))
                .elevation(pointList.getEle(i))
                .build());
        }

        // 안내 정보 추출 (GraphHopper 8.0 올바른 방식)
        InstructionList instructions = path.getInstructions();
        List<String> directions = new ArrayList<>();
        
        for (Instruction instruction : instructions) {
            // ✅ Translation 객체 사용 (8.0에서 올바른 방식)
            String text = instruction.getTurnDescription(koreanTranslation);
            if (text != null && !text.trim().isEmpty()) {
                directions.add(text);
            }
        }

        log.debug("경로 분석 완료 - 거리: {}km, 시간: {}분, 좌표: {}개, 안내: {}개", 
                 distanceInKm, timeInMinutes, coordinates.size(), directions.size());

        return RouteAnalysis.builder()
                .distanceInMeters(distanceInMeters)
                .distanceInKm(distanceInKm)
                .timeInMillis(timeInMillis)
                .timeInMinutes(timeInMinutes)
                .coordinates(coordinates)
                .directions(directions)
                .totalPoints(coordinates.size())
                .build();
    }

    /**
     * GraphHopper 상태 확인
     */
    public boolean checkHealth() {
        try {
            GHPoint testStart = new GHPoint(37.5665, 126.9780);
            GHPoint testEnd = new GHPoint(37.5759, 126.9768);
            
            GHRequest testRequest = new GHRequest(testStart, testEnd).setProfile("foot");
            GHResponse testResponse = hopper.route(testRequest);
            
            boolean isHealthy = !testResponse.hasErrors() && !testResponse.getAll().isEmpty();
            log.debug("GraphHopper 8.0 상태 체크: {}", isHealthy ? "정상" : "이상");
            
            return isHealthy;
            
        } catch (Exception e) {
            log.error("GraphHopper 상태 체크 중 오류", e);
            return false;
        }
    }

    @Builder
    @Getter
    public static class RouteAnalysis {
        private final double distanceInMeters;
        private final double distanceInKm;
        private final long timeInMillis;
        private final int timeInMinutes;
        private final List<RoutePoint> coordinates;
        private final List<String> directions;
        private final int totalPoints;
    }

    @Builder
    @Getter
    public static class RoutePoint {
        private final double latitude;
        private final double longitude;
        private final double elevation;
    }
}