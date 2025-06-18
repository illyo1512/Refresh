package com.refresh.refresh.controller.TestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.refresh.refresh.service.ExcutePythonSerivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class PythonTestController {

    private final ExcutePythonSerivce pythonService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 테스트 페이지 반환
     */
    @GetMapping("/crawling")
    public String crawlingTestPage() {
        return "python-test";
    }

    /**
     * 크롤링 실행 API
     */
    @PostMapping("/crawling/execute")
    @ResponseBody
    public ResponseEntity<String> executeCrawling() {
        try {
            log.info("크롤링 테스트 실행 요청");
            String result = pythonService.executeNewsCrawling();
            log.info("크롤링 테스트 완료");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("크롤링 테스트 실행 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body("{\"success\": false, \"message\": \"테스트 실행 실패: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 크롤링 + AI 키워드 추출 통합 실행 API (개선된 버전)
     * 1. 먼저 크롤링 실행
     * 2. hasNewArticles가 true면 crawlNum을 기반으로 AI 키워드 추출 실행
     * 3. 통합 결과 반환
     */
    @PostMapping("/crawling/with-ai")
    @ResponseBody
    public ResponseEntity<String> executeCrawlingWithAI() {
        try {
            log.info("크롤링 + AI 키워드 추출 통합 실행 요청");
            
            // 1단계: 크롤링 실행
            log.info("1단계: 뉴스 크롤링 실행 중...");
            String crawlingResult = pythonService.executeNewsCrawling();
            log.info("크롤링 원본 결과: {}", crawlingResult);
            
            // JSON 부분만 추출
            String cleanCrawlingJson = extractJsonFromOutput(crawlingResult);
            log.info("추출된 JSON: {}", cleanCrawlingJson);
            
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
            
            log.info("크롤링 완료 - 새로운 기사: {}, crawlNum: {}", hasNewArticles, crawlNum);
            
            // 2단계: 새로운 기사가 있으면 AI 키워드 추출 실행
            String aiResult = null;
            if (hasNewArticles && crawlNum > 0) {
                log.info("2단계: AI 키워드 추출 실행 중... (범위: 1-{})", crawlNum);
                
                // 기사 디렉토리 경로 (크롤링 결과에서 추출하거나 기본값 사용)
                String articleDirectory = extractArticleDirectory(crawlingJson);
                
                try {
                    // crawlNum을 기반으로 범위 자동 설정하여 AI 실행
                    String aiOutput = pythonService.executeAiKeywordExtractionByCrawlNum(articleDirectory, crawlNum);
                    aiResult = extractJsonFromOutput(aiOutput);
                    log.info("AI 키워드 추출 완료");
                } catch (Exception aiException) {
                    log.error("AI 키워드 추출 실패", aiException);
                    aiResult = "{\"success\": false, \"message\": \"AI 키워드 추출 실패: " + aiException.getMessage() + "\"}";
                }
            } else if (hasNewArticles && crawlNum == 0) {
                log.warn("새로운 기사가 있다고 하지만 crawlNum이 0입니다. 전체 파일 처리 시도...");
                
                String articleDirectory = extractArticleDirectory(crawlingJson);
                
                try {
                    String aiOutput = pythonService.executeAiKeywordExtraction(articleDirectory);
                    aiResult = extractJsonFromOutput(aiOutput);
                    log.info("AI 키워드 추출 완료 (전체 처리)");
                } catch (Exception aiException) {
                    log.error("AI 키워드 추출 실패 (전체 처리)", aiException);
                    aiResult = "{\"success\": false, \"message\": \"AI 키워드 추출 실패: " + aiException.getMessage() + "\"}";
                }
            } else {
                log.info("새로운 기사가 없어 AI 키워드 추출을 건너뜁니다.");
            }
            
            // 3단계: 통합 결과 생성
            String combinedResult = createCombinedResult(cleanCrawlingJson, aiResult, hasNewArticles, crawlNum);
            
            log.info("크롤링 + AI 키워드 추출 통합 실행 완료");
            return ResponseEntity.ok(combinedResult);
            
        } catch (Exception e) {
            log.error("크롤링 + AI 키워드 추출 통합 실행 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body("{\"success\": false, \"message\": \"통합 실행 실패: " + e.getMessage() + "\"}");
        }
    }

    /**
     * AI 키워드 추출만 실행 API (범위 지정 지원)
     */
    @PostMapping("/ai-keyword/execute")
    @ResponseBody
    public ResponseEntity<String> executeAiKeyword(
            @RequestParam(defaultValue = "../../data/crawling/naver_articles") String articleDirectory,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) Integer crawlNum) {
        try {
            log.info("AI 키워드 추출 실행 요청 - 디렉토리: {}, 범위: {}, crawlNum: {}", 
                    articleDirectory, range, crawlNum);
            
            String result;
            if (crawlNum != null && crawlNum > 0) {
                // 크롤링 개수 기반
                log.info("크롤링 개수 기반 AI 키워드 추출 (범위: 1-{})", crawlNum);
                result = pythonService.executeAiKeywordExtractionByCrawlNum(articleDirectory, crawlNum);
            } else if (range != null && !range.isEmpty()) {
                // 범위 지정
                log.info("범위 지정 AI 키워드 추출 (범위: {})", range);
                result = pythonService.executeAiKeywordExtraction(articleDirectory, range);
            } else {
                // 전체 처리
                log.info("전체 파일 AI 키워드 추출");
                result = pythonService.executeAiKeywordExtraction(articleDirectory);
            }
            
            // JSON 부분만 추출
            String cleanResult = extractJsonFromOutput(result);
            
            log.info("AI 키워드 추출 완료");
            return ResponseEntity.ok(cleanResult);
        } catch (Exception e) {
            log.error("AI 키워드 추출 실행 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body("{\"success\": false, \"message\": \"AI 키워드 추출 실행 실패: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 범위별 AI 키워드 추출 테스트 API
     */
    @PostMapping("/ai-keyword/test-range")
    @ResponseBody
    public ResponseEntity<String> testAiKeywordWithRange(
            @RequestParam(defaultValue = "../../data/crawling/naver_articles") String articleDirectory,
            @RequestParam String range) {
        try {
            log.info("범위별 AI 키워드 추출 테스트 - 디렉토리: {}, 범위: {}", articleDirectory, range);
            
            String result = pythonService.executeAiKeywordExtraction(articleDirectory, range);
            String cleanResult = extractJsonFromOutput(result);
            
            log.info("범위별 AI 키워드 추출 테스트 완료");
            return ResponseEntity.ok(cleanResult);
        } catch (Exception e) {
            log.error("범위별 AI 키워드 추출 테스트 중 오류", e);
            return ResponseEntity.internalServerError()
                    .body("{\"success\": false, \"message\": \"테스트 실행 실패: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 파이썬 출력에서 JSON 부분만 추출
     */
    private String extractJsonFromOutput(String output) {
        // 마커를 사용하는 경우 (AI 키워드 추출)
        String startMarker = "=== JSON_RESULT_START ===";
        String endMarker = "=== JSON_RESULT_END ===";
        
        int startIndex = output.indexOf(startMarker);
        int endIndex = output.indexOf(endMarker);
        
        if (startIndex != -1 && endIndex != -1) {
            return output.substring(startIndex + startMarker.length(), endIndex).trim();
        }
        
        // 마커가 없는 경우 JSON 직접 추출 시도 (크롤링)
        // 가장 마지막에 나오는 JSON 객체를 찾음
        String[] lines = output.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (line.startsWith("{") && line.endsWith("}")) {
                try {
                    // JSON 유효성 검사
                    objectMapper.readTree(line);
                    return line;
                } catch (Exception e) {
                    // JSON이 아니면 계속 찾기
                    continue;
                }
            }
        }
        
        // JSON을 찾지 못한 경우 전체 출력 반환
        log.warn("JSON을 찾지 못했습니다. 전체 출력: {}", output);
        return output;
    }

    /**
     * 크롤링 결과에서 기사 디렉토리 경로 추출
     */
    private String extractArticleDirectory(JsonNode crawlingJson) {
        // 크롤링 결과에 디렉토리 정보가 있으면 사용, 없으면 기본값
        String directory = crawlingJson.path("articleDirectory").asText("");
        if (directory.isEmpty()) {
            // 기본 디렉토리 경로 (paths.py에서 설정된 경로와 일치해야 함)
            directory = "../../data/crawling/naver_articles";
        }
        return directory;
    }

    /**
     * 크롤링 결과와 AI 결과를 통합한 JSON 생성 (개선된 버전)
     */
    private String createCombinedResult(String crawlingResult, String aiResult, boolean hasNewArticles, int crawlNum) {
        try {
            JsonNode crawlingJson = objectMapper.readTree(crawlingResult);
            
            StringBuilder combinedJson = new StringBuilder();
            combinedJson.append("{");
            combinedJson.append("\"success\": ").append(crawlingJson.path("success").asBoolean(false)).append(",");
            combinedJson.append("\"timestamp\": \"").append(java.time.LocalDateTime.now()).append("\",");
            combinedJson.append("\"crawling\": ").append(crawlingResult).append(",");
            combinedJson.append("\"hasNewArticles\": ").append(hasNewArticles).append(",");
            combinedJson.append("\"crawlNum\": ").append(crawlNum).append(",");
            
            if (hasNewArticles && aiResult != null) {
                combinedJson.append("\"aiKeywordExtraction\": ").append(aiResult).append(",");
                combinedJson.append("\"aiExecuted\": true,");
                combinedJson.append("\"aiRange\": \"").append(crawlNum > 1 ? "1-" + crawlNum : "1").append("\"");
            } else {
                combinedJson.append("\"aiKeywordExtraction\": null,");
                combinedJson.append("\"aiExecuted\": false,");
                combinedJson.append("\"aiRange\": null");
            }
            
            combinedJson.append(",\"message\": \"");
            if (hasNewArticles && aiResult != null) {
                combinedJson.append("크롤링 및 AI 키워드 추출 완료 (").append(crawlNum).append("개 기사 처리)");
            } else if (hasNewArticles) {
                combinedJson.append("크롤링 완료, AI 키워드 추출 실패 (").append(crawlNum).append("개 기사)");
            } else {
                combinedJson.append("크롤링 완료, 새로운 기사 없음");
            }
            combinedJson.append("\"}");
            
            return combinedJson.toString();
            
        } catch (Exception e) {
            log.error("통합 결과 생성 중 오류", e);
            return "{\"success\": false, \"message\": \"통합 결과 생성 실패: " + e.getMessage() + "\"}";
        }
    }
}