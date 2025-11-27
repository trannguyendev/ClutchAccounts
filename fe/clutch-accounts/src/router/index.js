import Index from '@/pages/index.vue'
import Login from '@/pages/Login.vue'
import MainUserPage from '@/pages/MainUserPage.vue'
import { createRouter, createWebHistory } from 'vue-router'
import AccDropMail from '@/pages/AccDropMail.vue'
import Error403 from '@/components/Error403.vue'
import Error404 from '@/components/Error404.vue'
import AccSieuSale from '@/pages/AccSieuSale.vue'
import AccRandomNFA from '@/pages/AccRandomNFA.vue'
import AccRandomFA from '@/pages/AccRandomFA.vue'


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
  //path to main page for auth users
  {
    path: '/main',
    name: 'main',
    component: MainUserPage,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  //path to type of accounts
  {
    path: '/accounts/acc-drop-mail',
    name: 'acc-drop-mail',
    component: AccDropMail,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  {
    path: '/accounts/acc-sieu-sale',
    name: 'acc-sieu-sale',
    component: AccSieuSale
  },
  {
    path: '/accounts/acc-random-nfa',
    name: 'acc-random-nfa',
    component: AccRandomNFA,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  {
    path: '/accounts/acc-random-fa',
    name: 'acc-random-fa',
    component: AccRandomFA,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  //error path 
  {
    path: '/denied',
    name: 'denied',
    component: Error403
  },
  {
    path: '/not-found',
    name: 'not-found',
    component: Error404
  },
  //payment path
  {
    path: '/payment/:transaction_content',
    name: 'payment',
    component: "hehe"
  }
]
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})
// router.beforeEach( async (to, from, next) => {
//   const isAuthenticated = true // placeholder for real auth check
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     if (!isAuthenticated) {
//       next({ name: 'auth' })
//     } else {
//       next()
//     }
//   } else {
//     next({name: 'denied'})
//   }
// })
export default router
