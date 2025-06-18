package com.refresh.refresh.service;

import com.refresh.refresh.entity.RealtimeDanger;
import com.refresh.refresh.repository.RealtimeDangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RealtimeDangerService {

    @Autowired
    private RealtimeDangerRepository realtimeDangerRepository;

    /**
     * 모든 실시간 위험 정보를 가져오는 메서드
     */
    public List<RealtimeDanger> getAllRealtimeDangers() {
        return realtimeDangerRepository.findAll();
    }

    /**
     * 특정 ID의 실시간 위험 정보를 가져오는 메서드
     */
    public RealtimeDanger getRealtimeDangerById(int id) {
        return realtimeDangerRepository.findById(id).orElseThrow(() -> new RuntimeException("RealtimeDanger not found"));
    }

    /**
     * 새로운 실시간 위험 정보를 생성하는 메서드
     */
    public RealtimeDanger createRealtimeDanger(RealtimeDanger realtimeDanger) {
        return realtimeDangerRepository.save(realtimeDanger);
    }

    /**
     * 중복 확인 후 실시간 위험 정보를 생성하는 메서드
     */
    public RealtimeDanger createRealtimeDangerIfNotExists(RealtimeDanger realtimeDanger) {
        // 같은 장소, 같은 위험도, 같은 날짜의 데이터가 있는지 확인
        boolean exists = realtimeDangerRepository.existsByLocateNameAndDangerDetailIdAndOccurredAtBetween(
            realtimeDanger.getLocateName(),
            realtimeDanger.getDangerDetailId(),
            realtimeDanger.getOccurredAt().toLocalDate().atStartOfDay(),
            realtimeDanger.getOccurredAt().toLocalDate().atTime(23, 59, 59)
        );
        
        if (exists) {
            return null; // 중복 데이터 있음
        }
        
        return realtimeDangerRepository.save(realtimeDanger);
    }

    /**
     * 기존 실시간 위험 정보를 업데이트하는 메서드
     */
    public RealtimeDanger updateRealtimeDanger(RealtimeDanger realtimeDanger) {
        if (!realtimeDangerRepository.existsById(realtimeDanger.getDangerId())) {
            throw new RuntimeException("RealtimeDanger not found");
        }
        return realtimeDangerRepository.save(realtimeDanger);
    }

    /**
     * 특정 ID의 실시간 위험 정보를 삭제하는 메서드
     */
    public void deleteRealtimeDanger(int id) {
        if (!realtimeDangerRepository.existsById(id)) {
            throw new RuntimeException("RealtimeDanger not found");
        }
        realtimeDangerRepository.deleteById(id);
    }
}
