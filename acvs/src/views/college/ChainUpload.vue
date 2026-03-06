<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import { issueChainService } from '@/api/college'

const userInfoStore = useUserInfoStore();

// 表单数据
const chainData = ref({
    studentName: '',
    idCard: '',
    collegeId: userInfoStore.info.id,
    major: '',
    degreeType: '',
    graduationDate: '',
    pdfPath: '',
    fileHash: '     '
})
// 提交状态
const submitting = ref(false)

// 表单验证规则
const rules = {
    studentName: [
        { required: true, message: '请输入学生姓名', trigger: 'blur' }
    ],
    idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /^\d{17}[\dXx]$/, message: '请输入正确的身份证号格式', trigger: 'blur' }
    ],
    collegeId: [
        { required: true, message: '请输入学院ID', trigger: 'blur' }
    ],
    major: [
        { required: true, message: '请输入专业', trigger: 'blur' }
    ],
    degreeType: [
        { required: true, message: '请选择学位类型', trigger: 'change' }
    ],
    graduationDate: [
        { required: true, message: '请选择毕业日期', trigger: 'change' }
    ],
    pdfPath: [
        { required: true, message: '请输入PDF文件URL地址', trigger: 'blur' }
    ],
    fileHash: [
        { required: true, message: '请输入PDF文件哈希值', trigger: 'blur' }
    ]
}

// 学位类型选项
const degreeTypes = [
    { label: '本科', value: '本科' },
    { label: '硕士', value: '硕士' },
    { label: '博士', value: '博士' },
    { label: '专科', value: '专科' }
]



// URL验证状态
const urlValid = ref(false)
const urlValidating = ref(false)

