// package com.refresh.refresh.controller;

// import com.graphhopper.GraphHopper;
// import com.graphhopper.GHRequest;
// import com.graphhopper.GHResponse;
// import com.graphhopper.config.Profile;
// import com.graphhopper.util.CustomModel;
// import com.refresh.refresh.service.navigate.ComplicateModelTest;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.List;
// import java.util.stream.Collectors;

// @Slf4j
// @RestController
// @RequestMapping("/test/graphhopper")
// @CrossOrigin(origins = "*")
// public class GraphHopperTestController {
    
//     @Autowired
//     private GraphHopper graphHopper;
    
//     @Autowired
//     private ComplicateModelTest complicateModelTest;
    
//     /**
//      * GraphHopper 초기화 상태 확인
//      */
//     @GetMapping("/status")
//     public String getStatus() {
//         try {
//             StringBuilder sb = new StringBuilder();
//             sb.append("=== GraphHopper 상태 ===\n");
//             sb.append("초기화 완료: ").append(graphHopper.getFullyLoaded()).append("\n");
//             sb.append("OSM 파일: ").append(graphHopper.getOSMFile()).append("\n");
//             sb.append("그래프 위치: ").append(graphHopper.getGraphHopperLocation()).append("\n");
//             sb.append("사용 가능한 Profile 수: ").append(graphHopper.getProfiles().size()).append("\n");
            
//             return sb.toString();
            
//         } catch (Exception e) {
//             log.error("GraphHopper 상태 확인 중 오류", e);
//             return "오류: " + e.getMessage();
//         }
//     }
    
//     /**
//      * 사용 가능한 Profile 목록 조회
//      */
//     @GetMapping("/profiles")
//     public List<String> getProfiles() {
//         try {
//             List<String> profileNames = graphHopper.getProfiles()
//                 .stream()
//                 .map(Profile::getName)
//                 .collect(Collectors.toList());
                
//             log.info("사용 가능한 Profile: {}", profileNames);
//             return profileNames;
            
//         } catch (Exception e) {
//             log.error("Profile 목록 조회 중 오류", e);
//             return List.of("오류: " + e.getMessage());
//         }
//     }
    
//     /**
//      * 간단한 라우팅 테스트 (서울 시내)
//      */
//     @GetMapping("/route/test")
//     public String testRouting(@RequestParam(defaultValue = "foot_shortest") String profile) {
//         try {
//             log.info("라우팅 테스트 시작: profile={}", profile);
            
//             // 서울역 -> 명동역 (약 1.5km)
//             double fromLat = 37.5547, fromLon = 126.9706;  // 서울역
//             double toLat = 37.5636, toLon = 126.9834;      // 명동역
            
//             GHRequest request = new GHRequest(fromLat, fromLon, toLat, toLon)
//                 .setProfile(profile);
                
//             GHResponse response = graphHopper.route(request);
            
//             if (response.hasErrors()) {
//                 String errors = response.getErrors().toString();
//                 log.error("라우팅 실패: {}", errors);
//                 return "라우팅 실패: " + errors;
//             }
            
//             double distance = response.getBest().getDistance();
//             long time = response.getBest().getTime();
            
//             String result = String.format(
//                 "라우팅 성공!\n" +
//                 "Profile: %s\n" +
//                 "거리: %.0fm\n" +
//                 "예상 시간: %d초\n" +
//                 "경로점 수: %d개",
//                 profile, distance, time / 1000, 
//                 response.getBest().getPoints().size()
//             );
            
//             log.info("라우팅 테스트 성공: profile={}, 거리={}m, 시간={}초", 
//                 profile, distance, time / 1000);
                
//             return result;
            
//         } catch (Exception e) {
//             log.error("라우팅 테스트 중 오류", e);
//             return "라우팅 테스트 오류: " + e.getMessage();
//         }
//     }
    
//     /**
//      * 모든 Profile로 라우팅 테스트
//      */
//     @GetMapping("/route/test-all")
//     public String testAllProfiles() {
//         try {
//             StringBuilder results = new StringBuilder();
//             results.append("=== 모든 Profile 라우팅 테스트 ===\n\n");
            
//             List<String> profiles = getProfiles();
            
//             for (String profile : profiles) {
//                 if (profile.startsWith("오류:")) continue;
                
//                 try {
//                     results.append("Profile: ").append(profile).append("\n");
//                     String result = testRouting(profile);
//                     results.append(result).append("\n\n");
                    
//                 } catch (Exception e) {
//                     results.append("오류: ").append(e.getMessage()).append("\n\n");
//                 }
//             }
            
//             return results.toString();
            
//         } catch (Exception e) {
//             log.error("전체 Profile 테스트 중 오류", e);
//             return "전체 테스트 오류: " + e.getMessage();
//         }
//     }
    
//     /**
//      * JSON 파일을 CustomModel로 변환 테스트
//      */
//     @GetMapping("/custommodel/load")
//     public String loadJsonToCustomModel(@RequestParam String jsonFilePath) {
//         try {
//             CustomModel customModel = complicateModelTest.loadJsonToCustomModel(jsonFilePath);
            
