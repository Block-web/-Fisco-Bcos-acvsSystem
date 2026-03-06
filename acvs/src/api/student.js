import request from '@/utils/request.js'
//提供更新基本信息服务
export const updateStudentInfoService = (studentInfo) => {
    // 发送post请求
    return request.put('/user/update', studentInfo)
}

//提供获取学历证书服务
export const GetCertificateService = (studentName) => {
    // 发送get请求
    return request.get('/certificate/get-certificate',
        {
            params: {
                studentName
            }
        }
    )
}