"""
merge_geojsons_fixed.py
-----------------------
❶ IN_DIR  : 여러 GeoJSON 이 들어 있는 폴더
❷ OUT_FILE: 병합 결과(.geojson) 저장 위치

실행
    (venv) PS> python merge_geojsons_fixed.py
"""

from pathlib import Path
import geopandas as gpd
import pandas as pd
from shapely.ops import unary_union
from shapely.validation import make_valid, explain_validity
import json, sys

# ────────────────── ❶❷ 고정 경로만 바꿔주면 끝 ──────────────────
IN_DIR  = Path(r"C:\Users\lee\IdeaProjects\Refresh\content\geojson\광주광역시")
OUT_FILE = Path(r"C:\Users\lee\IdeaProjects\Refresh\data\customModels\danger_zones.geojson")
# ────────────────────────────────────────────────────────────────

def main():
    geojson_files = list(IN_DIR.glob("*.geojson"))
    if not geojson_files:
        sys.exit(f"[!] {IN_DIR} 폴더에 *.geojson 파일이 없습니다.")

    cleaned = []
    for f in geojson_files:
        for geom in gpd.read_file(f)["geometry"]:
            # ① try make_valid (Shapely 2.x 기준)
            try:
                fixed = make_valid(geom)
            except Exception:
                fixed = geom
            # ② fallback: buffer(0) 로 덮어쓰기
            if not fixed.is_valid:
                fixed = fixed.buffer(0)

            if not fixed.is_valid or fixed.is_empty:
                print(f"[!] 잘못된 폴리곤 건너뜀 → {f.name}: {explain_validity(fixed)}")
                continue
            cleaned.append(fixed)

    if not cleaned:
        sys.exit("[!] 유효한 폴리곤이 남지 않았습니다.")

    merged = unary_union(cleaned)

    # MultiPolygon → 개별 Feature 로 분리
    if merged.geom_type == "Polygon":
        polys = [merged]
    else:
        polys = list(merged.geoms)

    feats = []
    for idx, poly in enumerate(polys, 1):
        feats.append({
            "type": "Feature",
            "id": f"danger_{idx:03d}",
            "properties": {},
            "geometry": json.loads(gpd.GeoSeries([poly]).to_json())["features"][0]["geometry"]
        })

    OUT_FILE.parent.mkdir(parents=True, exist_ok=True)
    json.dump({"type": "FeatureCollection", "features": feats},
              OUT_FILE.open("w", encoding="utf-8"), ensure_ascii=False, indent=2)

    print(f"[✓] {len(geojson_files)} → {len(feats)} 개 Feature 병합 완료 → {OUT_FILE}")

if __name__ == "__main__":
    main()
