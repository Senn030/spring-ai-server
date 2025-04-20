import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vueJsx()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    port: 5188,
    proxy: {
      '/api': {
        target: 'http://localhost:9909',
        changeOrigin: true,
        rewrite: (path) => {
          console.log('原始路径：', path)
          const rewrittenPath = path.replace(/^\/api/, '')
          console.log('重写后的路径：', rewrittenPath)
          return rewrittenPath
        }
      }
    }
  }
})
