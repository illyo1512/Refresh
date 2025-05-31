package com.refresh.refresh.controller;

import com.refresh.refresh.dto.DmDTO;
import com.refresh.refresh.entity.Dm;
import com.refresh.refresh.service.DmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dms")
public class DmController {

    @Autowired
    private DmService dmService;

    /**
     * 모든 DM 정보를 가져오는 API
     * @return List<DmDTO>
     */
    @GetMapping
    public List<DmDTO> getAllDms() {
        return dmService.getAllDms().stream()
                .map(dm -> {
                    DmDTO dmDTO = new DmDTO();
                    dmDTO.setDmId(dm.getDmId());
                    dmDTO.setUserId(dm.getUserId());
                    dmDTO.setContent(dm.getContent());
                    dmDTO.setSender(dm.getSender());
                    dmDTO.setSentAt(dm.getSentAt());
                    return dmDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 DM 정보를 가져오는 API
     * @param id DM ID
     * @return DmDTO
     */
    @GetMapping("/{id}")
    public DmDTO getDmById(@PathVariable int id) {
        Dm dm = dmService.getDmById(id);
        DmDTO dmDTO = new DmDTO();
        dmDTO.setDmId(dm.getDmId());
        dmDTO.setUserId(dm.getUserId());
        dmDTO.setContent(dm.getContent());
        dmDTO.setSender(dm.getSender());
        dmDTO.setSentAt(dm.getSentAt());
        return dmDTO;
    }

    /**
     * 새로운 DM 정보를 생성하는 API
     * @param dmDTO DM DTO
     * @return DmDTO
     */
    @PostMapping
    public DmDTO createDm(@RequestBody DmDTO dmDTO) {
        Dm dm = new Dm();
        dm.setUserId(dmDTO.getUserId());
        dm.setContent(dmDTO.getContent());
        dm.setSender(dmDTO.getSender());
        dm.setSentAt(dmDTO.getSentAt());
        Dm savedDm = dmService.createDm(dm);
        dmDTO.setDmId(savedDm.getDmId());
        return dmDTO;
    }

    /**
     * 기존 DM 정보를 업데이트하는 API
     * @param id DM ID
     * @param dmDTO DM DTO
     * @return DmDTO
     */
    @PutMapping("/{id}")
    public DmDTO updateDm(@PathVariable int id, @RequestBody DmDTO dmDTO) {
        Dm dm = new Dm();
        dm.setDmId(id);
        dm.setUserId(dmDTO.getUserId());
        dm.setContent(dmDTO.getContent());
        dm.setSender(dmDTO.getSender());
        dm.setSentAt(dmDTO.getSentAt());
        Dm updatedDm = dmService.updateDm(dm);
        dmDTO.setDmId(updatedDm.getDmId());
        return dmDTO;
    }

    /**
     * 특정 ID의 DM 정보를 삭제하는 API
     * @param id DM ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteDm(@PathVariable int id) {
        dmService.deleteDm(id);
        return "DM with ID " + id + " has been deleted.";
    }
}
