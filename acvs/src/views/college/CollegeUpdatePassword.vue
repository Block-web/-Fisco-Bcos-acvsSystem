<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo.js'
import { useRouter } from 'vue-router'
import { UpdatePasswordService } from '@/api/user.js'
const router = useRouter()
const userInfoStore = useUserInfoStore();
const formData = ref({
    username: '',
    password: '',
    repassword: '',
})
// 检查密码是否一致
const checkPassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== formData.value.password) {
        callback(new Error('两次输入密码不一致'))
    } else {
        callback()
    }
}
// 提供密码更新服务
const update = async () => {
    let result = await UpdatePasswordService(formData.value.username, formData.value.password);
    if(result.code === 0){
        ElMessage.success(result.msg?result.msg:'密码更新成功')
        router.push("/login")
    } else {
        ElMessage.error(result.msg ? result.msg : '密码更新失败')
    }

}
//提供表单校验规则
const rules = ref({
    password: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 12, message: '密码长度不能小于5位且不能大于12位', trigger: 'blur' }
    ],
    repassword: [
        { required: true, message: '请输入确认密码', trigger: 'blur' },
        { validator: checkPassword, trigger: 'blur' }
    ]
})
//在开始从userInfoStore中获取用户名
const setUsername = () => {
    formData.value.username = userInfoStore.info.username
}
setUsername();


</script>
<template>
    <div class="college-update-container">
        <div class="page-header">
            <h2 class="title">修改密码</h2>
        </div>

        <div class="content-area">
            <el-card class="info-card">
                <el-form class="info-form" label-width="120px" :model="formData" :rules="rules">
                    <el-form-item label="用户名">
                        <el-input :value="formData.username" disabled />
                    </el-form-item>
                    <el-form-item label="新密码" prop="password">
                        <el-input v-model="formData.password" placeholder="请输入新密码" />
                    </el-form-item>
                    <el-form-item label="确认密码" prop="repassword">
                        <el-input v-model="formData.repassword" placeholder="请输入确认密码" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" size="large" @click="update()">修改密码</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </div>
    </div>
</template>



<style scoped>
.college-update-container {
    height: 100%;
    padding: 20px;
    background: #f5f7fa;
}

.page-header {
    margin-bottom: 30px;
    text-align: center;
}

.title {
    color: #303133;
    margin-bottom: 10px;
    font-size: 24px;
}

.description {
    color: #606266;
    font-size: 16px;
}

.content-area {
    display: flex;
    justify-content: center;
    align-items: flex-start;
}

.info-card {
    width: 100%;
    max-width: 600px;
    padding: 30px;
}

.info-form {
    width: 100%;
}

:deep(.el-form-item) {
    margin-bottom: 20px;
}

:deep(.el-input) {
    width: 100%;
}
</style>