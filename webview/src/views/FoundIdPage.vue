<template>
  <div class="container">
    <h2>아이디 찾기</h2>
    <p>이메일과 일치하는 아이디입니다</p>

    <!-- 클릭 가능한 아이디 -->
    <p class="result-id" @click="goToLogin">
      <span class="clickable-id">abcd1234</span>
    </p>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}
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
