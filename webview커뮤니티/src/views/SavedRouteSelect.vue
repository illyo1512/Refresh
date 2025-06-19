<template>
  <div class="self-route-wrapper">
    <button @click="goBack" class="back-btn">←</button>
    <h2 class="title">즐겨찾기</h2>

    <div v-if="routes.length === 0" class="empty-message">
      즐겨찾기한 경로가 없습니다.
    </div>

    <div v-else class="route-list">
      <label
        v-for="route in routes"
        :key="route.selfRouteId"
        class="route-card"
      >
        <input
          type="radio"
          name="selectedRoute"
          :value="route"
          v-model="selectedRoute"
          class="radio"
        />
        <div class="route-content">
          <div class="summary-title">{{ route.routeName }}</div>
          <div class="summary-info">
            경유지 {{ getWaypointCount(route) }}개 →
            {{ getTransportMode(route) }}
          </div>
          <div class="saved-date">
            저장한 날짜: {{ formatDate(route.createdAt) }}
          </div>
        </div>
      </label>
    </div>

    <button class="confirm-button" @click="confirmSelection">
      확인 <span class="circle-number">③</span>
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const routes = ref([])
const selectedRoute = ref(null)

function goBack() {
  router.back()
}

function confirmSelection() {
  if (!selectedRoute.value) {
    alert('경로를 선택해주세요.')
    return
  }
  localStorage.setItem('selectedRoute', JSON.stringify(selectedRoute.value))
  router.push('/writepost')
}

function formatDate(dateStr) {
  const date = new Date(dateStr)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

function getWaypointCount(route) {
  try {
    const result = typeof route.routeResult === 'string'
      ? JSON.parse(route.routeResult)
      : route.routeResult
    return result.waypoints?.length ?? 0
  } catch {
    return 0
  }
}

function getTransportMode(route) {
  try {
    const result = typeof route.routeResult === 'string'
      ? JSON.parse(route.routeResult)
      : route.routeResult
    return result.transportModeDescription || '이동수단 미지정'
  } catch {
    return '이동수단 미지정'
  }
}

async function fetchRoutes() {
  try {
    const userId = localStorage.getItem("userId") || 1
    const res = await axios.get(`/api/self-routes/user/${userId}`)
    routes.value = res.data.map(item => ({
      ...item,
      routeResult: typeof item.routeResult === 'string'
        ? JSON.parse(item.routeResult)
        : item.routeResult
    }))
  } catch (err) {
    console.error("불러오기 실패:", err)
  }
}

onMounted(fetchRoutes)
</script>

<style scoped>
.self-route-wrapper {
  padding: 20px;
  background-color: #ffffff;
  min-height: 100vh;
}
.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 12px;
}
.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}
.route-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.route-card {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  border: 1.5px solid #ccc;
  border-radius: 12px;
  padding: 12px;
}
.radio {
  transform: scale(1.3);
  margin-top: 6px;
}
.route-content {
  flex: 1;
}
.summary-title {
  font-size: 16px;
  font-weight: bold;
}
.summary-info {
  font-size: 13px;
  color: #666;
}
.saved-date {
  font-size: 12px;
  color: gray;
  margin-top: 6px;
}
.confirm-button {
  margin-top: 30px;
  width: 100%;
  padding: 12px;
  background: #eee;
  font-size: 16px;
  font-weight: bold;
  border: none;
  border-radius: 8px;
}
.circle-number {
  border: 1px solid #000;
  border-radius: 50%;
  padding: 2px 8px;
  margin-left: 6px;
}
</style>
