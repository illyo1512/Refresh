package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.DangerDetail;

public interface DangerDetailRepository extends JpaRepository<DangerDetail, Integer> {
}