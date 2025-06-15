package com.refresh.refresh.service.navigate;

import com.graphhopper.*;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.*;
import lombok.extern.slf4j.Slf4j;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * GraphHopper 경로 계산 / 분석 서비스 (동기 방식)
 * - GraphHopper 인스턴스는 GraphHopperinit으로부터 직접 주입받아 사용
 * - 애플리케이션 시작 시 이미 준비된 상태이므로 상태 확인 불필요
 */
@Slf4j
@Service
public class GraphHopperService {

    private final GraphHopper graphHopper;
    private final Translation koreanTranslation;

    /**
     * 생성자 - GraphHopper 인스턴스 주입 및 한국어 번역 초기화
     */
    public GraphHopperService(GraphHopper graphHopper) {
        this.graphHopper = graphHopper;
        this.koreanTranslation = graphHopper.getTranslationMap().getWithFallBack(Locale.KOREA);
        log.info("[GHS] GraphHopperService 초기화 완료");
    }

    /* ──────────────────── 경로 계산 메서드 ───────────────────── */

    /**
     * 기본 경로 계산 메서드
     * @param sLat 출발지 위도
     * @param sLon 출발지 경도
     * @param eLat 도착지 위도
     * @param eLon 도착지 경도
     * @param profile 사용할 프로필 (기본값: foot_avoid)
     * @return 계산된 경로
     */
    public ResponsePath calculatePath(
            double sLat, double sLon, double eLat, double eLon, String profile) {

        try {
            GHRequest req = new GHRequest(sLat, sLon, eLat, eLon)
                    .setProfile(profile == null ? "foot_avoid" : profile)
                    .setLocale(Locale.KOREA);

            GHResponse resp = graphHopper.route(req);
            if (resp.hasErrors()) {
                log.error("[GHS] 경로 계산 오류 [{}]: {}", profile, resp.getErrors());
                throw new RuntimeException("경로 계산 실패: " + resp.getErrors().toString());
            }
            
            log.debug("[GHS] 경로 계산 성공 [{}]: {}m, {}ms", profile, 
                     resp.getBest().getDistance(), resp.getBest().getTime());
            return resp.getBest();
            
        } catch (Exception e) {
            log.error("[GHS] 경로 계산 중 예외 [{}]: {}", profile, e.getMessage());
            throw new RuntimeException("경로 계산 중 오류 발생", e);
        }
    }

    /**
     * 보행자 회피 경로 계산
     */
    public ResponsePath calculateWalkingPath(double sLat, double sLon,
                                             double eLat, double eLon) {
        return calculatePath(sLat, sLon, eLat, eLon, "foot_avoid");
    }

    /**
     * 자동차 회피 경로 계산
     */
    public ResponsePath calculateDrivingPath(double sLat, double sLon,
                                             double eLat, double eLon) {
        return calculatePath(sLat, sLon, eLat, eLon, "car_avoid");
    }

    /**
     * 보행자 최단 경로 계산
     */
    public ResponsePath calculateFastestWalkingPath(double sLat, double sLon,
                                                    double eLat, double eLon) {
        return calculatePath(sLat, sLon, eLat, eLon, "foot_fastest");
    }

    /**
     * 자동차 최단 경로 계산
     */
    public ResponsePath calculateFastestDrivingPath(double sLat, double sLon,
                                                    double eLat, double eLon) {
        return calculatePath(sLat, sLon, eLat, eLon, "car_fastest");
    }

    /* ──────────────────── 분석 & 헬스체크 ───────────────────── */

    /**
     * 경로 분석 - 상세 정보 추출
     * @param path 분석할 경로
     * @return 분석 결과
     */
    public RouteAnalysis analyzeRoute(ResponsePath path) {
        Objects.requireNonNull(path, "분석할 경로가 null입니다");

        try {
            // 좌표 정보 추출
            PointList pts = path.getPoints();
            List<RoutePoint> coords = new ArrayList<>(pts.size());
            for (int i = 0; i < pts.size(); i++) {
                coords.add(RoutePoint.builder()
                        .latitude(pts.getLat(i))
                        .longitude(pts.getLon(i))
                        .elevation(pts.getEle(i))
                        .build());
            }

            // 길찾기 안내 정보 추출
            InstructionList ins = path.getInstructions();
            List<String> directions = new ArrayList<>(ins.size());
            for (Instruction in : ins) {
                String txt = in.getTurnDescription(koreanTranslation);
                if (txt != null && !txt.isBlank()) {
                    directions.add(txt);
                }
            }

            RouteAnalysis analysis = RouteAnalysis.builder()
                    .distanceInMeters(path.getDistance())
                    .distanceInKm(Math.round(path.getDistance() / 1000.0 * 100.0) / 100.0)
                    .timeInMillis(path.getTime())
                    .timeInMinutes((int) Math.round(path.getTime() / 60000.0))
                    .coordinates(coords)
                    .directions(directions)
                    .totalPoints(coords.size())
                    .build();

            log.debug("[GHS] 경로 분석 완료: {}km, {}분, {}개 좌표, {}개 안내", 
                     analysis.getDistanceInKm(), analysis.getTimeInMinutes(), 
                     analysis.getTotalPoints(), analysis.getDirections().size());

            return analysis;
            
        } catch (Exception e) {
            log.error("[GHS] 경로 분석 중 오류", e);
            throw new RuntimeException("경로 분석 실패", e);
        }
    }

    /**
     * 서비스 헬스 체크 - 서울 시청 근처 1km 샘플 경로로 테스트
     * @return 정상 동작 여부
     */
    public boolean checkHealth() {
        try {
            // 서울 시청 → 명동 샘플 경로 테스트
            ResponsePath testPath = calculateWalkingPath(
                37.5665, 126.9780,  // 서울 시청
                37.5636, 126.9829   // 명동
            );
            
            boolean healthy = testPath != null && testPath.getDistance() > 0;
            log.debug("[GHS] 헬스 체크 결과: {}", healthy ? "정상" : "비정상");
            return healthy;
            
        } catch (Exception ex) {
            log.warn("[GHS] 헬스 체크 실패", ex);
            return false;
        }
    }

    /**
     * 사용 가능한 프로필 목록 조회
     * @return 프로필 이름 목록
     */
    public List<String> getAvailableProfiles() {
        try {
            return graphHopper.getProfiles()
                    .stream()
                    .map(profile -> profile.getName())
                    .sorted()
                    .toList();
        } catch (Exception e) {
            log.error("[GHS] 프로필 목록 조회 실패", e);
            return List.of();
        }
    }

    /**
     * GraphHopper 인스턴스 직접 접근 (고급 사용자용)
     * @return GraphHopper 인스턴스
     */
    public GraphHopper getGraphHopper() {
        return graphHopper;
    }

    /* ─────────────── DTO 내장 클래스 ─────────────── */

    /**
     * 경로 분석 결과 DTO
     */
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

    /**
     * 경로 좌표점 DTO
     */
    @Builder 
    @Getter
    public static class RoutePoint {
        private final double latitude;
        private final double longitude;
        private final double elevation;
    }
}
