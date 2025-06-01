<template>
  <div class="map-wrapper">
    <div class="route-bar">
      <div class="top-bar">
        <button @click="goBack" class="back-btn">←</button>
      </div>
      <div class="route-row">
        <span class="label" @click="goSearch('start')">📍 출발지:</span>
        <span class="location">{{ start || '선택 안됨' }}</span>
      </div>
      <div v-for="(wp, index) in waypoints" :key="index" class="route-row">
        <span class="label" @click="goSearch('waypoint', index)">➕ 경유지 {{ index + 1 }}:</span>
        <span class="location">{{ wp }}</span>
        <button class="remove-btn" @click="removeWaypoint(index)">✕</button>
      </div>
      <div class="route-row">
        <span class="label" @click="goSearch('end')">🏁 도착지:</span>
        <span class="location">{{ end || '선택 안됨' }}</span>
      </div>
      <button class="add-btn" @click="addWaypoint">➕ 경유지 추가</button>
    </div>

    <div id="map" class="map-area"></div>
    <button class="analyze-btn" @click="drawRoute">경로 안전도 진단</button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const start = ref('')
const end = ref('')
const waypoints = ref([])
const markers = []
let map = null

function goBack() {
  router.back()
}

function addWaypoint() {
  waypoints.value.push('')
}

function removeWaypoint(index) {
  waypoints.value.splice(index, 1)
}

function goSearch(target, index = null) {
  const query = {
    target,
    ...(index !== null && { index }),
    start: start.value,
    end: end.value,
    waypoints: JSON.stringify(waypoints.value)
  }
  router.push({ name: 'Search', query })
}

function geocodeAddress(address) {
  return new Promise((resolve, reject) => {
    const geocoder = new google.maps.Geocoder()
    geocoder.geocode({ address }, (results, status) => {
      if (status === 'OK') {
        resolve(results[0].geometry.location)
      } else {
        reject(`주소 변환 실패 (${address}): ${status}`)
      }
    })
  })
}

async function updateMarkersOnly() {
  if (!map) return
  markers.forEach(m => m.setMap(null))
  markers.length = 0

  const points = [start.value, ...waypoints.value, end.value]
  for (let i = 0; i < points.length; i++) {
    if (!points[i]) continue
    try {
      const loc = await geocodeAddress(points[i])
      const label = i === 0 ? 'S' : i === points.length - 1 ? 'E' : `${i}`
      const marker = new google.maps.Marker({ map, position: loc, label })
      markers.push(marker)
    } catch (err) {
      console.warn('마커 주소 변환 실패:', err)
    }
  }
}

async function drawRoute() {
  if (!start.value || !end.value) {
    alert('출발지 또는 도착지를 입력해주세요.')
    return
  }

  try {
    const allPoints = [start.value, ...waypoints.value, end.value]
    const locations = await Promise.all(allPoints.map(addr => geocodeAddress(addr)))

    const directionsService = new google.maps.DirectionsService()

    for (let i = 0; i < locations.length - 1; i++) {
      const origin = locations[i]
      const destination = locations[i + 1]

      const renderer = new google.maps.DirectionsRenderer({ suppressMarkers: true })
      renderer.setMap(map)

      await new Promise((resolve, reject) => {
        directionsService.route({
          origin,
          destination,
          travelMode: google.maps.TravelMode.TRANSIT
        }, (result, status) => {
          if (status === 'OK') {
            renderer.setDirections(result)
            resolve()
          } else {
            reject(status)
          }
        })
      })
    }
  } catch (err) {
    console.error(err)
    alert(`경로 계산 중 오류: ${err}`)
  }
}

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 37.5665, lng: 126.978 },
    zoom: 13
  })

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        const userPos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        }
        new google.maps.Marker({ map, position: userPos, title: '현재 위치' })
        map.setCenter(userPos)
      },
      (error) => {
        console.warn('위치 접근 실패:', error.message)
      }
    )
  }

  updateMarkersOnly()
}

onMounted(() => {
  if (!window.google || !window.google.maps) {
    const script = document.createElement('script')
    const apiKey = window.GoogleMapKey;
  script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`
    script.async = true
    script.defer = true
    script.onload = initMap
    document.head.appendChild(script)
  } else {
    initMap()
  }
})

// ✅ 항상 route.query 값 기반으로 start, end, waypoints 유지
watch(
  () => route.query,
  (q) => {
    start.value = q.start || ''
    end.value = q.end || ''
    try {
      waypoints.value = JSON.parse(q.waypoints || '[]')
    } catch (e) {
      waypoints.value = []
    }
  },
  { immediate: true }
)

watch([start, end, waypoints], updateMarkersOnly, { deep: true })
</script>

<style scoped>
.map-wrapper {
  position: relative;
  width: 100vw;
  height: 100vh;
}

#map {
  width: 100%;
  height: 100%;
}

.route-bar {
  position: absolute;
  top: 10px;
  left: 10px;
  right: 10px;
  background: white;
  padding: 10px;
  z-index: 10;
  border-radius: 8px;
  font-size: 14px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.3);
  line-height: 1.6;
}

.route-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.label {
  font-weight: bold;
  cursor: pointer;
  width: 100px;
  text-align: right;
  margin-right: 12px;
}

.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 8px;
}

.location {
  flex: 1;
  padding-left: 20px;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.add-btn, .remove-btn {
  background: none;
  border: none;
  font-size: 14px;
  cursor: pointer;
  color: #d32f2f;
}

.analyze-btn {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  background-color: #66a9e8;
  color: white;
  cursor: pointer;
}
</style>
