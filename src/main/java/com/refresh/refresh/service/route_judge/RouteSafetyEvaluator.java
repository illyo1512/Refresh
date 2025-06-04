package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.dto.CoordinateDTO;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.repository.DangerRecordRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteSafetyEvaluator {

    private final DangerRecordRepository dangerRecordRepository;
    private final DangerZoneLoaderService dangerZoneLoaderService;
    private final RouteIntersectionChecker routeIntersectionChecker;

    /**
     * 경로 좌표를 기반으로 위험지역과의 교차 여부를 판단
     * @param routeCoordinates 사용자의 경로 좌표
     * @return true = 위험지역과 겹침, false = 안전
     */
    public boolean evaluateRouteSafety(List<CoordinateDTO> routeCoordinates) {
        // 1. 경로의 BBOX 계산
        double[] pathBbox = DZFilter.computeBboxFromCoordinates(
            routeCoordinates.stream()
                .map(CoordinateDTO::toArray)
                .toList()
    );
        // 2. BBOX 겹치는 danger_record만 조회
        List<DangerRecord> candidateRecords = dangerRecordRepository.findIntersectingBbox(
                pathBbox[0], pathBbox[1], pathBbox[2], pathBbox[3]);

        // 3. GeoJSON에서 Polygon 추출
        List<Polygon> dangerZones = new ArrayList<>();
        for (DangerRecord record : candidateRecords) {
            dangerZones.addAll(dangerZoneLoaderService.loadDangerZonesFromRecord(record.getRecordId()));
        }

        // 4. 경로와 위험지역 간의 실제 교차 여부 판단
        return routeIntersectionChecker.isRouteIntersectingDangerZones(routeCoordinates, dangerZones);
    }
}