// URL地址输入处理
const handleUrlInput = () => {
    if (!chainData.value.pdfPath) {
        urlValid.value = false
        return
    }

    urlValidating.value = true

    // 模拟URL验证过程
    setTimeout(() => {
        // 简单的URL格式验证
        const urlRegex = /^(https?|ftp):\/\/[^\s/$.?#].[^\s]*$/i
        urlValid.value = urlRegex.test(chainData.value.pdfPath)

        if (urlValid.value) {
            // 模拟生成文件哈希  生成的哈希值赋值给formData.value.fileHash

        } else {
            chainData.value.fileHash = ''
            ElMessage.error('请输入有效的URL地址')
        }

        urlValidating.value = false
    }, 1000)
}

// 提交表单到区块链
const submitToBlockchain = async () => {
    try {
        submitting.value = true

        // 验证必填字段
        if (!chainData.value.studentName || !chainData.value.idCard ||
            !chainData.value.collegeId || !chainData.value.major ||
            !chainData.value.degreeType || !chainData.value.graduationDate ||
            !chainData.value.pdfPath || !chainData.value.fileHash) {
            ElMessage.error('请填写完整的学历信息和有效的PDF文件URL')
            submitting.value = false
            return
        }

        //调用issueChainService函数
        const result = await issueChainService(chainData.value)
        if (result.code === 0) {
            ElMessage.success(result.msg ? result.msg : '学历信息已成功上链！')
        } 
        // 重置表单
        resetForm()

    } catch (error) {
        ElMessage.error('上链失败，请重试')
    } finally {
        submitting.value = false
    }
}

// 表单提交处理
const handleSubmit = () => {
    // 验证URL是否有效
    if (!urlValid.value || !chainData.value.fileHash) {
        ElMessage.warning('请先输入有效的PDF文件URL地址')
        return
    }

    ElMessageBox.confirm(
        `确认将以下学历信息上传到区块链？\n\n学生姓名：${chainData.value.studentName}\n身份证号：${chainData.value.idCard}\n学位类型：${degreeTypes.find(d => d.value === chainData.value.degreeType)?.label}\nPDF文件：${chainData.value.pdfPath}\n此操作不可逆。`,
        '确认上链',
        {
            confirmButtonText: '确认上链',
            cancelButtonText: '取消',
            type: 'warning',
            customClass: 'blockchain-confirm-dialog',
            center: true
        }
    ).then(() => {
        submitToBlockchain()
    }).catch(() => {
        ElMessage.info('已取消上链操作')
    })
}

// 重置表单
const resetForm = () => {
    chainData.value = {
        studentName: '',
        idCard: '',
        collegeId: '',
        major: '',
        degreeType: '',
        graduationDate: '',
        pdfPath: '',
        fileHash: ''
    }
    urlValid.value = false
    ElMessage.info('表单已重置')
}
</script>

<template>
    <div class="chain-upload-container">
        <div class="page-wrapper">
            <!-- 页面标题区域 -->
            <div class="page-header">
                <div class="header-content">
                    <div class="blockchain-icon">
                        <div class="chain-link"></div>
                        <div class="chain-link"></div>
                        <div class="chain-link"></div>
                    </div>
                    <h1 class="page-title">学历证书区块链上链</h1>
                    <p class="page-description">录入学历证书信息并上传至区块链网络，确保数据的不可篡改性和永久存储</p>
                    <p class="page-description">请确认录入信息与PDF证书内容一致</p>
                </div>
            </div>

            <!-- 主要内容区域 -->
            <div class="main-content">
                <!-- 信息录入表单区域 -->
                <div class="form-section">
                    <el-card class="form-card" shadow="hover">
                        <template #header>
                            <div class="card-header">
                                <span class="header-title">
                                    <el-icon class="header-icon">
                                        <EditPen />
                                    </el-icon>
                                    学历证书信息录入
                                </span>
                                <span class="header-tip">请填写完整的学历证书信息</span>
                            </div>
                        </template>

                        <div class="form-content">
                            <el-form :model="chainData" :rules="rules" ref="formRef" label-width="120px"
                                class="info-form">
                                <!-- 学生基本信息 -->
                                <el-row :gutter="24">
                                    <el-col :span="12">
                                        <el-form-item label="学生姓名" prop="studentName">
                                            <el-input v-model="chainData.studentName" placeholder="请输入学生姓名"
                                                maxlength="20" show-word-limit />
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item label="身份证号" prop="idCard">
                                            <el-input v-model="chainData.idCard" placeholder="请输入身份证号" maxlength="18"
                                                show-word-limit />
                                        </el-form-item>
                                    </el-col>
                                </el-row>

                                <!-- 学院和专业信息 -->
                                <el-row :gutter="24">
                                    <el-col :span="12">
                                        <el-form-item label="专业名称" prop="major">
                                            <el-input v-model="chainData.major" placeholder="请输入专业名称" />
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item label="毕业日期" prop="graduationDate">
                                            <el-date-picker v-model="chainData.graduationDate" type="date"
                                                placeholder="选择毕业日期" style="width: 100%" value-format="YYYY-MM-DD" />
                                        </el-form-item>
                                    </el-col>
                                </el-row>

                                <!-- 学位和毕业信息 -->
                                <el-row :gutter="24">
                                    <el-col :span="12">
                                        <el-form-item label="学位类型" prop="degreeType">
                                            <el-select v-model="chainData.degreeType" placeholder="请选择学位类型"
                                                style="width: 100%">
                                                <el-option v-for="degree in degreeTypes" :key="degree.value"
                                                    :label="degree.label" :value="degree.value" />
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                    <el-form-item label="PDF哈希值" prop="fileHash">
                                        <el-input v-model="chainData.fileHash" placeholder="请输入PDF文件哈希值" />
                                    </el-form-item>

                                </el-row>

                                <!-- PDF文件URL录入区域 -->
                                <el-form-item label="PDF文件URL" prop="pdfPath">
                                    <div class="url-input-section">
                                        <div class="url-input-group">
                                            <el-input v-model="chainData.pdfPath" placeholder="请输入学历证书PDF文件的URL地址"
                                                class="url-input" @blur="handleUrlInput" />
                                            <el-button type="primary" :loading="urlValidating" @click="handleUrlInput"
                                                class="validate-btn">
                                                <el-icon>
                                                    <Check />
                                                </el-icon>
                                                验证URL
                                            </el-button>
                                        </div>

                                        <div class="url-info" v-if="chainData.pdfPath">
                                            <div class="url-status"
                                                :class="{ 'valid': urlValid, 'invalid': !urlValid }">
                                                <el-icon v-if="urlValid">
                                                    <SuccessFilled />
                                                </el-icon>
                                                <el-icon v-else>
                                                    <Warning />
                                                </el-icon>
                                                <span>{{ urlValid ? 'URL验证成功' : 'URL格式无效' }}</span>
                                            </div>
                                            <div class="file-hash" v-if="chainData.fileHash">
                                                文件哈希：{{ chainData.fileHash }}
                                            </div>
                                        </div>

                                        <div class="url-tips">
                                            <el-icon>
                                                <InfoFilled />
                                            </el-icon>
                                            <span>请输入有效的PDF文件URL地址（支持http/https/ftp协议）</span>
                                        </div>
                                    </div>
                                </el-form-item>

                                <!-- 操作按钮 -->
                                <el-form-item>
                                    <el-button type="primary" size="large" :loading="submitting" @click="handleSubmit"
                                        class="submit-btn">
                                        <el-icon>
                                            <Link />
                                        </el-icon>
                                        {{ submitting ? '上链中...' : '提交到区块链' }}
                                    </el-button>
                                    <el-button size="large" @click="resetForm">
                                        重置表单
                                    </el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.chain-upload-container {
    padding: 20px;
    background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
    min-height: 100vh;
    color: #e2e8f0;
}

.page-wrapper {
    max-width: 1200px;
    margin: 0 auto;
}

/* 页面标题区域 */
.page-header {
    text-align: center;
    margin-bottom: 40px;
    padding: 40px 20px;
    background: rgba(30, 41, 59, 0.8);
    border-radius: 16px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(100, 116, 139, 0.3);
    position: relative;
    overflow: hidden;
}

.page-header::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, #10b981, #3b82f6, #8b5cf6);
    animation: shimmer 3s infinite;
}

