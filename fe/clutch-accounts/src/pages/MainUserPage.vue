<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../stores/user'
import { useI18n } from 'vue-i18n'
import { RouterLink } from 'vue-router'
import Navbar from '@/components/Navbar.vue'
import axios from 'axios'
import { onMounted } from 'vue'
const { t } = useI18n()
let currentUser = useUserStore()
const isMenuOpen = ref(false)
const videoRef = ref(null)
const isAudioMuted = ref(true)
const carouselIndex = ref(0)
const itemsPerSlide = ref(8)
const announcements = ref([])

const loadNews = () => {
  axios.get('/api/news/info')
  .then((res) => {
    console.log(res.data)
    announcements.value = res.data
  })
  .catch((err) => {
    console.error('Error fetching news:', err)
  })
}
const handleLogout = () => {
  currentUser.logout()
  isMenuOpen.value = false
}

const toggleAudio = () => {
  isAudioMuted.value = !isAudioMuted.value
  if (videoRef.value) {
    videoRef.value.muted = isAudioMuted.value
  }
}

const toggleMenu = () => {
  isMenuOpen.value = !isMenuOpen.value
}

const closeMenu = () => {
  isMenuOpen.value = false
}

// Color mapping for text based on gradient colors
const colorMap = {
  'from-yellow-500 to-orange-500': 'text-yellow-500',
  'from-cyan-400 to-blue-500': 'text-cyan-400',
  'from-pink-500 to-purple-500': 'text-pink-500',
  'from-red-500 to-pink-500': 'text-red-500'
}

const categories = [
  { id: 1, name: t('main.accValorant'), subtitle: t('main.dropmail'), color: 'from-yellow-500 to-orange-500', textColor: 'text-yellow-500', img: '', link: '/accounts/acc-drop-mail' },
  { id: 2, name: t('main.accValorant'), subtitle: t('main.superSale'), color: 'from-cyan-400 to-blue-500', textColor: 'text-cyan-400', img: '', link: '/accounts/acc-sieu-sale' },
  { id: 3, name: t('main.accValorant'), subtitle: t('main.randomNFA'), color: 'from-pink-500 to-purple-500', textColor: 'text-pink-500', img: '', link: '/accounts/acc-random-nfa' },
  { id: 4, name: t('main.accValorant'), subtitle: t('main.randomFA'), color: 'from-red-500 to-pink-500', textColor: 'text-red-500', img: '', link: '/accounts/acc-random-fa' }
]

const otherProducts = [
  { id: 1, name: t('main.riotPass'), image: '🎮' },
  { id: 2, name: t('main.meleeWeapon'), image: '⚔️' },
  { id: 3, name: t('main.weaponCombo'), image: '🎯' },
  { id: 4, name: t('main.pcGamingSale'), image: '🖥️' }
]


// Carousel computed property
const visibleAnnouncements = computed(() => {
  const start = carouselIndex.value
  const end = start + itemsPerSlide.value
  return announcements.value.slice(start, end)
})

const totalSlides = computed(() => Math.ceil(announcements.value.length / itemsPerSlide.value))

const nextSlide = () => {
  carouselIndex.value = (carouselIndex.value + itemsPerSlide.value) % announcements.value.length
}

const prevSlide = () => {
  carouselIndex.value = (carouselIndex.value - itemsPerSlide.value + announcements.value.length) % announcements.value.length
}

const goToSlide = (index) => {
  carouselIndex.value = index * itemsPerSlide.value
}

// Helper function to truncate text
const truncateText = (text, length = 188) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// Quick links for sidebar
const quickLinks = [
  { icon: 'fa fa-question-circle', label: t('main.faq') || 'FAQ', route: '/faq', color: 'from-blue-500 to-cyan-500', borderColor: 'border-blue-500/30 hover:border-blue-400/50' },
  { icon: 'fa fa-history', label: t('userPanel.transactionHistory'), route: '/user/purchase-history', color: 'from-purple-500 to-pink-500', borderColor: 'border-purple-500/30 hover:border-purple-400/50' },
  { icon: 'fa fa-shield-alt', label: t('common.warranty'), route: '/warranty-policy', color: 'from-green-500 to-emerald-500', borderColor: 'border-green-500/30 hover:border-green-400/50' }
]

