<script setup>
import { ref } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import CryptoJS from 'crypto-js'
import { 
  UploadFilled, 
  Document, 
  Key, 
  DocumentCopy, 
  Refresh 
} from '@element-plus/icons-vue'

// 响应式数据
const pdfFile = ref(null) // 上传的PDF文件
const isCalculating = ref(false) // 计算状态
const hashValue = ref('') // 计算出的哈希值
const fileName = ref('') // 文件名称
const progress = ref(0) // 计算进度

const handleFileUpload = async (uploadFile) => {
  console.log('文件上传事件触发:', uploadFile)
  
  // 从上传事件对象中获取真实的File对象
  const file = uploadFile.raw
  if (!file) {
    ElMessage.error('文件获取失败')
    return false
  }
  
  // 检查文件类型
  if (file.type !== 'application/pdf' && !file.name.toLowerCase().endsWith('.pdf')) {
    ElMessage.error('请上传PDF文件')
    return false
  }
  
  // 检查文件大小
  if (file.size > 10 * 1024 * 1024) { // 限制10MB
    ElMessage.error('文件大小不能超过10MB')
    return false
  }
  
  // 如果有正在处理的文件，先重置
  if (pdfFile.value) {
    resetForm()
  }
  
  pdfFile.value = file
  fileName.value = file.name
  progress.value = 0
  ElMessage.success(`已选择文件: ${file.name}`)
  
  // 自动开始计算哈希值
  await calculateHash(file)
  return true
}