//             StringBuilder result = new StringBuilder();
//             result.append("=== JSON -> CustomModel 변환 성공 ===\n");
//             result.append("파일: ").append(jsonFilePath).append("\n");
//             result.append("distanceInfluence: ").append(customModel.getDistanceInfluence()).append("\n");
//             result.append("priority 규칙 수: ").append(customModel.getPriority().size()).append("\n");
//             result.append("speed 규칙 수: ").append(customModel.getSpeed().size()).append("\n");
            
//             return result.toString();
            
//         } catch (Exception e) {
//             log.error("JSON -> CustomModel 변환 실패", e);
//             return "변환 실패: " + e.getMessage();
//         }
//     }
    
//     /**
//      * Profile 복사 + JSON CustomModel 통합
//      */
//     @GetMapping("/profile/copy-with-json")
//     public String copyProfileWithJson(
//             @RequestParam String originalProfileName,
//             @RequestParam String jsonFilePath,
//             @RequestParam String newProfileName) {
//         try {
//             Profile newProfile = complicateModelTest.copyProfileWithJson(
//                 originalProfileName, jsonFilePath, newProfileName
//             );
            
//             StringBuilder result = new StringBuilder();
//             result.append("=== Profile 복사 + JSON 통합 성공 ===\n");
//             result.append("원본 Profile: ").append(originalProfileName).append("\n");
//             result.append("JSON 파일: ").append(jsonFilePath).append("\n");
//             result.append("새 Profile: ").append(newProfileName).append("\n");
//             result.append("Vehicle: ").append(newProfile.getVehicle()).append("\n");
//             result.append("Weighting: ").append(newProfile.getWeighting()).append("\n");
//             result.append("DistanceInfluence: ").append(newProfile.getCustomModel().getDistanceInfluence()).append("\n");
//             result.append("Priority 규칙 수: ").append(newProfile.getCustomModel().getPriority().size()).append("\n");
//             result.append("Speed 규칙 수: ").append(newProfile.getCustomModel().getSpeed().size()).append("\n");
            
//             return result.toString();
            
//         } catch (Exception e) {
//             log.error("Profile 복사 + JSON 통합 실패", e);
//             return "통합 실패: " + e.getMessage();
//         }
//     }
    
//     /**
//      * 통합된 Profile로 라우팅 테스트
//      */
//     @GetMapping("/profile/test-integrated")
//     public String testIntegratedProfile(
//             @RequestParam String originalProfileName,
//             @RequestParam String jsonFilePath,
//             @RequestParam String newProfileName) {
//         try {
//             StringBuilder result = new StringBuilder();
//             result.append("=== 통합 Profile 라우팅 테스트 ===\n\n");
            
//             // 1. Profile 복사 + JSON 통합
//             Profile newProfile = complicateModelTest.copyProfileWithJson(
//                 originalProfileName, jsonFilePath, newProfileName
//             );
            
//             result.append("1. Profile 통합 완료\n");
//             result.append("원본: ").append(originalProfileName).append("\n");
//             result.append("새 Profile: ").append(newProfileName).append("\n\n");
            
//             // 2. 원본 Profile로 라우팅 테스트
//             result.append("2. 원본 Profile 라우팅:\n");
//             String originalResult = testRouting(originalProfileName);
//             result.append(originalResult).append("\n\n");
            
//             // 3. 통합된 Profile로 라우팅 테스트
//             // 주의: 실제로는 GraphHopper에 등록되지 않으므로 CustomModel 정보만 표시
//             result.append("3. 통합된 Profile 정보:\n");
//             result.append("Profile: ").append(newProfile.getName()).append("\n");
//             result.append("Vehicle: ").append(newProfile.getVehicle()).append("\n");
//             result.append("DistanceInfluence: ").append(newProfile.getCustomModel().getDistanceInfluence()).append("\n");
//             result.append("Priority 규칙 수: ").append(newProfile.getCustomModel().getPriority().size()).append("\n");
//             result.append("Speed 규칙 수: ").append(newProfile.getCustomModel().getSpeed().size()).append("\n");
//             result.append("(실제 라우팅을 위해서는 GraphHopper에 Profile 등록 필요)\n");
            
//             return result.toString();
            
//         } catch (Exception e) {
//             log.error("통합 Profile 테스트 실패", e);
//             return "테스트 실패: " + e.getMessage();
//         }
//     }
    
//     /**
//      * 통합 기능 전체 테스트
//      */
//     @GetMapping("/integration/test")
//     public String runIntegrationTest(
//             @RequestParam(defaultValue = "foot_shortest") String originalProfileName,
//             @RequestParam(defaultValue = "data/customModels/foot_shortest_avoid_zone.json") String jsonFilePath,
//             @RequestParam(defaultValue = "foot_shortest_custom") String newProfileName) {
//         try {
//             complicateModelTest.runTest(originalProfileName, jsonFilePath, newProfileName);
//             return "통합 테스트 완료! 로그를 확인하세요.";
//         } catch (Exception e) {
//             log.error("통합 테스트 실패", e);
//             return "통합 테스트 실패: " + e.getMessage();
//         }
//     }
// }