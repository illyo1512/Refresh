package com.refresh.refresh.controller;

import com.refresh.refresh.dto.SettingDTO;
import com.refresh.refresh.entity.Setting;
import com.refresh.refresh.entity.SettingId;
import com.refresh.refresh.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    /**
     * 모든 설정 정보를 가져오는 API
     * @return List<SettingDTO>
     */
    @GetMapping
    public List<SettingDTO> getAllSettings() {
        return settingService.getAllSettings().stream()
                .map(setting -> {
                    SettingDTO settingDTO = new SettingDTO();
                    settingDTO.setSettingId(setting.getSettingId());
                    settingDTO.setUserId(setting.getUserId());
                    settingDTO.setNightMod(setting.getNightMod());
                    settingDTO.setColorBlind(setting.getColorBlind());
                    settingDTO.setAlertLevel(setting.getAlertLevel());
                    return settingDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 설정 정보를 가져오는 API
     * @param settingId 설정 ID
     * @param userId 사용자 ID
     * @return SettingDTO
     */
    @GetMapping("/{settingId}/{userId}")
    public SettingDTO getSettingById(@PathVariable Integer settingId, @PathVariable Integer userId) {
        SettingId id = new SettingId();
        id.setSettingId(settingId);
        id.setUserId(userId);
        Setting setting = settingService.getSettingById(id);
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setSettingId(setting.getSettingId());
        settingDTO.setUserId(setting.getUserId());
        settingDTO.setNightMod(setting.getNightMod());
        settingDTO.setColorBlind(setting.getColorBlind());
        settingDTO.setAlertLevel(setting.getAlertLevel());
        return settingDTO;
    }

    /**
     * 새로운 설정 정보를 생성하는 API
     * @param settingDTO 설정 DTO
     * @return SettingDTO
     */
    @PostMapping
    public SettingDTO createSetting(@RequestBody SettingDTO settingDTO) {
        Setting setting = new Setting();
        setting.setSettingId(settingDTO.getSettingId());
        setting.setUserId(settingDTO.getUserId());
        setting.setNightMod(settingDTO.getNightMod());
        setting.setColorBlind(settingDTO.getColorBlind());
        setting.setAlertLevel(settingDTO.getAlertLevel());
        Setting savedSetting = settingService.createSetting(setting);
        settingDTO.setSettingId(savedSetting.getSettingId());
        settingDTO.setUserId(savedSetting.getUserId());
        return settingDTO;
    }

    /**
     * 기존 설정 정보를 업데이트하는 API
     * @param settingId 설정 ID
     * @param userId 사용자 ID
     * @param settingDTO 설정 DTO
     * @return SettingDTO
     */
    @PutMapping("/{settingId}/{userId}")
    public SettingDTO updateSetting(@PathVariable Integer settingId, @PathVariable Integer userId, @RequestBody SettingDTO settingDTO) {
        Setting setting = new Setting();
        setting.setSettingId(settingId);
        setting.setUserId(userId);
        setting.setNightMod(settingDTO.getNightMod());
        setting.setColorBlind(settingDTO.getColorBlind());
        setting.setAlertLevel(settingDTO.getAlertLevel());
        Setting updatedSetting = settingService.updateSetting(setting);
        settingDTO.setSettingId(updatedSetting.getSettingId());
        settingDTO.setUserId(updatedSetting.getUserId());
        return settingDTO;
    }

    /**
     * 특정 ID의 설정 정보를 삭제하는 API
     * @param settingId 설정 ID
     * @param userId 사용자 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{settingId}/{userId}")
    public String deleteSetting(@PathVariable Integer settingId, @PathVariable Integer userId) {
        SettingId id = new SettingId();
        id.setSettingId(settingId);
        id.setUserId(userId);
        settingService.deleteSetting(id);
        return "Setting with ID (" + settingId + ", " + userId + ") has been deleted.";
    }
}
