package com.refresh.refresh.service;

import com.refresh.refresh.entity.SelfRoute;
import com.refresh.refresh.repository.SelfRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SelfRouteService {

    @Autowired
    private SelfRouteRepository selfRouteRepository;

    /**
     * 모든 자신 경로 정보를 가져오는 메서드
     * @return List<SelfRoute>
     */
    public List<SelfRoute> getAllSelfRoutes() {
        return selfRouteRepository.findAll();
    }

    /**
     * 특정 ID의 자신 경로 정보를 가져오는 메서드
     * @param id 경로 ID
     * @return SelfRoute
     */
    public SelfRoute getSelfRouteById(Integer id) {  // int → Integer 변경
        return selfRouteRepository.findById(id).orElseThrow(() -> new RuntimeException("SelfRoute not found"));
    }

    /**
     * USER ID로 즐겨찾기 경로 정보를 가져오는 메서드
     * @param userId 사용자 ID
     * @return List<SelfRoute>
     */
    public List<SelfRoute> getSelfRoutesByUserId(Integer userId) {
        return selfRouteRepository.findByUserId(userId);
    }

    /**
     * 새로운 자신 경로 정보를 생성하는 메서드
     * @param selfRoute 경로 엔티티
     * @return SelfRoute
     */
    public SelfRoute createSelfRoute(SelfRoute selfRoute) {
        return selfRouteRepository.save(selfRoute);
    }

    /**
     * 기존 자신 경로 정보를 업데이트하는 메서드
     * @param selfRoute 경로 엔티티
     * @return SelfRoute
     */
    public SelfRoute updateSelfRoute(SelfRoute selfRoute) {
        if (!selfRouteRepository.existsById(selfRoute.getSelfRouteId())) {
            throw new RuntimeException("SelfRoute not found");
        }
        return selfRouteRepository.save(selfRoute);
    }

    /**
     * 특정 ID의 자신 경로 정보를 삭제하는 메서드
     * @param id 경로 ID
     */
    public void deleteSelfRoute(Integer id) {  // int → Integer 변경
        if (!selfRouteRepository.existsById(id)) {
            throw new RuntimeException("SelfRoute not found");
        }
        selfRouteRepository.deleteById(id);
    }

    /**
     * 경로 저장
     */
    public Integer saveRoute(Integer userId, String routeName, String routeResult) {
        SelfRoute selfRoute = new SelfRoute();
        selfRoute.setUserId(userId);
        selfRoute.setRouteName(routeName);
        selfRoute.setRouteResult(routeResult);
        selfRoute.setCreatedAt(LocalDateTime.now());

        SelfRoute saved = selfRouteRepository.save(selfRoute);
        return saved.getSelfRouteId();
    }

}
