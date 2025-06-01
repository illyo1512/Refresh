# wms/fetch.py
# WMS 서버로부터 위험지도 이미지 요청 함수 (생활안전지도 OpenAPI 기반)

import requests
from io import BytesIO
from PIL import Image
import os
from wms.key import API_KEY  # 🔑 별도 키 관리 파일에서 API 키 로드

def fetch_wms_image(bbox, width=512, height=512):
    """
    WMS 서버로부터 지정된 BBOX 영역의 이미지를 요청함 (생활안전지도 API)
    Args:
        bbox (list): [min_lng, min_lat, max_lng, max_lat]
        width (int): 요청 이미지 가로 크기 (픽셀)
        height (int): 요청 이미지 세로 크기 (픽셀)
    Returns:
        PIL.Image or None: 수신된 이미지 객체 또는 실패 시 None
    """
    if not API_KEY:
        print("[ERROR] API 키가 설정되어 있지 않습니다. wms/key.py 파일을 확인하세요.")
        return None

    base_url = f"https://www.safemap.go.kr/openApiService/wms/getLayerData.do?apikey={API_KEY}"

    params = {
        "service": "WMS",
        "request": "GetMap",
        "version": "1.1.1",
        "layers": "A2SM_CRMNLHSPOT_TOT",
        "styles": "A2SM_CrmnlHspot_Tot_Tot",
        "format": "image/png",
        "transparent": "true",
        "exceptions": "text/xml",
        "srs": "EPSG:4326",
        "bbox": ",".join(map(str, bbox)),
        "width": width,
        "height": height
    }

    try:
        response = requests.get(base_url, params=params, timeout=10)
        response.raise_for_status()
        if response.content:
            return Image.open(BytesIO(response.content)).convert("RGBA")
        else:
            print("[ERROR] 응답 본문이 비어 있음")
            return None
    except Exception as e:
        print(f"[ERROR] WMS 요청 실패: {e}")
        return None
