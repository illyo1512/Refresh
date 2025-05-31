package com.refresh.refresh.repository;

import com.refresh.refresh.entity.DangerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangerRecordRepository extends JpaRepository<DangerRecord, Integer> {
}