// 导入request.js工具类
import request from '@/utils/request.js'

//提供用户注册服务
export const userRegisterService = (registerData) => {
    // 借助urlSearchParams对象完成传递
    const params = new URLSearchParams()
    //遍历registerData对象，将其键值对添加到params中
    for (let key in registerData) {
        params.append(key, registerData[key]);
    }
    // 发送post请求
    return request.post('/user/register', params)
}

//提供登录服务

export const userLoginService = (loginData) => {
    // 借助urlSearchParams对象完成传递
    const params = new URLSearchParams()
    //遍历registerData对象，将其键值对添加到params中
    for (let key in loginData) {
        params.append(key, loginData[key]);
    }
    // 发送post请求
    return request.post('/user/login', params)
}

//提供获取用户信息服务
export const userInfoService = () => {
    // 发送get请求
    return request.get('/user/userInfo')
}

//提供更新用户信息服务
export const updataUserInfoService = (userInfoData) => {
    return request.put('/user/update', userInfoData)
}

//更新用户头像
export const updateAvatarService = (avatarUrl) => {
    //拼接可以借助urlSearchParams
    const params = new URLSearchParams();  //调用URLSearchParams 获取实例
    params.append('avatarUrl', avatarUrl)
    return request.patch('/user/updateAvatar', params)
}