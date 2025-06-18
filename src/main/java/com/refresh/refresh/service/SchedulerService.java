package com.refresh.refresh.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.refresh.refresh.entity.RealtimeDanger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final ExcutePythonSerivce pythonService;
    private final RealtimeDangerService realtimeDangerService;
    private final DangerAlertWebSocketService dangerAlertWebSocketService; // 추가
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${python.executable.path:python}")
    private String pythonExecutablePath;

    @Value("${python.scripts.directory:scripts_py}")
    private String scriptsDirectory;

    @Value("${python.execution.timeout:300}")
    private long executionTimeoutSeconds;

    // 범죄 키워드별 위험도 ID 매핑
    private static final Map<String, Integer> CRIME_KEYWORD_TO_DANGER_ID = new HashMap<>();
    static {
        CRIME_KEYWORD_TO_DANGER_ID.put("강도", 1);
        CRIME_KEYWORD_TO_DANGER_ID.put("성폭행", 2);
        CRIME_KEYWORD_TO_DANGER_ID.put("폭행", 3);
        CRIME_KEYWORD_TO_DANGER_ID.put("절도", 4);
        CRIME_KEYWORD_TO_DANGER_ID.put("살인", 5);
    }

    /**
     * 5분마다 크롤링 + AI 키워드 추출 실행
     * cron: 초 분 시 일 월 요일
     * 0 *"/5" * * * * = 매 5분마다 실행
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void executeScheduledCrawlingWithAI() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("=== 스케줄러 실행 시작 [{}] ===", timestamp);
        
        try {
            // 1단계: 크롤링 실행
            log.info("1단계: 뉴스 크롤링 실행 중...");
            String crawlingResult = pythonService.executeNewsCrawling();
            
            // JSON 부분만 추출
            String cleanCrawlingJson = extractJsonFromOutput(crawlingResult);
            log.info("크롤링 결과 추출 완료");
            
            // 크롤링 결과 파싱
            JsonNode crawlingJson = objectMapper.readTree(cleanCrawlingJson);
            boolean hasNewArticles = crawlingJson.path("hasNewArticles").asBoolean(false);
            
            // crawlNum 추출 (문자열 또는 숫자 모두 처리)
            int crawlNum = 0;
            JsonNode crawlNumNode = crawlingJson.path("crawlNum");
            if (!crawlNumNode.isMissingNode()) {
                if (crawlNumNode.isTextual()) {
                    try {
                        crawlNum = Integer.parseInt(crawlNumNode.asText());
                    } catch (NumberFormatException e) {
                        log.warn("crawlNum 파싱 실패: {}", crawlNumNode.asText());
                    }
                } else {
                    crawlNum = crawlNumNode.asInt(0);
                }
            }
            
            // 기존 newArticleCount도 시도 (하위 호환)
            int newArticleCount = crawlingJson.path("newArticleCount").asInt(crawlNum);
            
            log.info("크롤링 완료 - 새로운 기사: {}, crawlNum: {}, newArticleCount: {}", 
                    hasNewArticles, crawlNum, newArticleCount);
            
            // 2단계: 새로운 기사가 있으면 AI 키워드 추출 실행
            if (hasNewArticles && crawlNum > 0) {
                log.info("2단계: AI 키워드 추출 실행 중... (범위: 1-{})", crawlNum);
                
                try {
                    String articleDirectory = extractArticleDirectory(crawlingJson);
                    
                    // crawlNum을 기반으로 범위 자동 설정하여 AI 실행
                    String aiOutput = pythonService.executeAiKeywordExtractionByCrawlNum(articleDirectory, crawlNum);
                    String aiResult = extractJsonFromOutput(aiOutput);
                    
                    JsonNode aiJson = objectMapper.readTree(aiResult);
                    int processedFiles = aiJson.path("processed_files").asInt(0);
                    
                    // 3단계: AI 결과를 RealtimeDanger에 저장
                    List<RealtimeDanger> savedDangers = saveAiResultsToRealtimeDanger(aiJson);
                    
                    log.info("AI 키워드 추출 완료 - 처리된 파일: {}개, DB 저장: {}개", 
                            processedFiles, savedDangers.size());
                    
                    // 성공 로그
                    log.info("=== 스케줄러 실행 성공 [{}] === 크롤링: {}개, AI 처리: {}개, DB 저장: {}개", 
                            timestamp, crawlNum, processedFiles, savedDangers.size());
                    
                } catch (Exception aiException) {
                    log.error("AI 키워드 추출 또는 DB 저장 실패", aiException);
                    log.info("=== 스케줄러 부분 실패 [{}] === 크롤링: {}개, AI 처리: 실패", 
                            timestamp, crawlNum);
                }
            } else {
                log.info("=== 스케줄러 실행 완료 [{}] === 새로운 기사 없음", timestamp);
            }
            
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생", e);
            log.info("=== 스케줄러 실행 실패 [{}] === 오류: {}", timestamp, e.getMessage());
        }
    }

    /**
     * AI 키워드 추출 결과를 RealtimeDanger에 저장
     */
    private List<RealtimeDanger> saveAiResultsToRealtimeDanger(JsonNode aiJson) {
        List<RealtimeDanger> savedDangers = new ArrayList<>();
        
        JsonNode resultsNode = aiJson.path("results");
        if (resultsNode.isArray()) {
            for (JsonNode resultNode : resultsNode) {
                try {
                    RealtimeDanger danger = convertAiResultToRealtimeDanger(resultNode);
                    if (danger != null) {
                        RealtimeDanger saved = realtimeDangerService.createRealtimeDanger(danger);
                        savedDangers.add(saved);
                        
                        // WebSocket으로 실시간 알림 전송
                        String alertSentence = resultNode.path("alert_sentence").asText("");
                        dangerAlertWebSocketService.sendDangerAlert(alertSentence, saved.getDangerDetailId());
                        
                        log.info("위험 정보 저장 및 알림 전송 완료 [ID: {}]: {} - {}", 
                                saved.getDangerId(), saved.getLocateName(), alertSentence);
                    }
                } catch (Exception e) {
                    log.error("AI 결과 저장 중 오류: {}", resultNode, e);
                }
            }
        }
        
        return savedDangers;
    }

    /**
     * AI 결과를 RealtimeDanger 엔티티로 변환
     */
    private RealtimeDanger convertAiResultToRealtimeDanger(JsonNode resultNode) {
        try {
            String status = resultNode.path("status").asText("");
            if (!"success".equals(status)) {
                log.debug("AI 결과 상태가 success가 아님: {}", status);
                return null;
            }
            
            String location = resultNode.path("location").asText("");
            String crimeKeyword = resultNode.path("crime_keyword").asText("");
            String timeStr = resultNode.path("time").asText("");
            
            // 필수 정보 검증
            if (location.isEmpty() || crimeKeyword.isEmpty()) {
                log.warn("AI 결과에 필수 정보 누락 - location: {}, crimeKeyword: {}", location, crimeKeyword);
                return null;
            }
            
            // RealtimeDanger 엔티티 생성
            RealtimeDanger danger = new RealtimeDanger();
            
            // 위치 정보 설정
            danger.setLocateName(location);
            danger.setPlaceLocation(location); // 동일하게 설정
            
            // 범죄 키워드에 따른 위험도 ID 설정
            Integer dangerDetailId = CRIME_KEYWORD_TO_DANGER_ID.get(crimeKeyword);
            if (dangerDetailId == null) {
                log.warn("알 수 없는 범죄 키워드: {}, 기본값 3(폭력) 사용", crimeKeyword);
                dangerDetailId = 3; // 기본값: 폭력
            }
            danger.setDangerDetailId(dangerDetailId);
            
            // 발생 시간 설정
            LocalDateTime occurredAt = parseTimeToDateTime(timeStr);
            danger.setOccurredAt(occurredAt);
            
            log.debug("RealtimeDanger 엔티티 생성 완료: {} - {} - {}", 
                     location, crimeKeyword, occurredAt);
            return danger;
            
        } catch (Exception e) {
            log.error("AI 결과 변환 중 오류", e);
            return null;
        }
    }

    /**
     * AI에서 추출한 시간 문자열을 LocalDateTime으로 변환
     */
    private LocalDateTime parseTimeToDateTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        
        try {
            // "7일", "8일" 형태의 문자열에서 숫자만 추출
            String dayStr = timeStr.replaceAll("[^0-9]", "");
            if (!dayStr.isEmpty()) {
                int day = Integer.parseInt(dayStr);
                
                LocalDateTime now = LocalDateTime.now();
                
                if (day >= 1 && day <= 31) {
                    try {
                        return now.withDayOfMonth(day).withHour(0).withMinute(0).withSecond(0).withNano(0);
                    } catch (Exception e) {
                        log.warn("날짜 설정 실패 ({}일), 현재 시간 사용", day);
                        return now;
                    }
                }
            }
        } catch (NumberFormatException e) {
            log.warn("시간 파싱 실패: {}, 현재 시간 사용", timeStr);
        }
        
        return LocalDateTime.now();
    }

    /**
     * 매일 자정에 로그 정리 (선택사항)
     * 0 0 0 * * * = 매일 자정
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void dailyLogCleanup() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.info("=== 일일 로그 정리 [{}] ===", timestamp);
        
        // 여기에 로그 정리 로직 추가 가능
        // 예: 오래된 크롤링 파일 삭제, 캐시 정리 등
    }

    /**
     * 파이썬 출력에서 JSON 부분만 추출 (간소화됨)
     * 이제 크롤링과 AI 스크립트 모두 마커 없이 JSON 출력
     */
    private String extractJsonFromOutput(String output) {
        // 마지막에 나오는 유효한 JSON 라인 찾기
        String[] lines = output.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (line.startsWith("{") && line.endsWith("}")) {
                try {
                    // JSON 유효성 검사
                    objectMapper.readTree(line);
                    log.debug("JSON 추출 성공: {}", line);
                    return line;
                } catch (Exception e) {
                    // 유효하지 않은 JSON이면 계속 찾기
                    continue;
                }
            }
        }
        
        // JSON을 찾지 못한 경우
        log.warn("JSON을 찾지 못했습니다. 전체 출력 길이: {} chars", output.length());
        log.debug("전체 출력 내용: {}", output);
        return "{\"success\": false, \"message\": \"JSON 결과를 찾을 수 없습니다.\"}";
    }

    /**
     * 크롤링 결과에서 기사 디렉토리 경로 추출
     */
    private String extractArticleDirectory(JsonNode crawlingJson) {
        String directory = crawlingJson.path("articleDirectory").asText("");
        if (directory.isEmpty()) {
            directory = "../../data/crawling/naver_articles";
        }
        return directory;
    }
}