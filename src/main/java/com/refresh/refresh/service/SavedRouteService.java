package com.refresh.refresh.service;

import com.refresh.refresh.entity.SavedRoute;
import com.refresh.refresh.repository.SavedRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SavedRouteService {

    @Autowired
    private SavedRouteRepository savedRouteRepository;

    /**
     * 모든 즐겨찾기 경로 정보를 가져오는 메서드
     * @return List<SavedRoute>
     */
    public List<SavedRoute> getAllSavedRoutes() {
        return savedRouteRepository.findAll();
    }

    /**
     * 특정 ID의 즐겨찾기 경로 정보를 가져오는 메서드
     * @param id 경로 ID
     * @return SavedRoute
     */
    public SavedRoute getSavedRouteById(Integer id) { 
        return savedRouteRepository.findById(id).orElseThrow(() -> new RuntimeException("SavedRoute not found"));
    }
    
    /**
     * USER ID로 즐겨찾기 경로 정보를 가져오는 메서드
     * @param userId 사용자 ID
     * @return List<SavedRoute>
     */
    public List<SavedRoute> getSavedRoutesByUserId(Integer userId) { 
        return savedRouteRepository.findByUserId(userId);
    }
    
    /**
     * USER ID로 경로 ID와 이름만 가져오는 메서드
     * @param userId 사용자 ID
     * @return List<Map<String, Object>> - savedRouteId, routeName 포함
     */
    public List<Map<String, Object>> getRouteIdAndNameByUserId(Integer userId) { 
        return savedRouteRepository.findRouteIdAndNameByUserId(userId);
    }

    /**
     * 새로운 즐겨찾기 경로 정보를 생성하는 메서드
     * @param savedRoute 경로 엔티티
     * @return SavedRoute
     */
    public SavedRoute createSavedRoute(SavedRoute savedRoute) {
        return savedRouteRepository.save(savedRoute);
    }

    /**
     * 기존 즐겨찾기 경로 정보를 업데이트하는 메서드
     * @param savedRoute 경로 엔티티
     * @return SavedRoute
     */
    public SavedRoute updateSavedRoute(SavedRoute savedRoute) {
        if (!savedRouteRepository.existsById(savedRoute.getSavedRouteId())) {
            throw new RuntimeException("SavedRoute not found");
        }
        return savedRouteRepository.save(savedRoute);
    }

    /**
     * 특정 ID의 즐겨찾기 경로 정보를 삭제하는 메서드
     * @param id 경로 ID
     */
    public void deleteSavedRoute(Integer id) { 
        if (!savedRouteRepository.existsById(id)) {
            throw new RuntimeException("SavedRoute not found");
        }
        savedRouteRepository.deleteById(id);
    }
}
