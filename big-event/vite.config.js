import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { Server } from 'node:http'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },

  /* 配置代理 */
  server:{
    proxy:{
      '/api':{  //获取路径中包含 ‘/api’ 的请求
        target:'http://localhost:8080',  //后台服务所在的源
        changeOrigin:true,  //是否修改源
        /* 路径重写   /api 替换为空字符串*/
        rewrite:path=>path.replace(/^\/api/,'')
      }
    }
  }
})