// 计算PDF文件的哈希值（修复版本）
const calculateHash = async (file) => {
  isCalculating.value = true
  hashValue.value = ''
  progress.value = 0
  
  const loading = ElLoading.service({
    lock: true,
    text: '正在计算SHA256哈希值...',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  try {
    // 设置超时时间（30秒）
    const timeoutPromise = new Promise((_, reject) => {
      setTimeout(() => reject(new Error('计算超时，请尝试较小的文件')), 30000)
    })

    const hashPromise = new Promise((resolve, reject) => {
      const reader = new FileReader()
      const chunkSize = 1024 * 1024 // 1MB chunks
      let offset = 0
      const totalSize = file.size
      const sha256 = CryptoJS.algo.SHA256.create()
      
      const readChunk = () => {
        if (offset >= totalSize) {
          // 所有chunk处理完成
          const hash = sha256.finalize().toString()
          resolve(hash)
          return
        }
        
        // 使用正确的slice方法
        const chunk = file.slice(offset, offset + chunkSize)
        reader.readAsArrayBuffer(chunk)
      }
      
      reader.onload = function(event) {
        try {
          const arrayBuffer = event.target.result
          const wordArray = CryptoJS.lib.WordArray.create(arrayBuffer)
          sha256.update(wordArray)
          
          offset += chunkSize
          progress.value = Math.min((offset / totalSize) * 100, 100)
          
          // 继续读取下一个chunk
          setTimeout(readChunk, 0) // 使用setTimeout避免阻塞UI
        } catch (error) {
          reject(new Error(`处理文件块失败: ${error.message}`))
        }
      }
      
      reader.onerror = function() {
        reject(new Error('文件读取失败'))
      }
      
      // 开始读取第一个chunk
      readChunk()
    })
    
    // 等待计算完成或超时
    const hash = await Promise.race([hashPromise, timeoutPromise])
    hashValue.value = hash
    progress.value = 100
    ElMessage.success('SHA256哈希值计算完成')
  } catch (error) {
    console.error('哈希计算错误:', error)
    ElMessage.error(`计算失败: ${error.message}`)
    hashValue.value = ''
  } finally {
    loading.close()
    isCalculating.value = false
    progress.value = 0
  }
}

// 复制哈希值到剪贴板
const copyHashValue = () => {
  if (!hashValue.value) {
    ElMessage.warning('暂无哈希值可复制')
    return
  }
  
  navigator.clipboard.writeText(hashValue.value)
    .then(() => {
      ElMessage.success('哈希值已复制到剪贴板')
    })
    .catch(() => {
      // 降级方案：使用textarea复制
      const textArea = document.createElement('textarea')
      textArea.value = hashValue.value
      document.body.appendChild(textArea)
      textArea.select()
      try {
        document.execCommand('copy')
        ElMessage.success('哈希值已复制到剪贴板')
      } catch (err) {
        ElMessage.error('复制失败，请手动复制')
      }
      document.body.removeChild(textArea)
    })
}

// 重置表单
const resetForm = () => {
  pdfFile.value = null
  fileName.value = ''
  hashValue.value = ''
  progress.value = 0
}
</script>

<template>
  <div class="verify-container">
    <div class="page-header">
      <h2 class="title">PDF文件SHA256哈希值计算</h2>
      <p class="description">上传PDF文件，自动计算并显示SHA256哈希值</p>
    </div>

    <div class="content-area">
      <el-card class="verify-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span class="card-title">哈希值计算</span>
            <el-button 
              v-if="pdfFile" 
              @click="resetForm" 
              type="text" 
              size="small"
              class="reset-btn">
              <el-icon><Refresh /></el-icon>
              重新上传
            </el-button>
          </div>
        </template>

        <!-- 文件上传区域 -->
        <div class="upload-area">
          <div class="upload-group">
            <div class="upload-label">上传PDF文件：</div>
            <el-upload
              class="upload-demo"
              drag
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleFileUpload"
              :before-upload="() => false" 
              accept=".pdf"
              :limit="1"
              :disabled="isCalculating">
              <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
              <div class="el-upload__text">
                将PDF文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  只能上传PDF文件，且不超过10MB
                </div>
              </template>
            </el-upload>
            
            <div v-if="fileName" class="file-info">
              <el-icon><Document /></el-icon>
              <span class="file-name">{{ fileName }}</span>
              <el-tag v-if="isCalculating" type="warning" effect="plain">
                计算中...
              </el-tag>
              <el-tag v-else-if="hashValue" type="success" effect="plain">
                计算完成
              </el-tag>
            </div>

            <!-- 进度条 -->
            <div v-if="isCalculating && progress > 0" class="progress-area">
              <el-progress 
                :percentage="Math.round(progress)" 
                :stroke-width="8"
                :show-text="true"
                status="success" />
              <div class="progress-text">计算进度: {{ Math.round(progress) }}%</div>
            </div>
          </div>

          <!-- 哈希值显示区域 -->
          <div v-if="hashValue" class="hash-display-area">
            <div class="section-title">
              <el-icon><Key /></el-icon>
              SHA256哈希值
            </div>
            <div class="hash-display">
              <el-input
                v-model="hashValue"
                readonly
                type="textarea"
                :rows="4"
                placeholder="哈希值将在此显示"
                class="hash-input">
                <template #append>
                  <el-button 
                    @click="copyHashValue"
                    type="primary"
                    class="copy-btn">
                    <el-icon><DocumentCopy /></el-icon>
                    复制
                  </el-button>
                </template>
              </el-input>
            </div>
            <div class="hash-info">
              <el-tag type="info" effect="dark">
                SHA-256
              </el-tag>
              <span class="timestamp">计算时间: {{ new Date().toLocaleString() }}</span>
            </div>
          </div>

          <!-- 空状态提示 -->
          <div v-if="!pdfFile" class="empty-state">
            <el-icon size="64" color="#C0C4CC"><Document /></el-icon>
            <p>请上传PDF文件开始计算哈希值</p>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.verify-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.title {
  font-size: 28px;
  color: #303133;
  margin-bottom: 8px;
}

.description {
  font-size: 16px;
  color: #606266;
}

.verify-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.reset-btn {
  color: #909399;
}

.upload-area {
  padding: 0 20px;
}

.upload-group {
  margin-bottom: 24px;
}

.upload-label {
  font-weight: 500;
  margin-bottom: 12px;
  color: #606266;
}

.file-info {
  display: flex;
  align-items: center;
  margin-top: 12px;
  padding: 8px 12px;
  background: #f5f7fa;
  border-radius: 6px;
}

.file-name {
  margin-left: 8px;
  margin-right: 12px;
  font-weight: 500;
}

.progress-area {
  margin-top: 16px;
}

.progress-text {
  text-align: center;
  margin-top: 8px;
  font-size: 14px;
  color: #909399;
}

.hash-display-area {
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #303133;
}

.section-title .el-icon {
  margin-right: 8px;
}

.hash-display {
  margin-bottom: 12px;
}

.hash-input {
  font-family: 'Courier New', monospace;
}

.copy-btn {
  height: 100%;
}

.hash-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.timestamp {
  font-size: 14px;
  color: #909399;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-state p {
  margin-top: 16px;
  font-size: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .verify-container {
    padding: 16px;
  }
  
  .title {
    font-size: 24px;
  }
  
  .upload-area {
    padding: 0 12px;
  }
}
</style>