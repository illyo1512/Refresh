import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import json from '@rollup/plugin-json'
import path from 'path'

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd()) // ← .env 로드

  return {
    plugins: [
      vue(),
      json()
    ],
    base: './',
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    },
    server: {
      allowedHosts: 'all',
      host: true,
      port: 5173,
      proxy: {
        '/api': {
          target: env.VITE_BACKEND_URL, // ← 여기에 적용
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    }
  }
})
