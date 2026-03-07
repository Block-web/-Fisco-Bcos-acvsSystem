<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { userRegisterService } from '@/api/user.js'
import router from '@/router'

const registerData = ref({
  username: '',
  password: '',
  rePassword: '',
  role: ''
})

const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' }
  ],
  rePassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: checkRePassword, trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户类型', trigger: 'change' },
    { type: 'enum', enum: ['COLLEGE', 'STUDENT', 'COMPANY'], message: '用户类型必须是COLLEGE、STUDENT或COMPANY', trigger: 'change' }
  ]
})

const registerFormRef = ref(null)

const submitForm = () => {
  registerFormRef.value.validate(async valid => {
    if (valid) {
      try {
        const result = await userRegisterService(registerData.value)
        if (result.code === 0) {
          ElMessage.success('注册成功')
          pushToLogin()
        } else {
          ElMessage.error(result.msg || '用户名或已存在')
        }
      } catch {}
    } else {
      ElMessage.error('参数错误')
    }
  })
}

const resetForm = () => registerFormRef.value.resetFields()
const pushToLogin = () => router.push('/login')
</script>

<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>学历证书验证系统</h2>
        <p>注册</p>
      </div>

      <el-form :model="registerData" :rules="rules" ref="registerFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerData.username" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="registerData.password" type="password" show-password />
        </el-form-item>

        <el-form-item label="确认密码" prop="rePassword">
          <el-input v-model="registerData.rePassword" type="password" show-password />
        </el-form-item>

        <el-form-item label="用户类型" prop="role">
          <el-radio-group v-model="registerData.role">
            <el-radio label="COLLEGE">院校</el-radio>
            <el-radio label="STUDENT">毕业生</el-radio>
            <el-radio label="COMPANY">企业</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">注册</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="pushToLogin">返回登录</el-button>
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