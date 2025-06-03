# 범죄 키워드 ,""으로 추가
crime_keywords = ["살인", "강도", "성폭행", "폭행", "절도", "납치", "방화", "강간", "흉기", "협박"]

def extract_crime_keyword(text):
    return next((kw for kw in crime_keywords if kw in text), "범죄")

def clean_word(word):
    return word.replace("##", "").strip()

def extract_location_single(ner_results):
    location = next(
        (clean_word(e['word']) for e in ner_results
         if e.get('entity', '').startswith(("LOC", "ORG")) and e.get('score', 1.0) > 0.8),
        "어딘가"
    )
    return location

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

    return f"{location}에서 {time}일에 {crime}이 발생했습니다."

def process_text_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        text = f.read()

    text = text[:1000]
    ner_results = ner(text)
    return build_alert_sentence(text, ner_results)

if __name__ == "__main__":
    file_path = "/content/sample_news.txt"
    result = process_text_file(file_path)
    print(result)
