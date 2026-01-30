import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import i18n from './i18n'
import './assets/main.css'
import '@fortawesome/fontawesome-free/css/all.css'
import { useUserStore } from './stores/user'
const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(i18n)
const userStore = useUserStore()

app.use(router)

userStore.init();
app.mount('#app')
