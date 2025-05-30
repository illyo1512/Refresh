package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.RealtimeDanger;

public interface RealtimeDangerRepository extends JpaRepository<RealtimeDanger, Integer> {
}