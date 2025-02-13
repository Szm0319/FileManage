import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/upload',
    name: 'Upload',
    component: () => import('@/views/UploadView.vue')
  },
  {
    path: '/management',
    name: 'Management',
    component: () => import('@/views/ManagementView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { showNav: false }  // 添加 meta 字段，标记不显示导航栏
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/RegisterView.vue'),
    meta: { showNav: false }  // 添加 meta 字段，标记不显示导航栏
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
