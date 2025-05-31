package com.refresh.refresh.repository;

import com.refresh.refresh.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> { // 엔티티 User 사용
}