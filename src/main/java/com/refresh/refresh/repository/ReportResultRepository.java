package com.refresh.refresh.repository;

import com.refresh.refresh.entity.ReportResult;
import com.refresh.refresh.entity.ReportResultId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportResultRepository extends JpaRepository<ReportResult, ReportResultId> {
}