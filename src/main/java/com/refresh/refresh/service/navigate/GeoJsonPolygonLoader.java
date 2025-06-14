// src/main/java/com/example/service/GeoJsonPolygonLoader.java

package com.refresh.refresh.service.navigate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class GeoJsonPolygonLoader {

    /**
     * 클래스패스(resource 폴더)에서 GeoJSON 파일을 읽어 Polygon 객체로 변환합니다.
     *
     * @param filename 리소스 폴더에 위치한 GeoJSON 파일명 (예: "avoid_zone.geojson")
     * @return Polygon 객체
     * @throws Exception 잘못된 GeoJSON 형식이거나 Polygon이 없을 경우 예외 발생
     */
    public Polygon loadPolygonFromResource(String filename) throws Exception {
        // Jackson ObjectMapper를 이용해 JSON 구조 파싱
        ObjectMapper mapper = new ObjectMapper();

        // classpath 내 리소스 파일 로딩
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("geojson/" + filename);
        if (inputStream == null) {
            throw new IllegalArgumentException("GeoJSON file not found: " + filename);
        }

        JsonNode root = mapper.readTree(inputStream);
        GeoJsonReader geoJsonReader = new GeoJsonReader();

        // GeoJSON 내의 features 배열 탐색
        for (JsonNode feature : root.get("features")) {
            JsonNode geometryNode = feature.get("geometry");

            if ("Polygon".equals(geometryNode.get("type").asText())) {
                // geometry 노드를 문자열로 변환 후 파싱
                String geometryStr = mapper.writeValueAsString(geometryNode);
                Geometry geometry = geoJsonReader.read(geometryStr);

                if (geometry instanceof Polygon) {
                    return (Polygon) geometry;
                }
            }
        }

        throw new IllegalArgumentException("No Polygon geometry found in: " + filename);
    }

    /**
     * 단일 GeoJSON 파일에서 첫 번째 Polygon을 읽어 반환합니다.
     *
     * @param inputStream GeoJSON 파일의 InputStream
     * @return Polygon 객체
     * @throws Exception 잘못된 형식 또는 Polygon 없음
     */
    public Polygon parsePolygonFromGeoJson(InputStream inputStream) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(inputStream);
        GeoJsonReader reader = new GeoJsonReader();

        for (JsonNode feature : root.get("features")) {
            JsonNode geometryNode = feature.get("geometry");
            if ("Polygon".equals(geometryNode.get("type").asText())) {
                String geometryStr = mapper.writeValueAsString(geometryNode);
                Geometry geometry = reader.read(geometryStr);
                if (geometry instanceof Polygon) {
                    return (Polygon) geometry;
                }
            }
        }
        throw new IllegalArgumentException("No Polygon found in GeoJSON file.");
    }

    /**
     * 지정된 classpath 폴더 경로 내의 모든 GeoJSON 파일을 읽어 Polygon 리스트로 반환합니다.
     *
     * @param folderPath 예: "geojson" (classpath 기준)
     * @return Polygon 객체들의 리스트
     */
    public List<Polygon> loadAllPolygonsFromFolder(String folderPath) throws Exception {
        List<Polygon> polygons = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();

        // 리소스 폴더 내부에 있는 파일 목록 열기
        URL resource = classLoader.getResource(folderPath);
        if (resource == null) {
            throw new IllegalArgumentException("Folder not found: " + folderPath);
        }

        File directory = new File(resource.toURI());
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".geojson"));

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No .geojson files in folder: " + folderPath);
        }

        for (File file : files) {
            try (InputStream inputStream = new FileInputStream(file)) {
                Polygon polygon = parsePolygonFromGeoJson(inputStream);
                polygons.add(polygon);
            }
        }

        return polygons;
    }
}