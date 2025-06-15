import os
from transformers import pipeline

# 파이프라인 생성
ner = pipeline("ner", model=model, tokenizer=tokenizer, aggregation_strategy="none")

# ✅ 범죄 키워드 목록
crime_keywords = ["살인", "강도", "성폭행", "폭행", "절도", "납치", "방화", "강간", "흉기", "협박", "칼부림"]

def extract_crime_keyword(text):
    return next((kw for kw in crime_keywords if kw in text), None)

def clean_word(word):
    return word.replace("##", "").strip()

def extract_location_single(ner_results):
    return next(
        (clean_word(e['word']) for e in ner_results
         if e.get('entity', '').startswith(("LOC", "ORG")) and e.get('score', 1.0) > 0.8),
        "어딘가"
    )

def build_alert_sentence(text, ner_results):
    location = extract_location_single(ner_results)
    time = next(
        (clean_word(e['word']) for e in ner_results
         if e.get('entity', '').startswith(("DAT", "TIM")) and e.get('score', 1.0) > 0.8),
        "어느 시각"
    )
    crime = extract_crime_keyword(text)
    if crime in ["흉기", "칼부림"]:
        crime += " 사건"
    return f"{location}에서 {time}일에 {crime}이(가) 발생했습니다."

def process_article_group(article_dir, number_part):
    url_path = os.path.join(article_dir, f"{number_part}_url.txt")
    title_path = os.path.join(article_dir, f"{number_part}_title.txt")
    article_path = os.path.join(article_dir, f"{number_part}_article.txt")

    if not all(os.path.exists(p) for p in [url_path, title_path, article_path]):
        print(f"❌ 누락된 파일 있음: {number_part}")
        return

    with open(article_path, 'r', encoding='utf-8') as f:
        article_text = f.read()[:500]

    keyword = extract_crime_keyword(article_text)
    if keyword is None:
        print(f"⏩ 스킵 (범죄 키워드 없음): {number_part}")
        return

    ner_results = ner(article_text)
    result = build_alert_sentence(article_text, ner_results)

    with open(os.path.join(article_dir, f"{number_part}_key.txt"), 'w', encoding='utf-8') as f:
        f.write(result)
    print(f"✅ 처리 완료: {number_part}_key.txt")

def process_all_articles(article_dir="/content/naver_articles"): #저장된 파일경로 변수로 바꾸면될듯?
    filenames = os.listdir(article_dir)
    number_prefixes = sorted(set(f.split("_")[0] for f in filenames if "_" in f and f.endswith(".txt")))

    for number_part in number_prefixes:
        if number_part.isdigit():
            process_article_group(article_dir, number_part)

# ✅ 실행
if __name__ == "__main__":
    process_all_articles()
