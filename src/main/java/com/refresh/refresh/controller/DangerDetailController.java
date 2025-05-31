package com.refresh.refresh.controller;

import com.refresh.refresh.dto.DangerDetailDTO;
import com.refresh.refresh.entity.DangerDetail;
import com.refresh.refresh.service.DangerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/danger-details")
public class DangerDetailController {

    @Autowired
    private DangerDetailService dangerDetailService;

    /**
     * 모든 위험 정보를 가져오는 API
     * @return List<DangerDetailDTO>
     */
    @GetMapping
    public List<DangerDetailDTO> getAllDangerDetails() {
        return dangerDetailService.getAllDangerDetails().stream()
                .map(dangerDetail -> {
                    DangerDetailDTO dangerDetailDTO = new DangerDetailDTO();
                    dangerDetailDTO.setDetailId(dangerDetail.getDetailId());
                    dangerDetailDTO.setDangerType(dangerDetail.getDangerType());
                    dangerDetailDTO.setDangerDetail(dangerDetail.getDangerDetail());
                    dangerDetailDTO.setDangerCountermeasure(dangerDetail.getDangerCountermeasure());
                    return dangerDetailDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 위험 정보를 가져오는 API
     * @param id 위험 정보 ID
     * @return DangerDetailDTO
     */
    @GetMapping("/{id}")
    public DangerDetailDTO getDangerDetailById(@PathVariable int id) {
        DangerDetail dangerDetail = dangerDetailService.getDangerDetailById(id);
        DangerDetailDTO dangerDetailDTO = new DangerDetailDTO();
        dangerDetailDTO.setDetailId(dangerDetail.getDetailId());
        dangerDetailDTO.setDangerType(dangerDetail.getDangerType());
        dangerDetailDTO.setDangerDetail(dangerDetail.getDangerDetail());
        dangerDetailDTO.setDangerCountermeasure(dangerDetail.getDangerCountermeasure());
        return dangerDetailDTO;
    }

    /**
     * 새로운 위험 정보를 생성하는 API
     * @param dangerDetailDTO 위험 정보 DTO
     * @return DangerDetailDTO
     */
    @PostMapping
    public DangerDetailDTO createDangerDetail(@RequestBody DangerDetailDTO dangerDetailDTO) {
        DangerDetail dangerDetail = new DangerDetail();
        dangerDetail.setDangerType(dangerDetailDTO.getDangerType());
        dangerDetail.setDangerDetail(dangerDetailDTO.getDangerDetail());
        dangerDetail.setDangerCountermeasure(dangerDetailDTO.getDangerCountermeasure());
        DangerDetail savedDangerDetail = dangerDetailService.createDangerDetail(dangerDetail);
        dangerDetailDTO.setDetailId(savedDangerDetail.getDetailId());
        return dangerDetailDTO;
    }

    /**
     * 기존 위험 정보를 업데이트하는 API
     * @param id 위험 정보 ID
     * @param dangerDetailDTO 위험 정보 DTO
     * @return DangerDetailDTO
     */
    @PutMapping("/{id}")
    public DangerDetailDTO updateDangerDetail(@PathVariable int id, @RequestBody DangerDetailDTO dangerDetailDTO) {
        DangerDetail dangerDetail = new DangerDetail();
        dangerDetail.setDetailId(id);
        dangerDetail.setDangerType(dangerDetailDTO.getDangerType());
        dangerDetail.setDangerDetail(dangerDetailDTO.getDangerDetail());
        dangerDetail.setDangerCountermeasure(dangerDetailDTO.getDangerCountermeasure());
        DangerDetail updatedDangerDetail = dangerDetailService.updateDangerDetail(dangerDetail);
        dangerDetailDTO.setDetailId(updatedDangerDetail.getDetailId());
        return dangerDetailDTO;
    }

    /**
     * 특정 ID의 위험 정보를 삭제하는 API
     * @param id 위험 정보 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteDangerDetail(@PathVariable int id) {
        dangerDetailService.deleteDangerDetail(id);
        return "DangerDetail with ID " + id + " has been deleted.";
    }
}
