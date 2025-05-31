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
     * 새로운 위험 기록 정보를 생성하는 메서드
     * @param dangerRecord 위험 기록 엔티티
     * @return DangerRecord
     */
    public DangerRecord createDangerRecord(DangerRecord dangerRecord) {
        return dangerRecordRepository.save(dangerRecord);
    }

    /**
     * 기존 위험 기록 정보를 업데이트하는 메서드
     * @param dangerRecord 위험 기록 엔티티
     * @return DangerRecord
     */
    public DangerRecord updateDangerRecord(DangerRecord dangerRecord) {
        if (!dangerRecordRepository.existsById(dangerRecord.getRecordId())) {
            throw new RuntimeException("DangerRecord not found");
        }
        return dangerRecordRepository.save(dangerRecord);
    }

    /**
     * 특정 ID의 위험 기록 정보를 삭제하는 메서드
     * @param id 위험 기록 ID
     */
    public void deleteDangerRecord(int id) {
        if (!dangerRecordRepository.existsById(id)) {
            throw new RuntimeException("DangerRecord not found");
        }
        dangerRecordRepository.deleteById(id);
    }
}
