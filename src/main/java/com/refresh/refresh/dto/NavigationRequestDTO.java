package com.refresh.refresh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavigationRequestDTO {
    
    /**
     * 출발지 위도
     */
    private double startLat;
    
    /**
     * 출발지 경도
     */
    private double startLng;
    
    /**
     * 도착지 위도
     */
    private double endLat;
    
    /**
     * 도착지 경도
     */
    private double endLng;
    
    /**
     * 프로필 (기본값: foot_shortest)
     */
    private String profile = "foot_shortest";
    
    /**
     * 회피할 구역 파일명 (단일)
     */
    private String avoidZoneFile;
    
    /**
     * 회피할 구역 파일명들 (다중)
     */
    private List<String> avoidZoneFiles;
}