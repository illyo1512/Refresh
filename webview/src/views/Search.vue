<template>
  <div class="top-bar">
    <!-- 뒤로가기 버튼 -->
    <button @click="goBack" class="back-btn">←</button>
    <h2 class="title">장소 검색</h2>
  </div>

  <div class="search-page">
    <!-- 검색창 및 버튼 -->
     <!-- 엔터로 검색 -->
    <div class="search-row">
      <input
        id="autocomplete"
        v-model="query"
        placeholder="장소를 입력하세요"
        @keyup.enter="search" 
      />
      <button @click="search">검색</button>
      <button @click="goToMapSelect">지도에서 선택</button>
    </div>

    <!-- 최근 검색어 목록 -->
    <h3>최근 검색어</h3>
    <ul class="recent-list">
      <li v-for="(item, index) in recentSearches" :key="index">
        <span @click="reSelect(item)">{{ item }}</span>
        <button class="x-btn" @click="remove(index)">✕</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getCurrentInstance } from 'vue'

const { appContext } = getCurrentInstance()
const apiKey = appContext.config.globalProperties.$GoogleMapKey

const router = useRouter()
const route = useRoute()

// 검색어 입력값
const query = ref('')
// 최근 검색어 리스트
const recentSearches = ref([])

// ← 버튼
function goBack() {
  router.back()
}

// 검색 실행
function search() {
  const rawInput = document.getElementById('autocomplete')?.value || ''
  query.value = rawInput.trim()

  if (!query.value) {
    alert('검색어를 입력하세요')
    return
  }

  addToRecent(query.value)

  const newParams = { name: query.value }
  const newQuery = { ...route.query }

  // 현재 검색 결과 페이지와 동일하면 replace, 아니면 push
  const current = router.currentRoute.value
  const goingToSameResult =
    current.name === 'SearchResult' &&
    current.params.name === query.value

  if (goingToSameResult) {
    router.replace({ name: 'SearchResult', params: newParams, query: newQuery })
  } else {
    router.push({ name: 'SearchResult', params: newParams, query: newQuery })
  }
}

// 지도에서 선택하기 버튼 클릭 시 → MapSelect로 이동
function goToMapSelect() {
  router.push({
    name: 'MapSelect',
    query: {
      target: route.query.target || 'start',
      index: route.query.index // 경유지일 경우 index 필요
    }
  })
}

// 최근 검색어 목록에 추가 (중복 제거 + 최대 10개 유지)
function addToRecent(place) {
  recentSearches.value = [
    place,
    ...recentSearches.value.filter((item) => item !== place)
  ].slice(0, 10)
}

// 최근 검색어 클릭 시 → 다시 검색창에 반영
function reSelect(place) {
  query.value = place
}

// 특정 최근 검색어 삭제
function remove(index) {
  recentSearches.value.splice(index, 1)
}

// 컴포넌트 마운트 시 초기화
onMounted(() => {
  // localStorage에서 최근 검색어 불러오기
  const saved = localStorage.getItem('recentSearches')
  if (saved) recentSearches.value = JSON.parse(saved)

  // Google Maps Places API Autocomplete 로딩
  const script = document.createElement('script')
  script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places&callback=initMap`
  script.async = true
  script.onload = () => {
    const input = document.getElementById('autocomplete')
    const autocomplete = new google.maps.places.Autocomplete(input, {
      componentRestrictions: { country: 'kr' }
    })

    // 자동완성에서 장소 선택되었을 때 → query에 주소 저장
    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace()
      query.value = place.formatted_address || place.name
    })
  }
  document.head.appendChild(script)

  // 지도에서 선택 후 돌아왔을 경우 → query에서 selectedAddress 사용
  if (route.query.selectedAddress) {
    query.value = route.query.selectedAddress
    addToRecent(query.value)
  }
})

// 최근 검색어 변경 시 → localStorage에 저장
watch(recentSearches, (newList) => {
  localStorage.setItem('recentSearches', JSON.stringify(newList))
}, { deep: true })
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
  margin-right: 32px; /* ← back-btn 공간 보정용 */
}

.search-page {
  padding: 20px;
  text-align: center;
}

.search-row {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}

.search-row input {
  padding: 8px;
  width: 60%;
  font-size: 16px;
}

.search-row button {
  padding: 8px 12px;
  font-size: 14px;
  cursor: pointer;
}

.recent-list {
  list-style: none;
  padding: 0;
}
.recent-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 12px;
  border-bottom: 1px solid #ccc;
}
.recent-list span {
  cursor: pointer;
}
.x-btn {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
}
</style>
