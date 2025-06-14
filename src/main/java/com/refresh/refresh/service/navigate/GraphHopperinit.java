package com.refresh.refresh.service.navigate;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.util.CustomModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * GraphHopper 8.0 전용 설정 클래스
 * CustomModel과 Profile 중앙 관리
 */
@Slf4j
@Configuration
public class GraphHopperinit {

    // CustomModel 템플릿 저장소
    private final Map<String, CustomModel> customModelTemplates = new HashMap<>();
    
    @Bean
    public GraphHopper graphHopper() {
        log.info("GraphHopper 8.0 초기화 시작");
        
        GraphHopper hopper = new GraphHopper();
        
        try {
            // 1. 기본 파일 경로 설정
            hopper.setOSMFile("data/south-korea-latest.osm.pbf");
            hopper.setGraphHopperLocation("data/graph-cache");
            
            // 2. CustomModel 템플릿들 초기화
            initializeCustomModels();
            
            // 3. Profile 생성 및 설정
            hopper.setProfiles(Arrays.asList(
                // 보행자 프로필들
                createFootShortestProfile(),
                createFootBalancedProfile(), 
                createFootFastestProfile(),
                createFootShProfile_aviod(),
                
                // 자동차 프로필들
                createCarFastestProfile(),
                createCarBalancedProfile(),
                createCarShortestProfile(),
                createCarShProfile_aviod()
            ));
            
            /*// 4. CH 최적화 설정
            hopper.getCHPreparationHandler()
                .setCHProfiles(
                    new CHProfile("foot"),
                    new CHProfile("car")
                );*/
            
            // 5. GraphHopper 초기화 실행
            log.info("OSM 데이터 로드 및 그래프 생성 시작");
            hopper.importOrLoad();
            
            log.info("GraphHopper 8.0 초기화 완료");
            return hopper;
            
        } catch (Exception e) {
            log.error("GraphHopper 8.0 초기화 중 오류 발생", e);
            throw new RuntimeException("GraphHopper 초기화 실패: " + e.getMessage(), e);
        }
    }
    
    /**
     * CustomModel 템플릿들 초기화
     */
    private void initializeCustomModels() {
        log.info("CustomModel 템플릿 초기화 시작");
        
        // === 보행자 프로필들 ===
        // 보행자 - 최단거리 우선
        CustomModel footShortest = new CustomModel();
        footShortest.setDistanceInfluence(200.0);
        customModelTemplates.put("foot_shortest", footShortest);
        
        // 보행자 - 균형잡힌 (거리+시간)
        CustomModel footBalanced = new CustomModel();
        footBalanced.setDistanceInfluence(70.0);
        customModelTemplates.put("foot_balanced", footBalanced);
        
        // 보행자 - 최빠른 시간
        CustomModel footFastest = new CustomModel();
        footFastest.setDistanceInfluence(0.0);
        customModelTemplates.put("foot_fastest", footFastest);
        
        // 보행자 - 회피용 (최단거리 기반)
        CustomModel footAvoid = new CustomModel();
        footAvoid.setDistanceInfluence(200.0);
        footAvoid.setHeadingPenalty(300d); // 회피 시 방향 변경 페널티
        customModelTemplates.put("foot_sh_aviod", footAvoid);
        
        // === 자동차 프로필들 ===
        // 자동차 - 최빠른 시간 우선
        CustomModel carFastest = new CustomModel();
        carFastest.setDistanceInfluence(0.0);
        customModelTemplates.put("car_fastest", carFastest);
        
        // 자동차 - 균형잡힌
        CustomModel carBalanced = new CustomModel();
        carBalanced.setDistanceInfluence(30.0);
        customModelTemplates.put("car_balanced", carBalanced);
        
        // 자동차 - 최단거리
        CustomModel carShortest = new CustomModel();
        carShortest.setDistanceInfluence(100.0);
        customModelTemplates.put("car_shortest", carShortest);
        
        // 자동차 - 회피용 (최단거리 기반)
        CustomModel carAvoid = new CustomModel();
        carAvoid.setDistanceInfluence(100.0);
        carAvoid.setHeadingPenalty(200d); // 자동차는 보행자보다 방향 변경 페널티 낮게
        customModelTemplates.put("car_sh_aviod", carAvoid);
        
        log.info("CustomModel 템플릿 {} 개 초기화 완료", customModelTemplates.size());
    }
    
    /**
     * 보행자 프로필들
     */
    private Profile createFootShortestProfile() {
        return new Profile("foot_shortest")
            .setVehicle("foot")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("foot_shortest")));
    }
    
    private Profile createFootBalancedProfile() {
        return new Profile("foot_balanced")
            .setVehicle("foot")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("foot_balanced")));
    }
    
    private Profile createFootFastestProfile() {
        return new Profile("foot_fastest")
            .setVehicle("foot")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("foot_fastest")));
    }

    private Profile createFootShProfile_aviod() {
        return new Profile("foot_sh_aviod")
            .setVehicle("foot")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("foot_sh_aviod"))); // 수정
    }

    /**
     * 자동차 프로필들
     */
    private Profile createCarFastestProfile() {
        return new Profile("car_fastest")
            .setVehicle("car")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("car_fastest")));
    }
    
    private Profile createCarBalancedProfile() {
        return new Profile("car_balanced")
            .setVehicle("car")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("car_balanced")));
    }
    
    private Profile createCarShortestProfile() {
        return new Profile("car_shortest")
            .setVehicle("car")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("car_shortest")));
    }

    private Profile createCarShProfile_aviod() {
        return new Profile("car_sh_aviod")
            .setVehicle("car")
            .setWeighting("custom")
            .setTurnCosts(false)
            .setCustomModel(copyCustomModel(customModelTemplates.get("car_sh_aviod"))); // 수정
    }
    
    public CustomModel getCustomModelTemplate(String templateName) {
        CustomModel template = customModelTemplates.get(templateName);
        return template != null ? copyCustomModel(template) : null;
    }

    /**
     * CustomModel 완전 복사 메서드
     */
    private CustomModel copyCustomModel(CustomModel original) {
        if (original == null) {
            return null;
        }
        
        CustomModel copy = new CustomModel();
        
        // 기본 속성들 복사
        if (original.getDistanceInfluence() != null) {
            copy.setDistanceInfluence(original.getDistanceInfluence());
        }
        if (original.getHeadingPenalty() != null) {
            copy.setHeadingPenalty(original.getHeadingPenalty());
        }
        
        // Areas 복사 (회피 구역 정보)
        if (original.getAreas() != null) {
            copy.setAreas(original.getAreas());
        }
        
        // Speed와 Priority 규칙들 복사
        if (!original.getSpeed().isEmpty()) {
            original.getSpeed().forEach(copy::addToSpeed);
        }
        if (!original.getPriority().isEmpty()) {
            original.getPriority().forEach(copy::addToPriority);
        }
        
        return copy;
    }
        
}