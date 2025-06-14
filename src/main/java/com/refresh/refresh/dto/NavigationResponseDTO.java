package com.refresh.refresh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NavigationResponseDTO {
    
    /**
     * 성공 여부
     */
    private boolean success;
    
    /**
     * 총 거리 (미터)
     */
    private double distance;
    
    /**
     * 소요 시간 (밀리초)
     */
    private long time;
    
    /**
     * 경로 좌표들 (LineString 형태)
     */
    private String points;
    
    /**
     * 사용된 프로필
     */
    private String profile;
    
    /**
     * 적용된 회피 구역 파일명 (단일)
     */
    private String avoidZoneApplied;
    
    /**
     * 적용된 회피 구역 파일명들 (다중)
     */
    private List<String> avoidZonesApplied;
    
    /**
     * 회피 구역 개수
     */
    private int avoidZoneCount;
    
    /**
     * 에러 메시지 (실패시)
     */
    private String error;
    
    /**
     * 상세 에러 정보
     */
    private String details;
}