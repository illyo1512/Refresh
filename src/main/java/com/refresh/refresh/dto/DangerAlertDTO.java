package com.refresh.refresh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DangerAlertDTO {
    
    private String alertSentence;      // AI가 생성한 경고 문장
    private Integer dangerDetailId;    // 위험도 ID (1:강도, 2:성폭행, 3:폭행, 4:절도, 5:살인)
    private String locateName;         // 위치명 (추가 정보)
    private LocalDateTime occurredAt;  // 발생 시간 (추가 정보)
    private LocalDateTime alertTime;   // 알림 전송 시간
    
    // 간단한 생성자 (필수 정보만)
    public DangerAlertDTO(String alertSentence, Integer dangerDetailId) {
        this.alertSentence = alertSentence;
        this.dangerDetailId = dangerDetailId;
        this.alertTime = LocalDateTime.now();
    }
}
