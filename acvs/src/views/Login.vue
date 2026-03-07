<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'          // 1. 引入 ElMessage
import { userLoginService } from '@/api/user.js'
import { useTokenStore } from '@/stores/token.js'
import router from '@/router'

const loginData = ref({
  username: '',
  password: '',
  role: ''
})

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户类型', trigger: 'change' },
    { type: 'enum', enum: ['COLLEGE', 'STUDENT', 'COMPANY'], message: '用户类型必须是COLLEGE、STUDENT或COMPANY', trigger: 'change' }
  ]
})

const loginFormRef = ref(null)
const tokenStore = useTokenStore()

const submitForm = () => {
  loginFormRef.value.validate(async valid => {
    if (valid) {
      try {
        const result = await userLoginService(loginData.value)
        console.log('登录接口响应结果:', result)
        console.log('登录成功，Token:', result.data)
        
        if (!result.data) {
          console.error('Token为空，请检查后端响应')
          ElMessage.error('登录失败：Token为空')
          return
        }
        
        tokenStore.setToken(result.data)
        
        // 等待Pinia持久化完成
        await new Promise(resolve => setTimeout(resolve, 100))
        
        console.log('Token已保存，当前Token:', tokenStore.token)
        ElMessage.success('登录成功')

        if (loginData.value.role === 'COLLEGE') router.push('/college')
        else if (loginData.value.role === 'STUDENT') router.push('/student')
        else if (loginData.value.role === 'COMPANY') router.push('/company')
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error('登录失败，请检查用户名、密码和用户类型')
      }
    } else {
      ElMessage.error('参数错误')
    }
  })
}

const resetForm = () => loginFormRef.value.resetFields()
const pushToRegister = () => router.push('/register')
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>学历证书验证系统</h2>
        <p>请登录您的账号</p>
      </div>

      <el-form :model="loginData" :rules="rules" ref="loginFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginData.username" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="loginData.password" type="password" show-password />
        </el-form-item>

        <el-form-item label="用户类型" prop="role">
          <el-radio-group v-model="loginData.role">
            <el-radio value="COLLEGE">院校</el-radio>  <!-- 使用value而不是label -->
            <el-radio value="STUDENT">毕业生</el-radio>
            <el-radio value="COMPANY">企业</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">登录</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="pushToRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
/* 原样式保持不变 */
.login-container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}
.login-card {
  width: 100%;
  max-width: 400px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 30px;
}
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-header h2 {
  color: #303133;
  margin-bottom: 8px;
  font-size: 24px;
  font-weight: 600;
}
</style>