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
      
      <div class="image-wrapper">
        <img :src="'/경로추천.png'" alt="경로 이미지" />
      </div>
    </div>

    <div class="file-upload">
      <input type="file" @change="handleImageUpload" />
      <span>이미지 첨부하기</span>
    </div>

    <!-- 이미지 미리보기 -->
    <div v-if="previewUrl" class="image-preview">
      <img :src="previewUrl" alt="미리보기" />
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
const previewUrl = ref(null)
const selectedRoute = ref(null)

// 컴포넌트가 마운트될 때 기존 입력값 & 선택된 경로 복원
onMounted(() => {
  const routeData = localStorage.getItem('selectedRoute')
  if (routeData) {
    selectedRoute.value = JSON.parse(routeData)
    localStorage.removeItem('selectedRoute')
  }

  title.value = localStorage.getItem('tempTitle') || ''
  content.value = localStorage.getItem('tempContent') || ''
  region1.value = localStorage.getItem('tempRegion1') || ''
  region2.value = localStorage.getItem('tempRegion2') || ''
  localStorage.removeItem('tempTitle')
  localStorage.removeItem('tempContent')
  localStorage.removeItem('tempRegion1')
  localStorage.removeItem('tempRegion2')
})

const goBack = () => {
  router.push('/route_board')
}

const handleImageUpload = (e) => {
  const file = e.target.files[0]
  if (file) {
    imageFile.value = file
    previewUrl.value = URL.createObjectURL(file)
  }
}

const importRoute = () => {
  // 입력값을 localStorage에 임시 저장
  localStorage.setItem('tempTitle', title.value)
  localStorage.setItem('tempContent', content.value)
  localStorage.setItem('tempRegion1', region1.value)
  localStorage.setItem('tempRegion2', region2.value)

  router.push('/savedroute')
}

const submitPost = async () => {
  const nickname = localStorage.getItem('nickname') || '익명'

  if (!title.value || !content.value) {
    alert('제목과 내용을 입력해주세요')
    return
  }

  const newPost = {
    routeBoardId: Date.now(),
    title: title.value,
    content: content.value,
    createdAt: new Date().toISOString(),
    viewCount: 0,
    likeCount: 0,
    boardStatus: 0,
    selfRouteId: selectedRoute.value.selfRouteId,
  }

  try {
    const data = JSON.stringify(newPost)

    await axios.post('/api/boards', data, {
  headers: {
    'Content-Type': 'application/json',
  },
})


    const existingPosts = JSON.parse(localStorage.getItem('posts')) || []
    existingPosts.unshift(newPost)
    localStorage.setItem('posts', JSON.stringify(existingPosts))

    // 입력값 임시 저장 제거
    localStorage.removeItem('tempTitle')
    localStorage.removeItem('tempContent')
    localStorage.removeItem('tempRegion1')
    localStorage.removeItem('tempRegion2')

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
.image-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}
.image-wrapper img {
  max-width: 100%;
  border-radius: 8px;
  border: 1px solid #ccc;
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
.image-preview {
  margin-bottom: 10px;
  text-align: center;
}
.image-preview img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 10px;
  border: 1px solid #999;
}
</style>
