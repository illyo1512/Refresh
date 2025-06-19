<template>
  <div class="container">
    <div class="top-bar">
      <button @click="router.back()" class="back-btn">←</button>
      <h2 class="title">범례</h2>
    </div>

    <div class="legend-table">
      <div class="legend-row header">
        <div class="label"></div>
        <div class="time">낮</div>
        <div class="time">밤</div>
      </div>

      <div v-for="item in legendItems" :key="item.label" class="legend-row">
        <div class="label">{{ item.label }}</div>
        <div class="color-box" :style="{ backgroundColor: item.day }"></div>
        <div class="color-box" :style="{ backgroundColor: item.night }"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const legendItems = [
  { label: '매우 위험', day: '#FF0000', night: '#AA1111' },
  { label: '위험', day: '#FFA500', night: '#B45309' },
  { label: '보통', day: '#FFFF00', night: '#B6B700' },
  { label: '안전', day: '#66FF99', night: '#45B567' },
  { label: '매우 안전', day: '#7FD8FF', night: '#1E50FF' },
]
</script>

<style scoped>
.container {
  background-color: #e0f7fa;
  min-height: 100vh;
  padding: 16px;
}

.top-bar {
  display: flex;
  align-items: center;
  height: 56px;
  border-bottom: 1px solid #ccc;
  margin-bottom: 16px;
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

.legend-table {
  background: white;
  padding: 16px;
  border-radius: 12px;
  max-width: 400px;
  margin: 0 auto;
}

.legend-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.header {
  font-weight: bold;
  font-size: 16px;
}

.label {
  width: 80px;
}

.time {
  width: 60px;
  text-align: center;
}

.color-box {
  width: 60px;
  height: 30px;
  border-radius: 4px;
  border: 1px solid #999;
  margin-left: 10px;
}
</style>
