import request from '@/utils/request.js'

export const updateCompanyInfoService = (companyInfo) => {
    return request.post('/company/info', companyInfo)
}
