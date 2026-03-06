<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { GetCertificateService } from '@/api/student.js'

const router = useRouter()
const userInfoStore = useUserInfoStore();
const studentName = ref(userInfoStore.info.realName.split('&')[0])

// 响应式数据
const certificateUrl = ref('') // 证书地址
const isLoading = ref(false) // 加载状态
const downloadLoading = ref(false) // 下载加载状态

// 获取学历证书地址
const getCertificateUrl = async () => {
    isLoading.value = true
    try {
        // 模拟API调用 - 这里需要替换为实际的API
        const result = await GetCertificateService(studentName.value)
        if (result.code === 1) { throw new Error(result.msg) }
        // 模拟返回的证书地址
        certificateUrl.value = result.data

        ElMessage.success('学历证书地址获取成功！')
    } catch (error) {
        ElMessage.error('获取学历证书地址失败：' + error.message)
    } finally {
        isLoading.value = false
    }
}

// 下载学历证书
const downloadCertificate = async () => {
    if (!certificateUrl.value) {
        ElMessage.warning('请先获取学历证书地址')
        return
    }

    downloadLoading.value = true
    try {
        // 模拟下载过程
        await new Promise(resolve => setTimeout(resolve, 1500))

        // 创建下载链接
        const link = document.createElement('a')
        link.href = certificateUrl.value
        link.download = '学历证书_' + new Date().toISOString().split('T')[0] + '.pdf'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)

        ElMessage.success('学历证书下载成功！')
    } catch (error) {
        ElMessage.error('下载学历证书失败：' + error.message)
    } finally {
        downloadLoading.value = false
    }
}

// 复制证书地址到剪贴板
const copyCertificateUrl = () => {
    if (!certificateUrl.value) {
        ElMessage.warning('暂无证书地址可复制')
        return
    }

    navigator.clipboard.writeText(certificateUrl.value)
        .then(() => {
            ElMessage.success('证书地址已复制到剪贴板')
        })
        .catch(() => {
            ElMessage.error('复制失败，请手动复制')
        })
}
</script>

<template>
    <div class="student-info-container">
        <div class="page-header">
            <h2 class="title">学历证书下载</h2>
            <p class="description">获取并下载您的学历证书</p>
        </div>

        <div class="content-area">
            <el-card class="info-card" shadow="hover">
                <template #header>
                    <div class="card-header">
                        <span class="card-title">学历证书操作</span>
                    </div>
                </template>

                <div class="button-group">
                    <el-button type="primary" :loading="isLoading" @click="getCertificateUrl" class="action-button"
                        size="large">
                        <template #loading>
                            <el-icon class="is-loading">
                                <Loading />
                            </el-icon>
                            获取中...
                        </template>
                        <template #default>
                            <el-icon>
                                <Link />
                            </el-icon>
                            获取学历证书地址
                        </template>
                    </el-button>

                    <el-button type="success" :loading="downloadLoading" @click="downloadCertificate"
                        class="action-button" size="large" :disabled="!certificateUrl">
                        <template #loading>
                            <el-icon class="is-loading">
                                <Loading />
                            </el-icon>
                            下载中...
                        </template>
                        <template #default>
                            <el-icon>
                                <Download />
                            </el-icon>
                            下载学历证书
                        </template>
                    </el-button>
                </div>

                <!-- 证书地址显示区域 -->
                <div v-if="certificateUrl" class="url-display-area">
                    <div class="url-label">学历证书地址：</div>
                    <div class="url-content">
                        <el-input v-model="certificateUrl" readonly placeholder="暂无证书地址" class="url-input">
                            <template #append>
                                <el-button @click="copyCertificateUrl" type="text" class="copy-btn">
                                    <el-icon>
                                        <DocumentCopy />
                                    </el-icon>
                                    复制
                                </el-button>
                            </template>
                        </el-input>
                    </div>
                </div>

                <!-- 状态提示 -->
                <div v-else class="status-tip">
                    <el-alert title="请先点击上方按钮获取学历证书地址" type="info" :closable="false" show-icon />
                </div>
            </el-card>
        </div>
    </div>
</template>

<style scoped>
.student-info-container {
    height: 100%;
    padding: 20px;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
    min-height: calc(100vh - 40px);
}

.page-header {
    margin-bottom: 30px;
    text-align: center;
}

.title {
    color: #303133;
    margin-bottom: 10px;
    font-size: 28px;
    font-weight: 600;
    background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.description {
    color: #606266;
    font-size: 16px;
    margin-top: 10px;
}

.content-area {
    display: flex;
    justify-content: center;
    align-items: flex-start;
}

.info-card {
    width: 100%;
    max-width: 700px;
    padding: 0;
    border-radius: 12px;
    overflow: hidden;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
    color: white;
}

.card-title {
    font-size: 18px;
    font-weight: 600;
}

.info-card :deep(.el-card__body) {
    padding: 30px;
}

.button-group {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
    flex-wrap: wrap;
    justify-content: center;
}

.action-button {
    flex: 1;
    min-width: 200px;
    height: 50px;
    font-size: 16px;
    font-weight: 500;
    border-radius: 8px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.action-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(64, 158, 255, 0.3);
}

.action-button:active {
    transform: translateY(0);
}

.url-display-area {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 20px;
    margin-top: 20px;
    border: 1px solid #e4e7ed;
}

.url-label {
    font-weight: 600;
    color: #303133;
    margin-bottom: 10px;
    font-size: 14px;
}

.url-content {
    display: flex;
    gap: 10px;
    align-items: center;
}

.url-input {
    flex: 1;
}

.url-input :deep(.el-input-group__append) {
    background-color: #409EFF;
    border-color: #409EFF;
    color: white;
}

.copy-btn {
    color: white !important;
    padding: 0 15px;
}

.copy-btn:hover {
    background-color: rgba(255, 255, 255, 0.1) !important;
}

.status-tip {
    margin-top: 20px;
}

:deep(.el-alert) {
    border-radius: 8px;
}

:deep(.el-alert__title) {
    font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .button-group {
        flex-direction: column;
    }

    .action-button {
        min-width: 100%;
    }

    .info-card {
        margin: 0 10px;
    }

    .title {
        font-size: 24px;
    }
}

/* 加载动画 */
:deep(.el-loading-spinner .circular) {
    animation: loading-rotate 2s linear infinite;
}

@keyframes loading-rotate {
    100% {
        transform: rotate(360deg);
    }
}
</style>