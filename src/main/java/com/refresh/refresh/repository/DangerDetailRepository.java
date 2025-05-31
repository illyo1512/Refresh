package com.refresh.refresh.repository;

import com.refresh.refresh.entity.DangerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DangerDetailRepository extends JpaRepository<DangerDetail, Integer> {
}