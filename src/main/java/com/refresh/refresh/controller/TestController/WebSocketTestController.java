package com.refresh.refresh.controller.TestController;

import com.refresh.refresh.service.DangerAlertWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class WebSocketTestController {

    private final DangerAlertWebSocketService dangerAlertWebSocketService;

    /**
     * 테스트 알림 전송
     */
    @PostMapping("/send-alert")
    public ResponseEntity<String> sendTestAlert() {
        try {
            dangerAlertWebSocketService.sendDangerAlert("테스트 위험 알림입니다.", 3);
            log.info("테스트 알림 전송 완료");
            return ResponseEntity.ok("테스트 알림 전송 완료");
        } catch (Exception e) {
            log.error("테스트 알림 전송 실패", e);
            return ResponseEntity.internalServerError().body("테스트 알림 전송 실패: " + e.getMessage());
        }
    }

    /**
     * 커스텀 테스트 알림 전송
     */
    @PostMapping("/send-custom-alert")
    public ResponseEntity<Map<String, Object>> sendCustomAlert(
            @RequestParam(defaultValue = "커스텀 테스트 알림입니다.") String message,
            @RequestParam(defaultValue = "1") Integer dangerLevel) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            dangerAlertWebSocketService.sendDangerAlert(message, dangerLevel);
            
            response.put("success", true);
            response.put("message", "커스텀 알림 전송 완료");
            response.put("sentMessage", message);
            response.put("dangerLevel", dangerLevel);
            
            log.info("커스텀 테스트 알림 전송 완료 - 메시지: {}, 위험도: {}", message, dangerLevel);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "커스텀 알림 전송 실패: " + e.getMessage());
            
            log.error("커스텀 테스트 알림 전송 실패", e);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 다중 테스트 알림 전송
     */
    @PostMapping("/send-multiple-alerts")
    public ResponseEntity<Map<String, Object>> sendMultipleAlerts(
            @RequestParam(defaultValue = "3") int count,
            @RequestParam(defaultValue = "1000") int interval) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            new Thread(() -> {
                for (int i = 1; i <= count; i++) {
                    try {
                        String message = String.format("다중 테스트 알림 %d/%d", i, count);
                        int dangerLevel = (i % 5) + 1; // 1~5 순환
                        
                        dangerAlertWebSocketService.sendDangerAlert(message, dangerLevel);
                        log.info("다중 테스트 알림 전송: {}", message);
                        
                        if (i < count) {
                            Thread.sleep(interval);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        log.error("다중 테스트 알림 전송 중 오류", e);
                    }
                }
            }).start();
            
            response.put("success", true);
            response.put("message", String.format("%d개의 알림을 %dms 간격으로 전송 시작", count, interval));
            response.put("count", count);
            response.put("interval", interval);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "다중 알림 전송 실패: " + e.getMessage());
            
            log.error("다중 테스트 알림 전송 실패", e);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 위험도별 테스트 알림 전송
     */
    @PostMapping("/send-danger-level-alerts")
    public ResponseEntity<Map<String, Object>> sendDangerLevelAlerts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String[] crimeTypes = {"강도", "성폭행", "폭행", "절도", "살인"};
            String[] locations = {"서울", "부산", "대구", "인천", "광주"};
            
            new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    try {
                        String message = String.format("%s에서 %s이(가) 발생했습니다.", 
                                locations[i-1], crimeTypes[i-1]);
                        
                        dangerAlertWebSocketService.sendDangerAlert(message, i);
                        log.info("위험도별 테스트 알림 전송: {} (위험도: {})", message, i);
                        
                        if (i < 5) {
                            Thread.sleep(2000); // 2초 간격
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (Exception e) {
                        log.error("위험도별 테스트 알림 전송 중 오류", e);
                    }
                }
            }).start();
            
            response.put("success", true);
            response.put("message", "위험도별 테스트 알림 전송 시작 (1~5단계, 2초 간격)");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "위험도별 알림 전송 실패: " + e.getMessage());
            
            log.error("위험도별 테스트 알림 전송 실패", e);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * WebSocket 연결 상태 확인 (참고용)
     */
    @GetMapping("/websocket-info")
    public ResponseEntity<Map<String, Object>> getWebSocketInfo() {
        Map<String, Object> info = new HashMap<>();
        
        info.put("endpoint", "/ws-danger-alerts");
        info.put("topic", "/topic/danger-alerts");
        info.put("description", "실시간 위험 알림을 위한 WebSocket 엔드포인트");
        info.put("testEndpoints", Map.of(
            "기본 테스트", "POST /api/test/send-alert",
            "커스텀 테스트", "POST /api/test/send-custom-alert?message=메시지&dangerLevel=1",
            "다중 테스트", "POST /api/test/send-multiple-alerts?count=5&interval=1000",
            "위험도별 테스트", "POST /api/test/send-danger-level-alerts"
        ));
        
        return ResponseEntity.ok(info);
    }
}
