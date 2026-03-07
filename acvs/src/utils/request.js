//导入axios  npm install axios
import axios from 'axios';
//定义一个变量,记录公共的前缀  ,  baseURL
//const baseURL = 'http://localhost:8081';
//当访问时  /api  会被代理到  http://localhost:8081 

// 引入element-plus的消息提示组件
import {
    ElMessage
} from 'element-plus'
const baseURL = '/api';
/* 定制请求的实例 */
const instance = axios.create({
    baseURL
})

//(pinia) 引入token模块的store 
import {
    useTokenStore
} from '@/stores/token';

import router from '@/router';

//添加请求拦截器
instance.interceptors.request.use(
    //请求前的回调
    (config) => {
        //添加token
        const tokenStore = useTokenStore();
        //判断有没有token
        if (tokenStore.token) {
            console.log('发送请求，Token:', tokenStore.token)
            console.log('请求URL:', config.url)
            console.log('请求方法:', config.method)
            config.headers.Authorization = tokenStore.token
        } else {
            console.log('Token为空，无法发送认证请求')
        }
        return config;
    },
    //请求失败
    (err) => {
        return Promise.reject(err)
    }
)

//导入路由器实例
/* import {
    useRoute
} from 'vue-router'; */
//添加响应拦截器
instance.interceptors.response.use(
    result => {
        /* 操作成功 */
        if (result.data.code === 0) {
            return result.data;
        }
        /* 操作失败  运用element-plus的消息提示组件*/
        ElMessage.error(result.data.msg ? result.data.msg : '操作失败');
        //异步操作的状态装换为异常
        return Promise.reject(result.data);



    },
    err => {
        //判断响应状态码如果为401，则表示未授权，跳转到登录页面
        if (err.response.status === 401) {
            ElMessage.error('未授权，请先登录');
            //跳转到登录页面
            router.push('/login');
        } else { //其他异常
            ElMessage.error('服务异常');
            return Promise.reject(err); //异步的状态转化成失败的状态
        }

    }
)
//导出实例
export default instance;