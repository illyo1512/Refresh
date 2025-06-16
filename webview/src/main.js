import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

axios.defaults.baseURL = import.meta.env.VITE_BACKEND_URL

const app = createApp(App)

app.config.globalProperties.$axios = axios // ✅ 전역 등록
app.config.globalProperties.$GoogleMapKey = import.meta.env.VITE_GOOGLE_MAP_KEY
app.config.globalProperties.$WmsKey = import.meta.env.VITE_WMS_KEY

app.use(router).mount('#app')
