# main.py
# 전체 분석 파이프라인: WMS → 마스크 → 외곽선 → GeoJSON 저장 (다중 BBOX 반복 지원)

import os
import geojson
from datetime import datetime
from wms.fetch import fetch_wms_image
from wms.analyze import extract_opaque_mask, find_danger_contours
from wms.geojson_util import contours_to_geojson
from math import ceil

TILE_LNG = 0.004
TILE_LAT = 0.004

# ✅ 분석 대상 전체 범위 지정 (예: 광주광역시)
TARGET_REGION = "광주광역시"
FULL_BBOX = [126.7, 35.0, 127.1, 35.3]
WIDTH, HEIGHT = 512, 512

# ✅ 저장 경로 구조 생성
img_dir = f"../content/images/{TARGET_REGION}"
geojson_dir = f"../content/geojson/{TARGET_REGION}"
log_dir = "../logs"
output_list_path = f"{log_dir}/generated_files.txt"
processed_path = f"{log_dir}/processed_bbox.txt"
log_path = f"{log_dir}/analysis.log"
os.makedirs(img_dir, exist_ok=True)
os.makedirs(geojson_dir, exist_ok=True)
os.makedirs(log_dir, exist_ok=True)

# ✅ 이전에 처리한 BBOX 불러오기
processed_set = set()
if os.path.exists(processed_path):
    with open(processed_path, "r") as f:
        for line in f:
            processed_set.add(line.strip())
    if processed_set:
        print(f"[INFO] 중단된 지점부터 이어서 분석합니다. (총 {len(processed_set)} 타일 건너뜀)")

# ✅ 진행률 계산
x_tiles = ceil((FULL_BBOX[2] - FULL_BBOX[0]) / TILE_LNG)
y_tiles = ceil((FULL_BBOX[3] - FULL_BBOX[1]) / TILE_LAT)
total_tiles = x_tiles * y_tiles
processed_tiles = 0

lng = FULL_BBOX[0]
while lng < FULL_BBOX[2]:
    lat = FULL_BBOX[1]
    while lat < FULL_BBOX[3]:
        processed_tiles += 1
        bbox = [lng, lat, lng + TILE_LNG, lat + TILE_LAT]
        bbox_str = f"{bbox[0]:.4f},{bbox[1]:.4f},{bbox[2]:.4f},{bbox[3]:.4f}"
        progress = f"[{processed_tiles}/{total_tiles} - {processed_tiles / total_tiles:.1%}]"

        if bbox_str in processed_set:
            lat += TILE_LAT
            continue

        image_path = f"{img_dir}/wms_{bbox_str.replace(',', '_')}.png"
        geojson_filename = f"danger_area_{bbox_str.replace(',', '_')}.geojson"
        geojson_path = f"{geojson_dir}/{geojson_filename}"

        try:
            print(f"{progress} [INFO] WMS 이미지 요청 중... BBOX={bbox_str}")
            image = fetch_wms_image(bbox, WIDTH, HEIGHT)

            if image == "error":
                with open(processed_path, "a", encoding="utf-8") as f:
                    f.write(bbox_str + "\n")
                print(f"{progress} [ERROR] 서버 요청 거부 또는 실패 - 분석 중단됨: BBOX={bbox_str}")
                raise RuntimeError("WMS 요청 오류. 처리 중단됨.")


            if image is None:
                with open(processed_path, "a", encoding="utf-8") as f:
                    f.write(bbox_str + "\n")
                lat += TILE_LAT
                continue

            image.save(image_path)

            print("[INFO] 위험 마스크 생성 중...")
            mask = extract_opaque_mask(image)

            if mask.sum() == 0:
                with open(processed_path, "a", encoding="utf-8") as f:
                    f.write(bbox_str + "\n")
                lat += TILE_LAT
                continue

            print("[INFO] 외곽선 추출 중...")
            contours = find_danger_contours(mask)
            print(f"[INFO] 외곽선 개수: {len(contours)}")

            print("[INFO] GeoJSON 변환 중...")
            geojson_data = contours_to_geojson(contours, bbox, WIDTH, HEIGHT)
            with open(geojson_path, "w") as f:
                geojson.dump(geojson_data, f)

            with open(log_path, "a", encoding="utf-8") as log_file:
                log_file.write(f"{datetime.now()} | {TARGET_REGION} | BBOX={bbox_str} | 외곽선={len(contours)} | 파일={geojson_path}\n")

            with open(output_list_path, "a", encoding="utf-8") as file_list:
                file_list.write(f"{geojson_filename}\n")

            with open(processed_path, "a", encoding="utf-8") as f:
                f.write(bbox_str + "\n")

            print(f"{progress} [INFO] 완료: {geojson_path}\n")

        except Exception as e:
            print(f"{progress} [ERROR] 처리 실패: BBOX={bbox_str} → {e}\n")
            print(f"[INFO] 현재까지 {processed_tiles}개 중 {processed_tiles}/{total_tiles} 완료됨. 중단.")
            exit(1)

        lat += TILE_LAT
    lng += TILE_LNG
