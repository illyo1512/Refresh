package com.refresh.refresh.controller;

import com.refresh.refresh.dto.LocateInfoDTO;
import com.refresh.refresh.entity.LocateInfo;
import com.refresh.refresh.service.LocateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locate-info")
public class LocateInfoController {

    @Autowired
    private LocateInfoService locateInfoService;

    /**
     * 모든 장소 정보를 가져오는 API
     * @return List<LocateInfoDTO>
     */
    @GetMapping
    public List<LocateInfoDTO> getAllLocateInfos() {
        return locateInfoService.getAllLocateInfos().stream()
                .map(locateInfo -> {
                    LocateInfoDTO locateInfoDTO = new LocateInfoDTO();
                    locateInfoDTO.setInfoId(locateInfo.getInfoId());
                    locateInfoDTO.setPlaceName(locateInfo.getPlaceName());
                    locateInfoDTO.setPlaceLocation(locateInfo.getPlaceLocation());
                    locateInfoDTO.setPlaceDetail(locateInfo.getPlaceDetail());
                    return locateInfoDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 장소 정보를 가져오는 API
     * @param id 장소 정보 ID
     * @return LocateInfoDTO
     */
    @GetMapping("/{id}")
    public LocateInfoDTO getLocateInfoById(@PathVariable int id) {
        LocateInfo locateInfo = locateInfoService.getLocateInfoById(id);
        LocateInfoDTO locateInfoDTO = new LocateInfoDTO();
        locateInfoDTO.setInfoId(locateInfo.getInfoId());
        locateInfoDTO.setPlaceName(locateInfo.getPlaceName());
        locateInfoDTO.setPlaceLocation(locateInfo.getPlaceLocation());
        locateInfoDTO.setPlaceDetail(locateInfo.getPlaceDetail());
        return locateInfoDTO;
    }

    /**
     * 새로운 장소 정보를 생성하는 API
     * @param locateInfoDTO 장소 정보 DTO
     * @return LocateInfoDTO
     */
    @PostMapping
    public LocateInfoDTO createLocateInfo(@RequestBody LocateInfoDTO locateInfoDTO) {
        LocateInfo locateInfo = new LocateInfo();
        locateInfo.setPlaceName(locateInfoDTO.getPlaceName());
        locateInfo.setPlaceLocation(locateInfoDTO.getPlaceLocation());
        locateInfo.setPlaceDetail(locateInfoDTO.getPlaceDetail());
        LocateInfo savedLocateInfo = locateInfoService.createLocateInfo(locateInfo);
        locateInfoDTO.setInfoId(savedLocateInfo.getInfoId());
        return locateInfoDTO;
    }

    /**
     * 기존 장소 정보를 업데이트하는 API
     * @param id 장소 정보 ID
     * @param locateInfoDTO 장소 정보 DTO
     * @return LocateInfoDTO
     */
    @PutMapping("/{id}")
    public LocateInfoDTO updateLocateInfo(@PathVariable int id, @RequestBody LocateInfoDTO locateInfoDTO) {
        LocateInfo locateInfo = new LocateInfo();
        locateInfo.setInfoId(id);
        locateInfo.setPlaceName(locateInfoDTO.getPlaceName());
        locateInfo.setPlaceLocation(locateInfoDTO.getPlaceLocation());
        locateInfo.setPlaceDetail(locateInfoDTO.getPlaceDetail());
        LocateInfo updatedLocateInfo = locateInfoService.updateLocateInfo(locateInfo);
        locateInfoDTO.setInfoId(updatedLocateInfo.getInfoId());
        return locateInfoDTO;
    }

    /**
     * 특정 ID의 장소 정보를 삭제하는 API
     * @param id 장소 정보 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteLocateInfo(@PathVariable int id) {
        locateInfoService.deleteLocateInfo(id);
        return "LocateInfo with ID " + id + " has been deleted.";
    }
}
