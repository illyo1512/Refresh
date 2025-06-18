from transformers import AutoTokenizer, AutoModelForTokenClassification, pipeline

# 모델 로딩
model_name = "monologg/koelectra-base-v3-naver-ner"

# 토크나이저에 설정 추가
tokenizer = AutoTokenizer.from_pretrained(
    model_name,
    model_max_length=512,  # 최대 길이 설정
    truncation=True,
    padding=True
)

model = AutoModelForTokenClassification.from_pretrained(model_name)
