<template>
  <div class="container">
    <div class="top-bar">
      <button @click="router.back()" class="back-btn">←</button>
      <h2 class="title">설정</h2>
    </div>

    <div class="settings-box">
      <!-- 색약 색상 변경 -->
      <div class="setting-item">
        <label>색약 색상 변경</label>
        <label class="switch">
          <input type="checkbox" v-model="colorBlind" />
          <span class="slider"></span>
        </label>
      </div>

      <!-- 위험도 단계 -->
      <div class="setting-item">
        <label>위험도 단계별 알림</label>
        <select v-model="riskLevel">
          <option>매우위험</option>
          <option>위험</option>
          <option>보통</option>
          <option>안전</option>
          <option>매우안전</option>
        </select>
      </div>

      <!-- 야간모드 -->
      <div class="setting-item">
        <label>야간모드</label>
        <select v-model="nightMode">
          <option>자동</option>
          <option>켜기</option>
          <option>끄기</option>
        </select>
      </div>

      <!-- 언어 선택 -->
      <div class="setting-item">
        <label>언어 설정</label>
        <select v-model="language">
          <option>한국어</option>
          <option>English</option>
        </select>
      </div>

      <!-- 이용약관 -->
      <div class="terms">
        <button @click="showTerms">이용약관</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const colorBlind = ref(false)
const riskLevel = ref('보통')
const nightMode = ref('자동')
const language = ref('한국어')

const showTerms = () => {
  alert('이용약관 페이지로 이동합니다.')
  // router.push('/terms') 실제 이동 가능
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

.settings-box {
  background: white;
  border: 1px solid #ccc;
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  max-width: 400px;
  margin: 40px auto;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  font-size: 16px;
}

select {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 14px;
  background: #fff;
}

/* 토글 스위치 */
.switch {
  position: relative;
  display: inline-block;
  width: 42px;
  height: 24px;
}
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}
.slider {
  position: absolute;
  cursor: pointer;
  background-color: #ccc;
  border-radius: 24px;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  transition: .4s;
}
.slider:before {
  position: absolute;
  content: "";
  height: 18px;
  width: 18px;
  border-radius: 50%;
  background-color: white;
  left: 3px;
  bottom: 3px;
  transition: .4s;
}
input:checked + .slider {
  background-color: #6ca7ff;
}
input:checked + .slider:before {
  transform: translateX(18px);
}

.terms {
  text-align: center;
  margin-top: 30px;
}
.terms button {
  background: none;
  border: none;
  color: #333;
  text-decoration: underline;
  cursor: pointer;
}
</style>
