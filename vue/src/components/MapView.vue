<template>
  <div class="map-container">
    <div ref="map" class="map"></div>
  </div>
</template>

<script>
export default {
  name: 'MapView',
  mounted() {
    this.loadGoogleMapsScript();
  },
  methods: {
    loadGoogleMapsScript() {
      // 이미 스크립트가 로드된 경우 바로 지도 렌더링
      if (window.google && window.google.maps) {
        this.renderMap();
        return;
      }

      // 중복 로딩 방지
      if (document.getElementById('google-maps-script')) return;

      const script = document.createElement('script');
      script.id = 'google-maps-script';
      script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBLRX25Kd0bDgua2i3xZvcNGyt6a7M0fn0&callback=initMapCallback';
      script.async = true;
      script.defer = true;

      // 전역 콜백 함수 정의
      window.initMapCallback = this.renderMap;

      document.head.appendChild(script);
    },
    renderMap() {
      const map = new window.google.maps.Map(this.$refs.map, {
        center: { lat: 35.1595, lng: 126.8526 },
        zoom: 14,
      });

      new window.google.maps.Marker({
        position: { lat: 35.1595, lng: 126.8526 },
        map: map,
        title: '광주 중심',
      });
    },
  },
};
</script>

<style scoped>
.map-container {
  width: 100%;
  height: 100vh;
}
.map {
  width: 100%;
  height: 100%;
}
</style>
