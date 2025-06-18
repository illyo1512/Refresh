package com.refresh.refresh.service;

import com.refresh.refresh.dto.DangerAlertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DangerAlertWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    
    // 토픽 경로 상수
    private static final String DANGER_ALERT_TOPIC = "/topic/danger-alerts";

    /**
     * 위험 알림을 모든 구독자에게 브로드캐스트
     */
    public void sendDangerAlert(String alertSentence, Integer dangerDetailId) {
        try {
            DangerAlertDTO alertDTO = new DangerAlertDTO(alertSentence, dangerDetailId);
            
            messagingTemplate.convertAndSend(DANGER_ALERT_TOPIC, alertDTO);
            
            log.info("위험 알림 전송 완료 - 토픽: {}, 메시지: {}, 위험도: {}", 
                    DANGER_ALERT_TOPIC, alertSentence, dangerDetailId);
            
        } catch (Exception e) {
            log.error("위험 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * 상세 정보 포함 위험 알림 전송
     */
    public void sendDangerAlert(DangerAlertDTO dangerAlertDTO) {
        try {
            dangerAlertDTO.setAlertTime(java.time.LocalDateTime.now());
            
            messagingTemplate.convertAndSend(DANGER_ALERT_TOPIC, dangerAlertDTO);
            
            log.info("상세 위험 알림 전송 완료 - 토픽: {}, 위치: {}, 위험도: {}", 
                    DANGER_ALERT_TOPIC, dangerAlertDTO.getLocateName(), dangerAlertDTO.getDangerDetailId());
            
        } catch (Exception e) {
            log.error("상세 위험 알림 전송 실패: {}", e.getMessage(), e);
        }
    }

    /**
     * 연결된 클라이언트 수 확인 (선택사항)
     */
    public void sendTestMessage() {
        try {
            DangerAlertDTO testAlert = new DangerAlertDTO(
                "테스트 위험 알림입니다.", 
                3
            );
            
            messagingTemplate.convertAndSend(DANGER_ALERT_TOPIC, testAlert);
            log.info("테스트 메시지 전송 완료");
            
        } catch (Exception e) {
            log.error("테스트 메시지 전송 실패: {}", e.getMessage(), e);
        }
    }
}