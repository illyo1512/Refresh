<template>
  <div class="map-wrapper">
    <!-- 경로 입력 영역 -->
    <div class="route-bar">
      <div class="top-bar">
        <button @click="goBack" class="back-btn">←</button>
      </div>

      <!-- 출발지 -->
      <div class="route-row">
        <span class="label" @click="goSearch('start')">📍 출발지:</span>
        <span class="location">{{ start || '선택 안됨' }}</span>
      </div>

      <!-- 경유지 리스트 -->
      <div v-for="(wp, index) in waypoints" :key="index" class="route-row">
        <span class="label" @click="goSearch('waypoint', index)">➕ 경유지 {{ index + 1 }}:</span>
        <span class="location">{{ wp }}</span>
        <button class="remove-btn" @click="removeWaypoint(index)">✕</button>
      </div>

      <!-- 도착지 -->
      <div class="route-row">
        <span class="label" @click="goSearch('end')">🏁 도착지:</span>
        <span class="location">{{ end || '선택 안됨' }}</span>
      </div>

      <button class="add-btn" @click="addWaypoint">➕ 경유지 추가</button>
    </div>

    <!-- 지도 영역 -->
    <div id="map" class="map-area"></div>

    <!-- 경로 진단 버튼 -->
    <button class="analyze-btn" @click="drawRoute">경로 안전도 진단</button>

    <!-- 즐겨찾기 저장 버튼 -->
    <button v-if="routeResult" class="analyze-btn" style="bottom: 70px" @click="saveToRoutes">
      ⭐ 즐겨찾기 저장
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getCurrentInstance } from 'vue'

const { appContext } = getCurrentInstance()
const apiKey = appContext.config.globalProperties.$GoogleMapKey
const BackEndUrl = import.meta.env.VITE_BACKEND_URL

// 상태 변수
const start = ref('');
const end = ref('');
const waypoints = ref([]);
const markers = []; // 지도 마커들
const routeResult = ref(null); // 경로 분석 결과
const showSaveButton = ref(false); // 저장 버튼 표시 여부
let map = null; // Google Map 인스턴스

const route = useRoute();
const router = useRouter();

// 뒤로가기
function goBack() {
  router.back();
}

// 경유지 추가
function addWaypoint() {
  waypoints.value.push('');
}

// 경유지 삭제
function removeWaypoint(index) {
  waypoints.value.splice(index, 1);
}

// 검색 페이지 이동
function goSearch(target, index = null) {
  const query = {
    target,
    ...(index !== null && { index }),
    start: start.value,
    end: end.value,
    waypoints: JSON.stringify(waypoints.value)
  };
  router.push({ name: 'Search', query });
}

// 주소 → 좌표 변환
function geocodeAddress(address) {
  return new Promise((resolve, reject) => {
    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({ address }, (results, status) => {
      if (status === 'OK') {
        const loc = results[0].geometry.location;
        resolve({ lat: loc.lat(), lng: loc.lng() });
      } else {
        reject(`주소 변환 실패 (${address}): ${status}`);
      }
    });
  });
}

// 지도에 마커만 갱신
async function updateMarkersOnly() {
  if (!map) return;
  markers.forEach(m => m.setMap(null)); // 기존 마커 제거
  markers.length = 0;

  const points = [start.value, ...waypoints.value, end.value];
  const bounds = new google.maps.LatLngBounds();

  for (let i = 0; i < points.length; i++) {
    if (!points[i]) continue;
    try {
      const loc = await geocodeAddress(points[i]);
      const label = i === 0 ? 'S' : i === points.length - 1 ? 'E' : `${i}`;
      const marker = new google.maps.Marker({ map, position: loc, label });
      markers.push(marker);
      bounds.extend(loc);
    } catch (err) {
      console.warn('마커 주소 변환 실패:', err);
    }
  }

  if (!bounds.isEmpty()) {
    map.fitBounds(bounds);
  }
}

