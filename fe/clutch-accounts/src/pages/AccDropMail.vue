<script setup>
import { ref, computed } from 'vue'
import Navbar from '@/components/Navbar.vue'
import { useUserStore } from '@/stores/user';
import { useI18n } from 'vue-i18n'
import axios from 'axios';
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css';
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();
const { t } = useI18n();
const user = useUserStore();

// Filter state
const searchNickname = ref('')
const selectedRank = ref('')
const selectedPrice = ref('')
const selectedSort = ref('')

// Pagination state
const currentPage = ref(1)
const itemsPerPage = ref(8)

// Popup state
const showBuyPopup = ref(false)
const selectedAcc = ref(null)
const voucherCode = ref('')
const totalDiscount = ref(0)

const rankOptions = [
  { value: '', label: t('filter.allRanks') },
  { value: 'IRON', label: 'IRON' },
  { value: 'BRONZE', label: 'BRONZE' },
  { value: 'SILVER', label: 'SILVER' },
  { value: 'GOLD', label: 'GOLD' },
  { value: 'PLATINUM', label: 'PLATINUM' },
  { value: 'DIAMOND', label: 'DIAMOND' },
  { value: 'ASCENDANT', label: 'ASCENDANT' },
  { value: 'IMMORTAL', label: 'IMMORTAL' },
  { value: 'RADIANT', label: 'RADIANT' }
]

const priceOptions = [
  { value: '', label: t('filter.allPrices') },
  { value: '0-2000000', label: '0 - 2M' },
  { value: '2000000-4000000', label: '2M - 4M' },
  { value: '4000000-6000000', label: '4M - 6M' },
  { value: '6000000-10000000', label: '6M - 10M' },
  { value: '10000000+', label: '10M+' }
]

const sortOptions = [
  { value: '', label: t('filter.sortDefault') },
  { value: 'price-asc', label: t('filter.priceLowest') },
  { value: 'price-desc', label: t('filter.priceHighest') },
  { value: 'newest', label: t('filter.newest') }
]

const fetchAccounts = () => {
  axios.get('/api/accounts/list-for-sell/drop-mail')
  .then((res) => {
    accs.value = res.data
  })
  .catch((err) => {
    console.error('Fetch accounts error:', err.message)
    toast.error('Fetch error: '+err, {
      position: "top-center"
    })
  })
}
const resetFilters = () => {
  searchNickname.value = ''
  selectedRank.value = ''
  selectedPrice.value = ''
  selectedSort.value = ''
  currentPage.value = 1
}

// Helper function to check price range
const isPriceInRange = (price, rangeStr) => {
  if (!rangeStr) return true
  
  if (rangeStr === '10000000+') {
    return price >= 10000000
  }
  
  const [min, max] = rangeStr.split('-').map(Number)
  return price >= min && price <= max
}

// Filtered and sorted accounts
const filteredAccounts = computed(() => {
  let result = accs.value

  // Filter by nickname
  if (searchNickname.value) {
    const search = searchNickname.value.toLowerCase()
    result = result.filter(acc => acc.account_id.toLowerCase().includes(search))
  }

  // Filter by rank
  if (selectedRank.value) {
    result = result.filter(acc => acc.rank_info === selectedRank.value)
  }

  // Filter by price range
  if (selectedPrice.value) {
    result = result.filter(acc => isPriceInRange(acc.price, selectedPrice.value))
  }

  // Sort
  if (selectedSort.value === 'price-asc') {
    result.sort((a, b) => a.price - b.price)
  } else if (selectedSort.value === 'price-desc') {
    result.sort((a, b) => b.price - a.price)
  } else if (selectedSort.value === 'newest') {
    result.sort((a, b) => b.id - a.id)
  }

  return result
})

// Paginated accounts
const paginatedAccounts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value
  const end = start + itemsPerPage.value
  return filteredAccounts.value.slice(start, end)
})

