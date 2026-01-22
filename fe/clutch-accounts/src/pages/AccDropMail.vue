<script setup>
import { ref } from 'vue'
import Navbar from '@/components/Navbar.vue'
import { useUserStore } from '@/stores/user';

const user = useUserStore();

const accs = ref([
  {
    id: 1197,
    name: "ACC #1197",
    rank: "SILVER",
    image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
    description: "Premium Valorant account with exclusive skins",
    price: 3600000,
    stats: {
      vp: 75,
      bpass: 0,
      ssung: 21,
      sdao: 5
    }
  },
  {
    id: 1198,
    name: "ACC #1198",
    rank: "GOLD",
    image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
    description: "Premium Valorant account with rare items",
    price: 4200000,
    stats: {
      vp: 120,
      bpass: 2,
      ssung: 35,
      sdao: 8
    }
  },
  {
    id: 1199,
    name: "ACC #1199",
    rank: "PLATINUM",
    image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
    description: "High-tier Valorant account",
    price: 5500000,
    stats: {
      vp: 200,
      bpass: 3,
      ssung: 42,
      sdao: 12
    }
  },
  {
    id: 1200,
    name: "ACC #1200",
    rank: "DIAMOND",
    image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
    description: "Elite Valorant account with premium skins",
    price: 7800000,
    stats: {
      vp: 350,
      bpass: 5,
      ssung: 58,
      sdao: 15
    }
  }
])

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN').format(price)
}

