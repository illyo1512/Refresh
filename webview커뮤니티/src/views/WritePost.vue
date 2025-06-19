<template>
  <div class="container">
    <div class="top-bar">
      <button class="close-btn" @click="goBack">✕</button>
    </div>

    <input v-model="title" class="title-input" placeholder="제목" />

    <div class="location-select">
      <select v-model="region1">
        <option disabled value="">시</option>
        <option>서울</option>
        <option>광주</option>
      </select>
      <select v-model="region2">
        <option disabled value="">도</option>
        <option>강남구</option>
        <option>광산구</option>
      </select>
    </div>

    <textarea v-model="content" class="content-input" placeholder="글내용 어쩌고 저쩌고"></textarea>

    <!-- 선택한 경로 시각화 -->
    <div v-if="selectedRoute" class="route-preview">
      <p><strong>선택한 경로명:</strong> {{ selectedRoute.routeName }}</p>
      <p>경유지 수: {{ selectedRoute.routeResult?.waypoints?.length || 0 }}</p>
      <div class="map-preview">[지도 자리]</div>
    </div>

    <div class="file-upload">
      <input type="file" @change="handleImageUpload" />
      <span>이미지 첨부하기</span>
    </div>

    <button class="route-btn" @click="importRoute">경로 가져오기</button>
    <button class="submit-btn" @click="submitPost">글 작성 완료하기</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const title = ref('')
const content = ref('')
const region1 = ref('')
const region2 = ref('')
const imageFile = ref(null)

const selectedRoute = ref(null)

// 페이지 진입 시 localStorage에서 선택된 경로 불러오기
onMounted(() => {
  const routeData = localStorage.getItem('selectedRoute')
  if (routeData) {
    selectedRoute.value = JSON.parse(routeData)
  }
})

const goBack = () => {
  router.back()
}

const handleImageUpload = (e) => {
  imageFile.value = e.target.files[0]
}

// 경로 선택 화면으로 이동
const importRoute = () => {
  router.push('/selectedRoute')
}

// 글 작성 제출
const submitPost = async () => {
  const nickname = localStorage.getItem('nickname') || '익명'

  const newPost = {
    route_board_id: Date.now(),
    title: title.value,
    content: content.value,
    region1: region1.value,
    region2: region2.value,
    created_at: new Date().toISOString(),
    nickname,
    view_count: 0,
    like_count: 0,
    comment_count: 0,
    isHot: false,
    routeInfo: selectedRoute.value, // 선택된 경로도 저장
  }

  try {
    const formData = new FormData()
    formData.append('title', title.value)
    formData.append('content', content.value)
    formData.append('region1', region1.value)
    formData.append('region2', region2.value)
    formData.append('nickname', nickname)
    if (selectedRoute.value) {
      formData.append('routeInfo', JSON.stringify(selectedRoute.value))
    }
    if (imageFile.value) {
      formData.append('image', imageFile.value)
    }

    await axios.post('/api/route-board', formData)

    const existingPosts = JSON.parse(localStorage.getItem('posts')) || []
    existingPosts.unshift(newPost)
    localStorage.setItem('posts', JSON.stringify(existingPosts))

    alert('글 작성이 완료되었습니다.')
    router.push('/route-board')
  } catch (err) {
    alert('글 작성 실패! 서버 오류 또는 통신 오류')
    console.error(err)
  }
}
</script>

<style scoped>
.container {
  background-color: #dbf0f0;
  min-height: 100vh;
  padding: 20px;
}
.top-bar {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 10px;
}
.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
}
.title-input,
.location-select select,
.content-input,
.file-upload,
.route-btn,
.submit-btn {
  width: 100%;
  box-sizing: border-box;
}
.title-input,
.location-select select,
.content-input {
  padding: 12px;
  font-size: 16px;
  border-radius: 8px;
  border: 1px solid #999;
  margin-bottom: 10px;
}
.location-select {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}
.content-input {
  height: 180px;
}
.file-upload {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border: 1px solid #999;
  border-radius: 8px;
  margin-bottom: 10px;
  font-size: 14px;
  background-color: #fff;
}
.file-upload input {
  flex: 1;
}
.route-btn,
.submit-btn {
  padding: 12px;
  margin-bottom: 10px;
  font-size: 16px;
  background-color: white;
  border: 1px solid #444;
  border-radius: 10px;
  cursor: pointer;
}
.route-preview {
  padding: 12px;
  border: 1px solid #aaa;
  border-radius: 8px;
  margin-bottom: 10px;
  background-color: #ffffff;
}
.map-preview {
  background-color: #ddd;
  height: 160px;
  border-radius: 6px;
  margin-top: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
