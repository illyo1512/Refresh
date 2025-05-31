package com.refresh.refresh.repository;

import com.refresh.refresh.entity.LocateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocateInfoRepository extends JpaRepository<LocateInfo, Integer> {
}