const getRankColor = (rank) => {
  const colors = {
    'IRON': 'from-gray-400 to-gray-600',
    'BRONZE': 'from-amber-600 to-amber-800',
    'SILVER': 'from-slate-300 to-slate-500',
    'GOLD': 'from-yellow-400 to-yellow-600',
    'PLATINUM': 'from-cyan-300 to-cyan-500',
    'DIAMOND': 'from-purple-400 to-pink-500',
    'ASCENDANT': 'from-emerald-400 to-emerald-600',
    'IMMORTAL': 'from-red-400 to-red-600',
    'RADIANT': 'from-yellow-200 to-yellow-400'
  }
  return colors[rank] || 'from-gray-400 to-gray-600'
}
</script>
<template>
      <Navbar></Navbar>
  <main class="min-h-screen main-background relative">
    <!-- Background Overlay for gradient and theme -->
    <div class="absolute inset-0 bg-gradient-to-b from-slate-950 via-slate-900 to-slate-950 opacity-50"></div>
    <!-- Hero Banner Section -->
    <section class="relative z-10 w-full h-96 overflow-hidden">

      <!-- Background Glow -->
      <div class="absolute inset-0 opacity-20">
        <div class="absolute bottom-0 right-1/4 w-96 h-96 bg-amber-500 rounded-full blur-3xl"></div>
      </div>

      <!-- Content -->
      <div class="relative z-10 flex items-center justify-between h-full px-8">
        <div class="flex gap-6">
          <!-- Balance Card -->
          <div
            class="bg-gradient-to-br from-amber-400/10 to-orange-600/10 backdrop-blur-md border border-amber-500/30 rounded-2xl p-6 min-w-max">
            <p class="text-amber-200 text-sm font-semibold mb-2">YOUR BALANCE</p>
            <p class="text-3xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">{{user.balance}} VND</p>
          </div>
          <!-- Level Card -->
          <div
            class="bg-gradient-to-br from-purple-400/10 to-pink-600/10 backdrop-blur-md border border-purple-500/30 rounded-2xl p-6 min-w-max">
            <div class="flex items-center gap-2.5">
              <img
                class="w-10 h-10 rounded-full font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]"
                src="" alt="userAvt">
              <div class="font-medium text-heading">
                <div class="text-purple-200 text-sm font-semibold mb-2">Have a good day!</div>
                <div class="text-2xl font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]">{{
                  user.username }}</div>
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
      <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> ACC VALORANT DROP MAIL
          <div class="flex-1 h-1 bg-gradient-to-r from-purple-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
          <div v-for="item in accs" :key="item.id"
            class="acc-card group relative bg-gradient-to-b from-black via-neutral-950 to-black backdrop-blur-xl border border-amber-500/30 rounded-3xl overflow-hidden transition-all duration-500 cursor-pointer hover:border-amber-400/70 hover:shadow-[0_0_60px_rgba(245,158,11,0.25)] hover:-translate-y-2">
            
            <!-- Glowing Border Effect -->
            <div class="absolute inset-0 rounded-3xl bg-gradient-to-r from-amber-500/20 via-yellow-500/20 to-amber-500/20 opacity-0 group-hover:opacity-100 transition-opacity duration-500 blur-xl"></div>
            
            <!-- Card Content -->
            <div class="relative z-10">
              <!-- Image Section -->
              <div class="relative overflow-hidden">
                <img :src="item.image" alt="Product Image" 
                  class="w-full h-48 object-cover transition-transform duration-700 group-hover:scale-110">
                <!-- Image Overlay -->
                <div class="absolute inset-0 bg-gradient-to-t from-black via-transparent to-transparent"></div>
                <!-- Gold Shimmer Effect -->
                <div class="absolute inset-0 shimmer-overlay opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
              </div>

              <!-- Title Section -->
              <div class="relative px-6 pt-4 pb-3 text-center border-b border-amber-500/20">
                <h3 class="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-amber-300 via-yellow-200 to-amber-300 tracking-wider drop-shadow-[0_0_10px_rgba(245,158,11,0.5)]">
                  {{ item.name }}
                </h3>
                <span :class="['inline-block mt-2 px-4 py-1 rounded-full text-sm font-bold text-white bg-gradient-to-r', getRankColor(item.rank)]">
                  {{ item.rank }}
                </span>
              </div>

              <!-- Stats Section -->
              <div class="grid grid-cols-4 gap-2 px-4 py-4 border-b border-amber-500/20 bg-gradient-to-r from-amber-950/30 via-amber-900/20 to-amber-950/30">
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">VP</p>
                  <p class="text-lg font-black text-amber-100">{{ item.stats.vp }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">B.PASS</p>
                  <p class="text-lg font-black text-amber-100">{{ item.stats.bpass }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">S.SÚNG</p>
                  <p class="text-lg font-black text-amber-100">{{ item.stats.ssung }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">S.DAO</p>
                  <p class="text-lg font-black text-amber-100">{{ item.stats.sdao }}</p>
                </div>
              </div>

              <!-- Price Section -->
              <div class="px-6 py-5 text-center">
                <p class="text-xs text-amber-400/60 font-semibold mb-1 uppercase tracking-wider">Price</p>
                <p class="text-3xl font-black text-transparent bg-clip-text bg-gradient-to-r from-amber-400 via-yellow-300 to-amber-400 drop-shadow-[0_0_20px_rgba(245,158,11,0.6)]">
                  {{ formatPrice(item.price) }} <span class="text-lg">VND</span>
                </p>
              </div>

              <!-- Buttons Section -->
              <div class="px-4 pb-5 flex gap-3">
                <!-- Details Button -->
                <button class="details-btn flex-1 flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-bold text-sm text-amber-200 bg-transparent border-2 border-amber-500/40 transition-all duration-300 hover:border-amber-400 hover:text-amber-300 hover:shadow-[0_0_20px_rgba(245,158,11,0.3)] active:scale-95">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <circle cx="12" cy="12" r="10" stroke-width="2"/>
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 16v-4m0-4h.01"/>
                  </svg>
                  DETAILS
                </button>
                
                <!-- Buy Now Button -->
                <button class="buy-btn flex-1 flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-bold text-sm text-black bg-gradient-to-r from-amber-500 via-yellow-400 to-amber-500 bg-size-200 bg-pos-0 transition-all duration-500 hover:bg-pos-100 hover:shadow-[0_0_30px_rgba(245,158,11,0.5)] active:scale-95">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"/>
                  </svg>
                  BUY NOW
                </button>
              </div>
            </div>

            <!-- Corner Decorations -->
            <div class="absolute top-0 left-0 w-16 h-16 border-t-2 border-l-2 border-amber-500/50 rounded-tl-3xl"></div>
            <div class="absolute bottom-0 right-0 w-16 h-16 border-b-2 border-r-2 border-amber-500/50 rounded-br-3xl"></div>
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

/* Card Styles - Black & Gold Luxury Theme */
.acc-card {
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.6),
    0 0 40px rgba(245, 158, 11, 0.1),
    inset 0 1px 0 rgba(245, 158, 11, 0.2);
}

.acc-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(245, 158, 11, 0.6), transparent);
}

