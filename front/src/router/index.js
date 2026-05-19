import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/explore',
    name: 'Explore',
    component: () => import('../views/Explore.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/create',
    name: 'Create',
    component: () => import('../views/Create.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile/:id?',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/post/:id',
    name: 'PostDetail',
    component: () => import('../views/PostDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { guestOnly: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { guestOnly: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  // 从 localStorage 直接检查登录状态，避免 Pinia store 初始化问题
  const token = localStorage.getItem('token')
  const isLoggedIn = !!token

  // 如果访问根路径且未登录，重定向到登录页
  if (to.path === '/' && !isLoggedIn) {
    next('/login')
    return
  }

  if (to.meta.requiresAuth && !isLoggedIn) {
    // 需要登录但未登录，重定向到登录页
    next('/login')
  } else if (to.meta.guestOnly && isLoggedIn) {
    // 已登录用户访问登录/注册页，重定向到首页
    next('/')
  } else {
    next()
  }
})

export default router
