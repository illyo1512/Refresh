<template>
  <div class="container">
    <div class="top-bar">
      <button class="back-btn" @click="router.back()">←</button>
      <h2 class="title">프로필 수정</h2>
    </div>

    <!-- 프로필 이미지 -->
    <div class="profile-image-box">
      <label for="image-upload" class="image-label">
        <img :src="previewUrl || defaultImage" class="profile-image" />
        <input id="image-upload" type="file" @change="handleImageChange" hidden />
        <span class="camera-icon">📷</span>
      </label>
    </div>

    <!-- 정보 필드 -->
    <div class="input-group">
      <label>사용자명</label>
      <input v-model="nickname" readonly />
    </div>
    <div class="input-group">
      <label>이메일</label>
      <input :value="email" readonly />
    </div>
    <div class="input-group">
      <label>전화번호</label>
      <input :value="phone" readonly />
    </div>
    <div class="input-group">
      <label>생년월일</label>
      <input :value="birth" readonly />
    </div>

    <!-- 완료 버튼 -->
    <button class="submit-btn" @click="submitProfile">프로필 수정 완료</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()

// 상태 변수
const nickname = ref('')
const email = ref('')
const phone = ref('')
const birth = ref('')
const imageFile = ref(null)
const previewUrl = ref(null)
const defaultImage = '/default-profile.png'

// 프로필 이미지 업로드
const handleImageChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    imageFile.value = file
    previewUrl.value = URL.createObjectURL(file)
  }
}

// 프로필 수정
const submitProfile = async () => {
  const formData = new FormData()
  formData.append('nickname', nickname.value)
  if (imageFile.value) {
    formData.append('image', imageFile.value)
  }

  try {
    await axios.put('/api/profile', formData)
    alert('프로필이 성공적으로 수정되었습니다.')
    router.push('/mypage')
  } catch (err) {
    console.error(err)
    alert('프로필 수정 실패!')
  }
}

// 쿼리로 정보 가져오기
onMounted(() => {
  nickname.value = route.query.nickname || '홍길동'
  email.value = route.query.email || 'email@example.com'
  phone.value = route.query.phone || '010-0000-0000'
  birth.value = route.query.birth || '1990-01-01'
})
</script>

<style scoped>
.container {
  background-color: #e6f9fa;
  min-height: 100vh;
  padding: 20px;
}
.top-bar {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.back-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}
.title {
  font-size: 20px;
  font-weight: bold;
  flex: 1;
  text-align: center;
  margin-right: 24px;
}
.profile-image-box {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.image-label {
  cursor: pointer;
  position: relative;
}
.profile-image {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #aaa;
}
.camera-icon {
  position: absolute;
  bottom: 5px;
  right: 5px;
  background: white;
  border-radius: 50%;
  padding: 4px;
  font-size: 12px;
}
.input-group {
  margin-bottom: 16px;
}
.input-group label {
  font-weight: bold;
  margin-bottom: 4px;
  display: block;
}
.input-group input {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #eee;
}
.submit-btn {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: bold;
  background-color: #ddd;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}
</style>
