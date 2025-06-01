<template>
  <div class="top-bar">
    <button @click="goBack" class="back-btn">←</button>
    <h2 class="title">장소 정보</h2>
  </div>
  <div class="result-page">
    <div class="place-card" v-if="place">
      <div class="place-header">
        <h2>{{ place.name }}</h2>
        <p>{{ place.formatted_address }}</p>
        <p>전화번호: {{ place.formatted_phone_number || '정보 없음' }}</p>
        <p>운영시간: {{ openingHours }}</p>
        <div class="stars">
          <span v-for="n in 5" :key="n" class="star">
            {{ n <= Math.round(place.rating || 0) ? '⭐' : '☆' }}
          </span>
        </div>
      </div>

      <div class="button-row">
        <button @click="select('start')">출발지로 설정</button>
        <button @click="select('waypoint')">경유지로 설정</button>
        <button @click="select('end')">목적지로 설정</button>
      </div>
    </div>

    <p v-else>📡 장소 정보를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const place = ref(null)
const openingHours = ref('정보 없음')

function goBack() {
  router.back()
}

onMounted(() => {
  console.log('[SearchResultPage 진입 시 query]', route.query)

  const query = decodeURIComponent(route.params.name)
  const script = document.createElement('script')
  const apiKey = window.GoogleMapKey;
  script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`

  script.async = true
  script.onload = () => {
    const service = new google.maps.places.PlacesService(document.createElement('div'))
    service.textSearch(
      {
        query,
        location: new google.maps.LatLng(36.5, 127.8),
        radius: 50000,
        region: 'kr',
      },
      (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK && results.length > 0) {
          const result = results[0]
          place.value = result

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

function select(type) {
  const location = place.value.geometry?.location
  if (!location) {
    alert('좌표 정보를 찾을 수 없습니다.')
    return
  }

  const query = {
    start: route.query.start || '',
    end: route.query.end || '',
    waypoints: route.query.waypoints || '[]'
  }

  console.log('[SearchResultPage → Route push]', type, place.value.formatted_address)
  console.log('[선택 직전 query 상태]', query)

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

  delete query.target
  delete query.index

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
