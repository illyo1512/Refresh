package com.refresh.refresh.controller;

import com.refresh.refresh.dto.SelfRouteDTO;
import com.refresh.refresh.entity.SelfRoute;
import com.refresh.refresh.service.SelfRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/self-routes")
public class SelfRouteController {

    @Autowired
    private SelfRouteService selfRouteService;

    /**
     * 모든 자신 경로 정보를 가져오는 API
     * @return List<SelfRouteDTO>
     */
    @GetMapping
    public List<SelfRouteDTO> getAllSelfRoutes() {
        return selfRouteService.getAllSelfRoutes().stream()
                .map(selfRoute -> {
                    SelfRouteDTO selfRouteDTO = new SelfRouteDTO();
                    selfRouteDTO.setSelfRouteId(selfRoute.getSelfRouteId());
                    selfRouteDTO.setUserId(selfRoute.getUserId());
                    selfRouteDTO.setRouteName(selfRoute.getRouteName());
                    selfRouteDTO.setRouteResult(selfRoute.getRouteResult());
                    selfRouteDTO.setCreatedAt(selfRoute.getCreatedAt());
                    return selfRouteDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 사용자 ID로 자신 경로 목록을 가져오는 API
     * @param userId 사용자 ID
     * @return List<SelfRouteDTO>
     */
    @GetMapping("/user/{userId}")
    public List<SelfRouteDTO> getRoutesByUserId(@PathVariable Integer userId) {
        return selfRouteService.getSelfRoutesByUserId(userId).stream()  // 전체 엔티티 조회
                .map(selfRoute -> {
                    SelfRouteDTO selfRouteDTO = new SelfRouteDTO();
                    selfRouteDTO.setSelfRouteId(selfRoute.getSelfRouteId());
                    selfRouteDTO.setRouteName(selfRoute.getRouteName());
                    selfRouteDTO.setCreatedAt(selfRoute.getCreatedAt());
                    return selfRouteDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 특정 ID의 자신 경로 정보를 가져오는 API
     * @param id 경로 ID
     * @return SelfRouteDTO
     */
    @GetMapping("/{id}")
    public SelfRouteDTO getSelfRouteById(@PathVariable int id) {
        SelfRoute selfRoute = selfRouteService.getSelfRouteById(id);
        SelfRouteDTO selfRouteDTO = new SelfRouteDTO();
        selfRouteDTO.setSelfRouteId(selfRoute.getSelfRouteId());
        selfRouteDTO.setUserId(selfRoute.getUserId());
        selfRouteDTO.setRouteName(selfRoute.getRouteName());
        selfRouteDTO.setRouteResult(selfRoute.getRouteResult());
        selfRouteDTO.setCreatedAt(selfRoute.getCreatedAt());
        return selfRouteDTO;
    }

    /**
     * 새로운 자신 경로 정보를 생성하는 API
     * @param selfRouteDTO 경로 DTO
     * @return SelfRouteDTO
     */
    @PostMapping
    public SelfRouteDTO createSelfRoute(@RequestBody SelfRouteDTO selfRouteDTO) {
        SelfRoute selfRoute = new SelfRoute();
        selfRoute.setUserId(selfRouteDTO.getUserId());
        selfRoute.setRouteName(selfRouteDTO.getRouteName());
        selfRoute.setRouteResult(selfRouteDTO.getRouteResult());
        selfRoute.setCreatedAt(selfRouteDTO.getCreatedAt());
        SelfRoute savedSelfRoute = selfRouteService.createSelfRoute(selfRoute);
        selfRouteDTO.setSelfRouteId(savedSelfRoute.getSelfRouteId());
        return selfRouteDTO;
    }

    /**
     * 기존 자신 경로 정보를 업데이트하는 API
     * @param id 경로 ID
     * @param selfRouteDTO 경로 DTO
     * @return SelfRouteDTO
     */
    @PutMapping("/{id}")
    public SelfRouteDTO updateSelfRoute(@PathVariable int id, @RequestBody SelfRouteDTO selfRouteDTO) {
        SelfRoute selfRoute = new SelfRoute();
        selfRoute.setSelfRouteId(id);
        selfRoute.setUserId(selfRouteDTO.getUserId());
        selfRoute.setRouteName(selfRouteDTO.getRouteName());
        selfRoute.setRouteResult(selfRouteDTO.getRouteResult());
        selfRoute.setCreatedAt(selfRouteDTO.getCreatedAt());
        SelfRoute updatedSelfRoute = selfRouteService.updateSelfRoute(selfRoute);
        selfRouteDTO.setSelfRouteId(updatedSelfRoute.getSelfRouteId());
        return selfRouteDTO;
    }

    /**
     * 특정 ID의 자신 경로 정보를 삭제하는 API
     * @param id 경로 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteSelfRoute(@PathVariable int id) {
        selfRouteService.deleteSelfRoute(id);
        return "SelfRoute with ID " + id + " has been deleted.";
    }
}
