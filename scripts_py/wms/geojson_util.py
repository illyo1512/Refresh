# 외곽선 픽셀 좌표 → 위경도 변환 후 GeoJSON Polygon 생성

from shapely.geometry import Polygon, mapping
import geojson


def pixel_to_latlng(x, y, bbox, width, height):
    """
    픽셀 좌표(x, y)를 위경도 좌표로 변환
    Args:
        x, y (int): 이미지 내 픽셀 좌표
        bbox (list): [min_lng, min_lat, max_lng, max_lat]
        width, height (int): 이미지 가로/세로 크기
    Returns:
        (lng, lat) 좌표 튜플
    """
    min_lng, min_lat, max_lng, max_lat = bbox
    lng = min_lng + (x / width) * (max_lng - min_lng)
    lat = max_lat - (y / height) * (max_lat - min_lat)
    return (lng, lat)


def contours_to_geojson(contours, bbox, width, height):
    """
    외곽선 리스트를 GeoJSON Polygon 형태로 변환
    Args:
        contours (list): OpenCV 외곽선 리스트
        bbox (list): WMS 이미지의 위경도 BBOX
        width, height (int): 이미지의 픽셀 크기
    Returns:
        dict: GeoJSON FeatureCollection 객체
    """
    features = []
    for contour in contours:
        coords = [
            [float(lng), float(lat)]  # 좌표를 [lng, lat] 형태로 변환
            for [[x, y]] in contour
            for lng, lat in [pixel_to_latlng(int(x), int(y), bbox, width, height)]
        ]
        # 첫 점과 끝 점이 일치하지 않으면 폐곡선으로 닫음
        if coords[0] != coords[-1]:
            coords.append(coords[0])
        polygon = Polygon(coords)  # 중첩된 리스트 형태로 전달
        features.append(geojson.Feature(geometry=mapping(polygon), properties={}))

    return geojson.FeatureCollection(features)
