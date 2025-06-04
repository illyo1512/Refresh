package com.refresh.refresh.service.route_judge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.refresh.refresh.entity.DangerRecord;
import com.refresh.refresh.repository.DangerRecordRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * danger_record 테이블에서 geojson 파일명을 조회하고,
 * 실제 파일을 읽어 Polygon 리스트로 변환하는 서비스
 */
@Service
@RequiredArgsConstructor
public class DangerZoneLoaderService {

    private final DangerRecordRepository dangerRecordRepository;

    // 프로젝트 루트를 기준으로 GeoJSON 파일이 저장된 위치
    private static final String DANGER_GEOJSON_DIR = System.getProperty("user.dir")
            + File.separator + "content"
            + File.separator + "crim_total"
            + File.separator + "geojson"
            + File.separator + "광주광역시";

    /**
     * recordId에 해당하는 위험지역 GeoJSON을 불러와 Polygon 리스트로 반환
     *
     * @param recordId danger_record 테이블의 primary key
     * @return Polygon 리스트 (위험구역 영역들)
     */
    public List<Polygon> loadDangerZonesFromRecord(int recordId) {
        try {
            // 1. DB에서 danger_record 조회
            DangerRecord record = dangerRecordRepository.findById(recordId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 danger_record가 존재하지 않습니다. ID: " + recordId));

            String fileName = record.getDangerJsonPath(); // 예: "danger_area.geojson"
            String fullPath = Paths.get(DANGER_GEOJSON_DIR, fileName).toString();

            // 2. GeoJSON 파일 로드
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(fullPath);
            if (!file.exists()) {
                throw new IllegalArgumentException("GeoJSON 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
            }

            JsonNode root = mapper.readTree(file);
            GeoJsonReader reader = new GeoJsonReader(new GeometryFactory());

            // 3. Polygon 추출
            List<Polygon> dangerZones = new ArrayList<>();
            for (JsonNode feature : root.get("features")) {
                String geometryStr = mapper.writeValueAsString(feature.get("geometry"));
                Geometry geometry = reader.read(geometryStr);
                if (geometry instanceof Polygon) {
                    dangerZones.add((Polygon) geometry);
                }
            }

            return dangerZones;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("GeoJSON 파일 로딩 중 오류 발생", e);
        }
    }

    public List<Polygon> loadDangerZonesFromRecords(List<DangerRecord> records) {
        List<Polygon> allPolygons = new ArrayList<>();
        for (DangerRecord record : records) {
            allPolygons.addAll(loadDangerZonesFromRecord(record.getRecordId()));
        }
        return allPolygons;
    }
}
