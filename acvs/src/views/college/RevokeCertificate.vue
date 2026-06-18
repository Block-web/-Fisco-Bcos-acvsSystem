<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElButton, ElForm, ElFormItem, ElInput, ElCard, ElResult, ElDivider, ElMessageBox } from 'element-plus'
import { Warning, Refresh } from '@element-plus/icons-vue'
import { revokeCertificateService } from '@/api/college.js'

// 表单数据
const formData = reactive({
  certNo: ''  // 证书编号
})

// 撤销结果
const revokeResult = ref(null)
const loading = ref(false)

// 撤销证书
const revokeCertificate = async () => {
  if (!formData.certNo.trim()) {
    ElMessage.warning('请输入证书编号')
    return
  }
  
  // 二次确认
  try {
    await ElMessageBox.confirm(
      `确定要撤销证书编号为 "${formData.certNo}" 的证书吗？`,
      '确认撤销',
      {
        confirmButtonText: '确定撤销',
        cancelButtonText: '取消',
        type: 'warning',
        icon: Warning
      }
    )
  } catch {
    ElMessage.info('已取消撤销操作')
    return
  }
  
  loading.value = true
  
  try {
    const result = await revokeCertificateService(formData.certNo)
    
    if (result.code === 0) {
      revokeResult.value = {
        success: true,
        message: result.msg || '证书撤销成功',
        data: result.data
      }
      ElMessage.success('证书撤销成功')
    } else {
      revokeResult.value = {
        success: false,
        message: result.msg || '证书撤销失败',
        data: result.data
      }
      ElMessage.error(result.msg || '证书撤销失败')
    }
  } catch (error) {
    revokeResult.value = {
      success: false,
      message: '撤销失败：' + error.message,
      data: null
    }
    ElMessage.error('撤销失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  formData.certNo = ''
  revokeResult.value = null
}
</script>

<template>
  <div class="revoke-container">
    <div class="page-header">
      <h2 class="title">撤销证书</h2>
      <p class="description">输入证书编号，撤销已颁发的证书</p>
    </div>

    <div class="content-area">
      <el-card class="revoke-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span class="card-title">证书撤销</span>
            <el-button 
              v-if="revokeResult" 
              @click="resetForm" 
              type="text" 
              size="small"
              class="reset-btn">
              <el-icon><Refresh /></el-icon>
              重新撤销
            </el-button>
          </div>
        </template>

        <!-- 撤销表单 -->
        <div class="revoke-form">
          <ElForm :model="formData" label-width="120px">
            <ElFormItem label="证书编号" prop="certNo">
              <ElInput 
                v-model="formData.certNo" 
                placeholder="请输入要撤销的证书编号"
                class="input-width"
                :disabled="loading"
              />
            </ElFormItem>
            
            <ElFormItem>
              <ElButton 
                type="danger" 
                :loading="loading"
                @click="revokeCertificate"
                class="revoke-btn"
                icon="AlertTriangle">
                确认撤销证书
              </ElButton>
            </ElFormItem>
          </ElForm>
        </div>

        <!-- 撤销结果 -->
        <ElDivider v-if="revokeResult" />
        
        <div v-if="revokeResult" class="result-area">
          <ElResult
            :icon="revokeResult.success ? 'success' : 'error'"
            :title="revokeResult.success ? '撤销成功' : '撤销失败'"
            :sub-title="revokeResult.message"
          >
            <template #extra>
              <div v-if="revokeResult.data" class="result-details">
                <p><strong>证书编号：</strong>{{ revokeResult.data.certNo }}</p>
                <p><strong>撤销时间：</strong>{{ revokeResult.data.revokeTime }}</p>
              </div>
            </template>
          </ElResult>
        </div>
      </el-card>

      <!-- 提示信息 -->
      <el-card class="tips-card" shadow="hover">
        <template #header>
          <span class="tips-title">
            <el-icon><AlertTriangle /></el-icon>
            操作提示
          </span>
        </template>
        <ul class="tips-list">
          <li>证书撤销后将无法恢复，请谨慎操作</li>
          <li>撤销操作会同时更新数据库和区块链上的证书状态</li>
          <li>撤销后该证书将无法通过验证</li>
        </ul>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.revoke-container {
  padding: 20px;
  max-width: 600px;
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

.revoke-card {
  border-radius: 12px;
  margin-bottom: 20px;
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

.revoke-form {
  padding: 20px 0;
}

.input-width {
  width: 100%;
  max-width: 400px;
}

.revoke-btn {
  margin-right: 10px;
}

.result-area {
  margin-top: 20px;
}

.result-details {
  text-align: left;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

.result-details p {
  margin: 8px 0;
  color: #606266;
}

.tips-card {
  border-radius: 12px;
}

.tips-title {
  font-size: 16px;
  font-weight: 600;
  color: #E6A23C;
}

.tips-list {
  padding-left: 20px;
  margin: 0;
}

.tips-list li {
  margin-bottom: 8px;
  color: #606266;
  line-height: 1.6;
}

.tips-list li:last-child {
  margin-bottom: 0;
}
</style>