// Total pages
const totalPages = computed(() => {
  return Math.ceil(filteredAccounts.value.length / itemsPerPage.value)
})

// Pagination info
const paginationInfo = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage.value + 1
  const end = Math.min(currentPage.value * itemsPerPage.value, filteredAccounts.value.length)
  const total = filteredAccounts.value.length
  return { start, end, total }
})

// Change page
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

// Reset pagination when filters change
const applyFilters = () => {
  currentPage.value = 1
}

const openBuyPopup = (item) => {
  selectedAcc.value = item
  showBuyPopup.value = true
}

const closeBuyPopup = () => {
  showBuyPopup.value = false
  selectedAcc.value = null
  voucherCode.value = ''
}

const applyVoucher = () => {
  // TODO: Implement voucher logic
  axios.post('/api/vouchers/caculateDiscount', {
    price: selectedAcc.value.price,
    voucher: voucherCode.value
  })
  .then((res) => {
    console.log('Voucher applied: ', res.data)
    totalDiscount.value = res.data
    toast.success(t('accs.voucherApplied'), {
      position: "top-center"
    })
  })
  .catch((err) => {
    console.error('Voucher error:', err.message)
    voucherCode.value = ''
    totalDiscount.value = 0
    toast.error(err.response?.data?.message || 'Invalid or your voucher has reached the limit', {
      position: "top-center"
    })
  })
}

const handleDeposit = () => {
  // TODO: Navigate to deposit page
  const payload = {
    email: user.username,
    type: 'DROP_MAIL',
    accId: selectedAcc.value.account_id,
    voucher: voucherCode.value
  }
  console.log('Buy account with payload: ', payload)
  axios.post('/api/accounts/buy', payload)
  .then((res) => {
    console.log('Purchase successful: ', res.data)
    toast.success(t('accs.purchaseSuccess'), {
      position: "top-center"
    })
    closeBuyPopup()
    fetchAccounts() // Refresh accounts after purchase
  })
  .catch((err) => {
    console.error('Purchase error:', err.message)
    toast.error(err.response?.data?.message || 'Purchase failed', {
      position: "top-center"
    })
  })
}

const handleWarrantyPolicy = () => {
  // TODO: Show warranty policy
  console.log('Show warranty policy')
  router.push('/warranty-policy')
}

const accs = ref([
  {
    id: 1197,
    name: "ACC #1197",
    rank: "SILVER",
    image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
    description: "Premium Valorant account with exclusive skins",
    price: 3600000,
    vp: 75,
    bpass: 0,
    ssung: 21,
    sdao: 5
  }
])

const formatPrice = (price) => {
  return new Intl.NumberFormat('vi-VN').format(price)
}

const getRankColor = (rank) => {
  const colors = {
    'Iron': 'from-gray-400 to-gray-600',
    'Bronze': 'from-amber-600 to-amber-800',
    'Silver': 'from-slate-300 to-slate-500',
    'Gold': 'from-yellow-400 to-yellow-600',
    'Platinum': 'from-cyan-300 to-cyan-500',
    'Diamond': 'from-purple-400 to-pink-500',
    'Ascendant': 'from-emerald-400 to-emerald-600',
    'Immortal': 'from-red-400 to-red-600',
    'Radiant': 'from-yellow-200 to-yellow-400'
  }
  return colors[rank] || 'from-gray-400 to-gray-600'
}

