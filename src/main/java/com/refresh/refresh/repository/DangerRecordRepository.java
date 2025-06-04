package com.refresh.refresh.repository;

import com.refresh.refresh.entity.DangerRecord;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DangerRecordRepository extends JpaRepository<DangerRecord, Integer> {
    @Query("SELECT d FROM DangerRecord d WHERE " +
           "d.bboxMaxLng >= :pathMinLng AND d.bboxMinLng <= :pathMaxLng AND " +
           "d.bboxMaxLat >= :pathMinLat AND d.bboxMinLat <= :pathMaxLat")
    List<DangerRecord> findIntersectingBbox(
            @Param("pathMinLng") double pathMinLng,
            @Param("pathMinLat") double pathMinLat,
            @Param("pathMaxLng") double pathMaxLng,
            @Param("pathMaxLat") double pathMaxLat);
}