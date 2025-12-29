<script setup>
import Navbar from '@/components/Navbar.vue';
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const items = [
  { id: 1, name: 'ACC VALORANT', subtitle: '67.000 đ', color: 'from-yellow-500 to-orange-500', textColor: 'text-yellow-500', img: 'fe\clutch-accounts\src\img\backGround + Logo.png' },
  { id: 2, name: 'ACC VALORANT', subtitle: '67.000 đ', color: 'from-yellow-500 to-orange-500', textColor: 'text-yellow-500', img: './' },
  { id: 3, name: 'ACC VALORANT', subtitle: '67.000 đ', color: 'from-yellow-500 to-orange-500', textColor: 'text-yellow-500', img: './' },
  { id: 4, name: 'ACC VALORANT', subtitle: '67.000 đ', color: 'from-yellow-500 to-orange-500', textColor: 'text-yellow-500', img: './' }
]
//toast
const router = useRouter()

const showToast = ref(false)
const selectedItem = ref(null)

const openToast = (item) => {
  selectedItem.value = item
  showToast.value = true
}

const showSuccess = ref(false)

const confirmBuy = () => {
  showToast.value = false
  showSuccess.value = true

  // giả lập xử lý (1.5s)
  setTimeout(() => {
    showSuccess.value = false
    router.push(selectedItem.value.link)
  }, 1500)
}

const showReject = ref(false)

const closeToast = () => {
  showToast.value = false

  setTimeout(() => {
    showReject.value = true

    setTimeout(() => {
      showReject.value = false
    }, 1200)

  }, 50)
}

</script>
<template>
<Navbar></Navbar>
<main class="min-h-screen main-background relative">
    <!-- Main Content -->
    <section class="max-w-7xl mx-auto px-8 py-16 relative z-10">
        <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> ACC VALORANT RANDOM NFA
          <div class="flex-1 h-1 bg-gradient-to-r from-purple-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div v-for="item in items" :key="item.id"
            class="group relative bg-gradient-to-br from-black/80 to-slate-950/80 backdrop-blur-md border border-amber-900/50 rounded-2xl overflow-hidden hover:border-amber-500/70 transition-all duration-300 cursor-pointer hover:shadow-2xl hover:shadow-amber-600/40">

            <!-- Background Gradient -->
            <div
              :class="`absolute inset-0 opacity-0 group-hover:opacity-20 bg-gradient-to-br ${item.color} transition-opacity duration-300`">
            </div>

            <!-- Content -->
            <div class="relative z-10 p-8 flex flex-col items-center justify-center h-64 text-center">
              <div>
                <img
                    :src="item.img"
                    alt=""
                    class="w-24 h-16 object-contain mb-4"
                />
              </div>
              <h3 class="text-white font-black text-xl mb-2">{{ item.name }}</h3>
                <p :class="`text-lg font-bold ${item.textColor}`">
                  {{ item.subtitle }}
                </p>
            <button
                @click.stop="openToast(item)"
                class="px-6 py-2 rounded-xl font-bold
                    bg-gradient-to-r from-amber-400 to-yellow-500
                    text-black hover:scale-105 transition-all
                    shadow-lg shadow-amber-500/40">
                MUA NGAY
            </button>
            </div>
            <!-- Border Glow -->
            <div
              class="absolute inset-0 rounded-2xl pointer-events-none border border-gradient-to-r from-amber-400/0 via-amber-400/0 to-amber-400/0 group-hover:from-amber-600/40 group-hover:via-amber-500/60 group-hover:to-amber-600/40 transition-all duration-300">
            </div>

          </div>
        </div>
      </div>
    </section>
    <!-- Toast -->
    <!-- OVERLAY -->
    <div
        v-if="showToast"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">

    <!-- MODAL -->
    <div
        class="w-[380px] bg-slate-900 border border-amber-500/50
            rounded-2xl p-6 shadow-2xl
            animate-scale-in">

    <h3 class="text-white font-black text-xl mb-3 text-center">
      ⚠️ XÁC NHẬN MUA
    </h3>

    <p class="text-slate-300 text-center mb-6">
      Bạn có chắc muốn mua
      <span class="text-amber-400 font-bold">
        {{ selectedItem?.name }}
      </span>
      ?
    </p>

    <div class="flex gap-4">
      <button
        @click="closeToast"
        class="flex-1 py-3 rounded-xl font-bold
               bg-slate-700 text-white
               hover:scale-105 transition">
        Hủy
      </button>

      <button
        @click="confirmBuy"
        class="flex-1 py-3 rounded-xl font-bold
               bg-gradient-to-r from-amber-400 to-yellow-500
               text-black hover:scale-105 transition">
        Xác nhận
      </button>
    </div>
  </div>
</div>
    <div
        v-if="showSuccess"
        class="fixed inset-0 z-50 flex items-center justify-center
            bg-black/70 backdrop-blur-md">

    <div
        class="w-[360px] bg-slate-900 rounded-2xl p-6
            border border-emerald-500/50
            text-center shadow-2xl
            animate-scale-in">

    <!-- ICON -->
    <div class="text-5xl mb-4">✅</div>

    <h3 class="text-white font-black text-xl mb-2">
      XÁC NHẬN THÀNH CÔNG
    </h3>

    <p class="text-slate-300 mb-4">
      M đã bị scam ahahahaha
    </p>

    <!-- LOADING -->
    <div class="w-full h-2 rounded-full bg-slate-700 overflow-hidden">
      <div class="h-full bg-gradient-to-r from-emerald-400 to-green-500 animate-loading"></div>
    </div>

  </div>
</div>
<!-- REJECT SCREEN -->
<div
  v-if="showReject"
  class="fixed inset-0 z-50 flex items-center justify-center
         bg-black/70 backdrop-blur-md">

  <div
    class="w-[340px] bg-slate-900 rounded-2xl p-6
           border border-red-500/50
           text-center shadow-2xl
           animate-scale-in">

    <!-- ICON -->
    <div class="text-5xl mb-4">❌</div>

    <h3 class="text-white font-black text-xl mb-2">
      ĐÃ HỦY GIAO DỊCH
    </h3>

    <p class="text-slate-300">
      Bạn đã từ chối mua acc này
    </p>

  </div>
</div>
</main>
</template>
<style scoped>
.main-background {
  background-image: url('../img/main-background.jpg');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}
/* animation */
@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-scale-in {
  animation: scaleIn 0.25s ease-out;
}

@keyframes loading {
  from {
    width: 0%;
  }
  to {
    width: 100%;
  }
}

.animate-loading {
  animation: loading 1.5s linear forwards;
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