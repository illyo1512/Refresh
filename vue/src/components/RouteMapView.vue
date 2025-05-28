<template>
  <div class="map-container">
    <div class="input-group">
      <input v-model="start" placeholder="출발지 입력" class="input-box" />
      <input v-model="destination" placeholder="도착지 입력" class="input-box" />
    </div>
    <div ref="map" class="map"></div>
    <button @click="analyzeRoute" class="analyze-button">경로 안전도 진단</button>
  </div>
</template>

<script>
export default {
  data() {
  return {
    map: null,
    start: '',
    destination: '',
    markers: [], // 여러 마커를 저장하는 배열로 변경
  };
},
 
  mounted() {
    if (window.google && window.google.maps) {
      this.initMap();
      return;
    }

    if (document.getElementById('google-maps-script')) return;

    const script = document.createElement('script');
    script.id = 'google-maps-script';
    script.src =
      'https://maps.googleapis.com/maps/api/js?key=AIzaSyBLRX25Kd0bDgua2i3xZvcNGyt6a7M0fn0&callback=initMapCallback';
    script.async = true;
    script.defer = true;

    window.initMapCallback = this.initMap;

    document.head.appendChild(script);
  },
  methods: {
    initMap() {
      if (!window.google || !window.google.maps) {
        // 구글 맵 API가 아직 로드되지 않았으면 재시도
        setTimeout(this.initMap, 100);
        return;
      }
      this.map = new window.google.maps.Map(this.$refs.map, {
        center: { lat: 35.146, lng: 126.922 },
        zoom: 16,
      });

      this.map.addListener('click', (e) => {
        const latLng = e.latLng;

        // 기존 마커 제거
        if (this.marker) {
          this.marker.setMap(null);
        }

        // 새로운 마커 생성
        this.marker = new window.google.maps.Marker({
          position: latLng,
          map: this.map,
        });

        // 지도 중앙 이동
        this.map.panTo(latLng);

        // 도착지 입력창에 좌표 표시
        this.destination = `(${latLng.lat().toFixed(5)}, ${latLng.lng().toFixed(5)})`;
      });
    },
    analyzeRoute() {
      alert(`출발지: ${this.start}\n도착지: ${this.destination}\n(여기서 경로 분석 로직 실행)`);
    },
  },
};
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.input-group {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  display: flex;
  flex-direction: column;
  gap: 6px;
  z-index: 10;
}

.input-box {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border-radius: 10px;
  border: 1px solid #ccc;
  background-color: white;
}

.map {
  width: 100%;
  height: 100%;
}

.analyze-button {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  width: 80%;
  padding: 15px;
  font-size: 18px;
  background-color: #4c6ef5;
  color: white;
  border: none;
  border-radius: 30px;
  z-index: 10;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}
</style>
