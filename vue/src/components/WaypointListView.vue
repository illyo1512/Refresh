<template>
  <div class="waypoint-container">
    <div class="back" @click="goBack">←</div>

    <div class="inputs">
      <input v-model="start" placeholder="출발지 입력" class="input" />
      <input v-model="end" placeholder="도착지 입력" class="input" />
    </div>

    <button class="add-button" @click="addWaypoint">+ 경유지 추가</button>

    <ul class="waypoint-list">
      <li v-for="(wp, idx) in waypoints" :key="idx">{{ wp }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      start: '',
      end: '',
      waypoints: [],
    };
  },
  methods: {
    goBack() {
      this.$router.push({ name: 'RouteMapView' }); // 또는 window.history.back()
    },
    addWaypoint() {
      const newWP = prompt("경유지를 입력하세요:");
      if (newWP) {
        this.waypoints.push(newWP);
      }
    },
  },
};
</script>

<style scoped>
.waypoint-container {
  padding: 20px;
  font-family: 'Noto Sans KR', sans-serif;
}
.back {
  font-size: 24px;
  cursor: pointer;
}
.inputs {
  margin-top: 10px;
}
.input {
  display: block;
  width: 100%;
  margin: 5px 0;
  padding: 10px;
  font-size: 16px;
}
.add-button {
  margin: 10px 0;
  padding: 8px;
  font-size: 16px;
}
.waypoint-list {
  list-style: none;
  padding: 0;
}
</style>
