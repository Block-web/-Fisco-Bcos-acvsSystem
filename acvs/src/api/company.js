import request from '@/utils/request.js'

export const updateCompanyInfoService = (companyInfo) => {
    return request.post('/company/info', companyInfo)
}
// 查询数据库验证证书
export const verifySqlService = (certificate, companyId) => {
    return request.post(`/verifyLog/getVerifyLogSql?companyName=${companyId}`, certificate)
}
// 查询区块链验证证书
export const verifyBlockchainService = (certificate, companyId) => {
    return request.post(`/verifyLog/getVerifyLog?companyName=${companyId}`, certificate)
}