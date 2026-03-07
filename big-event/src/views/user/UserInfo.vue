<script setup>
import { ref } from 'vue'
//导入pinia
import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();
//从pinia中获取用户信息  (...userInfoStore.info)  展开运算符将对象中的属性展开到新对象中
const userInfo = ref({ ...userInfoStore.info })
const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}
//导入接口函数
import { updataUserInfoService } from '@/api/user.js'
import { ElMessage } from 'element-plus'
//创建函数调用更新用户信息接口
const updateUserInfo = async () => {
    //调用接口  传递数据模型
    let result = await updataUserInfoService(userInfo.value);
    ElMessage.success(result.msg ? result.msg : '更新用户信息成功')
    //更新pinia中存储的用户信息  因为数据双向绑定  所以直接赋值即可
    userInfoStore.setInfo(userInfo.value);
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>