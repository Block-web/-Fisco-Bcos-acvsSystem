// src/router/index.js

import {
  createRouter,
  createWebHistory
} from 'vue-router'

/* 懒加载引入页面 */
const Register = () => import('@/views/Register.vue')
const Login = () => import('@/views/Login.vue')
const College = () => import('@/views/college/College.vue')
const Student = () => import('@/views/student/Student.vue')
const Company = () => import('@/views/company/Company.vue')




const routes = [{
    path: '/',
    redirect: '/login'
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/college',
    name: 'college',
    component: College,
    children: [{
        path: 'info',
        name: 'collegeInfo',
        component: () => import('@/views/college/CollegeInfo.vue')
      },
      {
        path: 'pdf-generation',
        name: 'PdfGeneration',
        component: () => import('@/views/college/PdfGeneration.vue')
      },
      {
        path: 'college-update-password',
        name: 'CollegeUpdatePassword',
        component: () => import('@/views/college/CollegeUpdatePassword.vue')
      },
      {
        path: 'pdf-upload',
        name: 'PdfUpload',
        component: () => import('@/views/college/PdfUpload.vue')
      },
      {
        path: 'chain-upload',
        name: 'ChainUpload',
        component: () => import('@/views/college/ChainUpload.vue')
      }, {
        path: 'verify-certificate',
        name: 'VerifyCertificate',
        component: () => import('@/views/college/VerifyCertificate.vue')
      }
    ]
  },
  {
    path: '/student',
    name: 'student',
    component: Student,
    children: [{
      path: 'info',
      name: 'studentInfo',
      component: () => import('@/views/student/StudentInfo.vue')
    }, {
      path: 'student-update-password',
      name: 'StudentUpdatePassword',
      component: () => import('@/views/student/StudentUpdatePassword.vue')
    }, {
      path: 'get-certificate',
      name: 'GetCertificate',
      component: () => import('@/views/student/GetCertificate.vue')
    }, ]
  },
  {
    path: '/company',
    name: 'company',
    component: Company,
    children: [{
      path: '/company/info',
      name: 'companyInfo',
      component: () => import('@/views/company/CompanyInfo.vue')
    },{
      path: '/company/company-update-password',
      name: 'CompanyUpdatePassword',
      component: () => import('@/views/company/CompanyUpdatePassword.vue')
    }]
  }
]

export default createRouter({
  history: createWebHistory(), // Router 4 历史模式
  routes: routes
})