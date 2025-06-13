<template>
  <div class="base">
    <div class="top-bar">
      <button @click="goBack" class="back-btn">←</button>
      <h2 class="title">회원가입</h2>
    </div>
    <div class="container">
      <input v-model="email" placeholder="이메일" class="input" />
      <input v-model="userId" placeholder="아이디" class="input" />
      <p v-if="error" class="error-msg">이메일이나 아이디가 일치하지 않습니다.</p>
      <button class="btn" @click="handleCheck">이메일 인증하기</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/jwt' // axios 인스턴스

const router = useRouter()

const email = ref('')
const userId = ref('')
const error = ref(false)

const handleCheck = async () => {
  error.value = false

  try {
    const res = await api.post('/auth/verify-user', {
      email: email.value,
      id: userId.value
    })

    if (res.data.success) {
      alert('입력하신 이메일로 비밀번호 재설정 링크를 전송했습니다.')
    } else {
      error.value = true
    }
  } catch (err) {
    console.error(err)
    error.value = true
  }
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.base {
  background-color: #e0f7fa;
  min-height: 100vh;
}

.container {
  min-height: 100vh;
  padding: 40px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 400px;
  margin: 0 auto;
}

.top-bar {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  border-bottom: 1px solid #ddd;
  position: relative;
  height: 56px;
}

.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 12px;
}

.title {
  font-size: 18px;
  font-weight: bold;
  flex: 1;
  text-align: center;
  margin-right: 32px;
}

.input {
  box-sizing: border-box;
  width: 100%;
  padding: 12px;
  margin-bottom: 10px;
  border-radius: 10px;
  border: 1px solid #ccc;
  font-size: 16px;
}

.btn {
  padding: 10px 20px;
  border: 1px solid #333;
  background: white;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.error-msg {
  color: red;
  font-size: 14px;
  margin-bottom: 10px;
}
</style>
