import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 5174, //ui-admin用5173，这里错开
    proxy: {
      '/api': { //请求路径中包含了/api
        target: 'http://localhost:8081', //要更换的源,也就是后台服务的源
        changeOrigin: true, //要不要更换源
        rewrite: (path) => path.replace(/^\/api/, '') //路径重写/api替换为’’
      }
    }
  }
})
