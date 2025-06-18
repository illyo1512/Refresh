package com.refresh.refresh.repository;

import com.refresh.refresh.entity.RealtimeDanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RealtimeDangerRepository extends JpaRepository<RealtimeDanger, Integer> {
    
    /**
     * 특정 위치, 위험도, 시간 범위로 중복 확인
     */
    boolean existsByLocateNameAndDangerDetailIdAndOccurredAtBetween(
        String locateName, 
        Integer dangerDetailId, 
        LocalDateTime startDateTime, 
        LocalDateTime endDateTime
    );
}