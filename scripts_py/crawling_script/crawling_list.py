import requests
from bs4 import BeautifulSoup
import os
import sys
from pathlib import Path

# 경로 설정을 위해 config 디렉토리를 Python path에 추가
sys.path.append(str(Path(__file__).parent.parent))
from config.paths import paths, setup_directories

# ✅ headers 설정
headers = {
    'User-Agent': 'Mozilla/5.0'
}

# ✅ 마지막 크롤링한 최신 기사 URL 로딩
def load_last_url():
    if paths.last_crawled_url_file.exists():
        with open(paths.last_crawled_url_file, 'r', encoding='utf-8') as f:
            return f.read().strip()
    return None

def save_last_url(url):
    # 디렉토리가 없으면 생성
    paths.ensure_dir_exists(paths.crawling_data_dir)
    with open(paths.last_crawled_url_file, 'w', encoding='utf-8') as f:
        f.write(url.strip())

# ✅ 기사 URL 수집 함수 (이전 URL 도달 시 중단)
def fetch_article_urls(base_url, last_url=None, max_count=30):
    response = requests.get(base_url, headers=headers)
    soup = BeautifulSoup(response.text, 'html.parser')

    article_links = []
    article_titles = []

    for a_tag in soup.select('a.sa_text_title'):
        href = a_tag.get('href')
        title = a_tag.get_text(strip=True)
        if href and href.startswith("https://"):
            if href == last_url:
                print("🛑 마지막 크롤링 위치 도달, 중단")
                break
            article_links.append(href)
            article_titles.append(title)
        if len(article_links) >= max_count:
            break

    return list(zip(article_links, article_titles))

# ✅ 본문 수집 함수
def fetch_article_body(url):
    try:
        res = requests.get(url, headers=headers)
        soup = BeautifulSoup(res.text, 'html.parser')

        content = soup.find("div", class_="go_trans _article_content")
        if not content:
            content = soup.find("article")

        if content:
            return content.get_text(separator='\n', strip=True)
        else:
            return "❌ 본문을 찾을 수 없음"
    except Exception as e:
        return f"❌ 오류: {e}"

# ✅ 스프링부트 호출용 함수 (리스트 반환)
def run_and_return_articles():
    # 필요한 디렉토리 초기화
    setup_directories()
    
    base_url = "https://news.naver.com/breakingnews/section/102/249"
    last_url = load_last_url()

    articles = fetch_article_urls(base_url, last_url=last_url)
    if not articles:
        return []

    # 최신 URL 저장
    save_last_url(articles[0][0])

    result = []
    for idx, (url, title) in enumerate(articles):
        body = fetch_article_body(url)
        result.append({
            "number": idx + 1,
            "url": url,
            "title": title,
            "content": body
        })

    return result

# ✅ 테스트용 standalone 실행
def main():
    articles = run_and_return_articles()
    for a in articles:
        print("=" * 60)
        print(f"[{a['number']}] {a['title']}")
        print(a['url'])
        print(a['content'][:300], "...")

if __name__ == "__main__":
    main()
