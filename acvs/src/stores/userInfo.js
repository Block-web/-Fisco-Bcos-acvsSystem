//定义store
import {
    defineStore
} from 'pinia'
import {
    ref
} from 'vue'
/* 
   第一个参数：名字，必须唯一
   第二个参数：函数，函数内部可以定义状态的所有内容

   返回值：函数
*/
//调用defineStore函数定义一个store模块
const useUserInfoStore = defineStore('userInfo', () => {
    const info = ref({}) 
    const setInfo = (newInfo) => {
        info.value = newInfo
    }
    const removeInfo = () => {
        info.value = {}
    }
    return {
        info,
        setInfo,
        removeInfo
    }
}, {
    //开启持久化存储   需要引入插件
    persist: true
})

//导出userInfoStore模块
export default useUserInfoStore;