package com.refresh.refresh.service.route_judge;

import com.refresh.refresh.entity.DangerRecord;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 경로 BBOX와 DB에서 불러온 BBOX 필드 간의 교차 여부를 판단하는 유틸리티
 */
@Service
public class DZFilter {

    /**
     * 경로 좌표 리스트로부터 BBOX 계산
     * @param coordinates 경로 좌표 리스트 (각 요소는 [lng, lat])
     * @return double[] {minLan, minLat, maxLan, maxLat}
     */
    public static double[] computeBboxFromCoordinates(List<double[]> coordinates) {
        double minLat = Double.MAX_VALUE;
        double minLan = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double maxLan = -Double.MAX_VALUE;

        for (double[] coord : coordinates) {
            double lan = coord[0];
            double lat = coord[1];
            if (lan < minLan) minLan = lan;
            if (lat < minLat) minLat = lat;
            if (lan > maxLan) maxLan = lan;
            if (lat > maxLat) maxLat = lat;
        }

        return new double[]{minLan, minLat, maxLan, maxLat};
    }

    /**
     * 두 개의 BBOX가 교차하는지 판단
     */
    public static boolean isBboxIntersecting(double[] pathBbox, double[] dangerBbox) {
        double pathMinX = pathBbox[0];
        double pathMinY = pathBbox[1];
        double pathMaxX = pathBbox[2];
        double pathMaxY = pathBbox[3];

        double dangerMinX = dangerBbox[0];
        double dangerMinY = dangerBbox[1];
        double dangerMaxX = dangerBbox[2];
        double dangerMaxY = dangerBbox[3];

        return !(pathMaxX < dangerMinX || pathMinX > dangerMaxX ||
                 pathMaxY < dangerMinY || pathMinY > dangerMaxY);
    }

    /**
     * 주어진 경로 BBOX와 DB의 DangerRecord 목록에서 교차하는 것만 필터링
     */
    public static List<DangerRecord> filterIntersectingRecords(double[] pathBbox, List<DangerRecord> records) {
        return records.stream()
                .filter(record -> intersectsWithDbBbox(pathBbox, record))
                .toList();
    }

    /**
     * 단일 DangerRecord와 경로 BBOX가 교차하는지 여부 확인
     */
    public static boolean intersectsWithDbBbox(double[] pathBbox, DangerRecord record) {
        return isBboxIntersecting(
                pathBbox,
                new double[]{
                        record.getBboxMinLng(),
                        record.getBboxMinLat(),
                        record.getBboxMaxLng(),
                        record.getBboxMaxLat()
                }
        );
    }
}