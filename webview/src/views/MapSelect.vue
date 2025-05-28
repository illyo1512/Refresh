<template>
  <div class="select-wrapper">
    <div class="header">지도에서 선택하기</div>
    <div id="map"></div>
    <div class="address-bar">
      <p>{{ selectedAddress || '지도를 클릭해 주소를 선택하세요' }}</p>
      <button @click="confirmSelection">확인</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const selectedAddress = ref('')
const router = useRouter()
const route = useRoute()
let map = null
let marker = null

function confirmSelection() {
  if (!selectedAddress.value) {
    alert('주소를 선택해주세요')
    return
  }

  router.push({
    name: 'SearchResult',
    params: { name: selectedAddress.value },
    query: {
      ...route.query, // ✅ 기존 쿼리 유지
      start: route.query.start || '',
      end: route.query.end || '',
      waypoints: route.query.waypoints || '[]',
      target: route.query.target || ''
    }
  })
}

function reverseGeocode(latlng) {
  const geocoder = new google.maps.Geocoder()
  geocoder.geocode({ location: latlng }, (results, status) => {
    if (status === 'OK' && results[0]) {
      selectedAddress.value = results[0].formatted_address
    } else {
      selectedAddress.value = '주소를 찾을 수 없습니다'
    }
  })
}

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 37.5665, lng: 126.978 },
    zoom: 15
  })

  map.addListener('click', (e) => {
    reverseGeocode(e.latLng)

    if (marker) marker.setMap(null)

    marker = new google.maps.Marker({
      map,
      position: e.latLng
    })
  })
}

onMounted(() => {
  if (!window.google || !window.google.maps || !window.google.maps.places) {
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
</script>

<style scoped>
.select-wrapper {
  position: relative;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

#map {
  flex: 1;
}

.header {
  background: #fff;
  padding: 10px;
  font-weight: bold;
  text-align: center;
  border-bottom: 1px solid #ccc;
}

.address-bar {
  background: white;
  padding: 12px;
  border-top: 1px solid #ccc;
  text-align: center;
}

.address-bar button {
  margin-top: 8px;
  padding: 8px 20px;
  font-size: 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
</style>
