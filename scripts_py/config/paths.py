"""
파이썬 스크립트용 경로 관리 모듈
"""
import os
from pathlib import Path

class PathManager:
    def __init__(self):
        # 스크립트 기준 루트 디렉토리 설정
        self.script_root = Path(__file__).parent.parent.parent # 프로젝트 루트 디렉토리 기준
        
    # === 크롤링 관련 경로 ===
    @property
    def crawling_data_dir(self):
        """크롤링 데이터 저장 디렉토리"""
        return self.script_root / "data" / "crawling"
    
    @property
    def naver_articles_dir(self):
        """네이버 기사 저장 디렉토리"""
        return self.crawling_data_dir / "naver_articles"
    
    @property
    def last_crawled_url_file(self):
        """마지막 크롤링 URL 저장 파일"""
        return self.crawling_data_dir / "last_crawled_url.txt"
    
    def ensure_dir_exists(self, path):
        """디렉토리가 존재하지 않으면 생성"""
        Path(path).mkdir(parents=True, exist_ok=True)
        return path
    
    def get_absolute_path(self, relative_path):
        """상대 경로를 절대 경로로 변환"""
        if Path(relative_path).is_absolute():
            return str(relative_path)
        return str(self.script_root / relative_path)

# 전역 인스턴스 생성
paths = PathManager()

def setup_directories():
    """필요한 디렉토리들 초기화"""
    paths.ensure_dir_exists(paths.crawling_data_dir)
    paths.ensure_dir_exists(paths.naver_articles_dir)
    
    return {
        "crawling_data_dir": str(paths.crawling_data_dir),
        "naver_articles_dir": str(paths.naver_articles_dir)
    }