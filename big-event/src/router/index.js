//导入vue-router  需要先npm install vue-router
import {
    createRouter,
    createWebHistory
} from "vue-router";

//导入路由组件
import LoginVue from '@/views/Login.vue';
import LayoutVue from '@/views/Layout.vue';
//导入子路由组件
import UserInfoVue from '@/views/user/UserInfo.vue';
import ArticleCategoryVue from '@/views/article/ArticleCategory.vue';
import UserResetPasswordVue from "@/views/user/UserResetPassword.vue";
import UserAvatarVue from "@/views/user/UserAvatar.vue";
import ArticleManageVue from "@/views/article/ArticleManage.vue";


//定义路由关系
const routes = [
    //path：路径  component：组件
    {
        path: '/login',
        component: LoginVue
    },
   
    {
        path: '/',
         /* redirect: 重定向路径  访问 / 时会默认跳转到/article/category */
        redirect: '/article/category',
        component: LayoutVue,
        //配置子路由 要在 父路由组件中配置<router-view></router-view>
        children: [
            //path：子路由路径  component：子路由组件
            {
                path: '/article/category',
                component: ArticleCategoryVue
            },
            {
                path: '/user/info',
                component: UserInfoVue
            },
            {
                path: '/user/resetPassword',
                component: UserResetPasswordVue
            },
            {
                path: '/user/avatar',
                component: UserAvatarVue
            },
            {
                path: '/article/manage',
                component: ArticleManageVue
            },

        ]
    }
]

//创建路由器
const router = createRouter({
    /* 路由关系 */
    routes: routes,
    /* 路由模式 */
    history: createWebHistory()
})

//导出路由
export default router;