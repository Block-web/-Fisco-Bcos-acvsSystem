<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Message, Menu, Setting } from '@element-plus/icons-vue'
import { getUserInfoService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import useTokenStore from '@/stores/token.js'
import router from '@/router'
//到时将业务数据 存到userInfoStore中
/* 字段：
         username
         role
         password @jsonignore
         realName
         idCard
         createTime
*/
const userInfoStore = useUserInfoStore()
onMounted(async () => {
  const result = await getUserInfoService()
  //将用户数据添加到pinia中
   userInfoStore.setInfo(result.data)
  if (!result.data.realName) {
    ElMessage.info('您需要先完善基本信息')
    router.push('/student/info')
  }
})


const logout = async () => {
  await ElMessageBox.confirm(
    '确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      // 清除token
      const tokenStore = useTokenStore()
      tokenStore.removeToken()

      // 清除用户信息
      userInfoStore.setInfo({})

      // 跳转到登录页面
      router.push('/login')
      ElMessage({
        type: 'success',
        message: '退出登录成功',
      })

    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '退出登录取消',
      })
    })
}
</script>


<template>
  <el-container class="full-height-container">
    <el-container class="main-container">
      <el-aside width="200px" class="sidebar">
        <el-menu :default-openeds="['1', '3']" class="full-height-menu" router :default-active="$route.path">
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <Message />
              </el-icon>我的学历列表
            </template>
            <el-menu-item-group>
              <el-menu-item index="/student/get-certificate">学历证书</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon>
                <Setting />
              </el-icon>账户
            </template>
            <el-menu-item-group>
              <el-menu-item index="/student/info">基本信息</el-menu-item>
              <el-menu-item index="/student/student-update-password">修改密码</el-menu-item>
              <el-menu-item @click="logout">退出登录</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container class="content-container">
        <el-header class="header">
          <span>毕业生:{{ userInfoStore.info.realName }}</span>
        </el-header>

        <el-main class="main-content">
          <div class="router-view-container">
            <router-view />
          </div>
        </el-main>
      </el-container>
    </el-container>

    <el-footer class="footer">
      ©2026 Created by USY.ZZW2201
    </el-footer>
  </el-container>
</template>

<style scoped>
.full-height-container {
  height: 100vh;
  width: 100vw;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.main-container {
  flex: 1;
  display: flex;
  min-height: 0;
  overflow: hidden;
}

.sidebar {
  background: #eef1f6;
  border-right: 1px solid #dcdfe6;
  overflow: hidden;
}

.full-height-menu {
  height: 100%;
  border: none;
  overflow-y: auto;
}

.content-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  background-color: #b3c0d1;
  color: #333;
  text-align: right;
  font-size: 16px;
  height: 60px;
  line-height: 60px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  border-bottom: 1px solid #dcdfe6;
  flex-shrink: 0;
}

.header-icon {
  margin-right: 15px;
  font-size: 18px;
  cursor: pointer;
}

.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  overflow: auto;
}

.router-view-container {
  height: 100%;
  width: 100%;
  min-height: 0;
}

.footer {
  height: 40px;
  line-height: 40px;
  text-align: center;
  background: #B3C0D1;
  color: #333;
  font-size: 14px;
  flex-shrink: 0;
}
</style>