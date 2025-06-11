package com.refresh.refresh.service.navigate;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.config.CHProfile;
import com.graphhopper.util.CustomModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * GraphHopper 8.0 전용 설정 클래스
 * 8.0에서 shortest 대신 custom + distance_influence 사용
 */
@Slf4j
@Configuration
public class GraphHopperinit {

    @Bean
    public GraphHopper graphHopper() {
        log.info("GraphHopper 8.0 초기화 시작");
        
        GraphHopper hopper = new GraphHopper();
        
        try {
            // 1. 기본 파일 경로 설정
            hopper.setOSMFile("data/south-korea-latest.osm.pbf");
            hopper.setGraphHopperLocation("data/graph-cache");
            
            // 2. GraphHopper 8.0 방식 - custom 가중치 + distance_influence 사용
            
            // 보행자 프로필 (최단 거리 우선)
            CustomModel footCustomModel = new CustomModel();
            footCustomModel.setDistanceInfluence(200.0);  // 높은 값 = 최단 거리 우선
            
            Profile footProfile = new Profile("foot")
                .setVehicle("foot")
                .setWeighting("custom")                    // ✅ shortest → custom으로 변경
                .setTurnCosts(false)
                .setCustomModel(footCustomModel);          // ✅ CustomModel 추가
            
            // 자동차 프로필 (최빠른 시간 우선)  
            CustomModel carCustomModel = new CustomModel();
            carCustomModel.setDistanceInfluence(0.0);     // 낮은 값 = 최빠른 시간 우선
            
            Profile carProfile = new Profile("car")
                .setVehicle("car")
                .setWeighting("custom")                    // ✅ fastest → custom으로 변경
                .setTurnCosts(false)
                .setCustomModel(carCustomModel);           // ✅ CustomModel 추가
            
            // 3. 프로필 설정
            hopper.setProfiles(Arrays.asList(footProfile, carProfile));
            
            // 4. CH 최적화
            hopper.getCHPreparationHandler()
                .setCHProfiles(
                    new CHProfile("foot"),
                    new CHProfile("car")
                );
            
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
}