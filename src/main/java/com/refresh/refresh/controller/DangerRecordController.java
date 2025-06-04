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
                    dangerRecordDTO.setDangerJsonPath(dangerRecord.getDangerJsonPath());
                    dangerRecordDTO.setMinLan(dangerRecord.getBboxMaxLat());
                    dangerRecordDTO.setMinLat(dangerRecord.getBboxMaxLng());
                    dangerRecordDTO.setMaxLan(dangerRecord.getBboxMinLat());
                    dangerRecordDTO.setMaxLat(dangerRecord.getBboxMinLng());
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
        dangerRecordDTO.setDangerJsonPath(dangerRecord.getDangerJsonPath());
        dangerRecordDTO.setMinLan(dangerRecord.getBboxMaxLat());
        dangerRecordDTO.setMinLat(dangerRecord.getBboxMaxLng());
        dangerRecordDTO.setMaxLan(dangerRecord.getBboxMinLat());
        dangerRecordDTO.setMaxLat(dangerRecord.getBboxMinLng());
        return dangerRecordDTO;
    }
}
