import os
import json
import sys
from pathlib import Path
from transformers import pipeline
from model_loader import model, tokenizer

# 경로 설정을 위해 config 디렉토리를 Python path에 추가
sys.path.append(str(Path(__file__).parent.parent))
from config.paths import paths, setup_directories

# =============================================================================
# NER 파이프라인 초기화
# =============================================================================
ner = pipeline(
    "ner", 
    model=model, 
    tokenizer=tokenizer, 
    aggregation_strategy="none"
)

# =============================================================================
# 범죄 관련 키워드 정의
# =============================================================================
crime_keywords = ["살인", "강도", "폭력", "절도", "살인"]    #["살인", "강도", "성폭행", "폭행", "절도", "납치", "방화", "강간", "흉기", "협박", "칼부림"]

# =============================================================================
# 범위 파싱 함수
# =============================================================================

def parse_range(range_str):
    """
    범위 문자열을 파싱하여 숫자 리스트 반환
    
    Args:
        range_str (str): 범위 문자열
            - "1-10": 1부터 10까지
            - "1,3,5": 1, 3, 5번만
            - "1-5,8,10-12": 1~5, 8, 10~12
            - "all": 모든 파일
            
    Returns:
        list: 처리할 파일 번호 리스트 (정수)
    """
    if not range_str or range_str.lower() == "all":
        return None  # 모든 파일 처리
    
    numbers = []
    parts = range_str.split(',')
    
    for part in parts:
        part = part.strip()
        if '-' in part:
            # 범위 처리 (예: "1-10")
            try:
                start, end = map(int, part.split('-'))
                numbers.extend(range(start, end + 1))
            except ValueError:
                print(f"⚠️ 잘못된 범위 형식: {part}")
        else:
            # 단일 숫자 처리 (예: "5")
            try:
                numbers.append(int(part))
            except ValueError:
                print(f"⚠️ 잘못된 숫자 형식: {part}")
    
    return sorted(list(set(numbers)))  # 중복 제거 및 정렬

# =============================================================================
# 경로 관리 함수
# =============================================================================

def get_article_directory(provided_path=None):
    """
    기사 디렉토리 경로를 자동으로 감지하거나 제공된 경로 사용
    
    Args:
        provided_path (str): 외부에서 제공된 디렉토리 경로
        
    Returns:
        str: 사용할 기사 디렉토리 절대 경로
    """
    if provided_path and os.path.exists(provided_path):
        print(f"📁 제공된 디렉토리 사용: {provided_path}")
        return provided_path
    
    # paths.py에서 정의한 네이버 기사 디렉토리 사용
    naver_articles_path = str(paths.naver_articles_dir)
    if os.path.exists(naver_articles_path):
        print(f"📁 기사 디렉토리 발견 (paths.py): {naver_articles_path}")
        return naver_articles_path
    
    # 대체 경로들 시도
    alternative_paths = [
        str(paths.crawling_data_dir / "articles"),
        str(paths.script_root / "crawled_articles"),
        "../../data/crawling/naver_articles"
    ]
    
    for path in alternative_paths:
        if os.path.exists(path):
            print(f"📁 기사 디렉토리 발견 (대체): {path}")
            return path
    
    # 경로가 없으면 paths.py 기본 경로로 생성
    print(f"⚠️ 기사 디렉토리를 찾을 수 없습니다. 기본 경로 생성: {naver_articles_path}")
    paths.ensure_dir_exists(naver_articles_path)
    return naver_articles_path

def get_available_file_numbers(article_dir):
    """
    디렉토리에서 사용 가능한 파일 번호들 추출
    
    Args:
        article_dir (str): 기사 디렉토리 경로
        
    Returns:
        list: 사용 가능한 파일 번호 리스트 (정수)
    """
    if not os.path.exists(article_dir):
        return []
    
    filenames = os.listdir(article_dir)
    number_prefixes = set(
        f.split("_")[0] for f in filenames 
        if "_" in f and f.endswith(".txt")
    )
    
    return sorted([int(n) for n in number_prefixes if n.isdigit()])

