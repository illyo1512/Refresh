package com.refresh.refresh.repository;

import com.refresh.refresh.entity.LocateReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocateReviewRepository extends JpaRepository<LocateReview, Integer> {
}