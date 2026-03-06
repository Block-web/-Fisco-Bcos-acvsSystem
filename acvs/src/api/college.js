import request from '@/utils/request.js'

//提供更新基本信息服务
export const updateCollegeInfoService = (collegeInfo) => {
    // 发送post请求
    return request.put('/user/update', collegeInfo)
}

// 提供上传链上文件服务
export const issueChainService = (chainData) => {
    // 发送post请求
    return request.post('/certificate/issue', chainData)
}
