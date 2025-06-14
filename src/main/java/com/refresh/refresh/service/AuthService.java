package com.refresh.refresh.service;

import com.refresh.refresh.entity.User;
import com.refresh.refresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 회원가입, 로그인, 비밀번호 변경 등의 인증 기능을 제공합니다.
 */
@Service
public class AuthService {
    
    /**
     * 사용자 데이터 접근을 위한 Repository 의존성 주입
     */
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 사용자 관련 비즈니스 로직을 위한 UserService 의존성 주입
     */
    @Autowired
    private UserService userService;
    
    /**
     * 비밀번호 암호화를 위한 BCryptPasswordEncoder 의존성 주입
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * 이메일 중복 여부를 확인합니다.
     * @param email 확인할 이메일 주소
     * @return 이메일이 이미 존재하면 true, 존재하지 않으면 false
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 닉네임 중복 여부를 확인합니다.
     * @param nickname 확인할 닉네임
     * @return 닉네임이 이미 존재하면 true, 존재하지 않으면 false
     */
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
    
    /**
     * ID 중복 여부를 확인합니다.
     * @param id 확인할 사용자 ID
     * @return ID가 이미 존재하면 true, 존재하지 않으면 false
     */
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }
    
    /**
     * 이메일로 사용자를 찾습니다.
     * @param email 찾을 사용자의 이메일 주소
     * @return 찾은 사용자 엔티티, 존재하지 않으면 null
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * ID로 사용자를 찾습니다.
     * @param id 찾을 사용자의 ID
     * @return 찾은 사용자 엔티티, 존재하지 않으면 null
     */
    public User findById(String id) {
        return userRepository.findById(id);
    }
    
    /**
     * 새로운 사용자를 회원가입 처리합니다.
     * 비밀번호를 BCrypt로 암호화하여 저장합니다.
     * @param user 회원가입할 사용자 정보
     * @return 저장된 사용자 엔티티
     */
    public User registerUser(User user) {
        // 비밀번호 BCrypt로 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // 기본값 설정
        user.setRole("USER");
        user.setUserBan(false);
        
        return userService.createUser(user);
    }
    
    /**
     * 사용자 로그인 인증을 처리합니다.
     * @param id 로그인할 사용자 ID
     * @param password 로그인할 비밀번호 (평문)
     * @return 인증 성공 시 사용자 엔티티, 실패 시 null
     */
    public User authenticateUser(String id, String password) {
        User user = findById(id);
        
        if (user == null) {
            return null; // 사용자가 존재하지 않음
        }
        
        if (user.getUserBan()) {
            throw new RuntimeException("차단된 계정입니다.");
        }
        
        // 비밀번호 확인
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user; // 인증 성공
        }
        
        return null; // 비밀번호 불일치
    }
    
    /**
     * 사용자의 비밀번호를 변경합니다.
     * @param userId 비밀번호를 변경할 사용자 ID
     * @param currentPassword 현재 비밀번호 (평문)
     * @param newPassword 새로운 비밀번호 (평문)
     * @return 비밀번호 변경 성공 여부
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        User user = userService.getUserById(userId);
        
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false; // 현재 비밀번호 불일치
        }
        
        // 새로운 비밀번호 암호화 후 저장
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userService.updateUser(user);
        
        return true; // 비밀번호 변경 성공
    }
}