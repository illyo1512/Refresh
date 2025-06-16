<template>
  <div class="container">
    <div class="top-bar">
    <button @click="router.push('/map')" class="back-btn">←</button>
    <h2 class="title">로그인</h2>
  </div>


    <!-- 탭 -->
    <div class="tab-bar">
      <button class="tab">구글리뷰</button>
      <button class="tab selected">앱리뷰</button>
    </div>

    <!-- 리뷰 박스 -->
    <div class="review-box">
      <div class="profile">
        <img src="https://via.placeholder.com/40" alt="프로필" class="avatar" />
        <div class="info">
          <div class="name">노현우</div>
          <div class="stars">★★★★★</div>
          <div class="date">2024-02-12</div>
        </div>
      </div>
      <p class="content">잘 가르치고 좋은 교수님들이 계세요</p>
    </div>

    <!-- 작성 버튼 -->
    <button class="write-btn" @click="goToWrite">리뷰 작성</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goBack = () => router.back()
const goToWrite = () => router.push('/writereview')
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
  margin-right: 32px; /* back 버튼 공간 보정 */
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

.review-box {
  background: white;
  border: 1px solid #ccc;
  border-radius: 20px;
  padding: 16px;
  margin-top: 16px;
}

.profile {
  display: flex;
  align-items: center;
  gap: 12px;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.info {
  display: flex;
  flex-direction: column;
}
.name {
  font-weight: bold;
}
.stars {
  color: green;
}
.date {
  font-size: 12px;
  color: #888;
}
.content {
  margin-top: 12px;
  font-size: 15px;
}

.write-btn {
  margin-top: 30px;
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
