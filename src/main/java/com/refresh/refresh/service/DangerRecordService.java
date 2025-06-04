package com.refresh.refresh.service;

import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.repository.DangerRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DangerRecordService {

    @Autowired
    private DangerRecordRepository dangerRecordRepository;

    /**
     * 모든 위험 기록 정보를 가져오는 메서드
     * @return List<DangerRecord>
     */
    public List<DangerRecord> getAllDangerRecords() {
        return dangerRecordRepository.findAll();
    }

    /**
     * 특정 ID의 위험 기록 정보를 가져오는 메서드
     * @param id 위험 기록 ID
     * @return DangerRecord
     */
    public DangerRecord getDangerRecordById(int id) {
        return dangerRecordRepository.findById(id).orElseThrow(() -> new RuntimeException("DangerRecord not found"));
    }

    /**
     * BBOX가 겹치는 위험 기록을 조회하는 메서드
     * @param bbox [minLan, minLat, maxLan, maxLat]
     * @return List<DangerRecord>
     */
    public List<DangerRecord> getIntersectingRecords(double[] pathBbox) {
    double minLng = pathBbox[0];
    double minLat = pathBbox[1];
    double maxLng = pathBbox[2];
    double maxLat = pathBbox[3];
    return dangerRecordRepository.findIntersectingBbox(minLng, minLat, maxLng, maxLat);
}

}

