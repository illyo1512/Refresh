import os
import re

def extract_bbox_from_filename(filename: str):
    """
    danger_area_126.7080_35.0160_126.7120_35.0200 형태의 파일명에서 bbox 추출
    """
    pattern = r'danger_area_(\d+\.\d+)_(\d+\.\d+)_(\d+\.\d+)_(\d+\.\d+)'
    match = re.search(pattern, filename)
    if not match:
        raise ValueError(f"❌ BBOX 정보가 없는 파일명: {filename}")
    return tuple(map(float, match.groups()))  # (min_lng, min_lat, max_lng, max_lat)

def generate_sql_from_txt(input_path: str, output_path: str, detail_id: int):
    with open(input_path, 'r', encoding='utf-8') as infile:
        lines = [line.strip() for line in infile if line.strip()]

    with open(output_path, 'w', encoding='utf-8') as outfile:
        outfile.write(
            "INSERT INTO danger_record "
            "(detail_id, danger_json_path, bbox_min_lng, bbox_min_lat, bbox_max_lng, bbox_max_lat) VALUES\n"
        )
        for i, line in enumerate(lines):
            try:
                filename = os.path.basename(line)
                min_lng, min_lat, max_lng, max_lat = extract_bbox_from_filename(filename)
                comma_or_semicolon = ';' if i == len(lines) - 1 else ','
                outfile.write(
                    f"({detail_id}, '{line}', {min_lng}, {min_lat}, {max_lng}, {max_lat}){comma_or_semicolon}\n"
                )
            except ValueError as e:
                print(e)
