package com.refresh.refresh.service;

import com.refresh.refresh.entity.DangerDetail;
import com.refresh.refresh.repository.DangerDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DangerDetailService {

    @Autowired
    private DangerDetailRepository dangerDetailRepository;

    /**
     * 모든 위험 정보를 가져오는 메서드
     * @return List<DangerDetail>
     */
    public List<DangerDetail> getAllDangerDetails() {
        return dangerDetailRepository.findAll();
    }

    /**
     * 특정 ID의 위험 정보를 가져오는 메서드
     * @param id 위험 정보 ID
     * @return DangerDetail
     */
    public DangerDetail getDangerDetailById(int id) {
        return dangerDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("DangerDetail not found"));
    }

    /**
     * 새로운 위험 정보를 생성하는 메서드
     * @param dangerDetail 위험 정보 엔티티
     * @return DangerDetail
     */
    public DangerDetail createDangerDetail(DangerDetail dangerDetail) {
        return dangerDetailRepository.save(dangerDetail);
    }

    /**
     * 기존 위험 정보를 업데이트하는 메서드
     * @param dangerDetail 위험 정보 엔티티
     * @return DangerDetail
     */
    public DangerDetail updateDangerDetail(DangerDetail dangerDetail) {
        if (!dangerDetailRepository.existsById(dangerDetail.getDetailId())) {
            throw new RuntimeException("DangerDetail not found");
        }
        return dangerDetailRepository.save(dangerDetail);
    }

    /**
     * 특정 ID의 위험 정보를 삭제하는 메서드
     * @param id 위험 정보 ID
     */
    public void deleteDangerDetail(int id) {
        if (!dangerDetailRepository.existsById(id)) {
            throw new RuntimeException("DangerDetail not found");
        }
        dangerDetailRepository.deleteById(id);
    }
}
