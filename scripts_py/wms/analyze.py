# wms/analyze.py
# WMS 이미지에서 투명 영역을 제외한 위험 외곽선 추출 (색상 기반 아님)

import cv2
import numpy as np
from PIL import Image

def extract_opaque_mask(image_pil):
    """
    WMS 이미지에서 투명하지 않은 픽셀(=위험 표현 픽셀)을 마스킹함
    Args:
        image_pil (PIL.Image): WMS 이미지 객체 (RGBA)
    Returns:
        mask (np.ndarray): 불투명한 영역의 마스크 (0/255)
    """
    image_rgba = image_pil.convert("RGBA")
    alpha_channel = np.array(image_rgba.split()[3])  # A 채널만 추출
    _, mask = cv2.threshold(alpha_channel, 0, 255, cv2.THRESH_BINARY)
    return mask

def find_danger_contours(mask):
    """
    마스크 이미지에서 외곽선 추출 (투명 영역 제외)
    Args:
        mask (np.ndarray): 불투명 마스크
    Returns:
        list[np.ndarray]: 외곽선 좌표 리스트
    """
    contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    return contours
