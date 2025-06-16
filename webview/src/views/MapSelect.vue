<template>
  <div class="select-wrapper">
    <!-- 상단 바: 뒤로가기 + 헤더 -->
    <div class="top-bar">
      <button @click="goBack" class="back-btn">←</button>
      <div class="header">지도에서 선택하기</div>
    </div>

    <!-- 구글 맵 표시 영역 -->
    <div id="map"></div>

    <!-- 주소 확인 및 확인 버튼 -->
    <div class="address-bar">
      <p>{{ selectedAddress || '지도를 클릭해 주소를 선택하세요' }}</p>
      <button @click="confirmSelection">확인</button>
    </div>
  </div>
</template>

<script setup>
// Vue의 Composition API import
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

// 선택된 주소를 저장할 ref 변수
const selectedAddress = ref('')

// 라우팅 객체
const router = useRouter()
const route = useRoute()

// 지도 및 마커 변수
let map = null
let marker = null

// "확인" 버튼 눌렀을 때 실행되는 함수
function confirmSelection() {
  if (!selectedAddress.value) {
    alert('주소를 선택해주세요')
    return
  }

  // SearchResult 페이지로 라우팅
  router.push({
    name: 'SearchResult',
    params: { name: selectedAddress.value },
    query: {
      ...route.query, // 기존 쿼리 유지
      start: route.query.start || '',
      end: route.query.end || '',
      waypoints: route.query.waypoints || '[]',
      target: route.query.target || ''
    }
  })
}

// 좌표를 주소로 변환하는 함수
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

// 지도 초기화 함수
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 37.5665, lng: 126.978 }, // 서울시청 근처 좌표
    zoom: 15,
    disableDefaultUI: true // 기본 UI 컨트롤 제거
  })

  // 지도 클릭 이벤트
  map.addListener('click', (e) => {
    reverseGeocode(e.latLng) // 주소 역지오코딩

    // 기존 마커 제거
    if (marker) marker.setMap(null)

    // 새 마커 추가
    marker = new google.maps.Marker({
      map,
      position: e.latLng
    })
  })
}

// 컴포넌트 마운트 시 구글맵 스크립트 로딩 + 초기화
onMounted(() => {
  // 만약 아직 구글맵이 로딩되지 않았다면
  if (!window.google || !window.google.maps || !window.google.maps.places) {
    const script = document.createElement('script')
    const apiKey = window.GoogleMapKey
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`
    script.async = true
    script.defer = true
    script.onload = initMap // 로드 완료 시 initMap 실행
    document.head.appendChild(script)
  } else {
    initMap()
  }
})

// 뒤로가기 버튼 함수
function goBack() {
  router.back()
}
</script>

<style scoped>
/* 전체 화면 구성 */
.select-wrapper {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 상단 바 스타일 */
.top-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  height: 50px;
  border-bottom: 1px solid #ccc;
  background-color: #fff;
}

/* 뒤로가기 버튼 스타일 */
.back-btn {
  position: absolute;
  left: 16px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

/* 헤더 텍스트 */
.header {
  font-weight: bold;
  font-size: 16px;
  text-align: center;
}

/* 지도 영역 스타일 */
#map {
  flex: 1; /* 나머지 영역을 꽉 채움 */
}

/* 주소 & 버튼 영역 */
.address-bar {
  background: white;
  padding: 12px;
  border-top: 1px solid #ccc;
  text-align: center;
}

/* 확인 버튼 스타일 */
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
