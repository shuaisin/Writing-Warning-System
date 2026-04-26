import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
    plugins: [vue()],
    server: {
        port: 5173,
        hmr: {
            overlay: true
        },
        // --- 新增以下代理配置 ---
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // 这里对准你截图里的 8080 端口
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, '') // 如果后端接口没带 /api 前缀，这一行很重要
            }
        }
    }
})