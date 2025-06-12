import requests
from bs4 import BeautifulSoup
import os

# 코드요약 url만 한번 쭉 받아오기 -> 안의 본문 내용 받아오기  제목 url 본문 따로 저장 txt파일로 되게해둔거 리스트로 수정필요


# ✅ 마지막 크롤링한 최신 기사 URL 로딩
LAST_URL_FILE = "/content/last_crawled_url.txt"
def load_last_url():
    if os.path.exists(LAST_URL_FILE):
        with open(LAST_URL_FILE, 'r', encoding='utf-8') as f:
            return f.read().strip()
    return None

def save_last_url(url):
    with open(LAST_URL_FILE, 'w', encoding='utf-8') as f:
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

# ✅ 전체 실행 및 저장

def run_and_save_articles():
    base_url = "https://news.naver.com/breakingnews/section/102/249"
    last_url = load_last_url()

    articles = fetch_article_urls(base_url, last_url=last_url)
    if not articles:
        print("🚫 새 기사 없음")
        return

    save_dir = "/content/naver_articles"
    os.makedirs(save_dir, exist_ok=True)

    # 최신 URL 저장
    save_last_url(articles[0][0])

    for idx, (url, title) in enumerate(articles):
        number_part = str(idx + 1)

        print(f"[{number_part}] Fetching: {url}")
        body = fetch_article_body(url)

        # ✅ 파일 저장
        with open(os.path.join(save_dir, f"{number_part}_url.txt"), 'w', encoding='utf-8') as f:
            f.write(url)
        with open(os.path.join(save_dir, f"{number_part}_title.txt"), 'w', encoding='utf-8') as f:
            f.write(title)
        with open(os.path.join(save_dir, f"{number_part}_article.txt"), 'w', encoding='utf-8') as f:
            f.write(body)

        print(f"✅ 저장 완료: {number_part}_*.txt")

# ✅ 실행
if __name__ == "__main__":
    run_and_save_articles()
