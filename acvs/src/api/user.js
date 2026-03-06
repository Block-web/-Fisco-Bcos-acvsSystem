import request from '@/utils/request.js'

//提供登录服务
export const userLoginService = (loginData) => {
    // 借助urlSearchParams对象完成传递
    const params = new URLSearchParams()
    //遍历loginData对象，将其键值对添加到params中
    for (let key in loginData) {
        params.append(key, loginData[key]);
    }
    // 发送post请求  返回jwt令牌
    return request.post('/user/login', params)
}

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

// 通过用户名查询用户信息   后端通过储存在 ThreadLocal中的jwt令牌获取用户信息
export const getUserInfoService = () => {
    return request.get('/user/userInfo')
}

//更新密码
export const UpdatePasswordService = (username, password) => {
    // 使用URLSearchParams传递参数，因为后端接口是通过URL参数接收的
    const params = new URLSearchParams()
    params.append('username', username)
    params.append('password', password)
    return request.put(`/user/update-password?${params.toString()}`)
}

//上传pdf
export const updatePDFService = (pdfUrl) => {
    //拼接可以借助urlSearchParams
    const params = new URLSearchParams();  //调用URLSearchParams 获取实例
    params.append('avatarUrl', pdfUrl)
    return request.patch('/user/updatePDF', params)
}