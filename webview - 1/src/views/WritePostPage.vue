<template>
  <div class="container">
    <div class="top-bar">
      <button @click="router.back()" class="back-btn">×</button>
      <h2 class="title">글 쓰기</h2>
    </div>

    <div class="form">
      <!-- 제목 입력 -->
      <input v-model="title" class="input title-input" placeholder="제목" />

      <!-- 지역 선택 -->
      <div class="select-row">
        <select v-model="sido" class="select">
          <option disabled value="">시</option>
          <option>서울</option>
          <option>부산</option>
        </select>
        <select v-model="gugun" class="select">
          <option disabled value="">도</option>
          <option>강남</option>
          <option>해운대</option>
        </select>
      </div>

      <!-- 글 내용 입력 -->
      <textarea v-model="content" class="textarea" placeholder="글내용 어쩌고 저쩌고"></textarea>

      <!-- 이미지 첨부 -->
      <div class="image-upload">
        <input type="file" @change="handleFileChange" />
        <span>이미지 첨부하기</span>
      </div>

      <!-- 경로 불러오기 -->
      <button class="btn" @click="getPath">경로 가져오기</button>

      <!-- 작성 완료 -->
      <button class="btn" @click="submitPost">글 작성 완료하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const title = ref('')
const content = ref('')
const sido = ref('')
const gugun = ref('')
const imageFile = ref(null)
const pathInfo = ref('') // 저장된 경로 정보

const handleFileChange = (e) => {
  imageFile.value = e.target.files[0]
}

const getPath = async () => {
  try {
    // 예시: 사용자 저장 경로 요청
    const res = await axios.get('/api/user/paths')
    pathInfo.value = res.data.path
    alert('경로가 불러와졌습니다!')
  } catch (err) {
    console.error(err)
    alert('경로를 불러오는 데 실패했습니다.')
  }
}

const submitPost = async () => {
  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('content', content.value)
  formData.append('sido', sido.value)
  formData.append('gugun', gugun.value)
  formData.append('path', pathInfo.value)
  if (imageFile.value) {
    formData.append('image', imageFile.value)
  }

  try {
    await axios.post('/api/posts', formData)
    alert('작성 완료!')
    router.push('/mypage') // 작성 후 이동
  } catch (err) {
    console.error(err)
    alert('작성 실패!')
  }
}
</script>

<style scoped>
.container {
  background-color: #e6f5f8;
  min-height: 100vh;
  padding: 20px;
}

.top-bar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.back-btn {
  font-size: 28px;
  border: none;
  background: none;
  cursor: pointer;
  margin-right: 10px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.form {
  background: white;
  padding: 16px;
  border-radius: 12px;
}

.input,
.select,
.textarea {
  width: 100%;
  margin-bottom: 12px;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

.title-input {
  font-weight: bold;
}

.select-row {
  display: flex;
  gap: 10px;
}

.textarea {
  height: 150px;
  resize: none;
}

.image-upload {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.btn {
  width: 100%;
  padding: 12px;
  border: none;
  background-color: #ddd;
  border-radius: 10px;
  font-weight: bold;
  margin-bottom: 10px;
  cursor: pointer;
}
</style>
