package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.RealTimeNotification;

public interface RealTimeNotificationRepository extends JpaRepository<RealTimeNotification, Integer> {
}