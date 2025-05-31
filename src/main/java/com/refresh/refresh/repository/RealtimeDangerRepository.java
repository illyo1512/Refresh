package com.refresh.refresh.repository;

import com.refresh.refresh.entity.RealtimeDanger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtimeDangerRepository extends JpaRepository<RealtimeDanger, Integer> {
}