# =============================================================================
# 유틸리티 함수들
# =============================================================================

def extract_crime_keyword(text):
    """텍스트에서 범죄 키워드 추출"""
    return next((kw for kw in crime_keywords if kw in text), None)

def clean_word(word):
    """NER 결과에서 추출된 단어 정리"""
    return word.replace("##", "").strip()

def extract_location_single(ner_results):
    """NER 결과에서 위치 정보 추출 (단일)"""
    return next(
        (clean_word(e['word']) for e in ner_results
         if e.get('entity', '').startswith(("LOC", "ORG")) and e.get('score', 1.0) > 0.8),
        "어딘가"
    )

def build_alert_sentence(text, ner_results):
    """NER 결과를 바탕으로 경고 문장 생성"""
    # 위치 정보 추출
    location = extract_location_single(ner_results)
    
    # 시간 정보 추출
    time = next(
        (clean_word(e['word']) for e in ner_results
         if e.get('entity', '').startswith(("DAT", "TIM")) and e.get('score', 1.0) > 0.8),
        "어느 시각"
    )
    
    # 범죄 키워드 추출
    crime = extract_crime_keyword(text)
    
    # 특정 키워드에 "사건" 접미사 추가
    if crime in ["흉기", "칼부림"]:
        crime += " 사건"
        
    # 최종 경고 문장 생성
    return f"{location}에서 {time}일에 {crime}이(가) 발생했습니다."

# =============================================================================
# 파일 처리 함수들
# =============================================================================

def process_article_group(article_dir, number_part):
    """하나의 기사 그룹(번호별) 처리"""
    # 필요한 파일 경로들 생성
    url_path = os.path.join(article_dir, f"{number_part}_url.txt")
    title_path = os.path.join(article_dir, f"{number_part}_title.txt")
    article_path = os.path.join(article_dir, f"{number_part}_article.txt")

    # 필수 파일들이 모두 존재하는지 확인
    if not all(os.path.exists(p) for p in [url_path, title_path, article_path]):
        print(f"❌ 누락된 파일 있음: {number_part}")
        return None

    # 기사 본문 읽기
    try:
        with open(article_path, 'r', encoding='utf-8') as f:
            article_text = f.read()[:500]
    except Exception as e:
        print(f"❌ 파일 읽기 오류 {number_part}: {e}")
        return None

    # 범죄 키워드 포함 여부 확인
    crime_keyword = extract_crime_keyword(article_text)
    if crime_keyword is None:
        print(f"⏩ 스킵 (범죄 키워드 없음): {number_part}")
        return None

    # NER 분석 및 결과 저장
    try:
        # NER 모델로 엔티티 추출
        ner_results = ner(article_text)
        
        # 위치와 시간 정보 추출
        location = extract_location_single(ner_results)
        time = next(
            (clean_word(e['word']) for e in ner_results
             if e.get('entity', '').startswith(("DAT", "TIM")) and e.get('score', 1.0) > 0.8),
            "어느 시각"
        )
        
        # 경고 문장 생성
        alert_sentence = build_alert_sentence(article_text, ner_results)

        # 결과를 _key.txt 파일로 저장
        key_filename = f"{number_part}_key.txt"
        with open(os.path.join(article_dir, key_filename), 'w', encoding='utf-8') as f:
            f.write(alert_sentence)
        
        print(f"✅ 처리 완료: {key_filename}")
        
        # 결과 딕셔너리 반환
        return {
            "file_number": number_part,
            "alert_sentence": alert_sentence,
            "crime_keyword": crime_keyword,
            "location": location,
            "time": time,
            "key_file": key_filename,
            "status": "success"
        }
        
    except Exception as e:
        print(f"❌ NER 처리 오류 {number_part}: {e}")
        return None

