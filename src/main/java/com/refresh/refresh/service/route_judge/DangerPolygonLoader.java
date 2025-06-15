package com.refresh.refresh.service.route_judge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.refresh.refresh.entity.DangerRecord;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * danger_record에서 geojson 파일을 읽고 Polygon 또는 MultiPolygon을 로드하는 클래스
 * - baseDir, 도시 이름, 파일명을 매개변수로 받아 유연한 로딩 가능
 */
@Service
@RequiredArgsConstructor
public class DangerPolygonLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * danger_record에서 Polygon 리스트를 로드
     */
    public List<Polygon> loadPolygonsFromRecord(DangerRecord record, String baseDir, String cityName) {
        String fileName = record.getDangerJsonPath();
        return loadPolygonsFromFile(baseDir, cityName, fileName);
    }

    /**
     * 단일 GeoJSON 파일을 읽어 Polygon 리스트로 반환
     *
     * @param baseDir 기본 디렉토리 (예: /data/content/geojson/)
     * @param cityName 도시 이름 (예: "광주광역시")
     * @param fileName danger_area_xxxx.geojson
     */
    public List<Polygon> loadPolygonsFromFile(String baseDir, String cityName, String fileName) {
        List<Polygon> polygons = new ArrayList<>();

        try {
            String fullPath = Paths.get(baseDir, cityName, fileName).toString();
            File file = new File(fullPath);

            if (!file.exists()) {
                throw new IllegalArgumentException("GeoJSON 파일이 존재하지 않음: " + fullPath);
            }

            JsonNode root = objectMapper.readTree(file);
            GeoJsonReader reader = new GeoJsonReader(geometryFactory);

            for (JsonNode feature : root.get("features")) {
                String geometryStr = objectMapper.writeValueAsString(feature.get("geometry"));
                Geometry geometry = reader.read(geometryStr);

                // Polygon 또는 MultiPolygon 처리
                if (geometry instanceof Polygon polygon) {
                    polygons.add(polygon);
                } else if (geometry instanceof MultiPolygon multiPolygon) {
                    for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                        Geometry part = multiPolygon.getGeometryN(i);
                        if (part instanceof Polygon) {
                            polygons.add((Polygon) part);
                        }
                    }
                }
            }

            return polygons;

        } catch (Exception e) {
            throw new RuntimeException("GeoJSON 로딩 실패: " + fileName, e);
        }
    }
}