// 경로 분석 API 호출 및 지도 표시
async function drawRoute() {
  if (!start.value || !end.value) {
    alert('출발지 또는 도착지를 입력하세요.');
    return;
  }

  try {
    const [startCoord, endCoord] = await Promise.all([
      geocodeAddress(start.value),
      geocodeAddress(end.value)
    ]);

    const query = new URLSearchParams({
      startLat: startCoord.lat,
      startLng: startCoord.lng,
      endLat: endCoord.lat,
      endLng: endCoord.lng,
      transportMode: "foot"
    });

    const res = await fetch(`${BackEndUrl}/api/navigation/route-with-danger-check?${query.toString()}`, {
      method: "POST"
    });

    if (!res.ok) throw new Error(`서버 요청 실패: ${res.status}`);
    const data = await res.json();
    routeResult.value = data;
    showSaveButton.value = true;

    // 위험 경로 안내
    if (data.passesThroughDangerZone) {
      alert(`⚠️ 위험 경로 포함됨\n추천: ${data.recommendedRoute}\n위험도: ${data.dangerZoneInfo.riskLevel}`);
    } else {
      alert("✅ 안전한 경로입니다.");
    }

    // 지도에 경로 표시
    if (data.basicRoute) {
      const basicPath = data.basicRoute.coordinates.map(p => ({ lat: p.y, lng: p.x }));
      new google.maps.Polyline({
        path: basicPath,
        map,
        strokeColor: "#FF0000",
        strokeOpacity: 1.0,
        strokeWeight: 4
      });
    }

    if (data.avoidRoute) {
      const avoidPath = data.avoidRoute.coordinates.map(p => ({ lat: p.y, lng: p.x }));
      new google.maps.Polyline({
        path: avoidPath,
        map,
        strokeColor: "#28a745",
        strokeOpacity: 1.0,
        strokeWeight: 4
      });
    }

  } catch (err) {
    console.error(err);
    alert("경로 분석 실패: " + err.message);
  }
}

function cleanCoordinates(obj) {
  const clone = JSON.parse(JSON.stringify(obj));
  ['basicRoute', 'avoidRoute'].forEach((routeType) => {
    if (clone[routeType] && Array.isArray(clone[routeType].coordinates)) {
      clone[routeType].coordinates = clone[routeType].coordinates.map(coord => {
        const { z, m, ...rest } = coord;
        return rest;
      });
    }
  });
  return clone;
}


async function saveToRoutes() {
  const userId = localStorage.getItem("userId");
  if (!userId) {
    const move = confirm("로그인이 필요합니다. 로그인 화면으로 이동할까요?");
    if (move) router.push("/login");
    return;
  }

  if (!routeResult.value) {
    alert("⚠️ 분석된 경로가 없습니다.");
    return;
  }

  try {
   const cleaned = cleanCoordinates(routeResult.value);

const res = await fetch(`${BackEndUrl}/api/navigation/save-route`, {
  method: "POST",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify({
  userId: parseInt(userId), // ← 여기 정수로 강제 형변환
  routeName: `${start.value} → ${end.value}`,
  routeResult: cleaned
})
});
    if (!res.ok) {
      const msg = await res.text();
      throw new Error(msg);
    }

    alert("⭐ 경로 저장 완료!");
  } catch (err) {
    console.error(err);
    alert("저장 실패: " + err.message);
  }
}


// 지도 초기화
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 35.15, lng: 126.85 },
    zoom: 14
  });
  updateMarkersOnly();
}

// 구글 맵 스크립트 로드 및 초기화
onMounted(() => {
  if (typeof document !== 'undefined' && document.head) {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places,geometry`;
    script.async = true;
    script.defer = true;
    script.onload = () => {
      initMap();
      fetchDangerRecords(); // 위험 지역 표시 함수 (외부 정의 필요)
    };
    document.head.appendChild(script);
  } else {
    console.error('document.head가 준비되지 않았습니다.');
  }
});

// 페이지 진입 시 URL 쿼리 반영
watch(() => route.query, (q) => {
  start.value = q.start || '';
  end.value = q.end || '';
  try {
    waypoints.value = JSON.parse(q.waypoints || '[]');
  } catch {
    waypoints.value = [];
  }
}, { immediate: true });

// 출발/도착/경유지 변경 시 지도 마커 업데이트
watch([start, end, waypoints], updateMarkersOnly, { deep: true });
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
  .analyze-btn:nth-of-type(2) {
    bottom: 70px;
  }
  .analyze-btn:nth-of-type(1) {
    bottom: 20px;
  }
  </style>
