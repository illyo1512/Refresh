import requests
from bs4 import BeautifulSoup
from fake_useragent import UserAgent

ua = UserAgent()
headers = {"User-Agent": ua.random}

url = "https://news.naver.com/breakingnews/section/102/249" #크롤링 대상사이트

# HTML 요청
response = requests.get(url, headers=headers)
response.raise_for_status()

soup = BeautifulSoup(response.text, "lxml")

news_tags = soup.select("a[href^='https://n.news.naver.com/']")


for tag in news_tags:
    title = tag.get_text(strip=True)
    link = tag.get("href")
    if title and link:
        print(f"[{title}]({link})")