<template>
  <div class="main-container">
    <!-- 메뉴 버튼들 -->
    <div class="menu-bar">
      <button @click="navigateTo('favorites')">즐겨찾기</button>
      <button @click="navigateTo('info')">경로게시판</button>
      <button @click="navigateTo('mypage')">마이페이지</button>
      <button @click="navigateTo('map')">지도</button>
    </div>

    <!-- 검색창 -->
    <div class="search-box">
      <input type="text" placeholder="검색" />
      <button>🔍</button>
    </div>

    <!-- 어플 소개 -->
    <div class="intro-box">
      <div class="intro-title">어플 소개</div>
      <div class="intro-video">어플의 시연 영상</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

function navigateTo(page) {
  const routes = {
    favorites: '/self_route',
    info: '/route_board',
    mypage: null,
    map: '/map',
  }

  if (routes[page]) {
    router.push(routes[page])
  } else {
    alert(`${page} 페이지는 현재 개발 중입니다.`)
  }
}

</script>

<style scoped>
.main-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  box-sizing: border-box;
  user-select: none;
  outline: none;
}

.menu-bar {
  display: flex;
  justify-content: space-around;
  width: 100%;
  background: white;
  padding: 12px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  margin-bottom: 16px;
  user-select: none;
  outline: none;
}

.menu-bar button {
  font-size: 14px;
  background: none;
  border: none;
  cursor: pointer;
}

.search-box {
  display: flex;
  align-items: center;
  width: 100%;
  background: white;
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  margin-bottom: 24px;
  user-select: none;
  outline: none;
}

.search-box input {
  flex: 1;
  padding: 8px;
  border: none;
  outline: none;
}

.search-box button {
  font-size: 20px;
  background: none;
  border: none;
  cursor: pointer;
}

.intro-box {
  background: #e5e5e5;
  width: 100%;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
}

.intro-title {
  font-weight: bold;
  margin-bottom: 20px;
}

.intro-video {
  font-size: 14px;
}
</style>
