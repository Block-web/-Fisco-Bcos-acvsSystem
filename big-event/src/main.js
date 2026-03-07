import './assets/main.scss'

import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
//导入路由
import router from '@/router/index.js'
//导入pinia
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
//导入中文语言包
import locale from 'element-plus/dist/locale/zh-cn.js'


//创建pinia实例
const pinia = createPinia()
const app = createApp(App)
//创建pinia持久化插件 PersistedState
const persist = createPersistedState();
//pinia 使用插件
pinia.use(persist);

//使用pinia
app.use(pinia)
//使用路由
app.use(router)
//使用element-plus
app.use(ElementPlus,{locale})
//挂载应用
app.mount('#app')

