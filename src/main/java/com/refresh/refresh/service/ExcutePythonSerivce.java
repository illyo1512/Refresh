package com.refresh.refresh.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ExcutePythonSerivce {

    @Value("${python.executable.path:python}")
    private String pythonExecutablePath;

    @Value("${python.scripts.directory:scripts_py}")
    private String scriptsDirectory;

    @Value("${python.execution.timeout:300}")
    private long executionTimeoutSeconds;

    /**
     * 네이버 뉴스 크롤링 실행
     * 
     * @return 크롤링 결과 JSON 문자열
     */
    public String executeNewsCrawling() {
        try {
            List<String> command = new ArrayList<>();
            command.add(pythonExecutablePath);
            command.add(getScriptPath("crawling_script/crawling_txt.py"));

            log.info("뉴스 크롤링 스크립트 실행 시작");
            String result = executeCommand(command);
            log.info("뉴스 크롤링 스크립트 실행 완료");
            
            return result;

        } catch (Exception e) {
            log.error("뉴스 크롤링 실행 중 오류", e);
            return "{\"success\": false, \"hasNewArticles\": false, \"message\": \"크롤링 실행 실패\"}";
        }
    }

    /**
     * AI 키워드 추출 실행 (전체 파일)
     * 
     * @param articleDirectory 기사 디렉토리 경로
     * @return AI 키워드 추출 결과 JSON 문자열
     */
    public String executeAiKeywordExtraction(String articleDirectory) throws Exception {
        log.info("AI 키워드 추출 시작 - 디렉토리: {}", articleDirectory);
        
        // 파이썬 스크립트 경로
        String scriptPath = getScriptPath("AI_script/ai_keyword.py");
        
        // 명령어 구성 (범위 없이 전체 파일 처리)
        List<String> command = Arrays.asList(
            pythonExecutablePath,
            scriptPath,
            articleDirectory  // 디렉토리 경로만 전달
        );
        
        // 파이썬 스크립트 실행
        String output = executeCommand(command);
        
        // JSON 결과 추출
        String jsonResult = extractJsonFromOutput(output);
        
        log.info("AI 키워드 추출 완료");
        return jsonResult;
    }

    /**
     * AI 키워드 추출 실행 (범위 지정)
     * 
     * @param articleDirectory 기사 디렉토리 경로
     * @param range 처리할 파일 범위 (예: "1-10", "1,3,5", "all")
     * @return AI 키워드 추출 결과 JSON 문자열
     */
    public String executeAiKeywordExtraction(String articleDirectory, String range) throws Exception {
        log.info("AI 키워드 추출 시작 - 디렉토리: {}, 범위: {}", articleDirectory, range);
        
        // 파이썬 스크립트 경로
        String scriptPath = getScriptPath("AI_script/ai_keyword.py");
        
        // 명령어 구성 (범위 포함)
        List<String> command = Arrays.asList(
            pythonExecutablePath,
            scriptPath,
            articleDirectory,  // 디렉토리 경로
            range             // 처리 범위
        );
        
        // 파이썬 스크립트 실행
        String output = executeCommand(command);
        
        // JSON 결과 추출
        String jsonResult = extractJsonFromOutput(output);
        
        log.info("AI 키워드 추출 완료 - 범위: {}", range);
        return jsonResult;
    }

    /**
     * AI 키워드 추출 실행 (크롤링 개수 기반 자동 범위 설정)
     * 
     * @param articleDirectory 기사 디렉토리 경로
     * @param crawlNum 크롤링된 기사 개수 (1이면 "1", 3이면 "1-3", 4이면 "1-4")
     * @return AI 키워드 추출 결과 JSON 문자열
     */
    public String executeAiKeywordExtractionByCrawlNum(String articleDirectory, int crawlNum) throws Exception {
        // crawlNum을 기반으로 범위 문자열 생성
        String range;
        if (crawlNum <= 0) {
            range = "all";  // 전체 처리
        } else if (crawlNum == 1) {
            range = "1";    // 1번만
        } else {
            range = "1-" + crawlNum;  // 1부터 crawlNum까지
        }
        
        log.info("크롤링 개수 기반 AI 키워드 추출 - 개수: {}, 범위: {}", crawlNum, range);
        
        // 범위 지정 버전 호출
        return executeAiKeywordExtraction(articleDirectory, range);
    }

    /**
     * 파이썬 명령어 실행 - UTF-8 인코딩 설정
     */
    private String executeCommand(List<String> command) throws Exception {
        log.info("실행할 명령어: {}", String.join(" ", command));
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        
        // 환경변수에 UTF-8 인코딩 설정
        Map<String, String> env = processBuilder.environment();
        env.put("PYTHONIOENCODING", "utf-8");
        env.put("PYTHONLEGACYWINDOWSSTDIO", "utf-8");
        env.put("HF_HUB_DISABLE_SYMLINKS_WARNING", "1");  // Hugging Face 경고 숨김
        
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        
        // UTF-8로 출력 읽기
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                log.info("Python 출력: {}", line);
            }
        }
        
        boolean finished = process.waitFor(executionTimeoutSeconds, TimeUnit.SECONDS);
        
        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("파이썬 스크립트 실행 시간 초과 (" + executionTimeoutSeconds + "초)");
        }
        
        int exitCode = process.exitValue();
        
        if (exitCode != 0) {
            log.error("파이썬 스크립트 실행 실패 (Exit Code: {})", exitCode);
            throw new RuntimeException("파이썬 스크립트 실행 실패 (Exit Code: " + exitCode + ")");
        }
        
        return output.toString().trim();
    }

    /**
     * 스크립트 파일 경로 생성 - 로그 추가
     */
    private String getScriptPath(String scriptName) throws IOException {
        Path scriptPath = Paths.get(scriptsDirectory, scriptName);
        log.info("스크립트 경로: {}", scriptPath.toAbsolutePath());
        
        if (!Files.exists(scriptPath)) {
            log.error("파이썬 스크립트를 찾을 수 없습니다: {}", scriptPath.toAbsolutePath());
            throw new IOException("파이썬 스크립트를 찾을 수 없습니다: " + scriptPath.toAbsolutePath());
        }
        
        return scriptPath.toAbsolutePath().toString();
    }

    /**
     * 출력 결과에서 JSON 추출
     */
    private String extractJsonFromOutput(String output) {
        // === JSON_RESULT_START ===와 === JSON_RESULT_END === 사이의 JSON 추출
        String startMarker = "=== JSON_RESULT_START ===";
        String endMarker = "=== JSON_RESULT_END ===";
        
        int startIndex = output.indexOf(startMarker);
        int endIndex = output.indexOf(endMarker);
        
        if (startIndex != -1 && endIndex != -1) {
            return output.substring(startIndex + startMarker.length(), endIndex).trim();
        }
        
        // 마커가 없는 경우 마지막 JSON 라인 찾기 시도
        String[] lines = output.split("\n");
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].trim();
            if (line.startsWith("{") && line.endsWith("}")) {
                return line;
            }
        }
        
        return "{\"success\": false, \"message\": \"JSON 결과를 찾을 수 없습니다.\"}";
    }
}
