import os
import json
import glob
from pathlib import Path
from typing import List, Dict, Any, Optional

class GenerateCustomModelUseByGeoJson:
    """
    폴더의 GeoJSON 파일들을 읽어서 GraphHopper CustomModel JSON 생성
    """
    
    def __init__(self):
        self.base_templates = {
            "foot_shortest": 200.0,
            "foot_balanced": 70.0,
            "foot_fastest": 0.0,
            "car_fastest": 0.0,
            "car_balanced": 30.0,
            "car_shortest": 100.0
        }
    
    def read_geojson_files_from_folder(self, folder_path: str) -> List[Dict[str, Any]]:
        """
        폴더에서 모든 GeoJSON 파일 읽기
        
        Args:
            folder_path: GeoJSON 파일들이 있는 폴더 경로
            
        Returns:
            GeoJSON 데이터 리스트
        """
        geojson_data = []
        folder = Path(folder_path)
        
        if not folder.exists():
            print(f"❌ 폴더가 존재하지 않습니다: {folder_path}")
            return geojson_data
        
        # .geojson, .json 파일 검색
        patterns = ['*.geojson', '*.json']
        files_found = []
        
        for pattern in patterns:
            files_found.extend(glob.glob(str(folder / pattern)))
        
        print(f"📁 폴더 경로: {folder_path}")
        print(f"🔍 발견된 파일 수: {len(files_found)}")
        
        for file_path in files_found:
            try:
                with open(file_path, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    
                filename = os.path.basename(file_path)
                print(f"✅ 읽기 성공: {filename}")
                
                # 파일명을 ID로 사용 (확장자 제거)
                file_id = os.path.splitext(filename)[0]
                
                # GeoJSON에 메타데이터 추가
                data['_file_info'] = {
                    'filename': filename,
                    'file_id': file_id,
                    'file_path': file_path
                }
                
                geojson_data.append(data)
                
            except Exception as e:
                print(f"❌ 파일 읽기 실패: {file_path} - {str(e)}")
        
        return geojson_data
    
    def convert_to_areas_format(self, geojson_list: List[Dict[str, Any]]) -> Dict[str, Any]:
        """
        GeoJSON 리스트를 GraphHopper areas 형태로 변환
        
        Args:
            geojson_list: GeoJSON 데이터 리스트
            
        Returns:
            areas 딕셔너리
        """
        areas = {}
        
        for i, geojson_data in enumerate(geojson_list):
            # 파일 ID를 기반으로 area ID 생성
            if '_file_info' in geojson_data:
                area_id = f"avoid_{geojson_data['_file_info']['file_id']}"
            else:
                area_id = f"avoid_zone_{i}"
            
            # GeoJSON 타입에 따라 처리
            if geojson_data.get('type') == 'FeatureCollection':
                # FeatureCollection인 경우 각 Feature를 별도 area로 추가
                features = geojson_data.get('features', [])
                for j, feature in enumerate(features):
                    feature_id = f"{area_id}_feature_{j}"
                    areas[feature_id] = {
                        "type": "Feature",
                        "geometry": feature.get('geometry'),
                        "properties": feature.get('properties', {})
                    }
                    print(f"🔧 추가됨: {feature_id} (FeatureCollection의 Feature)")
                    
            elif geojson_data.get('type') == 'Feature':
                # 단일 Feature인 경우
                areas[area_id] = {
                    "type": "Feature", 
                    "geometry": geojson_data.get('geometry'),
                    "properties": geojson_data.get('properties', {})
                }
                print(f"🔧 추가됨: {area_id} (Feature)")
                
            elif geojson_data.get('type') in ['Polygon', 'MultiPolygon']:
                # 순수 Geometry인 경우
                areas[area_id] = {
                    "type": "Feature",
                    "geometry": geojson_data,
                    "properties": {}
                }
                print(f"🔧 추가됨: {area_id} (Geometry)")
                
            else:
                print(f"⚠️ 지원하지 않는 GeoJSON 타입: {geojson_data.get('type')} in {area_id}")
        
        return areas
    
    def create_avoidance_rules(self, area_ids: List[str], avoidance_type: str = "block") -> List[Dict[str, Any]]:
        """
        회피 규칙 생성
        
        Args:
            area_ids: area ID 리스트
            avoidance_type: "block" (완전차단) 또는 "slow" (느리게)
            
        Returns:
            priority 규칙 리스트
        """
        if not area_ids:
            return []
        
        # 조건 문자열 생성 "in_avoid_zone1 || in_avoid_zone2 || ..."
        condition = " || ".join([f"in_{area_id}" for area_id in area_ids])
        
        if avoidance_type == "block":
            # 완전 통행 금지
            rule = {"if": condition, "limit_to": "0"}
        else:
            # 매우 느리게 (회피 유도)
            rule = {"if": condition, "multiply_by": "0.01"}
        
        return [rule]
    
    def generate_custom_model(self, 
                            folder_path: str,
                            base_template: str = "foot_shortest",
                            avoidance_type: str = "block",
                            output_file: Optional[str] = None) -> Dict[str, Any]:
        """
        폴더의 GeoJSON들로 CustomModel 생성
        
        Args:
            folder_path: GeoJSON 파일들이 있는 폴더
            base_template: 기본 템플릿
            avoidance_type: "block" 또는 "slow"
            output_file: 출력 파일명 (None이면 파일 저장 안함)
            
        Returns:
            CustomModel JSON
        """
        print(f"\n🚀 CustomModel 생성 시작")
        print(f"📂 대상 폴더: {folder_path}")
        print(f"📋 기본 템플릿: {base_template}")
        print(f"🚫 회피 타입: {avoidance_type}")
        
        # 1. GeoJSON 파일들 읽기
        geojson_list = self.read_geojson_files_from_folder(folder_path)
        
        if not geojson_list:
            print("❌ 읽을 수 있는 GeoJSON 파일이 없습니다.")
            return {}
        
        # 2. areas 형태로 변환
        areas = self.convert_to_areas_format(geojson_list)
        area_ids = list(areas.keys())
        
        # 3. CustomModel 생성
        custom_model = {
            "distanceInfluence": self.base_templates.get(base_template, 200.0),
            "areas": areas,
            "priority": self.create_avoidance_rules(area_ids, avoidance_type),
            "speed": []
        }
        
        # 4. 메타데이터 추가
        custom_model["_metadata"] = {
            "generated_from": folder_path,
            "total_areas": len(area_ids),
            "base_template": base_template,
            "avoidance_type": avoidance_type,
            "area_list": area_ids
        }
        
        print(f"\n✅ CustomModel 생성 완료!")
        print(f"📊 총 회피구역 수: {len(area_ids)}")
        print(f"🗃️ 회피구역 ID들: {', '.join(area_ids[:5])}{'...' if len(area_ids) > 5 else ''}")
        
        # 5. 파일 저장
        if output_file:
            with open(output_file, 'w', encoding='utf-8') as f:
                json.dump(custom_model, f, indent=2, ensure_ascii=False)
            print(f"💾 파일 저장 완료: {output_file}")
        
        return custom_model

def main():
    """메인 실행 함수"""
    converter = GenerateCustomModelUseByGeoJson()
    
    # 사용 예시
    examples = [
        {
            "folder": "./geojson_files",  # 실제 폴더 경로로 변경하세요
            "template": "foot_shortest",
            "type": "block",
            "output": "foot_shortest_blocked.json"
        },
        {
            "folder": "./danger_zones", 
            "template": "car_fastest",
            "type": "slow", 
            "output": "car_fastest_avoided.json"
        }
    ]
    
    for example in examples:
        print(f"\n{'='*50}")
        
        # 폴더가 존재하는지 확인
        if not os.path.exists(example["folder"]):
            print(f"⚠️ 폴더가 존재하지 않습니다: {example['folder']}")
            print(f"   mkdir {example['folder']}")
            continue
        
        # CustomModel 생성
        result = converter.generate_custom_model(
            folder_path=example["folder"],
            base_template=example["template"], 
            avoidance_type=example["type"],
            output_file=example["output"]
        )
        
        if result:
            print(f"🟢 {example['output']} 생성 완료")

if __name__ == "__main__":
    # 실제 사용 시 폴더 경로를 지정해서 실행
    import sys
    
    if len(sys.argv) > 1:
        # 명령행 인수로 폴더 경로 받기
        folder_path = sys.argv[1]
        template = sys.argv[2] if len(sys.argv) > 2 else "foot_shortest"
        
        converter = GenerateCustomModelUseByGeoJson()
        result = converter.generate_custom_model(
            folder_path=folder_path,
            base_template=template,
            avoidance_type="block",
            output_file=f"{template}_custom_model.json"
        )
    else:
        main()