@keyframes shimmer {
    0% {
        background-position: -200% 0;
    }

    100% {
        background-position: 200% 0;
    }
}

.header-content {
    position: relative;
    z-index: 2;
}

.blockchain-icon {
    display: flex;
    justify-content: center;
    gap: 8px;
    margin-bottom: 20px;
}

.chain-link {
    width: 12px;
    height: 12px;
    background: linear-gradient(135deg, #10b981, #3b82f6);
    border-radius: 50%;
    animation: pulse 2s infinite;
}

.chain-link:nth-child(2) {
    animation-delay: 0.3s;
}

.chain-link:nth-child(3) {
    animation-delay: 0.6s;
}

@keyframes pulse {

    0%,
    100% {
        transform: scale(1);
        opacity: 1;
    }

    50% {
        transform: scale(1.2);
        opacity: 0.7;
    }
}

.page-title {
    color: #f1f5f9;
    margin-bottom: 12px;
    font-size: 36px;
    font-weight: 700;
    background: linear-gradient(135deg, #10b981, #3b82f6, #8b5cf6);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
}

.page-description {
    color: #94a3b8;
    font-size: 18px;
    max-width: 600px;
    margin: 0 auto;
    line-height: 1.6;
}

/* 主要内容区域 */
.main-content {
    display: grid;
    gap: 24px;
    grid-template-columns: 1fr;
}

/* 表单区域 */
.form-section {
    grid-column: 1 / -1;
}

.form-card {
    background: rgba(30, 41, 59, 0.8);
    border: 1px solid rgba(100, 116, 139, 0.3);
    backdrop-filter: blur(10px);
}

.form-card :deep(.el-card__header) {
    background: rgba(15, 23, 42, 0.6);
    border-bottom: 1px solid rgba(100, 116, 139, 0.3);
}

.form-content {
    padding: 40px;
}

.info-form {
    max-width: 800px;
    margin: 0 auto;
}

.info-form :deep(.el-form-item__label) {
    color: #e2e8f0;
    font-weight: 500;
}

.info-form :deep(.el-input__inner),
.info-form :deep(.el-textarea__inner),
.info-form :deep(.el-select .el-input__inner) {
    background: rgba(15, 23, 42, 0.6);
    border: 1px solid rgba(100, 116, 139, 0.3);
    color: #f1f5f9;
}

.info-form :deep(.el-input__inner):focus,
.info-form :deep(.el-textarea__inner):focus,
.info-form :deep(.el-select .el-input__inner):focus {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

/* URL录入区域样式 */
.url-input-section {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.url-input-group {
    display: flex;
    gap: 12px;
    align-items: flex-start;
}

.url-input {
    flex: 1;
}

.validate-btn {
    white-space: nowrap;
}

.url-info {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.url-status {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 12px;
    border-radius: 4px;
    font-size: 14px;
}

.url-status.valid {
    background: rgba(16, 185, 129, 0.1);
    border: 1px solid rgba(16, 185, 129, 0.3);
    color: #10b981;
}

.url-status.invalid {
    background: rgba(239, 68, 68, 0.1);
    border: 1px solid rgba(239, 68, 68, 0.3);
    color: #ef4444;
}

.file-hash {
    font-family: 'Courier New', monospace;
    font-size: 12px;
    color: #94a3b8;
    background: rgba(15, 23, 42, 0.6);
    padding: 8px 12px;
    border-radius: 4px;
    border: 1px solid rgba(100, 116, 139, 0.3);
}

.url-tips {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #94a3b8;
    font-size: 12px;
    padding: 8px 12px;
    background: rgba(100, 116, 139, 0.1);
    border-radius: 4px;
}

.submit-btn {
    background: linear-gradient(135deg, #10b981, #3b82f6);
    border: none;
    padding: 12px 32px;
    font-size: 16px;
    font-weight: 600;
    margin-right: 16px;
}

.submit-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(59, 130, 246, 0.3);
}

.submit-btn:active {
    transform: translateY(0);
}

/* 区块链信息区域 */
.blockchain-info-section {
    grid-column: 1 / -1;
}

.info-card {
    background: rgba(30, 41, 59, 0.8);
    border: 1px solid rgba(100, 116, 139, 0.3);
    backdrop-filter: blur(10px);
}

.info-card :deep(.el-card__header) {
    background: rgba(15, 23, 42, 0.6);
    border-bottom: 1px solid rgba(100, 116, 139, 0.3);
}

.info-content {
    padding: 24px;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
    background: rgba(15, 23, 42, 0.6);
    border: 1px solid rgba(100, 116, 139, 0.3);
    border-radius: 8px;
}

.info-label {
    color: #94a3b8;
    font-size: 14px;
}

.info-value {
    color: #f1f5f9;
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 6px;
}

.status-connected {
    color: #10b981;
}

/* 响应式设计 */
@media (max-width: 768px) {
    .chain-upload-container {
        padding: 10px;
    }

    .page-header {
        padding: 20px 15px;
        margin-bottom: 20px;
    }

    .page-title {
        font-size: 28px;
    }

    .page-description {
        font-size: 16px;
    }

    .form-content {
        padding: 20px;
    }

    .info-form :deep(.el-form-item__label) {
        width: 100px !important;
    }

    .url-input-group {
        flex-direction: column;
    }

    .info-grid {
        grid-template-columns: 1fr;
    }

    .submit-btn {
        width: 100%;
        margin-bottom: 12px;
    }
}

/* 动画效果 */
@keyframes float {

    0%,
    100% {
        transform: translateY(0px);
    }

    50% {
        transform: translateY(-10px);
    }
}

.floating-block {
    animation: float 6s ease-in-out infinite;
}

/* 流光边框效果 */
.glow-border {
    position: relative;
}

.glow-border::before {
    content: '';
    position: absolute;
    top: -2px;
    left: -2px;
    right: -2px;
    bottom: -2px;
    background: linear-gradient(45deg, #10b981, #3b82f6, #8b5cf6, #10b981);
    border-radius: inherit;
    z-index: -1;
    animation: borderGlow 3s linear infinite;
    background-size: 400% 400%;
}

@keyframes borderGlow {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #f1f5f9;
}

.header-icon {
    color: #3b82f6;
}

.header-tip {
    color: #94a3b8;
    font-size: 14px;
}
</style>