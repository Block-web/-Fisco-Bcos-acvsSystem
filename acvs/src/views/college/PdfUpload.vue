<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { UploadFilled, Document, Delete, View } from '@element-plus/icons-vue'
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();
// 上传的文件列表
const fileList = ref([])
const uploadRef = ref()
const loading = ref(false)

// 上传配置
const uploadConfig = reactive({
  action: '/api/upload',
  accept: '.pdf',
  name: 'file',
  multiple: false,
  limit: 10,
  fileSize: 10,  // MB
  headers: {
    'Authorization': tokenStore.token,
  }
})


// 处理文件上传前验证
const beforeUpload = (file) => {
  const isPDF = file.type === 'application/pdf'
  const isLt10M = file.size / 1024 / 1024 < uploadConfig.fileSize

  if (!isPDF) {
    ElMessage.error('只能上传PDF文件！')
    return false
  }
  if (!isLt10M) {
    ElMessage.error(`PDF文件大小不能超过 ${uploadConfig.fileSize}MB！`)
    return false
  }
  return true
}
const pdfUrl = ref('')

// 处理上传成功 
const handleSuccess = (result) => {

  ElMessage.success(result.msg ? result.msg : '文件上传成功')
  pdfUrl.value = result.data
}
</script>

<template>
  <div class="pdf-upload-container">
    <div class="page-wrapper">
      <!-- A4纸张大小的容器 -->
      <div class="pdf-document">
        <div class="pdf-header">
          <h2 class="title">PDF文件上传管理</h2>
          <p class="description">学历证明、成绩单等PDF文档上传与管理</p>
        </div>

        <!-- 上传区域 - 放置在A4纸张内 -->
        <div class="upload-section">

          <el-card class="upload-card">
            <template #header>
              <div class="card-header">
                <span class="header-title">PDF文件上传</span>
                <span class="header-tip">支持PDF格式，单个文件不超过{{ uploadConfig.fileSize }}MB</span>
              </div>
            </template>

            <div class="upload-content">
              <el-upload ref="uploadRef" class="upload-demo" :action="uploadConfig.action" :accept="uploadConfig.accept"
                :headers="uploadConfig.headers" :name="uploadConfig.name" :limit="uploadConfig.limit"
                :data="uploadConfig.data" :on-success="handleSuccess" :auto-upload="true" drag>
                <el-icon class="el-icon--upload">
                  <UploadFilled />
                </el-icon>
                <div class="el-upload__text">
                  将PDF文件拖到此处，或<em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">
                    支持 .pdf 格式，文件大小不超过 {{ uploadConfig.fileSize }}MB
                  </div>
                </template>
              </el-upload>
            </div>
          </el-card>
        </div>

        <!-- 文件列表区域 -->
        <div class="file-list-section" v-if="fileList.length > 0">
          <el-card class="file-list-card">
            <template #header>
              <div class="card-header">
                <span class="header-title">已上传文件列表 ({{ fileList.length }})</span>
              </div>
            </template>

            <el-table :data="fileList" style="width: 100%" size="small">
              <el-table-column prop="name" label="文件名" min-width="200">
                <template #default="{ row }">
                  <div class="file-name">
                    <el-icon>
                      <Document />
                    </el-icon>
                    <span class="name-text">{{ row.name }}</span>
                  </div>
                </template>
              </el-table-column>

              <el-table-column prop="size" label="文件大小" width="100">
                <template #default="{ row }">
                  {{ (row.size / 1024 / 1024).toFixed(2) }} MB
                </template>
              </el-table-column>

              <el-table-column prop="uploadTime" label="上传时间" width="150" />
 
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag :type="row.status === 'success' ? 'success' : 'danger'" size="small">
                    {{ row.status === 'success' ? '成功' : '失败' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      <el-form-item label="PDF文件URL:  " prop="pdfUrl">
        <el-input v-model="pdfUrl" placeholder="请输入PDF文件URL" />
      </el-form-item>
      </div>
    </div>
  </div>
</template>

<style scoped>
.pdf-upload-container {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.page-wrapper {
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: center;
}

.pdf-document {
  width: 794px;
  /* A4宽度 - 210mm */
  min-height: 1123px;
  /* A4高度 - 297mm */
  background: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  padding: 40px;
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.pdf-header {
  text-align: center;
  border-bottom: 2px solid #409eff;
  padding-bottom: 20px;
  margin-bottom: 20px;
}

.title {
  color: #303133;
  margin-bottom: 10px;
  font-size: 28px;
  font-weight: bold;
  background: linear-gradient(135deg, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.description {
  color: #606266;
  font-size: 16px;
  font-weight: 500;
}

.upload-section {
  margin-bottom: 20px;
}

.upload-card,
.file-list-card {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.header-tip {
  font-size: 12px;
  color: #909399;
}

.upload-content {
  padding: 30px;
}

:deep(.upload-demo) {
  width: 100%;
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
  height: 160px;
  border: 2px dashed #dcdfe6;
  border-radius: 6px;
  background: #fafafa;
  transition: all 0.3s ease;
}

:deep(.el-upload-dragger:hover) {
  border-color: #409eff;
  background: #f0f7ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

:deep(.el-icon--upload) {
  font-size: 48px;
  color: #c0c4cc;
  margin: 20px 0 12px;
  line-height: 1;
}

:deep(.el-upload__text) {
  color: #606266;
  font-size: 14px;
  text-align: center;
  font-weight: 500;
}

:deep(.el-upload__tip) {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
  text-align: center;
}

.file-list-section {
  margin-bottom: 20px;
}

.file-name {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
}

.name-text {
  font-weight: 500;
  color: #303133;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px dashed #dcdfe6;
}

/* 响应式设计 */
@media (max-width: 900px) {
  .pdf-document {
    width: 100%;
    min-height: auto;
    margin: 10px;
    padding: 20px;
  }

  .page-wrapper {
    padding: 0 10px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  :deep(.el-upload-dragger) {
    height: 140px;
  }

  :deep(.el-icon--upload) {
    font-size: 40px;
    margin: 15px 0 10px;
  }
}

@media (max-width: 768px) {
  .pdf-upload-container {
    padding: 10px;
    background: #f5f7fa;
  }

  .pdf-document {
    box-shadow: none;
    border-radius: 0;
    margin: 0;
  }

  .title {
    font-size: 24px;
  }

  .upload-content {
    padding: 20px;
  }
}

/* 打印样式 */
@media print {
  .pdf-upload-container {
    background: white;
    padding: 0;
  }

  .pdf-document {
    box-shadow: none;
    border: 1px solid #ccc;
    margin: 0;
    width: 100%;
    min-height: auto;
  }

  .el-button {
    display: none;
  }
}
</style>