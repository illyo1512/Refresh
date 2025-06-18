package com.refresh.refresh.repository;

import com.refresh.refresh.entity.SelfRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SelfRouteRepository extends JpaRepository<SelfRoute, Integer> {
    
    /**
     * 사용자 ID로 자신 경로 목록 조회
     */
    List<SelfRoute> findByUserId(Integer userId);
    
    /**
     * 사용자 ID로 경로 ID, 이름, 생성일 조회
     */
    @Query("SELECT new map(s.selfRouteId as selfRouteId, s.routeName as routeName, s.createdAt as createdAt) FROM SelfRoute s WHERE s.userId = :userId")
    List<Map<String, Object>> findRouteIdAndNameByUserId(@Param("userId") Integer userId);
}