onMounted(() => {
  fetchAccounts()
})
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
            <p class="text-amber-200 text-sm font-semibold mb-2">{{ t('main.currentBalance') }}</p>
            <p class="text-3xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">{{formatPrice(user.balance)}} VND</p>
          </div>
          <!-- Level Card -->
          <div
            class="bg-gradient-to-br from-purple-400/10 to-pink-600/10 backdrop-blur-md border border-purple-500/30 rounded-2xl p-6 min-w-max">
            <div class="flex items-center gap-2.5">
              <img
                class="w-10 h-10 rounded-full font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]"
                src="" alt="userAvt">
              <div class="font-medium text-heading">
                <div class="text-purple-200 text-sm font-semibold mb-2">{{ t('main.quote') }}</div>
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
      <!-- Filter Section -->
      <div class="mb-16">
        <div class="bg-gradient-to-r from-slate-900/80 via-slate-950/90 to-slate-900/80 backdrop-blur-xl border border-purple-500/30 rounded-3xl p-8 shadow-[0_0_40px_rgba(168,85,247,0.2)]">
          <!-- Filter Title -->
          <h2 class="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-purple-300 to-purple-500 mb-6 flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-purple-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
            </svg>
            {{ t('filter.searchByRequirements') }}
          </h2>

          <!-- Filter Inputs -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
            <!-- Nickname Search -->
            <div>
              <input
                v-model="searchNickname"
                type="text"
                :placeholder="t('filter.searchNickname')"
                class="w-full px-4 py-3 rounded-2xl bg-slate-800/50 backdrop-blur-sm border border-purple-500/30 text-white placeholder-purple-300/50 focus:outline-none focus:border-purple-400 focus:ring-2 focus:ring-purple-500/50 transition-all duration-300"
              />
            </div>

            <!-- Rank Dropdown -->
            <div>
              <select
                v-model="selectedRank"
                class="w-full px-4 py-3 rounded-2xl bg-slate-800/50 backdrop-blur-sm border border-purple-500/30 text-white focus:outline-none focus:border-purple-400 focus:ring-2 focus:ring-purple-500/50 transition-all duration-300 appearance-none cursor-pointer"
              >
                <option v-for="rank in rankOptions" :key="rank.value" :value="rank.value">
                  {{ rank.label }}
                </option>
              </select>
            </div>

            <!-- Price Dropdown -->
            <div>
              <select
                v-model="selectedPrice"
                class="w-full px-4 py-3 rounded-2xl bg-slate-800/50 backdrop-blur-sm border border-purple-500/30 text-white focus:outline-none focus:border-purple-400 focus:ring-2 focus:ring-purple-500/50 transition-all duration-300 appearance-none cursor-pointer"
              >
                <option v-for="price in priceOptions" :key="price.value" :value="price.value">
                  {{ price.label }}
                </option>
              </select>
            </div>

            <!-- Sort Dropdown -->
            <div>
              <select
                v-model="selectedSort"
                class="w-full px-4 py-3 rounded-2xl bg-slate-800/50 backdrop-blur-sm border border-purple-500/30 text-white focus:outline-none focus:border-purple-400 focus:ring-2 focus:ring-purple-500/50 transition-all duration-300 appearance-none cursor-pointer"
              >
                <option v-for="sort in sortOptions" :key="sort.value" :value="sort.value">
                  {{ sort.label }}
                </option>
              </select>
            </div>
          </div>

          <!-- Filter Buttons -->
          <div class="flex gap-4 justify-end">
            <button
              @click="resetFilters"
              class="px-8 py-3 rounded-full bg-red-600/20 border-2 border-red-500/50 text-red-300 font-bold hover:bg-red-600/40 hover:border-red-500 transition-all duration-300 flex items-center gap-2"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
              </svg>
              {{ t('filter.clear') }}
            </button>
            <button
              @click="applyFilters"
              class="px-8 py-3 rounded-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold hover:shadow-[0_0_30px_rgba(168,85,247,0.5)] transition-all duration-300 flex items-center gap-2"
            >
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
              </svg>
              {{ t('filter.search') }}
            </button>
          </div>
        </div>
      </div>

      <!-- Quick Filter Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-16">
        <!-- Card 1: Budget Friendly -->
        <div class="group relative bg-gradient-to-br from-slate-900/80 via-slate-950/80 to-slate-900/80 backdrop-blur-xl border border-amber-500/30 rounded-3xl overflow-hidden cursor-pointer transition-all duration-500 hover:border-amber-400/70 hover:shadow-[0_0_40px_rgba(217,119,6,0.3)] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-yellow-500/10 via-transparent to-amber-500/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
          <div class="relative p-6 h-40 flex flex-col justify-center items-center text-center">
            <div class="text-4xl mb-2">💰</div>
            <h3 class="text-xl font-bold text-amber-300 mb-2">{{ t('filter.budgetFriendly') }}</h3>
            <p class="text-sm text-amber-200/70">{{ t('filter.under2m') }}</p>
          </div>
        </div>

        <!-- Card 2: Mid-Tier -->
        <div class="group relative bg-gradient-to-br from-slate-900/80 via-slate-950/80 to-slate-900/80 backdrop-blur-xl border border-cyan-500/30 rounded-3xl overflow-hidden cursor-pointer transition-all duration-500 hover:border-cyan-400/70 hover:shadow-[0_0_40px_rgba(34,211,238,0.3)] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-cyan-500/10 via-transparent to-blue-500/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
          <div class="relative p-6 h-40 flex flex-col justify-center items-center text-center">
            <div class="text-4xl mb-2">⭐</div>
            <h3 class="text-xl font-bold text-cyan-300 mb-2">{{ t('filter.midTier') }}</h3>
            <p class="text-sm text-cyan-200/70">2M - 6M VND</p>
          </div>
        </div>

        <!-- Card 3: Premium -->
        <div class="group relative bg-gradient-to-br from-slate-900/80 via-slate-950/80 to-slate-900/80 backdrop-blur-xl border border-purple-500/30 rounded-3xl overflow-hidden cursor-pointer transition-all duration-500 hover:border-purple-400/70 hover:shadow-[0_0_40px_rgba(168,85,247,0.3)] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-purple-500/10 via-transparent to-pink-500/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
          <div class="relative p-6 h-40 flex flex-col justify-center items-center text-center">
            <div class="text-4xl mb-2">👑</div>
            <h3 class="text-xl font-bold text-purple-300 mb-2">{{ t('filter.premium') }}</h3>
            <p class="text-sm text-purple-200/70">6M - 10M VND</p>
          </div>
        </div>

        <!-- Card 4: Elite -->
        <div class="group relative bg-gradient-to-br from-slate-900/80 via-slate-950/80 to-slate-900/80 backdrop-blur-xl border border-red-500/30 rounded-3xl overflow-hidden cursor-pointer transition-all duration-500 hover:border-red-400/70 hover:shadow-[0_0_40px_rgba(239,68,68,0.3)] hover:-translate-y-2">
          <div class="absolute inset-0 bg-gradient-to-br from-red-500/10 via-transparent to-rose-500/10 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
          <div class="relative p-6 h-40 flex flex-col justify-center items-center text-center">
            <div class="text-4xl mb-2">💎</div>
            <h3 class="text-xl font-bold text-red-300 mb-2">{{ t('filter.elite') }}</h3>
            <p class="text-sm text-red-200/70">{{ t('filter.above10m') }}</p>
          </div>
        </div>
      </div>

      <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> ACC VALORANT DROP MAIL
          <div class="flex-1 h-1 bg-gradient-to-r from-purple-400 to-transparent ml-4"></div>
        </h2>

        <!-- No Results Message -->
        <div v-if="filteredAccounts.length === 0" class="text-center py-16">
          <div class="text-6xl mb-4">🔍</div>
          <h3 class="text-2xl font-bold text-amber-400 mb-2">{{ t('filter.noResults') }}</h3>
          <p class="text-purple-300 mb-6">{{ t('filter.tryChangingFilters') }}</p>
          <button @click="resetFilters" class="px-6 py-3 rounded-full bg-gradient-to-r from-purple-600 to-pink-600 text-white font-bold hover:shadow-[0_0_30px_rgba(168,85,247,0.5)] transition-all">
            {{ t('filter.resetFilters') }}
          </button>
        </div>

        <!-- Accounts Grid -->
        <div v-if="filteredAccounts.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
          <div v-for="item in paginatedAccounts" :key="item.account_id"
            class="acc-card group relative bg-gradient-to-b from-black via-neutral-950 to-black backdrop-blur-xl border border-amber-500/30 rounded-3xl overflow-hidden transition-all duration-500 cursor-pointer hover:border-amber-400/70 hover:shadow-[0_0_60px_rgba(245,158,11,0.25)] hover:-translate-y-2">
            
            <!-- Glowing Border Effect -->
            <div class="absolute inset-0 rounded-3xl bg-gradient-to-r from-amber-500/20 via-yellow-500/20 to-amber-500/20 opacity-0 group-hover:opacity-100 transition-opacity duration-500 blur-xl"></div>
            
            <!-- Card Content -->
            <div class="relative z-10">
              <!-- Image Section -->
              <div class="relative overflow-hidden">
                <img :src="item.image_url ? item.image_url : 'https://valboosting.com/wp-content/uploads/2024/11/GmO2LZ3.png'" alt="Product Image" 
                  class="w-full h-48 object-cover transition-transform duration-700 group-hover:scale-110">
                <!-- Image Overlay -->
                <div class="absolute inset-0 bg-gradient-to-t from-black via-transparent to-transparent"></div>
                <!-- Gold Shimmer Effect -->
                <div class="absolute inset-0 shimmer-overlay opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
              </div>

              <!-- Title Section -->
              <div class="relative px-6 pt-4 pb-3 text-center border-b border-amber-500/20">
                <h3 class="text-2xl font-black text-transparent bg-clip-text bg-gradient-to-r from-amber-300 via-yellow-200 to-amber-300 tracking-wider drop-shadow-[0_0_10px_rgba(245,158,11,0.5)]">
                  {{ 'ACC #'+item.account_id }}
                </h3>
                <span :class="['inline-block mt-2 px-4 py-1 rounded-full text-sm font-bold text-white bg-gradient-to-r', getRankColor(item.rank)]">
                  {{ item.rank_info }}
                </span>
              </div>

              <!-- Stats Section -->
              <div class="grid grid-cols-4 gap-2 px-4 py-4 border-b border-amber-500/20 bg-gradient-to-r from-amber-950/30 via-amber-900/20 to-amber-950/30">
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">VP</p>
                  <p class="text-lg font-black text-amber-100">{{ item.vp }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">B.PASS</p>
                  <p class="text-lg font-black text-amber-100">{{ item.btp }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">{{t('accs.gun')}}</p>
                  <p class="text-lg font-black text-amber-100">{{ item.gun_amount }}</p>
                </div>
                <div class="text-center">
                  <p class="text-xs text-amber-400/70 font-semibold">{{ t('accs.melee') }}</p>
                  <p class="text-lg font-black text-amber-100">{{ item.melee_amount }}</p>
                </div>
              </div>

              <!-- Price Section -->
              <div class="px-6 py-5 text-center">
                <p class="text-xs text-amber-400/60 font-semibold mb-1 uppercase tracking-wider">{{ t('accs.price') }}</p>
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
                  <span class="whitespace-nowrap">{{ t('accs.viewDetails') }}</span>
                </button>
                
                <!-- Buy Now Button -->
                <button @click="openBuyPopup(item)" class="buy-btn flex-1 flex items-center justify-center gap-2 py-3 px-4 rounded-xl font-bold text-sm text-black bg-gradient-to-r from-amber-500 via-yellow-400 to-amber-500 bg-size-200 bg-pos-0 transition-all duration-500 hover:bg-pos-100 hover:shadow-[0_0_30px_rgba(245,158,11,0.5)] active:scale-95">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"/>
                  </svg>
                  <span>{{ t('accs.buyNow') }}</span>
                </button>
              </div>
            </div>

            <!-- Corner Decorations -->
            <div class="absolute top-0 left-0 w-16 h-16 border-t-2 border-l-2 border-amber-500/50 rounded-tl-3xl"></div>
            <div class="absolute bottom-0 right-0 w-16 h-16 border-b-2 border-r-2 border-amber-500/50 rounded-br-3xl"></div>
          </div>
        </div>

        <!-- Pagination Section -->
        <div v-if="filteredAccounts.length > 0" class="mt-12">
          <div class="flex flex-col items-center gap-6">
            <!-- Page Info -->
            <p class="text-purple-300 font-semibold text-lg">
              {{ t('filter.page') }} <span class="text-amber-400 font-bold">{{ currentPage }}</span> {{ t('filter.of') }} <span class="text-amber-400 font-bold">{{ totalPages }}</span>
            </p>

            <!-- Pagination Buttons -->
            <div class="flex items-center justify-center gap-3">
              <!-- Previous Button -->
              <button
                @click="prevPage"
                :disabled="currentPage === 1"
                class="px-4 py-3 rounded-lg bg-gradient-to-r from-purple-600 to-purple-700 text-white font-bold transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed hover:shadow-[0_0_20px_rgba(168,85,247,0.5)] active:scale-95"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
                </svg>
              </button>

              <!-- Page Number Buttons -->
              <div class="flex gap-2 flex-wrap justify-center">
                <!-- First Page Button -->
                <button
                  v-if="totalPages > 5 && currentPage > 3"
                  @click="goToPage(1)"
                  class="px-4 py-3 rounded-lg bg-slate-800/50 border border-purple-500/30 text-white font-semibold hover:bg-purple-600 hover:border-purple-400 transition-all"
                >
                  1
                </button>

                <!-- Ellipsis -->
                <div v-if="totalPages > 5 && currentPage > 3" class="px-3 py-3 text-purple-400">...</div>

                <!-- Page Numbers -->
                <button
                  v-for="page in Array.from({ length: totalPages }, (_, i) => i + 1).filter(p => {
                    if (totalPages <= 5) return true
                    if (currentPage <= 3) return p <= 5
                    if (currentPage >= totalPages - 2) return p >= totalPages - 4
                    return p >= currentPage - 2 && p <= currentPage + 2
                  })"
                  :key="page"
                  @click="goToPage(page)"
                  :class="[
                    'px-4 py-3 rounded-lg font-semibold transition-all',
                    currentPage === page
                      ? 'bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-[0_0_20px_rgba(168,85,247,0.5)]'
                      : 'bg-slate-800/50 border border-purple-500/30 text-white hover:bg-purple-600 hover:border-purple-400'
                  ]"
                >
                  {{ page }}
                </button>

                <!-- Ellipsis -->
                <div v-if="totalPages > 5 && currentPage < totalPages - 2" class="px-3 py-3 text-purple-400">...</div>

                <!-- Last Page Button -->
                <button
                  v-if="totalPages > 5 && currentPage < totalPages - 2"
                  @click="goToPage(totalPages)"
                  class="px-4 py-3 rounded-lg bg-slate-800/50 border border-purple-500/30 text-white font-semibold hover:bg-purple-600 hover:border-purple-400 transition-all"
                >
                  {{ totalPages }}
                </button>
              </div>

              <!-- Next Button -->
              <button
                @click="nextPage"
                :disabled="currentPage === totalPages"
                class="px-4 py-3 rounded-lg bg-gradient-to-r from-purple-600 to-purple-700 text-white font-bold transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed hover:shadow-[0_0_20px_rgba(168,85,247,0.5)] active:scale-95"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer Spacer -->
    <div class="h-20"></div>

    <!-- Buy Popup Modal -->
    <Teleport to="body">
      <Transition name="modal">
        <div v-if="showBuyPopup" class="fixed inset-0 z-50 flex items-center justify-center p-4">
          <!-- Backdrop -->
          <div class="absolute inset-0 bg-black/70 backdrop-blur-sm" @click="closeBuyPopup"></div>
          
          <!-- Modal Content -->
          <div class="relative bg-gradient-to-b from-slate-900 via-slate-950 to-black border border-amber-500/30 rounded-3xl w-full max-w-lg max-h-[90vh] overflow-y-auto shadow-[0_0_60px_rgba(245,158,11,0.2)]">
            <!-- Close Button -->
            <button @click="closeBuyPopup" class="absolute top-4 right-4 w-10 h-10 flex items-center justify-center text-gray-400 hover:text-white transition-colors z-10">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
              </svg>
            </button>

            <!-- Header -->
            <div class="p-6 pb-4">
              <h2 class="text-3xl font-black italic">
                <span class="text-purple-400">{{ t('accs.id') }}</span>
                <span class="text-amber-400"> #{{ selectedAcc?.account_id }}</span>
              </h2>
            </div>

            <!-- Category Info -->
            <div class="px-6 pb-4">
              <p class="text-gray-400">
                {{t('accs.category')}}: <span class="text-purple-400 font-bold">ACC VALORANT DROP MAIL</span>
              </p>
            </div>

            <!-- Payment Info Box -->
            <div class="mx-6 mb-4 bg-white/5 backdrop-blur-sm rounded-2xl p-4 border border-white/10">
              <div class="flex justify-between items-center mb-2">
                <span class="text-gray-300 font-semibold">{{ t('accs.transFee') }}</span>
                <span class="text-gray-300 font-semibold">{{ t('common.free') }}</span>
              </div>
              <div class="flex justify-between items-center">
                <span class="text-gray-300 font-semibold">{{ t('accs.transWay') }}</span>
                <span class="text-gray-300 font-semibold">{{ t('common.auto') }}</span>
              </div>
            </div>

            <!-- Balance Info -->
            <div class="px-6 pb-4 flex justify-between items-center">
              <span class="text-gray-300 font-semibold italic">{{t('main.currentBalance')}}</span>
              <span class="text-3xl font-black text-amber-400">{{ formatPrice(user.balance) }}đ</span>
            </div>

            <!-- Voucher Section -->
            <div class="mx-6 mb-4 bg-white rounded-2xl p-5">
              <div class="flex items-center gap-2 mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-gray-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"/>
                </svg>
                <span class="text-gray-800 font-bold">{{ t('common.voucher') }}</span>
              </div>
              <div class="flex gap-2">
                <input
                  v-model="voucherCode"
                  type="text"
                  :placeholder="t('common.voucherQuote')"
                  class="flex-1 px-4 py-3 bg-gray-100 rounded-full text-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-amber-400"
                >
                <button @click="applyVoucher" class="px-6 py-3 bg-gradient-to-r from-amber-500 to-orange-500 text-white font-bold rounded-full hover:shadow-lg transition-all">
                  {{ t('common.apply') }}
                </button>
              </div>
            </div>

            <!-- Total Payment -->
            <div class="px-6 pb-4 flex justify-between items-center">
              <span class="text-2xl font-black text-purple-400 italic">{{ t('common.totalPrice') }}</span>
              <span class="text-3xl font-black text-amber-400">{{ formatPrice(selectedAcc?.price) }}Đ</span>
              <div v-if="totalDiscount" class="text-3xl font-black text-red-700 italic whitespace-nowrap">- {{formatPrice(totalDiscount)}} Đ</div>
            </div>

            <!-- Action Buttons -->
            <div class="p-6 pt-2 flex gap-4">
              <button @click="handleWarrantyPolicy" class="flex-1 py-4 px-6 bg-transparent border-2 border-gray-600 text-white font-bold rounded-full hover:border-gray-400 transition-all">
                {{ t('common.warranty') }}
              </button>
              <button @click="handleDeposit" class="flex-1 py-4 px-6 bg-gradient-to-r from-cyan-500 to-blue-600 text-white font-bold rounded-full hover:shadow-[0_0_20px_rgba(6,182,212,0.5)] transition-all">
                {{ t('payment.buy') }}
              </button>
            </div>
          </div>
        </div>
      </Transition>
    </Teleport>
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
