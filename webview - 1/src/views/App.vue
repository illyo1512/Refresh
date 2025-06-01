<template>
  <router-view />
  </template>

<script setup>
import { ref, watch } from 'vue'

const currentPage = ref('home')

// 지도 로딩
let mapLoaded = false

watch(currentPage, (newPage) => {
  if (newPage === 'map' && !mapLoaded) {
    const apiKey = window.__MY_GOOGLE_MAPS_API_KEY__;

    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&libraries=places`;

    script.async = true
    script.defer = true
    document.head.appendChild(script)

    window.initMap = () => {
      new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.5665, lng: 126.978 },
        zoom: 14,
      })
    }

    mapLoaded = true
  }
})
</script>
