<template>
  <div class="base">
    <div class="top-bar">
      <button @click="goBack" class="back-btn">←</button>
      <h2 class="title">회원가입</h2>
    </div>

    <div class="container">
      <!-- 약관 동의 섹션 -->

      <div class="terms-box">
        <div class="terms-content">
          약관동의 어쩌고 저쩌고 내용이 여기에 들어갑니다.
        </div>

        <div class="terms-checks">
          <label>
            <input type="radio" value="agree" v-model="termsAgreement" />
            동의
          </label>
          <label>
            <input type="radio" value="disagree" v-model="termsAgreement" />
            비동의
          </label>
        </div>
       
      </div>

      <!-- 아이디 -->
      <div class="input-group">
        <input v-model="id" placeholder="아이디" />
        <div>
          <button @click="checkId">중복 확인</button>
        </div>
      </div>
      <p v-if="idChecked" class="check-msg">사용 가능한 아이디입니다.</p>
      <p v-if="idError" class="error-msg">이미 사용 중인 아이디입니다.</p>

      <!-- 비밀번호 -->
      <input v-model="password" type="password" placeholder="비밀번호" class="input" />
      <p v-if="pwFormatError" class="error-msg">
        8~14자의 영문, 숫자 및 특수문자 조합으로 입력해야 합니다.
      </p>
      <input v-model="confirmPassword" type="password" placeholder="비밀번호 확인" class="input" />
      <p v-if="pwMatchError" class="error-msg">비밀번호가 일치하지 않습니다.</p>

      <!-- 이메일 -->
      <input v-model="email" type="email" placeholder="이메일" class="input" />

      <!-- 사용자명 -->
      <input v-model="username" type="text" placeholder="사용자명" class="input" />

      <!-- 전화번호 -->
      <input v-model="phone" type="tel" placeholder="전화번호" class="input" />

      <!-- 성별 -->
      <select v-model="gender" class="input">
        <option disabled value="">성별 선택</option>
        <option>남자</option>
        <option>여자</option>
      </select>

      <!-- 생년월일 -->
      <div class="birth-group">
        <input v-model="year" placeholder="연" />
        <input v-model="month" placeholder="월" />
        <input v-model="day" placeholder="일" />
      </div>

      <!-- 완료 -->
      <button class="submit-btn" @click="handleSubmit">완료</button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const id = ref('')
const idChecked = ref(false)
const idError = ref(false)
const password = ref('')
const confirmPassword = ref('')
const pwFormatError = ref(false)
const pwMatchError = ref(false)
const email = ref('')
const username = ref('')
const phone = ref('')
const gender = ref('')
const year = ref('')
const month = ref('')
const day = ref('')

const termsAgreement = ref('')
const showTermsError = ref(false)

const handleSubmit = () => {
 
  alert(`🎉 ${username.value}님, 가입을 축하드립니다!`)
  // router.push('/main')
}

const checkId = () => {
  if (id.value === 'testuser') {
    idChecked.value = false
    idError.value = true
  } else {
    idChecked.value = true
    idError.value = false
  }
}

const validatePassword = (value) => {
  const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,14}$/
  return regex.test(value)
}

watch(password, (val) => {
  pwFormatError.value = !validatePassword(val)
})

watch([password, confirmPassword], ([pw, conf]) => {
  pwMatchError.value = pw !== conf
})

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
  max-width: 400px;
  margin: 0 auto;
  padding: 30px 20px;
  border-radius: 12px;
  font-family: sans-serif;
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

.terms-box {
  background: white;
  padding: 16px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.terms-content {
  height: 100px;
  overflow-y: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  font-size: 14px;
  color: #333;
}

.terms-checks {
  display: flex;
  gap: 20px;
  font-size: 14px;
}
.terms-checks input{
  width: 2em;
}
.input-group {
  position: relative;
  margin-bottom: 8px;
  align-items: center;
}

.input-group input {
  margin: 0;
}
.input-group button {
  margin: 0 8px;
  pointer-events: all;
}
.input-group div {
  pointer-events: none;
  left: 0;
  top: 0;
  position: absolute;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

input,
select {
  box-sizing: border-box;
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border-radius: 8px;
  border: 1px solid #ccc;
}

.check-msg {
  color: blue;
  font-size: 13px;
  margin: 0;
  margin-top: -8px;
  margin-bottom: 16px;
}

.birth-group {
  display: flex;
  justify-content: space-between;
  gap: 8px;
}

.birth-group input {
  flex: 1;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  font-weight: bold;
  background-color: white;
  border: 2px solid #444;
  border-radius: 10px;
  cursor: pointer;
}
.error-msg {
  color: red;
  font-size: 13px;
  margin: 0;
  margin-top: -8px;
  margin-bottom: 16px;
}
</style>
