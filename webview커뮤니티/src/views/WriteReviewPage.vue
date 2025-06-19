<template>
  <div class="container">
    <div class="top-bar">
    <button @click="router.push('/reviewlist')" class="back-btn">←</button>
    <h2 class="title">로그인</h2>
  </div>


    <!-- 탭 -->
    <div class="tab-bar">
      <button class="tab">구글리뷰</button>
      <button class="tab selected">앱리뷰</button>
    </div>

    <!-- 프로필 -->
    <div class="profile">
      <img src="https://via.placeholder.com/40" class="avatar" />
      <div class="name">노현우</div>
    </div>

    <!-- 별점 -->
    <div class="stars">
      <span
        v-for="i in 5"
        :key="i"
        @click="rating = i"
        :class="{ filled: i <= rating }"
      >★</span>
    </div>

    <!-- 리뷰 입력 -->
    <textarea
      v-model="content"
      placeholder="숙박 경험에 대해 다른 사람들에게 알려주세요."
    ></textarea>

    <p class="info">해당 리뷰는 앱리뷰에 작성됩니다.</p>

    <!-- 제출 버튼 -->
    <button class="submit-btn" @click="submitReview">게시</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const rating = ref(0)
const content = ref('')

const goBack = () => router.back()

const submitReview = () => {
  if (rating.value === 0) {
    alert('별점을 입력해주세요.')
    return
  }
  alert('리뷰가 등록되었습니다!')
  router.push('/reviewlist')
}
</script>

<style scoped>
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
  padding: 16px;
}

.top-bar {
  display: flex;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ccc;
  height: 56px;
  position: relative;
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

.tab-bar {
  display: flex;
  justify-content: center;
  margin: 16px 0;
  gap: 12px;
}
.tab {
  border: 1px solid #888;
  padding: 6px 16px;
  border-radius: 20px;
  background: white;
  font-size: 14px;
}
.selected {
  background: #ddd;
}

.profile {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 16px 0;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.name {
  font-weight: bold;
}

.stars {
  font-size: 28px;
  margin: 10px 0;
}
.stars span {
  cursor: pointer;
  color: #ccc;
}
.stars .filled {
  color: #6666ff;
}

textarea {
  width: 100%;
  height: 100px;
  border: 1px solid #aaa;
  border-radius: 10px;
  padding: 10px;
  box-sizing: border-box;
  font-size: 14px;
  margin-top: 10px;
}

.info {
  font-size: 12px;
  color: #888;
  margin: 10px 0;
}

.submit-btn {
  width: 100%;
  background-color: #e0e0e0;
  padding: 12px;
  border-radius: 20px;
  font-weight: bold;
  font-size: 16px;
  border: none;
  cursor: pointer;
}
</style>
