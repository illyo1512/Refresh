package com.refresh.refresh.repository;

import com.refresh.refresh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 사용자 데이터 접근을 위한 Repository 인터페이스
 * Spring Data JPA를 사용하여 데이터베이스 작업을 수행합니다.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    /**
     * 이메일 존재 여부를 확인합니다.
     * Spring Data JPA가 메소드 이름을 분석하여 자동으로 쿼리를 생성합니다.
     * 생성되는 쿼리: SELECT COUNT(*) > 0 FROM users WHERE email = ?
     * @param email 확인할 이메일 주소
     * @return 이메일이 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByEmail(String email);
    
    /**
     * 닉네임 존재 여부를 확인합니다.
     * 생성되는 쿼리: SELECT COUNT(*) > 0 FROM users WHERE nickname = ?
     * @param nickname 확인할 닉네임
     * @return 닉네임이 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByNickname(String nickname);
    
    /**
     * 이메일로 사용자를 찾습니다.
     * 생성되는 쿼리: SELECT * FROM users WHERE email = ?
     * @param email 찾을 사용자의 이메일 주소
     * @return 찾은 사용자 엔티티, 존재하지 않으면 null
     */
    User findByEmail(String email);
}