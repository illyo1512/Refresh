package com.refresh.refresh.controller;

import com.refresh.refresh.dto.RealtimeDangerDTO;
import com.refresh.refresh.entity.RealtimeDanger;
import com.refresh.refresh.service.RealtimeDangerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/realtime-dangers")
public class RealtimeDangerController {

    @Autowired
    private RealtimeDangerService realtimeDangerService;

    /**
     * 모든 실시간 위험 정보를 가져오는 API
     * @return List<RealtimeDangerDTO>
     */
    @GetMapping
    public List<RealtimeDangerDTO> getAllRealtimeDangers() {
        return realtimeDangerService.getAllRealtimeDangers().stream()
                .map(realtimeDanger -> {
                    RealtimeDangerDTO realtimeDangerDTO = new RealtimeDangerDTO();
                    realtimeDangerDTO.setDangerId(realtimeDanger.getDangerId());
                    realtimeDangerDTO.setLocateName(realtimeDanger.getLocateName());
                    realtimeDangerDTO.setDangerDetailId(realtimeDanger.getDangerDetailId());
                    realtimeDangerDTO.setPlaceLocation(realtimeDanger.getPlaceLocation());
                    realtimeDangerDTO.setOccurredAt(realtimeDanger.getOccurredAt());
                    return realtimeDangerDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 실시간 위험 정보를 가져오는 API
     * @param id 위험 정보 ID
     * @return RealtimeDangerDTO
     */
    @GetMapping("/{id}")
    public RealtimeDangerDTO getRealtimeDangerById(@PathVariable int id) {
        RealtimeDanger realtimeDanger = realtimeDangerService.getRealtimeDangerById(id);
        RealtimeDangerDTO realtimeDangerDTO = new RealtimeDangerDTO();
        realtimeDangerDTO.setDangerId(realtimeDanger.getDangerId());
        realtimeDangerDTO.setLocateName(realtimeDanger.getLocateName());
        realtimeDangerDTO.setDangerDetailId(realtimeDanger.getDangerDetailId());
        realtimeDangerDTO.setPlaceLocation(realtimeDanger.getPlaceLocation());
        realtimeDangerDTO.setOccurredAt(realtimeDanger.getOccurredAt());
        return realtimeDangerDTO;
    }

    /**
     * 새로운 실시간 위험 정보를 생성하는 API
     * @param realtimeDangerDTO 위험 정보 DTO
     * @return RealtimeDangerDTO
     */
    @PostMapping
    public RealtimeDangerDTO createRealtimeDanger(@RequestBody RealtimeDangerDTO realtimeDangerDTO) {
        RealtimeDanger realtimeDanger = new RealtimeDanger();
        realtimeDanger.setLocateName(realtimeDangerDTO.getLocateName());
        realtimeDanger.setDangerDetailId(realtimeDangerDTO.getDangerDetailId());
        realtimeDanger.setPlaceLocation(realtimeDangerDTO.getPlaceLocation());
        realtimeDanger.setOccurredAt(realtimeDangerDTO.getOccurredAt());
        RealtimeDanger savedRealtimeDanger = realtimeDangerService.createRealtimeDanger(realtimeDanger);
        realtimeDangerDTO.setDangerId(savedRealtimeDanger.getDangerId());
        return realtimeDangerDTO;
    }

    /**
     * 기존 실시간 위험 정보를 업데이트하는 API
     * @param id 위험 정보 ID
     * @param realtimeDangerDTO 위험 정보 DTO
     * @return RealtimeDangerDTO
     */
    @PutMapping("/{id}")
    public RealtimeDangerDTO updateRealtimeDanger(@PathVariable int id, @RequestBody RealtimeDangerDTO realtimeDangerDTO) {
        RealtimeDanger realtimeDanger = new RealtimeDanger();
        realtimeDanger.setDangerId(id);
        realtimeDanger.setLocateName(realtimeDangerDTO.getLocateName());
        realtimeDanger.setDangerDetailId(realtimeDangerDTO.getDangerDetailId());
        realtimeDanger.setPlaceLocation(realtimeDangerDTO.getPlaceLocation());
        realtimeDanger.setOccurredAt(realtimeDangerDTO.getOccurredAt());
        RealtimeDanger updatedRealtimeDanger = realtimeDangerService.updateRealtimeDanger(realtimeDanger);
        realtimeDangerDTO.setDangerId(updatedRealtimeDanger.getDangerId());
        return realtimeDangerDTO;
    }

    /**
     * 특정 ID의 실시간 위험 정보를 삭제하는 API
     * @param id 위험 정보 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteRealtimeDanger(@PathVariable int id) {
        realtimeDangerService.deleteRealtimeDanger(id);
        return "RealtimeDanger with ID " + id + " has been deleted.";
    }
}
