<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElButton, ElForm, ElFormItem, ElInput, ElCard, ElResult, ElDivider } from 'element-plus'
import { Message, Setting } from '@element-plus/icons-vue'
import { verifySqlService, verifyBlockchainService } from '@/api/company.js'
import useUserInfoStore from '@/stores/userInfo.js'

const userInfoStore = useUserInfoStore()

// 表单数据
const formData = reactive({
  certNo: '',       // 证书编号
  fileHash: '',     // 文件哈希
  collegeId: ''     // 院校ID
})

// 验证结果
const verifyResult = ref(null)
const verifyType = ref('')  // 'sql' 或 'blockchain'
const loading = ref(false)

// 查询数据库验证
const verifySql = async () => {
  if (!validateForm()) return
  
  loading.value = true
  verifyType.value = 'sql'
  
  try {
    const companyId = userInfoStore.info.id
    const result = await verifySqlService(formData, companyId)
    
    if (result.code === 0) {
       const isSuccess = result.data && result.data.includes('PASS')
      verifyResult.value = {
        success: isSuccess,
        message: isSuccess ? '验证成功' : '验证失败',
        data: { result: isSuccess ? 'PASS' : 'FAIL', verifyTime: new Date().toLocaleString() }
      }
    } else {
      verifyResult.value = {
        success: false,
        message: result.msg || '验证失败',
        data: result.data
      }
    }
  } catch (error) {
    verifyResult.value = {
      success: false,
      message: '验证失败：' + error.message,
      data: null
    }
    ElMessage.error('验证失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 查询区块链验证
const verifyBlockchain = async () => {
  if (!validateForm()) return
  
  loading.value = true
  verifyType.value = 'blockchain'
  
  try {
    const companyId = userInfoStore.info.id
    const result = await verifyBlockchainService(formData, companyId)
    
    if (result.code === 0) {
      // 区块链验证成功时，data是VerifyLog对象，result字段为'PASS'或'FAIL'
      const isSuccess = result.data && result.data.result === 'PASS'
      verifyResult.value = {
         success: isSuccess,
        message: isSuccess ? '区块链验证成功' : '区块链验证失败',
        data: { certNo: formData.certNo, result: result.data?.result || 'FAIL', verifyTime: new Date().toLocaleString() },
        txHash: result.data?.txHash
      }
    } else {
      verifyResult.value = {
        success: false,
        message: result.msg || '区块链验证失败',
        data: result.data
      }
    }
  } catch (error) {
    verifyResult.value = {
      success: false,
      message: '区块链验证失败：' + error.message,
      data: null
    }
    ElMessage.error('区块链验证失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 表单验证
const validateForm = () => {
  if (!formData.certNo.trim()) {
    ElMessage.warning('请输入证书编号')
    return false
  }
  if (!formData.fileHash.trim()) {
    ElMessage.warning('请输入文件哈希')
    return false
  }
  if (!formData.collegeId.trim()) {
    ElMessage.warning('请输入院校ID')
    return false
  }
  return true
}

// 重置表单
const resetForm = () => {
  formData.certNo = ''
  formData.fileHash = ''
  formData.collegeId = ''
  verifyResult.value = null
  verifyType.value = ''
}
</script>

<template>
  <div class="verify-container">
    <h2 class="page-title">证书验证</h2>
    
    <!-- 验证表单 -->
    <ElCard class="form-card">
      <ElForm :model="formData" label-width="120px">
        <ElFormItem label="证书编号" prop="certNo">
          <ElInput 
            v-model="formData.certNo" 
            placeholder="请输入证书编号"
            class="input-width"
          />
        </ElFormItem>
        
        <ElFormItem label="文件哈希" prop="fileHash">
          <ElInput 
            v-model="formData.fileHash" 
            placeholder="请输入证书PDF的SHA256哈希值"
            class="input-width"
          />
        </ElFormItem>
        
        <ElFormItem label="院校ID" prop="collegeId">
          <ElInput 
            v-model="formData.collegeId" 
            placeholder="请输入颁发院校的ID"
            class="input-width"
          />
        </ElFormItem>
        
        <ElFormItem>
          <ElButton 
            type="primary" 
            :loading="loading"
            @click="verifySql"
            class="verify-btn"
          >
            <ElIcon><Message /></ElIcon>
            查询数据库
          </ElButton>
          
          <ElButton 
            type="success" 
            :loading="loading"
            @click="verifyBlockchain"
            class="verify-btn"
          >
            <ElIcon><Setting  /></ElIcon>
            查询区块链
          </ElButton>
          
          <ElButton 
            type="default" 
            @click="resetForm"
            class="verify-btn"
          >
            <ElIcon><Refresh /></ElIcon>
            重置
          </ElButton>
        </ElFormItem>
      </ElForm>
    </ElCard>
    
    <!-- 验证结果 -->
    <ElDivider v-if="verifyResult" />
    
    <ElCard v-if="verifyResult" class="result-card">
      <h3 class="result-title">
        <span v-if="verifyType === 'sql'">数据库验证</span>
        <span v-else>区块链验证</span>
        结果
      </h3>
      
      <ElResult
        :icon="verifyResult.success ? 'success' : 'error'"
        :title="verifyResult.success ? '验证通过' : '验证失败'"
        :sub-title="verifyResult.message"
      >
        <template #extra>
          <div v-if="verifyResult.data" class="result-details">
            <p><strong>证书编号：</strong>{{ verifyResult.data.certNo || formData.certNo }}</p>
            <p v-if="verifyResult.txHash"><strong>交易哈希：</strong>{{ verifyResult.txHash }}</p>
          </div>
        </template>
      </ElResult>
    </ElCard>
  </div>
</template>

<style scoped>
.verify-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.form-card {
  margin-bottom: 20px;
}

.input-width {
  width: 400px;
}

.verify-btn {
  margin-right: 10px;
}

.result-card {
  margin-top: 20px;
}

.result-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
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
</style>