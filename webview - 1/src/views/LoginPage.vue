<template>
  <div class="container">
    <div class="top-bar">
    <button @click="router.push('/map')" class="back-btn">←</button>
    <h2 class="title">로그인</h2>
  </div>

    <!-- 로그인 박스 -->
    <div class="login-box">
      <input
        v-model="username"
        placeholder="아이디"
        class="input"
      />
      <input
        v-model="password"
        type="password"
        placeholder="비밀번호"
        class="input"
      />

      <button class="login-button" @click="handleLogin">
        로그인
      </button>

      <div class="action-buttons">
        <button @click="goTo('findId')">
          아이디 찾기
        </button>
        <button @click="goTo('findPw')">
          비밀번호 찾기
        </button>
        <button @click="goTo('signup')">
          회원가입
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '@/jwt'


const username = ref('')
const password = ref('')

const handleLogin = async () => {
  if (!username.value || !password.value) {
    alert('아이디와 비밀번호를 모두 입력하세요.')
    return
  }

  try {
    const response = await api.post('/auth/login', {
      username: username.value,
      password: password.value,
    })

    const token = response.data.token // 백엔드 응답에 따라 수정 필요
    localStorage.setItem('token', token)

    alert('로그인 성공!')
    router.push('/map')
  } catch (err) {
    console.error(err)
    alert('로그인 실패: 아이디 또는 비밀번호를 확인하세요.')
  }
}

import { useRouter } from 'vue-router'

const router = useRouter()

const goTo = (page) => {
  switch (page) {
    case 'findId':
      router.push('/findid')
      break
    case 'findPw':
      router.push('/findpassword')
      break
    case 'signup':
      router.push('/register')
      break
  }
}


</script>

<style scoped>
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
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
  margin-right: 32px; /* ← back-btn 공간 보정용 */
}

.login-box {
  background: white;
  border: 1px solid #ccc;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
  margin-top: 40px;
}

.input {
  box-sizing: border-box;
  width: 100%;
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
}

.login-button {
  width: 100%;
  background-color: #e0e0e0;
  padding: 12px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 16px;
  border: none;
  margin-bottom: 20px;
  cursor: pointer;
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  text-align: center;
}

.action-buttons button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #444;
}

.circle {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 1px solid #888;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4px;
  font-size: 12px;
}
</style>
