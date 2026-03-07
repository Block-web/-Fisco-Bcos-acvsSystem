<script setup>
import {
    Management,
    Promotion,
    UserFilled,
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import { userInfoService } from '@/api/user.js'
//导入pinia
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js'
//获取模块
const userInfoStore = useUserInfoStore();
const tokenStore = useTokenStore();
//获取用户详细信息
const getUserInfo = async () => {
    let result = await userInfoService();
    userInfoStore.setInfo(result.data);
}
//调用函数获取用户信息
getUserInfo();

//引入路由
import router from '@/router/index.js'

import { ElMessageBox } from 'element-plus'
import { ElMessage } from 'element-plus'

//提供command 事件的函数  command 为下拉菜单中点击的项
const handleCommand = (command) => {
    //判断是否为logout 如果是则直接退出
    if (command === 'logout') {
        //删除分类  
        ElMessageBox.confirm(
            '确定退出登录吗？',
            '温馨提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(async () => {
                //退出登录
                //清空pinia中存储的token和用户信息
                userInfoStore.removeInfo();
                tokenStore.removeToken();
                //退出登录后跳转到登录页面
                router.push('/login');
                ElMessage({
                    type: 'success',
                    message: '退出登录成功',
                })

            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '已取消',
                })
            })
    } else {
        //跳转路由
        router.push('/user/' + command)
    }

}


</script>

<template>
    <!-- element-plus的布局容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <!-- index=：路由路径  点击后会跳转到对应的路由组件  -->
                <el-menu-item index="/article/category">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>文章分类</span>
                </el-menu-item>
                <el-menu-item index="/article/manage">
                    <el-icon>
                        <Promotion />
                    </el-icon>
                    <span>文章管理</span>
                </el-menu-item>
                <el-sub-menu>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <!-- index=：路由路径  点击后会跳转到对应的路由组件  -->
                    <el-menu-item index="/user/info">
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <!-- index=：路由路径  点击后会跳转到对应的路由组件  -->
                    <el-menu-item index="/user/avatar">
                        <el-icon>
                            <Crop />
                        </el-icon>
                        <span>更换头像</span>
                    </el-menu-item>
                    <!-- index=：路由路径  点击后会跳转到对应的路由组件  -->
                    <el-menu-item index="/user/resetPassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>黑马程序员：<strong>{{ userInfoStore.info.username }}</strong></div>
                <!-- 下拉菜单 -->
                <!-- command : 点击下拉菜单项时触发的事件，用于处理不同的操作 命名与router路由中的路径一致即可  要在组件声明@command="handleCommand"-->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <!-- 头像 -->
                        <el-avatar :src="userInfoStore.info.userPic ? userInfoStore.info.userPic : avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetpassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->

                <!-- 配置子路由出口 ：在需要点击切换的地方添加index属性-->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>大事件 ©2023 Created by 黑马程序员</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #232323;

        &__logo {
            height: 120px;
            background: url('@/assets/logo.png') no-repeat center / 120px auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>