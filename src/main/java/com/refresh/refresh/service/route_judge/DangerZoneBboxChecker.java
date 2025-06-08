package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.entity.DangerRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 경로와 DB의 위험지역 bbox 간의 교차 여부를 판단하는 유틸리티 클래스
 *
 * - 경로를 일정 길이로 분할한 후, 각 구간의 bbox를 계산하여 교차 여부를 정밀히 확인함
 * - 단순 bbox 1개를 사용하는 것보다 false positive가 적고 정밀도 높음
 */
@Service
public class DangerZoneBboxChecker {

    /**
     * 경로를 일정 단위(구간 개수)로 나누어 각 구간에 대해 BBOX를 계산
     *
     * @param coordinates 전체 경로의 좌표 리스트 (각 요소는 [lng, lat])
     * @param segmentCount 나눌 구간 수 (예: 10 → 경로를 10등분)
     * @return 각 구간의 bbox 배열 리스트 (각 요소는 [minLng, minLat, maxLng, maxLat])
     */
    public static List<double[]> computeSegBbox(List<double[]> coordinates, int segmentCount) {
        List<double[]> bboxes = new ArrayList<>();

        if (coordinates.size() < 2 || segmentCount < 1) return bboxes;

        int total = coordinates.size();
        int step = Math.max(1, total / segmentCount);

        for (int i = 0; i < total - 1; i += step) {
            int end = Math.min(i + step, total);
            List<double[]> segment = coordinates.subList(i, end);
            bboxes.add(computeBbox(segment));
        }

        return bboxes;
    }

    /**
     * 주어진 좌표 리스트에서 BBOX 계산
     */
    public static double[] computeBbox(List<double[]> coords) {
        double minLat = Double.MAX_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double maxLng = -Double.MAX_VALUE;

        for (double[] coord : coords) {
            double lng = coord[0];
            double lat = coord[1];
            if (lng < minLng) minLng = lng;
            if (lat < minLat) minLat = lat;
            if (lng > maxLng) maxLng = lng;
            if (lat > maxLat) maxLat = lat;
        }

        return new double[]{minLng, minLat, maxLng, maxLat};
    }

    /**
     * 두 개의 BBOX가 겹치는지 여부 확인
     */
    public static boolean isOverlap(double[] bbox1, double[] bbox2) {
        double minX1 = bbox1[0], minY1 = bbox1[1], maxX1 = bbox1[2], maxY1 = bbox1[3];
        double minX2 = bbox2[0], minY2 = bbox2[1], maxX2 = bbox2[2], maxY2 = bbox2[3];

        return !(maxX1 < minX2 || minX1 > maxX2 || maxY1 < minY2 || minY1 > maxY2);
    }

    /**
     * 경로 구간 bbox들과 DB 레코드의 bbox가 하나라도 겹치면 true
     * @param segmentedBboxes 경로를 나눈 여러 구간의 bbox 리스트
     * @param record DangerRecord (DB에서 조회된 위험지역)
     * @return 교차 여부
     */
    public static boolean isAnySegOverlap(List<double[]> segmentedBboxes, DangerRecord record) {
        double[] dangerBbox = new double[]{
            record.getBboxMinLng(),
            record.getBboxMinLat(),
            record.getBboxMaxLng(),
            record.getBboxMaxLat()
        };

        for (double[] pathBbox : segmentedBboxes) {
            if (isOverlap(pathBbox, dangerBbox)) return true;
        }
        return false;
    }

    /**
     * 위험지역들 중 경로의 세그먼트들과 겹치는 것만 필터링
     */
    public static List<DangerRecord> filterSegOverlap(List<double[]> segmentedBboxes, List<DangerRecord> records) {
        List<DangerRecord> result = new ArrayList<>();
        for (DangerRecord record : records) {
            if (isAnySegOverlap(segmentedBboxes, record)) {
                result.add(record);
            }
        }
        return result;
    }
}
