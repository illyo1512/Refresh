<template>
  <div class="container">
    <div class="top-bar">
      <button class="back-btn" @click="router.back()">←</button>
      <h2 class="title">신고 내역 확인</h2>
    </div>

    <div class="search-section">
      <label>검색</label>
      <input v-model="search" placeholder="검색어 입력" />
    </div>

    <div class="filter-section">
      <label>구분</label>
      <label>
        <input type="radio" value="처리" v-model="status" /> 처리
      </label>
      <label>
        <input type="radio" value="미처리" v-model="status" /> 미처리
      </label>
    </div>

    <div class="btn-group">
      <button @click="fetchReports">검색</button>
      <button @click="resetFilters">초기화</button>
    </div>

    <table class="report-table">
      <thead>
        <tr>
          <th>번호</th>
          <th>신고내용</th>
          <th>신고날짜</th>
          <th>처리상태</th>
          <th>상세</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(report, index) in filteredReports" :key="report.id">
          <td>{{ index + 1 }}</td>
          <td>{{ report.content }}</td>
          <td>{{ report.date }}</td>
          <td>{{ report.status }}</td>
          <td><button @click="goToDetail(report.id)">▶</button></td>
        </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button :disabled="page === 1" @click="page--">〈</button>
      <span>{{ page }}</span>
      <button @click="page++">〉</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const search = ref('')
const status = ref('처리')
const reports = ref([])
const page = ref(1)
const pageSize = 10

const fetchReports = async () => {
  try {
    const response = await axios.get('/api/reports', {
      params: {
        search: search.value,
        status: status.value
      }
    })
    reports.value = response.data
  } catch (err) {
    console.error('신고 내역 가져오기 실패', err)
  }
}

const resetFilters = () => {
  search.value = ''
  status.value = '처리'
  fetchReports()
}

const filteredReports = computed(() => {
  const start = (page.value - 1) * pageSize
  const end = start + pageSize
  return reports.value.slice(start, end)
})

const goToDetail = (id) => {
  router.push(`/admin/report/${id}`)
}

onMounted(fetchReports)
</script>

<style scoped>
.container {
  background-color: #dff5f6;
  min-height: 100vh;
  padding: 20px;
  font-family: sans-serif;
}

.top-bar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  margin-right: 8px;
}
.title {
  font-size: 20px;
  font-weight: bold;
}

.search-section,
.filter-section {
  margin: 10px 0;
}
.search-section input {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
  border-radius: 6px;
  border: 1px solid #aaa;
}
.filter-section label {
  margin-right: 15px;
  font-size: 14px;
}

.btn-group {
  display: flex;
  gap: 10px;
  margin: 10px 0 20px;
}
.btn-group button {
  padding: 6px 16px;
  border-radius: 6px;
  border: 1px solid #555;
  cursor: pointer;
  background: white;
}

.report-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}
.report-table th,
.report-table td {
  border: 1px solid #444;
  padding: 8px;
  text-align: center;
}
.report-table thead {
  background: #e6f7ff;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
.pagination button {
  background: none;
  border: none;
  font-size: 18px;
  margin: 0 10px;
  cursor: pointer;
}
</style>
