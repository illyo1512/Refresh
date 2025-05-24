<template>
  <div class="result-container">
    <!-- 헤더 -->
    <div class="header">
      <button class="icon-button" @click="goBack">←</button>
      <span class="header-title">{{ name }}</span>
    </div>

    <!-- 장소 정보 카드 -->
    <div class="info-card">
      <div class="info-top">
        <div>
          <strong class="main-name">{{ name }}</strong>
          <p class="sub-text">{{ address }}</p>
        </div>
        <div class="rating-block">
          <p class="phone">전화번호: {{ phone }}</p>
          <div class="stars">⭐️⭐️⭐️☆☆</div>
        </div>
      </div>
      <p class="hours">
        운영시간: {{ hours }}
        <button class="toggle-arrow" @click="toggleDetail">{{ showDetail ? '▲' : '▼' }}</button>
      </p>

      <div v-if="showDetail" class="extra-info">
        📌 이 장소는 학생 식당, 도서관, 정문 근처에 위치해 있습니다.<br />
        주차 가능 여부: 불가능<br />
        휴일 운영 여부: 미운영
      </div>

      <div class="button-group">
        <button class="tag-btn">출발지로 설정</button>
        <button class="tag-btn">경유지로 설정</button>
        <button class="tag-btn">목적지로 설정</button>
      </div>
    </div>

    <!-- 추천 관광지 영역 -->
    <div
      class="recommend-panel"
      :style="{ height: recommendHeight + 'px' }"
      ref="recommendPanel"
    >
      <div
        class="drag-bar"
        @mousedown="startDrag"
      ></div>
      <div class="recommend-content">
        <p class="recommend-title">주변에 좋은 관광지가 있어요!</p>

        <div
          class="recommend-place"
          v-for="(place, index) in nearbyPlaces"
          :key="index"
        >
          <div class="place-title">
            <strong>{{ place.name }}</strong>
            <span>{{ place.distance }}m</span>
          </div>
          <div class="info-row">
            <p class="sub-text">{{ place.address }}</p>
            <p class="sub-text">전화번호: {{ place.phone || '-' }}</p>
          </div>
          <p class="sub-text">운영시간: {{ place.hours || '-' }}</p>
          <div class="bottom-row">
            <div class="stars">☆☆☆☆☆</div>
            <div>
              <button class="tag-btn">경유지로 설정</button>
              <button class="tag-btn">목적지로 설정</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['name'],
  data() {
    return {
      showDetail: false,
      address: '광주 남구 행암동',
      phone: '062-670-2114',
      hours: '월요일 오전 9:00 ~ 오후 6:00',
      nearbyPlaces: [
        { name: '광주대학교 전산관', address: '광주 남구 행암동', distance: 500, phone: '', hours: '' },
        { name: '광주대학교 종합운동장', address: '광주 남구 행암동', distance: 270, phone: '', hours: '' },
        { name: '노천 극장', address: '광주 남구 행암동', distance: 320, phone: '', hours: '' },
      ],
      isDragging: false,
      startY: 0,
      startHeight: 200,
      recommendHeight: 200,
      minHeight: 100,
      maxHeight: 500
    };
  },
  methods: {
    goBack() {
      this.$router.back();
    },
    toggleDetail() {
      this.showDetail = !this.showDetail;
    },
    startDrag(e) {
      this.isDragging = true;
      this.startY = e.clientY;
      this.startHeight = this.recommendHeight;
      document.addEventListener('mousemove', this.onDrag);
      document.addEventListener('mouseup', this.stopDrag);
    },
    onDrag(e) {
      if (!this.isDragging) return;
      const delta = this.startY - e.clientY;
      let newHeight = this.startHeight + delta;
      newHeight = Math.min(this.maxHeight, Math.max(this.minHeight, newHeight));
      this.recommendHeight = newHeight;
    },
    stopDrag() {
      this.isDragging = false;
      document.removeEventListener('mousemove', this.onDrag);
      document.removeEventListener('mouseup', this.stopDrag);
    }
  }
};
</script>

<style scoped>
.result-container {
  position: relative;
  height: 100vh;
  max-width: 480px;
  margin: 0 auto;
  font-family: 'Noto Sans KR', sans-serif;
  background-color: white;
  overflow: hidden;
}
.header {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #000;
  padding: 10px;
}
.icon-button {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  margin-right: 10px;
}
.header-title {
  font-size: 18px;
  font-weight: bold;
}
.info-card {
  padding: 12px;
  border-bottom: 1px solid #000;
}
.info-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.main-name {
  font-size: 16px;
}
.sub-text {
  font-size: 13px;
  color: #333;
  margin: 2px 0;
}
.phone {
  font-size: 13px;
  margin-bottom: 4px;
}
.stars {
  font-size: 16px;
  color: #f5c518;
}
.hours {
  font-size: 13px;
  margin: 4px 0;
  display: flex;
  align-items: center;
}
.toggle-arrow {
  background: none;
  border: none;
  font-size: 14px;
  margin-left: 6px;
  cursor: pointer;
}
.extra-info {
  background: #f2f2f2;
  padding: 8px;
  font-size: 13px;
  border-radius: 6px;
  margin-top: 6px;
  line-height: 1.5;
}
.button-group {
  display: flex;
  gap: 6px;
  margin-top: 6px;
}
.tag-btn {
  background-color: #eee;
  border: 1px solid #aaa;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
}
.recommend-panel {
  position: absolute;
  bottom: 0;
  width: 100%;
  background: #fff;
  border-top: 1px solid #ccc;
  overflow: hidden;
  transition: height 0.2s ease;
}
.drag-bar {
  width: 40px;
  height: 6px;
  background: black;
  border-radius: 3px;
  margin: 8px auto;
  cursor: ns-resize;
}
.recommend-content {
  padding: 8px 12px;
}
.recommend-title {
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  margin: 8px 0;
}
.recommend-place {
  border-top: 1px solid #aaa;
  padding: 8px 0;
}
.place-title {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  margin-bottom: 4px;
}
.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
.bottom-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 4px;
}
</style>