def process_articles_with_range(article_dir=None, range_str=None):
    """
    지정된 범위의 기사 그룹 처리
    
    Args:
        article_dir (str): 기사 파일들이 저장된 디렉토리 경로
        range_str (str): 처리할 파일 범위
            - "1-10": 1부터 10까지
            - "1,3,5": 1, 3, 5번만
            - "all" 또는 None: 모든 파일
            
    Returns:
        dict: 전체 처리 결과
    """
    # paths.py를 사용하여 디렉토리 자동 설정
    article_dir = get_article_directory(article_dir)
    
    print(f"📂 사용할 기사 디렉토리: {article_dir}")
    
    # 디렉토리 존재 확인
    if not os.path.exists(article_dir):
        return {
            "success": False,
            "total_files": 0,
            "processed_files": 0,
            "skipped_files": 0,
            "results": [],
            "message": f"디렉토리가 존재하지 않습니다: {article_dir}",
            "requested_range": range_str
        }
    
    # 사용 가능한 파일 번호들 가져오기
    available_numbers = get_available_file_numbers(article_dir)
    
    # 범위 파싱
    target_numbers = parse_range(range_str)
    
    if target_numbers is None:
        # 모든 파일 처리
        target_numbers = available_numbers
        print(f"📁 모든 파일 처리: {len(target_numbers)}개")
    else:
        # 요청된 범위와 사용 가능한 파일의 교집합
        target_numbers = [n for n in target_numbers if n in available_numbers]
        print(f"📁 범위 지정 처리: {len(target_numbers)}개 (요청: {range_str})")
    
    if not target_numbers:
        return {
            "success": False,
            "total_files": 0,
            "processed_files": 0,
            "skipped_files": 0,
            "results": [],
            "message": f"처리할 파일이 없습니다. 요청 범위: {range_str}",
            "requested_range": range_str,
            "available_numbers": available_numbers
        }
    
    processed_files = 0
    skipped_files = 0
    results = []
    
    print(f"🎯 처리 대상: {target_numbers}")

    # 각 번호별 기사 그룹 처리
    for number in target_numbers:
        result = process_article_group(article_dir, str(number))
        if result:
            results.append(result)
            processed_files += 1
        else:
            skipped_files += 1
    
    # 최종 결과 반환
    return {
        "success": True,
        "total_files": len(target_numbers),
        "processed_files": processed_files,
        "skipped_files": skipped_files,
        "results": results,
        "message": f"AI 키워드 추출 완료 - 총 {len(target_numbers)}개 중 {processed_files}개 처리됨",
        "article_directory": article_dir,
        "requested_range": range_str,
        "target_numbers": target_numbers,
        "available_numbers": available_numbers
    }

# =============================================================================
# 메인 실행부 (스프링부트 호출용)
# =============================================================================
def main():
    """
    메인 함수 - 스프링부트에서 호출할 때 사용
    명령행 인자:
    1. 디렉토리 경로 (선택사항)
    2. 처리 범위 (선택사항)
    
    사용 예시:
    - python ai_keyword.py
    - python ai_keyword.py /path/to/articles
    - python ai_keyword.py /path/to/articles 1-10
    - python ai_keyword.py "" 1,3,5,7
    """
    print("🤖 AI 키워드 추출 시작...")
    print(f"🔍 현재 작업 디렉토리: {os.getcwd()}")
    print(f"📍 스크립트 위치: {__file__}")
    print(f"📁 프로젝트 루트 (paths.py): {paths.script_root}")
    print(f"📁 네이버 기사 디렉토리 (paths.py): {paths.naver_articles_dir}")
    
    # 필요한 디렉토리 생성
    setup_directories()
    
    # 명령행 인자 처리
    article_dir = None
    range_str = None
    
    if len(sys.argv) > 1:
        article_dir = sys.argv[1] if sys.argv[1] else None
        print(f"📥 전달받은 디렉토리: {article_dir}")
    
    if len(sys.argv) > 2:
        range_str = sys.argv[2] if sys.argv[2] else None
        print(f"🎯 전달받은 범위: {range_str}")
    
    # 범위를 고려한 기사 처리
    final_result = process_articles_with_range(article_dir, range_str)
    
    # JSON 형태로 결과 출력 (마커 제거, 크롤링 스크립트와 동일)
    print(json.dumps(final_result, ensure_ascii=False))
    
    print("🎉 처리 완료!")
    
    return final_result

if __name__ == "__main__":
    main()