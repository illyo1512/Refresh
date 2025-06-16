<template>
  <!-- 홈 버튼 -->
  <button class="floating-home-btn" @click="goToHome">🏠</button>

  <div class="map-container">
    <!-- 상단 바: 햄버거 메뉴 + 검색창 + 즐겨찾기 -->
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
      <span class="route_save" @click="goToRoutesave">⭐</span>
    </div>

    <!-- 사이드 메뉴 -->
    <div class="sidebar" :class="{ open: sidebarOpen }" :style="{ top: sidebarTop + 'px' }">
      <!-- 로그인 X -->
      <ul v-if="!isLoggedIn">
        <li @click="goToLogin">로그인</li>
        <li @click="goToBoard">경로게시판</li>
        <li @click="alertMenu('마이페이지')">마이페이지</li>
        <li @click="alertMenu('범례')">범례</li>
        <li @click="goToHome">메인페이지</li>
      </ul>

      <!-- 로그인 O -->
      <ul v-else>
        <li><strong>{{ nickname }} 님</strong></li>
        <li @click="goToBoard">경로게시판</li>
        <li @click="alertMenu('마이페이지')">마이페이지</li>
        <li @click="alertMenu('범례')">범례</li>
        <li @click="goToHome">메인페이지</li>
        <li @click="logout" class="logout">로그아웃</li>
      </ul>
    </div>

    <!-- 위험 알림 컴포넌트 -->
    <DangerAlert
      v-if="dangerVisible"
      :dangerData="dangerInfo"
      :visible="dangerVisible"
      @close="dangerVisible = false"
    />

    <!-- 실제 지도 -->
    <div id="map"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import DangerAlert from '@/components/DangerAlert.vue'
import { getCurrentInstance } from 'vue'

const router = useRouter()
const { appContext } = getCurrentInstance()
const GoogleMapKey = appContext.config.globalProperties.$GoogleMapKey
const WmsKey = appContext.config.globalProperties.$WmsKey
const BackEndUrl = appContext.config.globalProperties.$axios.defaults.baseURL

// 상태
const sidebarOpen = ref(false)
const sidebarTop = ref(0)

const dangerVisible = ref(false)
const dangerInfo = ref({})

// 로그인 관련 상태
const nickname = ref(localStorage.getItem('nickname') || null)
const isLoggedIn = computed(() => !!nickname.value)

// 메뉴 동작 함수
const toggleSidebar = () => sidebarOpen.value = !sidebarOpen.value
const goToLogin = () => router.push('/login')
const goToRoutesave = () => router.push('/self_route')
const goToSearch = () => router.push('/search')
const goToHome = () => router.push('/')
const goToBoard = () => router.push('/route_board')
const alertMenu = (name) => alert(`${name} 기능은 현재 준비 중입니다.`)

// 로그아웃 처리
function logout() {
  localStorage.removeItem('nickname')
  localStorage.removeItem('userId')
  localStorage.removeItem('token')
  nickname.value = null
  alert('로그아웃되었습니다.')
  router.push('/map')
}

// 지도 관련 변수
let map
const polygons = []
const userMarker = ref(null)
const recordMap = {}

