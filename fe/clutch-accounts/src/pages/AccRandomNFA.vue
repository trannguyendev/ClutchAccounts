<script setup>
import Navbar from '@/components/Navbar.vue';
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios';
import { onMounted } from 'vue';
import { useUserStore } from '@/stores/user';
import toast from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const user = useUserStore()
const products = ref([])
const router = useRouter()
let username = ref('')
let password = ref('')
let emailAcc = ref('')
// Pagination
const currentPage = ref(1)
const pageSize = 8
const totalPages = computed(() => Math.ceil(products.value.length / pageSize) || 0)
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return products.value.slice(start, start + pageSize)
})

const showToast = ref(false)
const selectedItem = ref(null)

const loadAccInfo = async () => {
  await axios.get("/api/accounts/random/acc-nfa/list-acc")
  .then((res) => {
    console.log(res.data)
    products.value = res.data
    currentPage.value = 1
  })
  .catch((err) => {
    console.log(err)
  })
}

const loadPurchasedAcc = async () => {
  await axios.post("/api/accounts/random/acc-nfa", {
    email: user.username
  })
  .then((res) => {
    console.log(res.data)
    username.value = res.data.username
    password.value = res.data.account_psw
    emailAcc.value = res.data.email
  })
  .catch((err) => {
    console.log(err)
    toast.error("Error loading purchased account info")
  })
}
const openToast = (item) => {
  selectedItem.value = item
  showToast.value = true
}

// flip state for click-to-flip behavior
const flippedId = ref(null)
const toggleFlip = (item) => {
  flippedId.value = flippedId.value === item.account_id ? null : item.account_id
}

const setPage = (page) => {
  if (totalPages.value === 0) return
  if (page < 1) page = 1
  if (page > totalPages.value) page = totalPages.value
  currentPage.value = page
  // reset flip state when changing pages
  flippedId.value = null
}

const showSuccess = ref(false)
const showCredentials = ref(false)
const copiedField = ref('')

const copyToClipboard = async (text, field) => {
  try {
    await navigator.clipboard.writeText(text || '')
    copiedField.value = field
    setTimeout(() => (copiedField.value = ''), 1500)
  } catch (err) {
    console.error('Copy failed', err)
  }
}

const closeCredentials = () => {
  showCredentials.value = false
  selectedItem.value = null
}