onMounted(() => {
  loadNews()
})
</script>
<template>
  <Navbar></Navbar>
  <main class="min-h-screen main-background relative">
    <!-- Background Overlay for gradient and theme -->
    <div class="absolute inset-0 bg-gradient-to-b from-slate-950 via-slate-900 to-slate-950 opacity-50"></div>

    <!-- Hero Banner Section -->
    <section class="relative z-10 w-full h-96 overflow-hidden">
      <!-- Background Video -->
      <video ref="videoRef" src="../assets/Videos/video-main.mp4" autoplay muted loop
        class="absolute inset-0 w-full h-full object-cover">
      </video>

      <!-- Volume Control Button -->
      <button @click="toggleAudio"
        class="absolute top-4 right-4 z-20 bg-amber-600/80 hover:bg-amber-600 text-white font-bold py-2 px-4 rounded-lg transition-all duration-300 hover:shadow-lg hover:shadow-amber-600/50 flex items-center gap-2">
        <i :class="isAudioMuted ? 'fa fa-volume-mute' : 'fa fa-volume-high'"></i>
        <span class="text-sm">{{ isAudioMuted ? t('main.unmute') : t('main.mute') }}</span>
      </button>

      <!-- Video Overlay for better text contrast -->
      <div class="absolute inset-0 bg-gradient-to-r from-black/70 via-black/50 to-black/70"></div>

      <!-- Background Glow -->
      <div class="absolute inset-0 opacity-20">
        <div class="absolute bottom-0 right-1/4 w-96 h-96 bg-amber-500 rounded-full blur-3xl"></div>
      </div>

      <!-- Content -->
      <div class="relative z-10 flex items-center justify-between h-full px-8">
        <div class="flex-1">
          <h1 class="text-5xl font-black text-white mb-4 drop-shadow-lg">{{ t('main.welcomeBack') }}</h1>
          <p class="text-xl text-amber-300 font-semibold drop-shadow-md">{{ t('main.subtitle') }}</p>
        </div>

        <div class="flex gap-6 mt-12">
          <!-- Balance Card -->
          <div
            class="bg-gradient-to-br from-amber-400/10 to-orange-600/10 backdrop-blur-md border border-amber-500/30 rounded-2xl p-6 min-w-max">
            <p class="text-amber-200 text-sm font-semibold mb-2">{{ t('main.currentBalance') }}</p>
            <p class="text-3xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">{{
              currentUser.balance}} VND</p>
          </div>

          <!-- Level Card -->
          <div
            class="bg-gradient-to-br from-purple-400/10 to-pink-600/10 backdrop-blur-md border border-purple-500/30 rounded-2xl p-6 min-w-max">
            <div class="flex items-center gap-2.5 relative">
              <img
                class="w-10 h-10 rounded-full font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)] cursor-pointer hover:scale-110 transition-transform duration-200"
                :src="currentUser.avatar" alt="userAvt"
                @click="toggleMenu">
              
              <!-- User Avatar Dropdown Menu -->
              <div v-if="isMenuOpen" class="absolute -top-32 -left-32 w-56 z-50">
                <div class="bg-gradient-to-br from-slate-900 to-black backdrop-blur-md border border-amber-500/40 rounded-xl shadow-2xl shadow-amber-600/20 overflow-hidden">
                  <!-- Menu Header -->
                  <div class="bg-gradient-to-r from-amber-600/20 to-orange-600/20 border-b border-amber-500/30 px-6 py-4 flex items-center gap-3">
                    <img :src="currentUser.avatar" alt="avatar" class="w-12 h-12 rounded-full border-2 border-amber-400/50">
                    <div class="flex-1">
                      <p class="text-amber-300 font-bold text-sm">{{ currentUser.username }}</p>
                      <p class="text-slate-400 text-xs">Role: {{currentUser.role}}</p>
                    </div>
                  </div>

                  <!-- Menu Items -->
                  <div class="py-2">
                    <!-- Profile -->
                    <RouterLink to="/user/profile" class="w-full px-6 py-3 text-left text-slate-200 hover:bg-amber-500/10 hover:text-amber-300 transition-all duration-200 flex items-center gap-3 group border-b border-slate-800/50">
                      <i class="fa fa-user text-amber-400 group-hover:scale-110 transition-transform"></i>
                      <span class="font-medium">{{ t('common.profile') }}</span>
                      <span class="ml-auto text-amber-400/60 text-xs">→</span>
                    </RouterLink>
                    <!-- Transaction History -->
                    <RouterLink to="/user/purchase-history" class="w-full px-6 py-3 text-left text-slate-200 hover:bg-amber-500/10 hover:text-amber-300 transition-all duration-200 flex items-center gap-3 group border-b border-slate-800/50">
                      <i class="fa fa-magnifying-glass-dollar text-amber-400 group-hover:scale-110 transition-transform"></i>
                      <span class="font-medium">{{ t('userPanel.transactionHistory') }}</span>
                      <span class="ml-auto text-amber-400/60 text-xs">→</span>
                    </RouterLink>
                    <!-- Help & Support -->
                    <button class="w-full px-6 py-3 text-left text-slate-200 hover:bg-amber-500/10 hover:text-amber-300 transition-all duration-200 flex items-center gap-3 group border-b border-slate-800/50">
                      <i class="fa fa-question-circle text-amber-400 group-hover:scale-110 transition-transform"></i>
                      <span class="font-medium">Help & Support</span>
                      <span class="ml-auto text-amber-400/60 text-xs">→</span>
                    </button>

                    <!-- Logout -->
                    <button @click="handleLogout" class="w-full px-6 py-3 text-left text-red-400 hover:bg-red-500/10 hover:text-red-300 transition-all duration-200 flex items-center gap-3 group">
                      <i class="fa fa-sign-out text-red-500 group-hover:scale-110 transition-transform"></i>
                      <span class="font-medium">{{ t('common.logout') }}</span>
                      <span class="ml-auto text-red-500/60 text-xs">→</span>
                    </button>
                  </div>
                </div>
              </div>

              <!-- Click outside to close menu -->
              <div v-if="isMenuOpen" @click="closeMenu" class="fixed inset-0 z-40"></div>
              
              <div class="font-medium text-heading">
                <div class="text-purple-200 text-sm font-semibold mb-2">{{ t('main.quote') }}</div>
                <div class="text-2xl font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]">{{
                  currentUser.username }}</div>
              </div>
            </div>
            <button @click="handleLogout"
              class="mt-4 w-full bg-gradient-to-r from-red-600 to-red-700 hover:from-red-700 hover:to-red-800 text-white font-bold py-2 px-4 rounded-lg transition-all duration-300 hover:shadow-lg hover:shadow-red-600/50 hover:scale-105 active:scale-95 group">
              <i class="fa fa-power-off transition-transform duration-300 group-hover:rotate-12" aria-hidden="true"></i>
              {{ t('common.logout') }}
            </button>
          </div>
        </div>
      </div>

      <!-- Decorative Lines -->
      <div class="absolute top-20 left-8 w-1 h-20 bg-gradient-to-b from-amber-400 to-transparent"></div>
      <div class="absolute bottom-20 right-8 w-1 h-20 bg-gradient-to-t from-purple-400 to-transparent"></div>
    </section>

    <!-- Main Content -->
    <section class="max-w-7xl mx-auto px-8 py-16 relative z-10">
      <!-- Announcements Carousel Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-amber-400">📢</span> {{ t('main.announcements') }}
          <div class="flex-1 h-1 bg-gradient-to-r from-amber-400 to-transparent ml-4"></div>
        </h2>

        <div class="flex gap-8">
          <!-- Carousel Container -->
          <div class="flex-1">
            <!-- Carousel Wrapper -->
            <div class="relative">
              <!-- Carousel Items -->
              <div class="grid grid-cols-2 gap-4 overflow-hidden">
                <div v-for="item in visibleAnnouncements" 
                  :key="item.news_id"
                  class="announcement-card group bg-gradient-to-br from-slate-800/50 to-slate-900/50 backdrop-blur-md border border-amber-500/20 rounded-2xl p-4 hover:border-amber-400/50 transition-all duration-500 cursor-pointer hover:shadow-2xl hover:shadow-amber-600/20 hover:scale-105 animate-fadeInSlide">
                  <div class="flex gap-3 h-full">
                    <!-- Left: Image -->
                    <div class="w-20 h-20 bg-gradient-to-br from-amber-400/20 to-orange-600/20 rounded-lg flex items-center justify-center flex-shrink-0 group-hover:from-amber-400/40 group-hover:to-orange-600/40 transition-all duration-300 overflow-hidden">
                      <img class="object-cover w-full h-full" :src="item.embed_link ? item.embed_link : 'https://cdn.oneesports.vn/cdn-data/sites/4/2021/11/valorant-chamber-6180efdccf50a-scaled.jpg'" alt="Image section">
                    </div>
                    <!-- Right: Title and Content -->
                    <div class="flex-1 flex flex-col justify-start gap-1">
                      <h3 class="text-amber-300 font-bold group-hover:text-amber-200 transition-colors text-sm line-clamp-1">{{ item.title }}</h3>
                      <p class="text-slate-400 text-xs group-hover:text-slate-300 transition-colors">{{ truncateText(item.content, 188) }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Navigation Buttons -->
              <button @click="prevSlide"
                class="absolute -left-16 top-1/2 -translate-y-1/2 z-20 w-12 h-12 bg-gradient-to-r from-amber-600 to-orange-600 hover:from-amber-500 hover:to-orange-500 text-white rounded-full flex items-center justify-center transition-all duration-300 hover:shadow-lg hover:shadow-amber-600/50 hover:scale-110 active:scale-95 disabled:opacity-50"
                title="Previous">
                <i class="fa fa-chevron-left"></i>
              </button>

              <button @click="nextSlide"
                class="absolute -right-16 top-1/2 -translate-y-1/2 z-20 w-12 h-12 bg-gradient-to-r from-amber-600 to-orange-600 hover:from-amber-500 hover:to-orange-500 text-white rounded-full flex items-center justify-center transition-all duration-300 hover:shadow-lg hover:shadow-amber-600/50 hover:scale-110 active:scale-95"
                title="Next">
                <i class="fa fa-chevron-right"></i>
              </button>
            </div>

            <!-- Carousel Indicators -->
            <div class="flex justify-center gap-3 mt-8">
              <button v-for="(_, index) in Array(totalSlides)"
                :key="index"
                @click="goToSlide(index)"
                :class="[
                  'transition-all duration-300 rounded-full',
                  carouselIndex === index * itemsPerSlide ? 
                    'w-8 h-3 bg-gradient-to-r from-amber-400 to-orange-500 shadow-lg shadow-amber-600/50' : 
                    'w-3 h-3 bg-slate-600 hover:bg-slate-500'
                ]"
                :aria-label="`Go to slide ${index + 1}`">
              </button>
            </div>
          </div>

          <!-- Right Sidebar - Quick Links -->
          <div class="hidden lg:flex flex-col gap-4 w-60">
            <div class="text-sm font-bold text-amber-300 mb-4 text-center px-4 py-2 bg-amber-500/10 rounded-lg border border-amber-500/20">
              Quick Access
            </div>
            
            <RouterLink v-for="link in quickLinks" 
              :key="link.label"
              :to="link.route"
              class="group relative bg-gradient-to-br from-slate-800/50 to-slate-900/50 backdrop-blur-md border transition-all duration-300 rounded-xl p-4 hover:shadow-xl overflow-hidden"
              :class="link.borderColor">
              
              <!-- Background Gradient Overlay -->
              <div :class="`absolute inset-0 opacity-0 group-hover:opacity-10 bg-gradient-to-br ${link.color} transition-opacity duration-300`"></div>
              
              <!-- Content -->
              <div class="relative z-10 flex flex-col items-center gap-3">
                <div :class="`w-12 h-12 rounded-lg bg-gradient-to-br ${link.color} flex items-center justify-center group-hover:scale-110 transition-transform duration-300`">
                  <i :class="`${link.icon} text-white text-xl`"></i>
                </div>
                <span class="text-center text-sm font-semibold text-slate-200 group-hover:text-white transition-colors">{{ link.label }}</span>
                <div class="w-0 h-0.5 bg-gradient-to-r from-amber-400 to-orange-500 group-hover:w-full transition-all duration-300"></div>
              </div>
            </RouterLink>
          </div>

          <!-- Mobile Quick Links -->
          <div class="lg:hidden flex gap-3 mt-4">
            <RouterLink v-for="link in quickLinks" 
              :key="link.label"
              :to="link.route"
              class="flex-1 group bg-gradient-to-br from-slate-800/50 to-slate-900/50 backdrop-blur-md border border-amber-500/20 rounded-xl p-3 hover:border-amber-400/50 transition-all duration-300 hover:shadow-lg hover:shadow-amber-600/20 text-center">
              <div class="flex flex-col items-center gap-2">
                <i :class="`${link.icon} text-amber-400 text-lg`"></i>
                <span class="text-xs font-semibold text-slate-200 group-hover:text-amber-300 transition-colors">{{ link.label }}</span>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>

      <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> {{ t('main.accValorant') }}
          <div class="flex-1 h-1 bg-gradient-to-r from-purple-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div v-for="item in categories" :key="item.id"
            class="group relative bg-gradient-to-br from-black/80 to-slate-950/80 backdrop-blur-md border border-amber-900/50 rounded-2xl overflow-hidden hover:border-amber-500/70 transition-all duration-300 cursor-pointer hover:shadow-2xl hover:shadow-amber-600/40">

            <!-- Background Gradient -->
            <div
              :class="`absolute inset-0 opacity-0 group-hover:opacity-20 bg-gradient-to-br ${item.color} transition-opacity duration-300`">
            </div>

            <!-- Content -->
            <div class="relative z-10 p-8 flex flex-col items-center justify-center h-64 text-center">
              <div
                class="w-20 h-20 rounded-full bg-gradient-to-br from-amber-900 to-black flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300">
                <span class="text-3xl text-amber-300">🎮</span>
              </div>
              <h3 class="text-white font-black text-xl mb-2">{{ item.name }}</h3>
              <router-link :to="item.link">
                <p :class="`text-lg font-bold ${item.textColor}`">
                  {{ item.subtitle }}
                </p>
              </router-link>
            </div>

            <!-- Border Glow -->
            <div
              class="absolute inset-0 rounded-2xl pointer-events-none border border-gradient-to-r from-amber-400/0 via-amber-400/0 to-amber-400/0 group-hover:from-amber-600/40 group-hover:via-amber-500/60 group-hover:to-amber-600/40 transition-all duration-300">
            </div>
          </div>
        </div>
      </div>

      <!-- PC Gaming Section -->
      <div>
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-cyan-400">🖥️</span> {{ t('main.accValorant') }} - {{ t('main.superSale') }} PC GAMING
          <div class="flex-1 h-1 bg-gradient-to-r from-cyan-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div v-for="item in otherProducts" :key="item.id"
            class="group relative bg-gradient-to-br from-slate-800/60 to-slate-900/60 backdrop-blur-md border border-cyan-500/30 rounded-2xl overflow-hidden hover:border-cyan-400/70 transition-all duration-300 hover:shadow-lg hover:shadow-cyan-500/30">

            <!-- Content -->
            <div class="relative z-10 p-8 flex flex-col items-center justify-center h-64 text-center">
              <div
                class="w-24 h-24 rounded-lg bg-gradient-to-br from-cyan-500/20 to-blue-600/20 flex items-center justify-center mb-6 group-hover:scale-110 group-hover:from-cyan-500/40 group-hover:to-blue-600/40 transition-all duration-300">
                <span class="text-4xl">{{ item.image }}</span>
              </div>
              <h3 class="text-white font-black text-lg">{{ item.name }}</h3>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer Spacer -->
    <div class="h-20"></div>
  </main>
</template>

<style scoped>
main {
  position: relative;
}

.main-background {
  background-image: url('../img/main-background.jpg');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}

/* Carousel Animation */
@keyframes fadeInSlide {
  from {
    opacity: 0;
    transform: translateX(20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.animate-fadeInSlide {
  animation: fadeInSlide 0.5s ease-out forwards;
}

.announcement-card {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Shimmer animation for premium feel */
@keyframes shimmer {
  0% {
    background-position: -1000px 0;
  }

  100% {
    background-position: 1000px 0;
  }
}

.shimmer {
  animation: shimmer 3s infinite;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.1) 50%, rgba(255, 255, 255, 0) 100%);
  background-size: 1000px 100%;
}

/* Glow effects */
.glow-amber {
  box-shadow: 0 0 30px rgba(255, 184, 28, 0.3), inset 0 0 30px rgba(255, 184, 28, 0.1);
}

.glow-purple {
  box-shadow: 0 0 30px rgba(168, 85, 247, 0.3), inset 0 0 30px rgba(168, 85, 247, 0.1);
}

/* Hover effects */
.card-hover {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card-hover:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
}
</style>
