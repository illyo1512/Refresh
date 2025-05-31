package com.refresh.refresh.repository;

import com.refresh.refresh.entity.Setting;
import com.refresh.refresh.entity.SettingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, SettingId> {
}