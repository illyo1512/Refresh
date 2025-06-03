from transformers import AutoTokenizer, AutoModelForTokenClassification, pipeline

# 모델 로딩
model_name = "monologg/koelectra-base-v3-naver-ner"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForTokenClassification.from_pretrained(model_name)

# 파이프라인 생성
ner = pipeline("ner", model=model, tokenizer=tokenizer, aggregation_strategy="none")