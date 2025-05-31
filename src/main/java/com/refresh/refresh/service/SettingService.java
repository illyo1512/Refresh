package com.refresh.refresh.service;

import com.refresh.refresh.entity.Setting;
import com.refresh.refresh.entity.SettingId;
import com.refresh.refresh.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    /**
     * 모든 설정 정보를 가져오는 메서드
     * @return List<Setting>
     */
    public List<Setting> getAllSettings() {
        return settingRepository.findAll();
    }

    /**
     * 특정 ID의 설정 정보를 가져오는 메서드
     * @param id 다중 키 ID
     * @return Setting
     */
    public Setting getSettingById(SettingId id) {
        return settingRepository.findById(id).orElseThrow(() -> new RuntimeException("Setting not found"));
    }

    /**
     * 새로운 설정 정보를 생성하는 메서드
     * @param setting 설정 엔티티
     * @return Setting
     */
    public Setting createSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    /**
     * 기존 설정 정보를 업데이트하는 메서드
     * @param setting 설정 엔티티
     * @return Setting
     */
    public Setting updateSetting(Setting setting) {
        SettingId id = new SettingId();
        id.setSettingId(setting.getSettingId());
        id.setUserId(setting.getUserId());
        if (!settingRepository.existsById(id)) {
            throw new RuntimeException("Setting not found");
        }
        return settingRepository.save(setting);
    }

    /**
     * 특정 ID의 설정 정보를 삭제하는 메서드
     * @param id 다중 키 ID
     */
    public void deleteSetting(SettingId id) {
        if (!settingRepository.existsById(id)) {
            throw new RuntimeException("Setting not found");
        }
        settingRepository.deleteById(id);
    }
}
