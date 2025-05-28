<template>
  <button class="floating-home-btn" @click="goToHome">🏠</button>
  <div class="map-container">
    <div class="top-bar">
      <div class="menu" :class="{ active: sidebarOpen }" @click="toggleSidebar">
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
      </div>

      <div class="search-container" @click="goToSearch">
        <input class="search-input" type="text" placeholder="검색" readonly />
        <span class="search-icon">🔍</span>
      </div>
      <span class="star">⭐</span>
    </div>


    <div
      class="sidebar"
      :class="{ open: sidebarOpen }"
      :style="{ top: sidebarTop + 'px' }">
      <ul>
        <li @click="alertMenu('로그인')">로그인</li>
        <li @click="alertMenu('경로게시판')">경로게시판</li>
        <li @click="alertMenu('마이페이지')">마이페이지</li>
        <li @click="alertMenu('범례')">범례</li>
        <li @click="goToHome">메인페이지</li>
      </ul>
    </div>

    <!-- 지도 -->
    <div id="map">

    </div>

  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const sidebarOpen = ref(false)
const sidebarTop = ref(0)

const toggleSidebar = () => {
  sidebarOpen.value = !sidebarOpen.value
}
const goToSearch = () => {
  router.push('/search')
}
const router = useRouter()
const goToHome = () => {
  router.push('/')
}
const alertMenu = (name) => {
  alert(`${name} 기능은 현재 준비 중입니다.`)
}

onMounted(() => {
  const barHeight = document.querySelector('.top-bar')?.offsetHeight ?? 0
  sidebarTop.value = barHeight

  const script = document.createElement('script')
  script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyBLRX25Kd0bDgua2i3xZvcNGyt6a7M0fn0 &callback=initMap`
  script.async = true
  window.initMap = () => {
    new window.google.maps.Map(document.getElementById('map'), {
      center: { lat: 37.5665, lng: 126.978 },
      zoom: 15,
    })
  }
  document.head.appendChild(script)
})
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100vw;
  height: 100vh;
}

#map {
  width: 100%;
  height: 100%;
}

/* 상단 바 */
.top-bar {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
  width: 100%;
  padding: 10px;
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
}

/* 햄버거 메뉴 */
.menu {
  cursor: pointer;
  width: 25px;
  height: 30px;
  position: relative;
  margin-right: 10px;
  z-index: 30;
}
.bar {
  width: 25px;
  height: 3px;
  background-color: black;
  margin: 6px 0;
  transition: 0.5s;
}
/* .menu.active .bar:nth-child(2) {
  opacity: 0;
}
.menu.active .bar:nth-child(1) {
  transform: rotate(45deg) translateY(8px);
}
.menu.active .bar:nth-child(3) {
  transform: rotate(-45deg) translateY(-8px);
} */

/* 검색창 */
.search-container {
  flex-grow: 1;
  display: flex;
  align-items: center;
  background: white;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 6px 10px;
  margin-right: 10px;
  cursor: pointer;
}

.search-input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
}

.search-icon {
  font-size: 18px;
  margin-left: 8px;
  color: #666;
}

/* 별 아이콘 */
.star {
  font-size: 20px;
  cursor: pointer;
}

/* 사이드바 */
.sidebar {
  position: absolute;
  left: 0;
  width: 220px;
  height: calc(100% - 50px);
  background: white;
  z-index: 20;
  padding: 10px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.3);
  transform: translateX(-100%);
  transition: transform 0.3s ease;
}

.sidebar.open {
  transform: translateX(0);
}

.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}

/* 홈 버튼 */
/* .home-btn {
  position: absolute;
  bottom: 20px;
  left: 20px;
  z-index: 10;
  background: white;
  border: 1px solid #ccc;
  border-radius: 50%;
  padding: 12px;
  font-size: 20px;
} */
/* 홈버튼 */
.floating-home-btn {
  position: fixed;
  bottom: 100px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 100;
  background: white;
  border: 2px solid #ccc;
  border-radius: 50%;
  padding: 15px;
  font-size: 24px;
  box-shadow: 2px 2px 10px rgba(0,0,0,0.3);
  cursor: pointer;
  user-select: none;
  outline: none;
}
</style>
