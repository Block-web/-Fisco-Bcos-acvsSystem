<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo.js'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfoService } from '@/api/user.js'
const router = useRouter()
const userInfoStore = useUserInfoStore();
const formData = ref({
    username: '',
    collegeName: '',
    adminName: '',
    idCard: '',
    createTime: '',
})
onMounted(() => {
    if (userInfoStore.info.IdCard) {
        formData.value.idCard = userInfoStore.info.idCard
    }
    //有用户名一定有创建时间
    if (userInfoStore.info.username) {
        formData.value.username = userInfoStore.info.username
        formData.value.createTime = userInfoStore.info.createTime
    }
    //有realName字段则截取 第一个&之前的内容作为管理员姓名 第二个为院校名称
    if (userInfoStore.info.realName) {
        formData.value.adminName = userInfoStore.info.realName.split('&')[0]
        formData.value.collegeName = userInfoStore.info.realName.split('&')[1]
    }

})
// 提供更新基本信息服务
import { updateCollegeInfoService } from '@/api/college.js'
const update = async () => {
    const updateInfo = ref({
        realName: formData.value.adminName + '&' + formData.value.collegeName,
        idCard: formData.value.idCard,
        username: formData.value.username
    })
    let result = await updateCollegeInfoService(updateInfo.value)
    if (result.code === 0) {
        ElMessage.success(result.msg ? result.msg : '更新成功')
    } else { ElMessage.error(result.msg ? result.msg : '更新失败') }
}

</script>
<template>
    <div class="college-info-container">
        <div class="page-header">
            <h2 class="title">院校信息管理</h2>
            <p class="description">请完善您的院校基本信息</p>
        </div>

        <div class="content-area">
            <el-card class="info-card">
                <el-form class="info-form" label-width="120px" :model="formData">
                    <el-form-item label="用户名">
                        <el-input :value="formData.username" disabled />
                    </el-form-item>
                    <el-form-item label="院校名称">
                        <el-input v-model="formData.collegeName" placeholder="请输入院校名称" />
                    </el-form-item>
                    <el-form-item label="管理员姓名">
                        <el-input v-model="formData.adminName" placeholder="请输入管理员姓名" />
                    </el-form-item>
                    <el-form-item label="身份证号">
                        <el-input v-model="formData.idCard" placeholder="管理员身份证号已隐藏" />
                    </el-form-item>
                    <el-form-item label="创建时间">
                        <el-input v-model="formData.createTime" placeholder="创建时间" disabled />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" size="large" @click="update()">保存信息</el-button>
                        <el-button type="primary" size="large" @click="console.log('修改密码'),router.push('/college-update-password')">修改密码</el-button>
                    </el-form-item>
                </el-form>
            </el-card>
        </div>
    </div>
</template>



<style scoped>
.college-info-container {
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