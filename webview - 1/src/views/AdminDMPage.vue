<template>
  <div class="container">
    <!-- 상단 바 -->
    <div class="top-bar">
      <button class="back-btn" @click="router.back()">←</button>
      <h2 class="title">모든 사용자에게<br />DM 보내기</h2>
    </div>

    <!-- 제목 -->
    <div class="input-group">
      <label>제목</label>
      <input v-model="title" placeholder="제목 입력" />
    </div>

    <!-- 내용 -->
    <div class="input-group">
      <label>내용</label>
      <textarea v-model="content" rows="6" placeholder="내용 입력"></textarea>
    </div>

    <!-- 이미지 첨부 -->
    <button class="attach-btn" @click="selectImage">이미지 첨부하기</button>
    <input type="file" ref="fileInput" @change="onImageChange" hidden />

    <!-- 전송 버튼 -->
    <button class="send-btn" @click="sendDM">발송하기</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const title = ref('')
const content = ref('')
const imageFile = ref(null)
const fileInput = ref(null)

const selectImage = () => {
  fileInput.value?.click()
}

const onImageChange = (e) => {
  const file = e.target.files[0]
  if (file) {
    imageFile.value = file
  }
}

const sendDM = async () => {
  if (!title.value || !content.value) {
    alert('제목과 내용을 모두 입력해주세요.')
    return
  }

  const formData = new FormData()
  formData.append('title', title.value)
  formData.append('content', content.value)
  if (imageFile.value) {
    formData.append('image', imageFile.value)
  }

  try {
    await axios.post('/api/admin/send-dm', formData)
    alert('모든 사용자에게 메시지를 발송했습니다.')
    router.push('/admin')
  } catch (err) {
    console.error(err)
    alert('발송 실패!')
  }
}
</script>

<style scoped>
.container {
  background-color: #dcf4f3;
  min-height: 100vh;
  padding: 20px;
  font-family: sans-serif;
}

.top-bar {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}
.back-btn {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  margin-right: 12px;
}
.title {
  font-size: 18px;
  font-weight: bold;
  flex: 1;
  text-align: center;
  margin-right: 24px;
  line-height: 1.3;
}

.input-group {
  margin-bottom: 16px;
}
.input-group label {
  display: block;
  font-weight: bold;
  margin-bottom: 6px;
}
.input-group input,
.input-group textarea {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: #f3f3f3;
}

.attach-btn,
.send-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 10px;
  background-color: white;
  border: 2px solid #444;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 10px;
}
</style>
