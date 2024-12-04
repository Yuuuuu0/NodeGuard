import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/index.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/node/vless',
    children: [
      {
        path: 'node',
        component: () => import('@/views/node/index.vue'),
        redirect: '/node/vless',
        meta: { title: '节点管理' },
        children: [
          {
            path: 'vless',
            component: () => import('@/views/node/vless/index.vue'),
            meta: { title: 'Vless节点' }
          }
        ]
      },
      {
        path: 'token',
        component: () => import('@/views/token/index.vue'),
        meta: { title: 'Token管理' }
      },
      {
        path: 'rule',
        component: () => import('@/views/rule/index.vue'),
        meta: { title: '规则管理' }
      },
      {
        path: 'sys/user',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'sys/dict',
        component: () => import('@/views/system/dict/index.vue'),
        meta: { title: '字典管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router 