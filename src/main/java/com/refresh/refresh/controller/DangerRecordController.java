package com.refresh.refresh.controller;

import com.refresh.refresh.dto.DangerRecordDTO;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.service.DangerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/danger-records")
public class DangerRecordController {

    @Autowired
    private DangerRecordService dangerRecordService;

    /**
     * 모든 위험 기록 정보를 가져오는 API
     * @return List<DangerRecordDTO>
     */
    @GetMapping
    public List<DangerRecordDTO> getAllDangerRecords() {
        return dangerRecordService.getAllDangerRecords().stream()
                .map(dangerRecord -> {
                    DangerRecordDTO dangerRecordDTO = new DangerRecordDTO();
                    dangerRecordDTO.setRecordId(dangerRecord.getRecordId());
                    dangerRecordDTO.setDetailId(dangerRecord.getDetailId());
                    dangerRecordDTO.setDangerLocation(dangerRecord.getDangerLocation());
                    dangerRecordDTO.setDangerRadius(dangerRecord.getDangerRadius());
                    dangerRecordDTO.setDangerLevel(dangerRecord.getDangerLevel());
                    return dangerRecordDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 위험 기록 정보를 가져오는 API
     * @param id 위험 기록 ID
     * @return DangerRecordDTO
     */
    @GetMapping("/{id}")
    public DangerRecordDTO getDangerRecordById(@PathVariable int id) {
        DangerRecord dangerRecord = dangerRecordService.getDangerRecordById(id);
        DangerRecordDTO dangerRecordDTO = new DangerRecordDTO();
        dangerRecordDTO.setRecordId(dangerRecord.getRecordId());
        dangerRecordDTO.setDetailId(dangerRecord.getDetailId());
        dangerRecordDTO.setDangerLocation(dangerRecord.getDangerLocation());
        dangerRecordDTO.setDangerRadius(dangerRecord.getDangerRadius());
        dangerRecordDTO.setDangerLevel(dangerRecord.getDangerLevel());
        return dangerRecordDTO;
    }

    /**
     * 새로운 위험 기록 정보를 생성하는 API
     * @param dangerRecordDTO 위험 기록 DTO
     * @return DangerRecordDTO
     */
    @PostMapping
    public DangerRecordDTO createDangerRecord(@RequestBody DangerRecordDTO dangerRecordDTO) {
        DangerRecord dangerRecord = new DangerRecord();
        dangerRecord.setDetailId(dangerRecordDTO.getDetailId());
        dangerRecord.setDangerLocation(dangerRecordDTO.getDangerLocation());
        dangerRecord.setDangerRadius(dangerRecordDTO.getDangerRadius());
        dangerRecord.setDangerLevel(dangerRecordDTO.getDangerLevel());
        DangerRecord savedDangerRecord = dangerRecordService.createDangerRecord(dangerRecord);
        dangerRecordDTO.setRecordId(savedDangerRecord.getRecordId());
        return dangerRecordDTO;
    }

    /**
     * 기존 위험 기록 정보를 업데이트하는 API
     * @param id 위험 기록 ID
     * @param dangerRecordDTO 위험 기록 DTO
     * @return DangerRecordDTO
     */
    @PutMapping("/{id}")
    public DangerRecordDTO updateDangerRecord(@PathVariable int id, @RequestBody DangerRecordDTO dangerRecordDTO) {
        DangerRecord dangerRecord = new DangerRecord();
        dangerRecord.setRecordId(id);
        dangerRecord.setDetailId(dangerRecordDTO.getDetailId());
        dangerRecord.setDangerLocation(dangerRecordDTO.getDangerLocation());
        dangerRecord.setDangerRadius(dangerRecordDTO.getDangerRadius());
        dangerRecord.setDangerLevel(dangerRecordDTO.getDangerLevel());
        DangerRecord updatedDangerRecord = dangerRecordService.updateDangerRecord(dangerRecord);
        dangerRecordDTO.setRecordId(updatedDangerRecord.getRecordId());
        return dangerRecordDTO;
    }

    /**
     * 특정 ID의 위험 기록 정보를 삭제하는 API
     * @param id 위험 기록 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteDangerRecord(@PathVariable int id) {
        dangerRecordService.deleteDangerRecord(id);
        return "DangerRecord with ID " + id + " has been deleted.";
    }
}
