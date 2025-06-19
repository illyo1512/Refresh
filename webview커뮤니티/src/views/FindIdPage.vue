<template>
  <div class="base">
    <div class="top-bar">
    <button @click="goBack" class="back-btn">←</button>
    <h2 class="title">아이디찾기</h2>
  </div>
  <div class="container">
    <input
      v-model="email"
      placeholder="이메일 입력"
      class="input"
    />
    <p v-if="emailError" class="error-msg">이메일이 일치하지 않습니다.</p>
    <button @click="findId" class="btn">아이디 찾기</button>
  </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const emailError = ref(false)

// 예시 이메일-아이디 매핑 데이터 (실제 구현 시 API 연동)
const userDB = {
  'abc@example.com': 'abcd1234',
  'hello@world.com': 'hello777'
}

const findId = () => {
  if (userDB[email.value]) {
    // 라우터 이동하면서 아이디를 props로 전달
    router.push('/foundid', {
      query: { id: userDB[email.value] }
    })
  } else {
    emailError.value = true
  }
}
function goBack() {
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

.input {
  box-sizing: border-box;
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 8px;
  max-width: 400px;
}

.btn {
  padding: 10px 20px;
  border: 1px solid black;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}

.error-msg {
  color: red;
  font-size: 14px;
}
</style>
