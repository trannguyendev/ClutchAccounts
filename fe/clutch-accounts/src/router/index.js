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
]
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
