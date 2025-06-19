<template>
  <div class="container">
    <!-- 상단바: 뒤로가기 버튼과 제목 -->
    <div class="top-bar">
      <button @click="router.push('/map')" class="back-btn">←</button>
      <h2 class="title">로그인</h2>
    </div>

    <!-- 로그인 입력 영역 박스 -->
    <div class="login-box">
      <!-- 아이디 입력 -->
      <input
        v-model="id"
        placeholder="아이디"
        class="input"
      />
      <!-- 비밀번호 입력 -->
      <input
        v-model="password"
        type="password"
        placeholder="비밀번호"
        class="input"
      />

      <!-- 로그인 버튼 -->
      <button class="login-button" @click="handleLogin">
        로그인
      </button>

      <!-- 부가 기능: 아이디 찾기, 비밀번호 찾기, 회원가입 -->
      <div class="action-buttons">
        <button @click="goTo('findId')">아이디 찾기</button>
        <button @click="goTo('findPw')">비밀번호 찾기</button>
        <button @click="goTo('signup')">회원가입</button>
      </div>
    </div>
  </div>
</template>

<script setup>
// Composition API로 구성
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/jwt.js' // 로그인용 Axios 인스턴스

const router = useRouter()

// 사용자 입력값 상태
const id = ref('')
const password = ref('')

// 로그인 실행 함수
const handleLogin = async () => {
  if (!id.value || !password.value) {
    alert('아이디와 비밀번호를 모두 입력하세요.')
    return
  }

  try {
    // 로그인 요청
    const response = await api.post('/api/auth/login', {
      id: id.value,
      password: password.value,
    })

    const { userId, nickname } = response.data

    if (!userId) {
      alert('로그인 응답에 사용자 정보가 없습니다.')
      return
    }

    // 로그인 성공 시 로컬스토리지에 저장
    localStorage.setItem('userId', userId)
    localStorage.setItem('nickname', nickname)

    alert(`${nickname}님, 로그인 성공!`)
    router.push('/map') // 로그인 후 지도 페이지로 이동
  } catch (err) {
    console.error(err)
    alert('로그인 실패: 아이디 또는 비밀번호를 확인하세요.')
  }
}

// 페이지 이동용 함수 (아이디찾기, 비번찾기, 회원가입)
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
/* 전체 화면 기본 배경 */
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
}

/* 상단바 레이아웃 */
.top-bar {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  border-bottom: 1px solid #ddd;
  position: relative;
  height: 56px;
}

/* ← 뒤로가기 버튼 */
.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 12px;
}

/* 로그인 텍스트 제목 */
.title {
  font-size: 18px;
  font-weight: bold;
  flex: 1;
  text-align: center;
  margin-right: 32px;
}

/* 로그인 입력 영역 박스 */
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

/* 입력 필드 스타일 */
.input {
  box-sizing: border-box;
  width: 100%;
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
}

/* 로그인 버튼 */
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

/* 아이디 찾기, 비밀번호 찾기, 회원가입 버튼 영역 */
.action-buttons {
  display: flex;
  justify-content: space-between;
  text-align: center;
}

/* 개별 버튼 공통 스타일 */
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

</style>
