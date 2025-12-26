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
import Payment from '@/pages/Payment.vue'
import AdminDashBoard from '@/pages/AdminDashBoard.vue'
import UserInfo from '@/pages/UserInfo.vue'
import ForgotPass from '@/pages/ForgotPass.vue'



const routes = [
  {
    path: '/',
    name: 'home',
    component: Index
  },
  {
    path: '/ok',
    name: 'ok',
    component: AccRandomFA
  },
  {
    path: '/auth',
    name: 'auth',
    component: Login
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminDashBoard,
    meta: { requiresAuth: true , roles: ['admin']}
  },
  {
    path: '/forgot',
    name: 'forgot',
    component: ForgotPass
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
    component: AccSieuSale,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
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
    component: Payment,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  //Path to function of submenu page
  {
    path: '/user/profile',
    name: 'user-profile',
    component: UserInfo,
    meta: { requiresAuth: true , roles: ['user', 'admin']},
  },
  //Catch-all route for some s2pid access paths which doesn't exist
  {
    path: '/:pathMatch(.*)*',
    redirect: '/not-found'
  }
]
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})
router.beforeEach((to, from, next) => {
  let token = null
  let role = null

  //Get data from currentuser object in localStorage
  const currentUser = localStorage.getItem('currentuser')
  if (currentUser) {
    try {
      const userData = JSON.parse(currentUser)
      token = userData.token
      role = userData.role
    } catch (err) {
      console.log("Errror: "+err)
    }
  }

  if (to.meta.requiresAuth && (!token || token.trim().length === 0)) {
    // No token and auth required
    next('/denied')
  } else if (to.meta.requiresAuth && to.meta.roles) {
    // Auth required AND specific roles required
    if (role && to.meta.roles.includes(role)) {
      next()
    } else {
      next('/denied')
    }
  } else if (to.meta.requiresAuth) {
    // Auth required but no specific roles
    next()
  } else {
    // No auth required
    next()
  }
})
export default router
