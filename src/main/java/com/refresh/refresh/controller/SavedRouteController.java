package com.refresh.refresh.controller;

import com.refresh.refresh.dto.SavedRouteDTO;
import com.refresh.refresh.entity.SavedRoute;
import com.refresh.refresh.service.SavedRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/saved-routes")
public class SavedRouteController {

    @Autowired
    private SavedRouteService savedRouteService;

    /**
     * 모든 즐겨찾기 경로 정보를 가져오는 API
     * @return List<SavedRouteDTO>
     */
    @GetMapping
    public List<SavedRouteDTO> getAllSavedRoutes() {
        return savedRouteService.getAllSavedRoutes().stream()
                .map(savedRoute -> {
                    SavedRouteDTO savedRouteDTO = new SavedRouteDTO();
                    savedRouteDTO.setSavedRouteId(savedRoute.getSavedRouteId());
                    savedRouteDTO.setUserId(savedRoute.getUserId());
                    savedRouteDTO.setRouteName(savedRoute.getRouteName());
                    savedRouteDTO.setRouteResult(savedRoute.getRouteResult());
                    savedRouteDTO.setCreatedAt(savedRoute.getCreatedAt());
                    return savedRouteDTO;
                }).collect(Collectors.toList());
    }

    /**
     * 특정 ID의 즐겨찾기 경로 정보를 가져오는 API
     * @param id 경로 ID
     * @return SavedRouteDTO
     */
    @GetMapping("/{id}")
    public SavedRouteDTO getSavedRouteById(@PathVariable int id) {
        SavedRoute savedRoute = savedRouteService.getSavedRouteById(id);
        SavedRouteDTO savedRouteDTO = new SavedRouteDTO();
        savedRouteDTO.setSavedRouteId(savedRoute.getSavedRouteId());
        savedRouteDTO.setUserId(savedRoute.getUserId());
        savedRouteDTO.setRouteName(savedRoute.getRouteName());
        savedRouteDTO.setRouteResult(savedRoute.getRouteResult());
        savedRouteDTO.setCreatedAt(savedRoute.getCreatedAt());
        return savedRouteDTO;
    }

    /**
     * 새로운 즐겨찾기 경로 정보를 생성하는 API
     * @param savedRouteDTO 경로 DTO
     * @return SavedRouteDTO
     */
    @PostMapping
    public SavedRouteDTO createSavedRoute(@RequestBody SavedRouteDTO savedRouteDTO) {
        SavedRoute savedRoute = new SavedRoute();
        savedRoute.setUserId(savedRouteDTO.getUserId());
        savedRoute.setRouteName(savedRouteDTO.getRouteName());
        savedRoute.setRouteResult(savedRouteDTO.getRouteResult());
        savedRoute.setCreatedAt(savedRouteDTO.getCreatedAt());
        SavedRoute savedSavedRoute = savedRouteService.createSavedRoute(savedRoute);
        savedRouteDTO.setSavedRouteId(savedSavedRoute.getSavedRouteId());
        return savedRouteDTO;
    }

    /**
     * 기존 즐겨찾기 경로 정보를 업데이트하는 API
     * @param id 경로 ID
     * @param savedRouteDTO 경로 DTO
     * @return SavedRouteDTO
     */
    @PutMapping("/{id}")
    public SavedRouteDTO updateSavedRoute(@PathVariable int id, @RequestBody SavedRouteDTO savedRouteDTO) {
        SavedRoute savedRoute = new SavedRoute();
        savedRoute.setSavedRouteId(id);
        savedRoute.setUserId(savedRouteDTO.getUserId());
        savedRoute.setRouteName(savedRouteDTO.getRouteName());
        savedRoute.setRouteResult(savedRouteDTO.getRouteResult());
        savedRoute.setCreatedAt(savedRouteDTO.getCreatedAt());
        SavedRoute updatedSavedRoute = savedRouteService.updateSavedRoute(savedRoute);
        savedRouteDTO.setSavedRouteId(updatedSavedRoute.getSavedRouteId());
        return savedRouteDTO;
    }

    /**
     * 특정 ID의 즐겨찾기 경로 정보를 삭제하는 API
     * @param id 경로 ID
     * @return 삭제 메시지
     */
    @DeleteMapping("/{id}")
    public String deleteSavedRoute(@PathVariable int id) {
        savedRouteService.deleteSavedRoute(id);
        return "SavedRoute with ID " + id + " has been deleted.";
    }
}
