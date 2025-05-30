package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.DangerRecord;

public interface DangerRecordRepository extends JpaRepository<DangerRecord, Integer> {
}