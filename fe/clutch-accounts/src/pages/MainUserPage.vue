<script setup>
import { ref } from 'vue'
import { useUserStore } from '../stores/user'
// const currentUser = ref([]); //this section for fetch user info from server <declare here for using later>
import { RouterLink } from 'vue-router'
const user = useUserStore()
const currentUser =
{
  id: user.id,
  name: user.username,
  balance: user.balance,
  img: user.avatar
}
console.log(currentUser);
const categories = [
  { id: 1, name: 'ACC VALORANT', subtitle: 'DROPMAIL', color: 'from-yellow-500 to-orange-500', img: '', link: '/accounts/acc-drop-mail' },
  { id: 2, name: 'ACC VALORANT', subtitle: 'SUPER SALE', color: 'from-cyan-400 to-blue-500', img: '', link: '/accounts/acc-sieu-sale' },
  { id: 3, name: 'ACC VALORANT', subtitle: 'RANDOM NFA', color: 'from-pink-500 to-purple-500', img: '', link: '/accounts/acc-random-nfa' },
  { id: 4, name: 'ACC VALORANT', subtitle: 'RANDOM FA', color: 'from-red-500 to-pink-500', img: '', link: '/accounts/acc-random-fa' }
]

const otherProducts = [
  { id: 1, name: 'ĐẾN RIOT', image: '🎮' },
  { id: 2, name: 'DAO MỎ HÌNH MELEE', image: '⚔️' },
  { id: 3, name: 'COMBO HỢP 2-3 MỎ HÌNH', image: '🎯' },
  { id: 4, name: 'PC GAMING SALE', image: '🖥️' }
]

const announcements = [
  { id: 1, title: 'Shop bán acc uy tín', desc: 'Lựa chọn hàng đầu của...' },
  { id: 2, title: 'Shop Acc Valorant Uy Tín', desc: 'Nền bản đăng tin mua...' },
  { id: 3, title: 'Hướng dẫn nạp Valorant', desc: 'Hướng dẫn nạp Valorant...' }
]
</script>
<template>
  <main class="min-h-screen main-background relative">
    <!-- Background Overlay for gradient and theme -->
    <div class="absolute inset-0 bg-gradient-to-b from-slate-950 via-slate-900 to-slate-950 opacity-50"></div>

    <!-- Hero Banner Section -->
    <section class="relative z-10 w-full h-96 overflow-hidden">
      <!-- Background Video -->
      <video src="../assets/Videos/video-main.mp4" autoplay muted loop
        class="absolute inset-0 w-full h-full object-cover">
      </video>

      <!-- Video Overlay for better text contrast -->
      <div class="absolute inset-0 bg-gradient-to-r from-black/70 via-black/50 to-black/70"></div>

      <!-- Background Glow -->
      <div class="absolute inset-0 opacity-20">
        <div class="absolute bottom-0 right-1/4 w-96 h-96 bg-amber-500 rounded-full blur-3xl"></div>
      </div>

      <!-- Content -->
      <div class="relative z-10 flex items-center justify-between h-full px-8">
        <div class="flex-1">
          <h1 class="text-5xl font-black text-white mb-4 drop-shadow-lg">WELCOME BACK!</h1>
          <p class="text-xl text-amber-300 font-semibold drop-shadow-md">Step into the game with top-tier Valorant accounts — variety, quality, and trust in one place</p>
        </div>

        <div class="flex gap-6">
          <!-- Balance Card -->
          <div
            class="bg-gradient-to-br from-amber-400/10 to-orange-600/10 backdrop-blur-md border border-amber-500/30 rounded-2xl p-6 min-w-max">
            <p class="text-amber-200 text-sm font-semibold mb-2">CURRENT BALANCE</p>
            <p class="text-3xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">{{
              currentUser.balance}} VND</p>
          </div>

          <!-- Level Card -->
          <div
            class="bg-gradient-to-br from-purple-400/10 to-pink-600/10 backdrop-blur-md border border-purple-500/30 rounded-2xl p-6 min-w-max">
            <div class="flex items-center gap-2.5">
              <img
                class="w-10 h-10 rounded-full font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]"
                :src="currentUser.img" alt="userAvt">
              <div class="font-medium text-heading">
                <div class="text-purple-200 text-sm font-semibold mb-2">Have a good day!</div>
                <div class="text-2xl font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]">{{
                  currentUser.name }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Decorative Lines -->
      <div class="absolute top-20 left-8 w-1 h-20 bg-gradient-to-b from-amber-400 to-transparent"></div>
      <div class="absolute bottom-20 right-8 w-1 h-20 bg-gradient-to-t from-purple-400 to-transparent"></div>
    </section>

    <!-- Main Content -->
    <section class="max-w-7xl mx-auto px-8 py-16 relative z-10">
      <!-- Announcements Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-amber-400">📢</span> NEWS
          <div class="flex-1 h-1 bg-gradient-to-r from-amber-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div v-for="item in announcements" :key="item.id"
            class="group bg-gradient-to-br from-slate-800/50 to-slate-900/50 backdrop-blur-md border border-amber-500/20 rounded-xl p-5 hover:border-amber-400/50 transition-all duration-300 cursor-pointer">
            <div class="flex gap-4">
              <div
                class="w-16 h-16 bg-gradient-to-br from-amber-400/20 to-orange-600/20 rounded-lg flex items-center justify-center text-2xl flex-shrink-0">
                <p><img class="" :src="item.img" alt="Image section"></p>
              </div>
              <div class="flex-1">
                <h3 class="text-amber-300 font-bold mb-1 group-hover:text-amber-200 transition-colors">{{ item.title }}
                </h3>
                <p class="text-slate-400 text-sm line-clamp-2">{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> ACC VALORANT
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
                <p
                  :class="`text-lg font-bold ${item.color.split(' ')[1].replace('to-', 'text-').replace('-500', '-400').replace('-600', '-400')}`">
                  {{ item.subtitle }}
                </p>
              </router-link>
              <p class="text-amber-400 text-xs mt-4 font-semibold">🔥 ĐỐI FULL THÔNG TIN 🔥</p>
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
          <span class="text-cyan-400">🖥️</span> MỎ HÌNH VALORANT - SUPER PC GAMING
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
