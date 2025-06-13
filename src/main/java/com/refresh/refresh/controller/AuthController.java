package com.refresh.refresh.controller;

import com.refresh.refresh.dto.UserDTO;
import com.refresh.refresh.dto.LoginRequestDTO;
import com.refresh.refresh.dto.SignupRequestDTO;
import com.refresh.refresh.entity.User;
import com.refresh.refresh.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 REST API 컨트롤러
 * 회원가입, 로그인, 비밀번호 변경 등의 인증 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 인증 관련 비즈니스 로직 처리를 위한 AuthService 의존성 주입
     */
    @Autowired
    private AuthService authService;

    /**
     * 회원가입 API
     * @param signupRequest 회원가입 요청 데이터
     * @return 회원가입 결과 (성공 시 사용자 정보, 실패 시 오류 메시지)
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequestDTO signupRequest) {
        try {
            // 이메일 중복 확인
            if (authService.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity.badRequest().body("이미 존재하는 이메일입니다.");
            }

            // 닉네임 중복 확인
            if (authService.existsByNickname(signupRequest.getNickname())) {
                return ResponseEntity.badRequest().body("이미 존재하는 닉네임입니다.");
            }

            // 사용자 엔티티 생성
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setNickname(signupRequest.getNickname());
            user.setGender(signupRequest.getGender());
            user.setPhoneNumber(signupRequest.getPhoneNumber());
            user.setUserImage(signupRequest.getUserImage());
            user.setPassword(signupRequest.getPassword()); // 평문으로 전달, AuthService에서 암호화

            // 회원가입 처리
            User savedUser = authService.registerUser(user);
            
            // 응답용 DTO 변환 (비밀번호 제외)
            UserDTO userDTO = convertToDTO(savedUser);
            return ResponseEntity.ok(userDTO);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 로그인 API
     * @param loginRequest 로그인 요청 데이터
     * @return 로그인 결과 (성공 시 사용자 정보, 실패 시 오류 메시지)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // 사용자 인증 처리
            User user = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            
            if (user == null) {
                return ResponseEntity.badRequest().body("이메일 또는 비밀번호가 올바르지 않습니다.");
            }

            // 응답용 DTO 변환 (비밀번호 제외)
            UserDTO userDTO = convertToDTO(user);
            return ResponseEntity.ok(userDTO);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 비밀번호 변경 API
     * @param userId 사용자 ID
     * @param currentPassword 현재 비밀번호
     * @param newPassword 새로운 비밀번호
     * @return 비밀번호 변경 결과
     */
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam int userId, 
                                          @RequestParam String currentPassword, 
                                          @RequestParam String newPassword) {
        try {
            boolean success = authService.changePassword(userId, currentPassword, newPassword);
            
            if (success) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("현재 비밀번호가 일치하지 않습니다.");
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비밀번호 변경에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * User 엔티티를 UserDTO로 변환하는 헬퍼 메소드
     * 보안상 비밀번호는 DTO에 포함하지 않습니다.
     * @param user 변환할 User 엔티티
     * @return 변환된 UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setNickname(user.getNickname());
        userDTO.setGender(user.getGender());
        userDTO.setRole(user.getRole());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUserImage(user.getUserImage());
        userDTO.setUserBan(user.getUserBan());
        userDTO.setCreatedAt(user.getCreatedAt());
        // 비밀번호는 DTO에 포함하지 않음 (보안상)
        return userDTO;
    }
}
