<template>
  <div class="container">
    <h2>아이디 찾기</h2>
    <p>이메일과 일치하는 아이디입니다</p>

    <!-- 클릭 가능한 아이디 -->
    <p class="result-id" @click="goToLogin">
      <span class="clickable-id">{{ userId }}</span>
    </p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/jwt' // axios 인스턴스

const router = useRouter()
const route = useRoute()

const userId = ref('')

const goToLogin = () => {
  router.push('/login')
}

onMounted(async () => {
  const emailId = route.query.id

  try {
    const res = await api.get(`/auth/found-id?id=${emailId}`)

    if (res.data?.id) {
      userId.value = res.data.id
    } else {
      userId.value = '아이디를 찾을 수 없습니다'
    }
  } catch (err) {
    console.error(err)
    userId.value = '오류가 발생했습니다'
  }
})
</script>

<style scoped>
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.result-id {
  margin-top: 12px;
}

.clickable-id {
  font-weight: bold;
  color: #007bff;
  font-size: 18px;
  cursor: pointer;
  text-decoration: underline;
}
</style>
