<template>
  <!-- 상단 바 -->
  <div class="top-bar">
    <button @click="goBack" class="back-btn">←</button>
    <h2 class="title">장소 정보</h2>
  </div>

  <div class="result-page">
    <!-- 장소 정보 카드 (데이터 로딩 완료 시에만 표시됨) -->
    <div class="place-card" v-if="place">
      <div class="place-header">
        <h2>{{ place.name }}</h2>
        <p>{{ place.formatted_address }}</p>
        <p>전화번호: {{ place.formatted_phone_number || '정보 없음' }}</p>
        <p>운영시간: {{ openingHours }}</p>
        <!-- 별점 표시 -->
        <div class="stars">
          <span v-for="n in 5" :key="n" class="star">
            {{ n <= Math.round(place.rating || 0) ? '⭐' : '☆' }}
          </span>
        </div>
      </div>

      <!-- 출발지/경유지/도착지 설정 버튼 -->
      <div class="button-row">
        <button @click="select('start')">출발지로 설정</button>
        <button @click="select('waypoint')">경유지로 설정</button>
        <button @click="select('end')">목적지로 설정</button>
      </div>
    </div>

    <!-- 로딩 중 메시지 -->
    <p v-else>📡 장소 정보를 불러오는 중입니다...</p>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCurrentInstance } from 'vue'

const { appContext } = getCurrentInstance()
const apiKey = appContext.config.globalProperties.$GoogleMapKey
const route = useRoute()
const router = useRouter()

const place = ref(null)               // 장소 기본 정보
const openingHours = ref('정보 없음') // 운영 시간 텍스트

function goBack() {
  router.back()
}

// 마운트 시 구글 API 로딩 및 검색 실행
onMounted(() => {
  const query = decodeURIComponent(route.params.name)

  const script = document.createElement('script')
  script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`
  script.async = true

  // 스크립트 로드 후 장소 검색
  script.onload = () => {
    const service = new google.maps.places.PlacesService(document.createElement('div'))

    // 1차 검색: 텍스트로 장소 찾기
    service.textSearch(
      {
        query,
        location: new google.maps.LatLng(36.5, 127.8), // 대한민국 중앙쯤
        radius: 50000,
        region: 'kr',
      },
      (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK && results.length > 0) {
          const result = results[0]
          place.value = result

          // 2차 요청: 상세 정보 가져오기
          service.getDetails(
            {
              placeId: result.place_id,
              fields: ['opening_hours', 'formatted_phone_number']
            },
            (detail, detailStatus) => {
              if (detailStatus === google.maps.places.PlacesServiceStatus.OK) {
                openingHours.value = detail.opening_hours?.weekday_text?.[0] || '정보 없음'
                place.value.formatted_phone_number = detail.formatted_phone_number
              }
            }
          )
        } else {
          alert('장소 정보를 찾을 수 없습니다.')
        }
      }
    )
  }

  document.head.appendChild(script)
})

// 출발지/경유지/도착지로 설정 시
function select(type) {
  const location = place.value.geometry?.location
  if (!location) {
    alert('좌표 정보를 찾을 수 없습니다.')
    return
  }

  // 기존 쿼리값 복사
  const query = {
    start: route.query.start || '',
    end: route.query.end || '',
    waypoints: route.query.waypoints || '[]'
  }

  // 타입에 따라 쿼리값 수정
  if (type === 'start') {
    query.start = place.value.formatted_address
  } else if (type === 'end') {
    query.end = place.value.formatted_address
  } else if (type === 'waypoint') {
    const index = parseInt(route.query.index)
    let waypoints = []

    try {
      waypoints = JSON.parse(query.waypoints)
    } catch (e) {}

    if (!isNaN(index)) {
      waypoints[index] = place.value.formatted_address
      query.waypoints = JSON.stringify(waypoints)
    }
  } else {
    alert('잘못된 요청입니다.')
    return
  }

  // target, index는 더 이상 필요 없음
  delete query.target
  delete query.index

  // route 페이지로 이동
  router.push({
    name: 'route',
    query
  })
}
</script>

<style scoped>
.top-bar {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  border-bottom: 1px solid #ddd;
  position: relative;
  height: 56px;
  background-color: white;
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

.result-page {
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}

.place-card {
  border: 1px solid #ccc;
  border-radius: 12px;
  padding: 16px;
  background-color: #fafafa;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.place-header {
  margin-bottom: 12px;
  text-align: left;
}

.stars {
  margin-top: 4px;
  font-size: 20px;
  color: gold;
}

.button-row {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.button-row button {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  background: #e0e0e0;
  cursor: pointer;
  font-size: 14px;
}
</style>
