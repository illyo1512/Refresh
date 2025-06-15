<!-- 글 수정 -->

<template>
  <div class="container">
    <div class="top-bar">
      <button class="back-btn" @click="goBack">\u2715</button>
      <h2 class="title">글 수정</h2>
    </div>

    <div class="form-box">
      <input v-model="title" placeholder="제목" class="input" />

      <div class="location-select">
        <select v-model="city">
          <option disabled value="">시</option>
          <option>서울</option>
          <option>부산</option>
        </select>
        <select v-model="district">
          <option disabled value="">도</option>
          <option>강남구</option>
          <option>해운대구</option>
        </select>
      </div>

      <textarea v-model="content" placeholder="본문 내용을 입력하세요" class="textarea"></textarea>

      <button class="btn" @click="goToRoute">경로 수정하기</button>
      <button class="btn" @click="handleUpdate">글 수정 완료하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const route = useRoute()
const postId = route.params.id

const title = ref('')
const content = ref('')
const city = ref('')
const district = ref('')
const selectedRouteId = ref(null)

const handleUpdate = async () => {
  try {
    await axios.put(`/api/post/${postId}`, {
      title: title.value,
      content: content.value,
      city: city.value,
      district: district.value,
      routeId: selectedRouteId.value,
    })
    alert('\u2705 글 수정이 완료되었습니다!')
    router.push('/board')
  } catch (error) {
    console.error(error)
    alert('\u274C 수정 실패: 다시 시도해주세요.')
  }
}

const goBack = () => router.back()
const goToRoute = () => router.push('/selectroute')

onMounted(async () => {
  try {
    const res = await axios.get(`/api/post/${postId}`)
    const data = res.data
    title.value = data.title
    content.value = data.content
    city.value = data.city
    district.value = data.district
    selectedRouteId.value = data.routeId
  } catch (err) {
    console.error('글 로드 실패', err)
  }
})
</script>

<style scoped>
.container {
  background-color: #e5fafa;
  min-height: 100vh;
  padding: 16px;
}
.top-bar {
  display: flex;
  align-items: center;
  padding: 10px;
}
.back-btn {
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
}
.title {
  flex: 1;
  text-align: center;
  font-weight: bold;
}
.form-box {
  margin-top: 20px;
}
.input,
select,
.textarea {
  width: 100%;
  padding: 10px;
  margin-bottom: 12px;
  border-radius: 10px;
  border: 1px solid #ccc;
}
.location-select {
  display: flex;
  gap: 10px;
}
.textarea {
  height: 180px;
  resize: none;
}
.btn {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  background-color: white;
  border: 1px solid #999;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
}
</style>
