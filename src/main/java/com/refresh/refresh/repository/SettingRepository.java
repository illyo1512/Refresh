package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
}