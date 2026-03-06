import { createApp } from 'vue'
import { createPinia } from 'pinia'
//pinia持久化插件
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'
import 'element-plus/dist/index.css'   // 全局基础样式 + 图标字体
//导入中文语言包
import locale from 'element-plus/dist/locale/zh-cn.js'
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(locale)
app.mount('#app')
