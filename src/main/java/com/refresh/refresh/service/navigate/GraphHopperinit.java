package com.refresh.refresh.service.navigate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.json.Statement;
import com.graphhopper.util.CustomModel;
import com.graphhopper.util.JsonFeature;
import com.graphhopper.util.JsonFeatureCollection;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * GraphHopper 8.0 초기화 + GeoJSON No-Go 영역 적용 (util.* 패키지에 맞춘 최종 버전)
 * -----------------------------------------------------------------------------
 * - JsonFeature / JsonFeatureCollection 은 com.graphhopper.util.* 에 존재
 * - Statement 는 com.graphhopper.json.Statement 그대로 유지
 * - CustomModel#addAreas(JsonFeatureCollection) & addToPriority(Statement) 사용
 */
@Slf4j
@Configuration
public class GraphHopperinit {

    private static final String NO_GO_GEOJSON = "C:\\Users\\lee\\IdeaProjects\\Refresh\\data\\customModels\\danger_zones.geojson";

    /* ───────────────────────── GraphHopper Bean ───────────────────────── */
    @Bean
    public GraphHopper graphHopper() {
        log.info("GraphHopper 8.0 초기화 시작");
        GraphHopper hopper = new GraphHopper();
        try {
            hopper.setOSMFile("data/south-korea-latest.osm.pbf");
            hopper.setGraphHopperLocation("data/graph-cache");

            hopper.setProfiles(Arrays.asList(
                    createFootFastestProfile(),
                    createFootAvoidProfile(),
                    createCarFastestProfile(),
                    createCarAvoidProfile()
            ));

            log.info("OSM 데이터 로드 / 그래프 생성");
            hopper.importOrLoad();
            log.info("GraphHopper 8.0 초기화 완료");
            return hopper;
        } catch (Exception e) {
            log.error("GraphHopper 8.0 초기화 실패", e);
            throw new RuntimeException(e);
        }
    }

    /* ──────────────────────── No-Go 영역 주입 ───────────────────────── */
    private void applyNoGoAreas(CustomModel cm) {
        Path path = Paths.get(NO_GO_GEOJSON);
        if (!Files.exists(path)) {
            log.warn("No-Go GeoJSON 파일을 찾을 수 없어 회피 영역을 적용하지 않습니다: {}", path);
            return;
        }
        try {
            String raw = Files.readString(path);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(raw);
            GeoJsonReader reader = new GeoJsonReader();

            int idx = 0;
            for (JsonNode feat : root.path("features")) {
                JsonNode geomNode = feat.get("geometry");
                if (geomNode == null) continue;
                Geometry geom = reader.read(geomNode.toString());
                if (!(geom instanceof Polygon) && !(geom instanceof MultiPolygon)) continue;

                String areaId = "no_go_" + idx++;

                /* 1) JsonFeature 생성 & Collection에 담아 addAreas */
                JsonFeature jf = new JsonFeature(areaId, "Feature", null, geom, null);
                JsonFeatureCollection col = new JsonFeatureCollection();
                col.getFeatures().add(jf);
                cm.addAreas(col);

                /* 2) priority × 0 규칙 */
                cm.addToPriority(Statement.If("in_" + areaId, Statement.Op.MULTIPLY, "0"));
            }
            log.info("No-Go 영역 {}개 적용 완료", idx);
        } catch (Exception ex) {
            log.error("No-Go GeoJSON 파싱/주입 실패", ex);
        }
    }

    /* ───────────────────────── 프로필 생성 ─────────────────────────── */
    private Profile createFootFastestProfile() {
        CustomModel cm = new CustomModel();
        cm.setDistanceInfluence(0.0);
        // No-Go 영역은 FASTEST 프로필에 적용하지 않음
        return new Profile("foot_fastest")
                .setVehicle("foot")
                .setWeighting("custom")
                .setTurnCosts(false)
                .setCustomModel(cm);
    }

    private Profile createFootAvoidProfile() {
        CustomModel cm = new CustomModel();
        cm.setDistanceInfluence(200.0);
        cm.setHeadingPenalty(300d);
        applyNoGoAreas(cm);
        return new Profile("foot_avoid")
                .setVehicle("foot")
                .setWeighting("custom")
                .setTurnCosts(false)
                .setCustomModel(cm);
    }

    private Profile createCarFastestProfile() {
        CustomModel cm = new CustomModel();
        cm.setDistanceInfluence(0.0);
        // No-Go 영역은 FASTEST 프로필에 적용하지 않음
        return new Profile("car_fastest")
                .setVehicle("car")
                .setWeighting("custom")
                .setTurnCosts(false)
                .setCustomModel(cm);
    }

    private Profile createCarAvoidProfile() {
        CustomModel cm = new CustomModel();
        cm.setDistanceInfluence(100.0);
        cm.setHeadingPenalty(200d);
        applyNoGoAreas(cm);
        return new Profile("car_avoid")
                .setVehicle("car")
                .setWeighting("custom")
                .setTurnCosts(false)
                .setCustomModel(cm);
    }
}
