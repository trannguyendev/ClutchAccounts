import Index from '@/pages/index.vue'
import Login from '@/pages/Login.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Index
  },
  {
    path: '/auth',
    name: 'auth',
    component: Login
  },
  {
    path: '/admin',
    name: 'admin',
    component: "smth",
    meta: { requiresAuth: true , roles: ['admin']}
  },
  {
    path: '/main',
    name: 'main',
    component: "smth",
    meta: { requiresAuth: true , roles: ['user', 'admin']}
  }
]
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
