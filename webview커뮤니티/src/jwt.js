// src/api/axios.js
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL, // 실제 API 주소
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청 시 JWT 토큰 자동 추가
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