/* Gold Shimmer overlay animation */
@keyframes shimmer-slide {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

.shimmer-overlay {
  background: linear-gradient(
    90deg,
    transparent 0%,
    rgba(245, 158, 11, 0.15) 50%,
    transparent 100%
  );
  animation: shimmer-slide 1.5s ease-in-out infinite;
}

/* Button gradient animation */
.bg-size-200 {
  background-size: 200% 100%;
}

.bg-pos-0 {
  background-position: 0% 0%;
}

.bg-pos-100 {
  background-position: 100% 0%;
}

/* Glow effects */
.glow-amber {
  box-shadow: 0 0 30px rgba(245, 158, 11, 0.3), inset 0 0 30px rgba(245, 158, 11, 0.1);
}

/* Gold Button pulse animation on hover */
@keyframes pulse-glow-gold {
  0%, 100% {
    box-shadow: 0 0 20px rgba(245, 158, 11, 0.4);
  }
  50% {
    box-shadow: 0 0 40px rgba(245, 158, 11, 0.7);
  }
}

.buy-btn:hover {
  animation: pulse-glow-gold 1.5s ease-in-out infinite;
}

/* Details button border animation - Gold */
@keyframes border-glow-gold {
  0%, 100% {
    border-color: rgba(245, 158, 11, 0.5);
  }
  50% {
    border-color: rgba(245, 158, 11, 1);
  }
}

.details-btn:hover {
  animation: border-glow-gold 1.5s ease-in-out infinite;
}

/* Card entrance animation */
@keyframes card-entrance {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.acc-card {
  animation: card-entrance 0.6s ease-out forwards;
}

.acc-card:nth-child(1) { animation-delay: 0.1s; }
.acc-card:nth-child(2) { animation-delay: 0.2s; }
.acc-card:nth-child(3) { animation-delay: 0.3s; }
.acc-card:nth-child(4) { animation-delay: 0.4s; }
.acc-card:nth-child(5) { animation-delay: 0.5s; }
.acc-card:nth-child(6) { animation-delay: 0.6s; }
.acc-card:nth-child(7) { animation-delay: 0.7s; }
.acc-card:nth-child(8) { animation-delay: 0.8s; }

/* Floating animation for price */
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

.acc-card:hover .text-3xl {
  animation: float 2s ease-in-out infinite;
}

/* Stats hover effect */
.acc-card .grid > div {
  transition: all 0.3s ease;
}

.acc-card:hover .grid > div {
  transform: scale(1.05);
}

/* Gold sparkle effect on card hover */
@keyframes sparkle {
  0%, 100% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
}

.acc-card::after {
  content: '';
  position: absolute;
  top: 10%;
  right: 10%;
  width: 4px;
  height: 4px;
  background: radial-gradient(circle, rgba(245, 158, 11, 1) 0%, transparent 70%);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.acc-card:hover::after {
  animation: sparkle 1s ease-in-out infinite;
}

/* Responsive adjustments */
@media (max-width: 640px) {
  .acc-card {
    max-width: 100%;
  }
}
</style>