const confirmBuy = () => {
  showToast.value = false
  showSuccess.value = true

  // giả lập xử lý (1.5s)
  setTimeout(() => {
    showSuccess.value = false
    // show credentials modal after purchase is processed
    showCredentials.value = true
    // ensure card flips back
    flippedId.value = null
  }, 1500)

  loadPurchasedAcc();
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
onMounted(() => {
  loadAccInfo()
})

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
          <div v-for="product in paginatedProducts" :key="product.account_id"
            @click="toggleFlip(product)"
            class="group relative bg-gradient-to-br from-black/80 to-slate-950/80 backdrop-blur-md border border-amber-900/50 ring-2 ring-amber-500/25 rounded-2xl overflow-hidden hover:border-amber-500/70 group-hover:ring-4 group-hover:ring-amber-500/40 transition-all duration-300 cursor-pointer hover:shadow-2xl hover:shadow-amber-600/40">

            <!-- Background Gradient -->
            <div
              class="absolute inset-0 opacity-0 group-hover:opacity-20 bg-gradient-to-br from-yellow-500 to-orange-500 transition-opacity duration-300">
            </div>

            <!-- Flip Card (front: image, back: description + price + buy) -->
            <div class="relative z-10 h-96 w-full flip-card">
              <div :class="['flip-card-inner', { 'is-flipped': flippedId === product.account_id }]">
                <!-- Front -->
                <div class="flip-card-front">
                  <img
                    src="../img/GunCard.png"
                    alt=""
                    class="w-full h-full object-cover"
                  />
                  <div class="absolute inset-0 bg-black/10 transition-all"></div>
                </div>

                <!-- Back -->
                <div class="flip-card-back">
                  <div class="p-4 text-center">
                    <p class="text-lg text-slate-200 mb-4 font-bold">
                      {{ product.account_type }}
                    </p>
                    <p class="text-lg text-amber-400 mb-4 font-bold">
                      {{ product.price }} VND
                    </p>
                    <button
                      @click.stop="openToast(product)"
                      class="px-4 py-2 rounded-lg bg-amber-400 text-black font-bold shadow-md hover:scale-105 transition">
                      MUA NGAY
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <!-- Border Glow -->
            <div
              class="absolute inset-0 rounded-2xl pointer-events-none border-2 border-transparent group-hover:border-amber-500/40 transition-all duration-300">
            </div>

          </div>
        </div>

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="flex items-center justify-center gap-3 mt-6">
          <button @click="setPage(1)" :disabled="currentPage === 1" class="px-2 py-1 rounded bg-slate-700 text-white disabled:opacity-40">«</button>
          <button @click="setPage(currentPage - 1)" :disabled="currentPage === 1" class="px-3 py-1 rounded bg-slate-700 text-white disabled:opacity-40">‹</button>
          <div class="px-3 py-1 rounded bg-slate-800 text-white">Trang {{ currentPage }} / {{ totalPages }}</div>
          <button @click="setPage(currentPage + 1)" :disabled="currentPage === totalPages" class="px-3 py-1 rounded bg-slate-700 text-white disabled:opacity-40">›</button>
          <button @click="setPage(totalPages)" :disabled="currentPage === totalPages" class="px-2 py-1 rounded bg-slate-700 text-white disabled:opacity-40">»</button>
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
      ⚠️ CONFIRM TRANSACTION
    </h3>

    <p class="text-slate-300 text-center mb-6">
      Did you want to purchase this account type ?
      <span class="text-amber-400 font-bold">
        {{ selectedItem.account_type }}
      </span>
      ?
    </p>

    <div class="flex gap-4">
      <button
        @click="closeToast"
        class="flex-1 py-3 rounded-xl font-bold
               bg-slate-700 text-white
               hover:scale-105 transition">
        Cancel
      </button>

      <button
        @click="confirmBuy"
        class="flex-1 py-3 rounded-xl font-bold
               bg-gradient-to-r from-amber-400 to-yellow-500
               text-black hover:scale-105 transition">
        Confirm
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
      CONFIRM PURCHASE SUCCESSFUL
    </h3>

    <p class="text-slate-300 mb-4">
      Loading...
    </p>

    <!-- LOADING -->
    <div class="w-full h-2 rounded-full bg-slate-700 overflow-hidden">
      <div class="h-full bg-gradient-to-r from-emerald-400 to-green-500 animate-loading"></div>
    </div>

  </div>
</div>

<!-- CREDENTIALS MODAL -->
<div
  v-if="showCredentials"
  class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm">

  <div
    class="w-[420px] bg-slate-900 border border-amber-500/50
        rounded-2xl p-6 shadow-2xl animate-scale-in">

    <h3 class="text-white font-black text-xl mb-3 text-center">🔐 Thông tin tài khoản</h3>

    <div class="space-y-4">
      <div>
        <div class="text-xs text-slate-400">Username</div>
        <div class="flex items-center justify-between mt-1">
          <div class="font-mono text-amber-300 text-lg">{{ username.value }}</div>
          <button @click="copyToClipboard(username.value, 'username')" class="ml-4 px-3 py-1 rounded bg-slate-700 text-white">Copy</button>
        </div>
        <div v-if="copiedField === 'username'" class="text-xs text-emerald-400 mt-1">Đã copy username</div>
      </div>

      <div>
        <div class="text-xs text-slate-400">Password</div>
        <div class="flex items-center justify-between mt-1">
          <div class="font-mono text-amber-300 text-lg">{{ password.value }}</div>
          <button @click="copyToClipboard(password.value, 'account_psw')" class="ml-4 px-3 py-1 rounded bg-slate-700 text-white">Copy</button>
        </div>
        <div v-if="copiedField === 'password'" class="text-xs text-emerald-400 mt-1">Đã copy password</div>
      </div>
    </div>

    <div class="flex gap-4 mt-6">
      <button @click="closeCredentials" class="flex-1 py-3 rounded-xl font-bold bg-slate-700 text-white hover:scale-105 transition">Đóng</button>
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

/* Flip card (3D) */
.flip-card {
  perspective: 1000px;
}

.flip-card-inner {
  position: relative;
  width: 100%;
  height: 100%;
  transition: transform 0.7s ease;
  transform-style: preserve-3d;
}

/* rotate when the .is-flipped class is applied */
.flip-card-inner.is-flipped {
  transform: rotateY(180deg);
}

.flip-card-front,
.flip-card-back {
  position: absolute;
  inset: 0;
  -webkit-backface-visibility: hidden;
  backface-visibility: hidden;
  border-radius: inherit;
  overflow: hidden;
}

.flip-card-back {
  transform: rotateY(180deg);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, rgba(15,23,42,0.85), rgba(15,23,42,0.95));
}

/* Make the content on back readable */
.flip-card-back p {
  line-height: 1.4;
}
</style>