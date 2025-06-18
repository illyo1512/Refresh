package com.refresh.refresh.repository;

import com.refresh.refresh.entity.SavedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedRouteRepository extends JpaRepository<SavedRoute, Integer> {
    
    /**
     * 사용자 ID로 즐겨찾기 경로 목록 조회
     */
    List<SavedRoute> findByUserId(Integer userId);
    
    /**
     * 사용자 ID로 경로 ID와 이름만 조회
     */
    @Query("SELECT new map(s.savedRouteId as savedRouteId, s.routeName as routeName, s.createdAt as createdAt) FROM SavedRoute s WHERE s.userId = :userId")
    List<java.util.Map<String, Object>> findRouteIdAndNameByUserId(@Param("userId") Integer userId);
}