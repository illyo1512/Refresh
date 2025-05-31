package com.refresh.refresh.repository;

import com.refresh.refresh.entity.RealTimeNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealTimeNotificationRepository extends JpaRepository<RealTimeNotification, Integer> {
}