package com.refresh.refresh.service;

import com.refresh.refresh.entity.User;
import com.refresh.refresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 관리 서비스 클래스
 * 사용자 CRUD 작업 및 인증 관련 비즈니스 로직을 처리합니다.
 */
@Service
public class UserService {

    /**
     * 사용자 데이터 접근을 위한 Repository 의존성 주입
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * 모든 사용자 목록을 조회합니다.
     * @return 모든 사용자 엔티티 리스트
     */
    public List<User> getAllUsers() {
        return userRepository.findAll(); // JPA의 findAll() 메소드로 모든 사용자 조회
    }

    /**
     * 사용자 ID로 특정 사용자를 조회합니다.
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 엔티티
     * @throws RuntimeException 사용자를 찾을 수 없는 경우
     */
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * 새로운 사용자를 생성합니다.
     * @param user 생성할 사용자 엔티티
     * @return 저장된 사용자 엔티티 (자동 생성된 ID 포함)
     */
    public User createUser(User user) {
        // JPA의 save() 메소드로 사용자 저장
        // 새로운 엔티티인 경우 INSERT, 기존 엔티티인 경우 UPDATE 수행
        return userRepository.save(user);
    }

    /**
     * 기존 사용자 정보를 수정합니다.
     * @param user 수정할 사용자 엔티티
     * @return 수정된 사용자 엔티티
     * @throws RuntimeException 사용자를 찾을 수 없는 경우
     */
    public User updateUser(User user) {
        // 사용자 존재 여부 확인
        if (!userRepository.existsById(user.getUserId())) {
            throw new RuntimeException("User not found with id: " + user.getUserId());
        }
        return userRepository.save(user); // 기존 엔티티 업데이트
    }

    /**
     * 사용자를 삭제합니다.
     * @param id 삭제할 사용자의 ID
     * @throws RuntimeException 사용자를 찾을 수 없는 경우
     */
    public void deleteUser(int id) {
        // 사용자 존재 여부 확인
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id); // 사용자 삭제
    }

    // ================================
    // 인증 관련 메소드들 (AuthController에서 사용)
    // ================================

    /**
     * 이메일 중복 여부를 확인합니다.
     * 회원가입 시 이메일 중복 검사에 사용됩니다.
     * @param email 확인할 이메일 주소
     * @return 이메일이 이미 존재하면 true, 존재하지 않으면 false
     */
    public boolean existsByEmail(String email) {
        // Spring Data JPA의 exists 쿼리 메소드 사용
        // SELECT COUNT(*) > 0 FROM users WHERE email = ? 쿼리 실행
        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     * 회원가입 시 닉네임 중복 검사에 사용됩니다.
     * @param nickname 확인할 닉네임
     * @return 닉네임이 이미 존재하면 true, 존재하지 않으면 false
     */
    public boolean existsByNickname(String nickname) {
        // Spring Data JPA의 exists 쿼리 메소드 사용
        // SELECT COUNT(*) > 0 FROM users WHERE nickname = ? 쿼리 실행
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 이메일로 사용자를 찾습니다.
     * 로그인 시 사용자 인증에 사용됩니다.
     * @param email 찾을 사용자의 이메일 주소
     * @return 찾은 사용자 엔티티, 존재하지 않으면 null
     */
    public User findByEmail(String email) {
        // Spring Data JPA의 find 쿼리 메소드 사용
        // SELECT * FROM users WHERE email = ? 쿼리 실행
        return userRepository.findByEmail(email);
    }
}
