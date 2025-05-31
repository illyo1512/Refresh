package com.refresh.refresh.service;

import com.refresh.refresh.entity.LocateInfo;
import com.refresh.refresh.repository.LocateInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocateInfoService {

    @Autowired
    private LocateInfoRepository locateInfoRepository;

    /**
     * 모든 장소 정보를 가져오는 메서드
     * @return List<LocateInfo>
     */
    public List<LocateInfo> getAllLocateInfos() {
        return locateInfoRepository.findAll();
    }

    /**
     * 특정 ID의 장소 정보를 가져오는 메서드
     * @param id 장소 정보 ID
     * @return LocateInfo
     */
    public LocateInfo getLocateInfoById(int id) {
        return locateInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("LocateInfo not found"));
    }

    /**
     * 새로운 장소 정보를 생성하는 메서드
     * @param locateInfo 장소 정보 엔티티
     * @return LocateInfo
     */
    public LocateInfo createLocateInfo(LocateInfo locateInfo) {
        return locateInfoRepository.save(locateInfo);
    }

    /**
     * 기존 장소 정보를 업데이트하는 메서드
     * @param locateInfo 장소 정보 엔티티
     * @return LocateInfo
     */
    public LocateInfo updateLocateInfo(LocateInfo locateInfo) {
        if (!locateInfoRepository.existsById(locateInfo.getInfoId())) {
            throw new RuntimeException("LocateInfo not found");
        }
        return locateInfoRepository.save(locateInfo);
    }

    /**
     * 특정 ID의 장소 정보를 삭제하는 메서드
     * @param id 장소 정보 ID
     */
    public void deleteLocateInfo(int id) {
        if (!locateInfoRepository.existsById(id)) {
            throw new RuntimeException("LocateInfo not found");
        }
        locateInfoRepository.deleteById(id);
    }
}
