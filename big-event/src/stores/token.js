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
//调用defineStore函数定义一个store模块    //模块名字：token
export const useTokenStore = defineStore('token', () => {
    //定义状态的内容

    //1.定义响应式变量
    const token = ref('')

    //2.定义一个函数用来修改token的值
    const setToken = (newToken) => {
        token.value = newToken
    }

    //3.定义一个函数用于移除token的值
    const removeToken = () => {
        token.value = ''
    }

    return {
        token,
        setToken,
        removeToken
    }
}, {
    //开启持久化存储
    persist: true
});
export default useTokenStore;