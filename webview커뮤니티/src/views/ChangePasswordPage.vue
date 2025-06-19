<template>
  <div class="container">
    <h1>비밀번호 변경하기</h1>

    <input
      v-model="password"
      type="password"
      placeholder="새로운 비밀번호 입력"
      class="input"
    />
    <p v-if="showWarning" class="warning">
      8~14자의 영문, 숫자 및 특수문자 조합으로 입력하셔야 합니다.
    </p>

    <button @click="handleSubmit" class="button">
      비밀번호 변경하기
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const password = ref('')
const showWarning = ref(false)
const router = useRouter()

function validatePassword(pw) {
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])[A-Za-z\d!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]{8,14}$/
  return regex.test(pw)
}

function handleSubmit() {
  if (!validatePassword(password.value)) {
    showWarning.value = true
    return
  }

  showWarning.value = false
  alert('비밀번호가 변경되었습니다.')
  router.push('/login') // 메인 화면으로 이동 (경로는 프로젝트에 맞게 수정)
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

h1 {
  font-size: 20px;
  margin-bottom: 20px;
}

.input {
  box-sizing: border-box;
  width: 100%;
  padding: 10px;
  font-size: 14px;
  margin-bottom: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  max-width: 400px;
}

.warning {
  color: red;
  font-size: 12px;
  margin-bottom: 10px;
}

.button {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  background-color: white;
  border: 1px solid #999;
  border-radius: 5px;
  cursor: pointer;
  max-width: 400px;
}
</style> 