package com.refresh.refresh.service;

import com.refresh.refresh.entity.RealtimeDanger;
import com.refresh.refresh.repository.RealtimeDangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealtimeDangerService {

    @Autowired
    private RealtimeDangerRepository realtimeDangerRepository;

    /**
     * 모든 실시간 위험 정보를 가져오는 메서드
     * @return List<RealtimeDanger>
     */
    public List<RealtimeDanger> getAllRealtimeDangers() {
        return realtimeDangerRepository.findAll();
    }

    /**
     * 특정 ID의 실시간 위험 정보를 가져오는 메서드
     * @param id 위험 정보 ID
     * @return RealtimeDanger
     */
    public RealtimeDanger getRealtimeDangerById(int id) {
        return realtimeDangerRepository.findById(id).orElseThrow(() -> new RuntimeException("RealtimeDanger not found"));
    }

    /**
     * 새로운 실시간 위험 정보를 생성하는 메서드
     * @param realtimeDanger 위험 정보 엔티티
     * @return RealtimeDanger
     */
    public RealtimeDanger createRealtimeDanger(RealtimeDanger realtimeDanger) {
        return realtimeDangerRepository.save(realtimeDanger);
    }

    /**
     * 기존 실시간 위험 정보를 업데이트하는 메서드
     * @param realtimeDanger 위험 정보 엔티티
     * @return RealtimeDanger
     */
    public RealtimeDanger updateRealtimeDanger(RealtimeDanger realtimeDanger) {
        if (!realtimeDangerRepository.existsById(realtimeDanger.getDangerId())) {
            throw new RuntimeException("RealtimeDanger not found");
        }
        return realtimeDangerRepository.save(realtimeDanger);
    }

    /**
     * 특정 ID의 실시간 위험 정보를 삭제하는 메서드
     * @param id 위험 정보 ID
     */
    public void deleteRealtimeDanger(int id) {
        if (!realtimeDangerRepository.existsById(id)) {
            throw new RuntimeException("RealtimeDanger not found");
        }
        realtimeDangerRepository.deleteById(id);
    }
}
