<template>
  <div class="self-route-wrapper">
    <!-- 상단 바 -->
    <div class="header">
      <button @click="goBack" class="back-btn">←</button>
      <h2 class="title">즐겨찾기</h2>
    </div>

    <!-- 즐겨찾기한 경로가 없을 경우 -->
    <div v-if="routes.length === 0" class="empty-message">
      즐겨찾기한 경로가 없습니다.
    </div>

    <!-- 즐겨찾기한 경로 리스트 -->
    <div v-else class="route-list">
      <div
        v-for="route in routes"
        :key="route.selfRouteId"
        class="route-card"
      >
        <!-- 경로 요약 정보 -->
        <div class="route-summary">
          <div class="summary-title">{{ route.routeName }}</div>
          <div class="summary-info">
            경유지 {{ getWaypointCount(route) }}개 → {{ getTransportMode(route) }}
          </div>
        </div>

        <!-- 버튼 그룹: 경로 확인 / 수정 / 삭제 -->
        <div class="button-group">
          <button class="btn check" @click="goToRoute(route)">경로 확인</button>
          <button class="btn edit" @click="editRoute(route)">경로 수정</button>
          <button class="btn delete" @click="deleteRoute(route.selfRouteId)">삭제</button>
        </div>

        <!-- 저장일자 -->
        <div class="saved-date">
          저장한 날짜: {{ formatDate(route.createdAt) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const routes = ref([])
const router = useRouter()

function goBack() {
  router.back()
}

function formatDate(dateStr) {
  const date = new Date(dateStr)
  return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`
}

function deleteRoute(id) {
  if (!confirm("이 경로를 삭제하시겠습니까?")) return

  axios.delete(`/api/self-routes/${id}`)
    .then(() => {
      routes.value = routes.value.filter(route => route.selfRouteId !== id)
    })
    .catch(err => console.error("삭제 실패:", err))
}

function goToRoute(route) {
  try {
    localStorage.setItem('reRouteResult', JSON.stringify(route.routeResult))
    router.push({ name: 'route' })
  } catch (err) {
    alert("경로 이동 실패: 데이터 형식 오류")
  }
}

function editRoute(route) {
  alert(`경로 수정 예정: ${route.routeName}`)
}

function getWaypointCount(route) {
  try {
    const result = typeof route.routeResult === 'string' ? JSON.parse(route.routeResult) : route.routeResult
    return result.waypoints ? result.waypoints.length : 0
  } catch {
    return 0
  }
}

function getTransportMode(route) {
  try {
    const result = typeof route.routeResult === 'string' ? JSON.parse(route.routeResult) : route.routeResult
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
    console.log("💾 불러온 데이터:", routes.value)
  } catch (err) {
    console.error("불러오기 실패:", err)
  }
}

onMounted(() => {
  fetchRoutes()
})
</script>

<style scoped>
.self-route-wrapper {
  padding: 20px;
  background-color: #ffffff;
  min-height: 100vh;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
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
}

.empty-message {
  text-align: center;
  color: gray;
  font-size: 16px;
  margin-top: 40px;
}

.route-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.route-card {
  background-color: #ffffff;
  border: 2px solid #bbbbbb;
  border-radius: 12px;
  padding: 16px;
}

.route-summary {
  margin-bottom: 10px;
}

.summary-title {
  font-size: 16px;
  font-weight: bold;
}

.summary-info {
  font-size: 13px;
  color: #666;
}

.button-group {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.btn {
  padding: 6px 12px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-size: 13px;
}

.btn.check {
  background-color: #e0e0e0;
}

.btn.edit {
  background-color: #c0c0c0;
}

.btn.delete {
  background-color: #ff4d4d;
  color: white;
}

.saved-date {
  font-size: 12px;
  color: gray;
}
</style>