// 컴포넌트 마운트 시 실행
onMounted(() => {
  sidebarTop.value = document.querySelector('.top-bar')?.offsetHeight ?? 0

  // 구글맵 로드
  const apiKey = GoogleMapKey
  const script = document.createElement('script')
  script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places,geometry`
  script.async = true
  script.defer = true
  script.onload = () => {
    initMap()
    fetchDangerRecords()
  }
  document.head.appendChild(script)

  // 네이티브 위치 수신 이벤트
  window.addEventListener('nativeLocation', event => {
    const { lat, lng } = event.detail
    const pos = { lat, lng }

    if (userMarker.value) {
      userMarker.value.setPosition(pos)
    } else {
      userMarker.value = new google.maps.Marker({ map, position: pos, draggable: true, title: '현재위치' })
      userMarker.value.addListener('dragend', () => {
        const newPos = userMarker.value.getPosition()
        map.setCenter(newPos)
        checkDangerArea(newPos)
      })
    }
    map.setCenter(pos)
    checkDangerArea(pos)
  })
})

// 구글맵 초기화
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 35.15, lng: 126.89 },
    zoom: 15
  })
  addWMSOverlay()
  enableGeolocationMarker()
  loadLocalGeoJsonFiles()
}

// 현재 위치 마커 표시
function enableGeolocationMarker() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
      const pos = { lat: position.coords.latitude, lng: position.coords.longitude }
      userMarker.value = new google.maps.Marker({ map, position: pos, draggable: true, title: '현재 위치' })
      map.setCenter(pos)
      checkDangerArea(pos)
      userMarker.value.addListener('dragend', () => {
        const newPos = userMarker.value.getPosition()
        map.setCenter(newPos)
        checkDangerArea(newPos)
      })
    }, error => console.error('위치 정보 에러:', error.message))
  }
}

// 위험 지역 감지
function checkDangerArea(userPos) {
  polygons.forEach(async poly => {
    if (google.maps.geometry.poly.containsLocation(userPos, poly)) {
      if (!dangerVisible.value && poly.detailId) {
        try {
          const res = await fetch(`${BackEndUrl}/api/danger-details/${poly.detailId}`)
          if (!res.ok) throw new Error('상세 정보 요청 실패')
          const data = await res.json()
          dangerInfo.value = data
          dangerVisible.value = true
        } catch {}
      }
    }
  })
}

// 위험 데이터 불러오기
function fetchDangerRecords() {
  fetch(`${BackEndUrl}/api/danger-records`)
    .then(res => res.json())
    .then(records => {
      records.forEach(r => {
        recordMap[r.dangerJsonPath] = r.detailId
      })
    })
    .catch(() => {})
}

// GeoJSON 파일 로드
function loadLocalGeoJsonFiles() {
  const isAndroid = window.location.href.startsWith('file://assets')
  const basePath = isAndroid ? 'geojson/Gwangju/' : '/geojson/Gwangju/'

  fetch(`${basePath}filelist.json`)
    .then(res => res.json())
    .then(files => {
      files.forEach(file => {
        fetch(`${basePath}${file}`)
          .then(res => res.json())
          .then(geojson => {
            geojson.features.forEach(feature => {
              const polygon = geoJsonToPolygon(feature)
              if (!polygon) return
              const detailId = recordMap[file] || null
              polygon.detailId = detailId
              polygons.push(polygon)
              polygon.setMap(map)
            })
          })
      })
    })
}

// GeoJSON → Polygon 변환
function geoJsonToPolygon(feature) {
  if (feature.geometry.type !== 'Polygon') return null
  const coords = feature.geometry.coordinates[0].map(c => ({ lat: +c[1], lng: +c[0] }))
  return new google.maps.Polygon({
    paths: coords,
    strokeColor: '#FF0000',
    fillColor: '#FF0000',
    strokeWeight: 0,
    fillOpacity: 0,
    zIndex: 0
  })
}

// WMS 오버레이 추가
function addWMSOverlay() {
  const wmsOptions = {
    getTileUrl: (coord, zoom) => {
      const proj = map.getProjection()
      const zfactor = Math.pow(2, zoom)
      const top = proj.fromPointToLatLng(new google.maps.Point(coord.x * 256 / zfactor, coord.y * 256 / zfactor))
      const bot = proj.fromPointToLatLng(new google.maps.Point((coord.x + 1) * 256 / zfactor, (coord.y + 1) * 256 / zfactor))
      const bbox = `${top.lng()},${bot.lat()},${bot.lng()},${top.lat()}`
      const Wmskey = WmsKey;
      return `https://www.safemap.go.kr/openApiService/wms/getLayerData.do?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=TRUE&SRS=EPSG:4326&BBOX=${bbox}&WIDTH=256&HEIGHT=256&LAYERS=A2SM_CRMNLHSPOT_TOT&STYLES=A2SM_CrmnlHspot_Tot_Rape&apikey=${Wmskey}`
    },
    tileSize: new google.maps.Size(256, 256),
    opacity: 0.6,
    name: '범죄주의구간',
    maxZoom: 19
  }
  const wmsMapType = new google.maps.ImageMapType(wmsOptions)
  map.overlayMapTypes.insertAt(0, wmsMapType)
}
</script>

<style scoped>
html, body {
  margin: 0;
  padding: 0;
  overflow-x: hidden;
  height: 100%;
}
.map-container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  box-sizing: border-box;
}
#map {
  width: 100%;
  height: 100%;
}
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background: rgba(255, 255, 255, 0.9);
  z-index: 10;
}
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
.sidebar {
  position: absolute;
  left: 0;
  top: 0;
  width: 220px;
  height: 100%;
  background: white;
  z-index: 20;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.3);
  transform: translateX(-100%);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
}
.sidebar.open {
  transform: translateX(0);
}
.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 0;
}
.sidebar li {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}
.sidebar li.logout {
  color: red;
}
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