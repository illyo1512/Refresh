package com.refresh.refresh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/* 길찾기에 관련된 DTO 클래스 */
public class Route_InfoDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteCoordinate {
        private double lat;
        private double lng;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteCheckRequest {
        private RouteCoordinate start;
        private RouteCoordinate end;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteGeoJsonResponse {
        private String type; // "FeatureCollection"
        private List<Feature> features;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Feature {
            private String type; // "Feature"
            private Geometry geometry;
            private Properties properties;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Geometry {
            private String type; // "LineString"
            private List<List<Double>> coordinates; // [ [lng, lat], ... ]
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Properties {
            private String name; // e.g., "Original Route", "Bypass Points"
        }
    }
}