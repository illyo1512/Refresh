package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}