<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../stores/user'
import Navbar from '@/components/Navbar.vue'
import { onMounted } from 'vue'
import axios from 'axios'


const currentUser = useUserStore()
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const changePasswordMessage = ref('')
const showChangeSuccess = ref(false)
const currentPage = ref(1)
const itemsPerPage = 10

const loginHistory = ref([])

const loadAudit = async () => {
  await axios.get("/api/user/login-history")
  .then((res) => {
    console.log(res.data)
    loginHistory.value = res.data
  })
  .catch((err) => {
    console.error("Error fetching login history:", err)
  })  
}

const paginatedHistory = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return loginHistory.value.slice(start, end)
})

const totalPages = computed(() => {
  return Math.ceil(loginHistory.value.length / itemsPerPage)
})

const handleChangePassword = () => {
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    changePasswordMessage.value = 'Please fill in all fields'
    return
  }
  if (newPassword.value !== confirmPassword.value) {
    changePasswordMessage.value = 'New passwords do not match'
    return
  }
  if (newPassword.value.trim().length < 8) {
    changePasswordMessage.value = 'Password must be at least 8 characters'
    return
  }
  
  
  
  const dataSend = {
    email: currentUser.username,
    oldPassword: oldPassword.value,
    newPassword: newPassword.value
  }
  console.log('Data to send for password change:', dataSend)
axios.post('/api/user/change-psw', dataSend)
    .then((response) => {
      console.log('Password change response:', response.data)
      showChangeSuccess.value = true
      changePasswordMessage.value = 'Password changed successfully!'
      oldPassword.value = ''
      newPassword.value = ''
      confirmPassword.value = ''
    })
    .catch((error) => {
      console.error('Error changing password:', error)
      changePasswordMessage.value = error.response.data.error
      console.log('Error data:', error.response.data.error)
    })
  setTimeout(() => {
    showChangeSuccess.value = false
  }, 3000)
}

const getBrowserInfo = (userAgent) => {
  if (userAgent.includes('Chrome')) return 'Chrome'
  if (userAgent.includes('Safari')) return 'Safari'
  if (userAgent.includes('Firefox')) return 'Firefox'
  if (userAgent.includes('Edge')) return 'Edge'
  return 'Other'
}

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}
onMounted(() => {
  loadAudit()
})
</script>

<template>
  <Navbar></Navbar>
  <main class="min-h-screen main-background relative">
    <!-- Background Overlay -->
    <div class="absolute inset-0 bg-gradient-to-b from-slate-950 via-slate-900 to-slate-950 opacity-50"></div>

    <!-- Main Container -->
    <div class="relative z-10 max-w-7xl mx-auto px-8 py-12">
      <!-- Header -->
      <div class="mb-12">
        <h1 class="text-4xl font-black text-white mb-2 flex items-center gap-3">
          <i class="fa fa-user-circle text-amber-400"></i> User Information
          <div class="flex-1 h-1 bg-gradient-to-r from-amber-400 to-transparent ml-4"></div>
        </h1>
        <p class="text-slate-400">Manage your account information and security</p>
      </div>

      <!-- Main Content Grid -->
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">
        <!-- Left: Personal Information -->
        <div class="bg-gradient-to-br from-slate-900 to-black backdrop-blur-md border border-amber-500/30 rounded-2xl p-8">
          <h2 class="text-2xl font-black text-white mb-8 flex items-center gap-3">
            <i class="fa fa-address-card text-amber-400"></i> Personal Information
          </h2>

          <div class="flex items-start gap-6">
            <!-- Avatar Section -->
            <div class="flex-shrink-0">
              <div class="w-32 h-32 rounded-xl bg-gradient-to-br from-amber-500 to-orange-600 border-4 border-amber-400/50 flex items-center justify-center overflow-hidden">
                <img :src="currentUser.avatar" alt="avatar" class="w-full h-full object-cover">
              </div>
            </div>

            <!-- User Details Frame -->
            <div class="flex-1 space-y-4">
              <!-- Username -->
              <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700">
                <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-1">Name</label>
                <p class="text-white font-bold text-lg">{{ currentUser.username }}</p>
              </div>

              <!-- ID -->
              <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700">
                <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-1">ID</label>
                <p class="text-amber-300 font-bold">{{ currentUser.id || '12345' }}</p>
              </div>

              <!-- Balance -->
              <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700">
                <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-1">Balance</label>
                <p class="text-amber-400 font-black text-xl">{{ currentUser.balance }} VND</p>
              </div>

              <!-- Date -->
              <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700">
                <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-1">Today</label>
                <p class="text-slate-300 font-semibold text-sm">{{ new Date().toLocaleString() }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Right: Change Password -->
        <div class="bg-gradient-to-br from-slate-900 to-black backdrop-blur-md border border-amber-500/30 rounded-2xl p-8">
          <h2 class="text-2xl font-black text-white mb-8 flex items-center gap-3">
            <i class="fa fa-lock text-amber-400"></i> Change Password
          </h2>

          <!-- Success Message -->
          <div v-if="showChangeSuccess" class="mb-6 bg-green-500/20 border border-green-500/50 rounded-lg p-4">
            <p class="text-green-300 font-semibold flex items-center gap-2">
              <i class="fa fa-check-circle"></i> {{ changePasswordMessage }}
            </p>
          </div>

          <!-- Error Message -->
          <div v-if="changePasswordMessage && !showChangeSuccess" class="mb-6 bg-red-500/20 border border-red-500/50 rounded-lg p-4">
            <p class="text-red-300 font-semibold flex items-center gap-2">
              <i class="fa fa-exclamation-circle"></i> {{ changePasswordMessage }}
            </p>
          </div>

          <!-- Password Form -->
          <div class="space-y-4 mb-8">
            <!-- Old Password -->
            <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700 hover:border-amber-400/50 transition-colors">
              <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-2">Old Password</label>
              <input 
                v-model="oldPassword"
                type="password" 
                placeholder="••••••••" 
                class="w-full bg-slate-900 border border-slate-600 rounded-lg px-4 py-3 text-slate-300 placeholder-slate-500 focus:border-amber-400 focus:outline-none transition-colors">
            </div>

            <!-- New Password -->
            <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700 hover:border-amber-400/50 transition-colors">
              <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-2">New Password</label>
              <input 
                v-model="newPassword"
                type="password" 
                placeholder="••••••••" 
                class="w-full bg-slate-900 border border-slate-600 rounded-lg px-4 py-3 text-slate-300 placeholder-slate-500 focus:border-amber-400 focus:outline-none transition-colors">
            </div>

            <!-- Confirm Password -->
            <div class="bg-slate-800/50 rounded-lg p-4 border border-slate-700 hover:border-amber-400/50 transition-colors">
              <label class="text-amber-300/70 text-xs font-bold uppercase tracking-wider block mb-2">Confirm new password</label>
              <input 
                v-model="confirmPassword"
                type="password" 
                placeholder="••••••••" 
                class="w-full bg-slate-900 border border-slate-600 rounded-lg px-4 py-3 text-slate-300 placeholder-slate-500 focus:border-amber-400 focus:outline-none transition-colors">
            </div>
          </div>

          <!-- Change Password Button -->
          <button 
            @click="handleChangePassword"
            class="w-full bg-gradient-to-r from-amber-600 to-orange-600 hover:from-amber-700 hover:to-orange-700 text-white font-black text-lg py-3 px-6 rounded-lg transition-all duration-300 hover:shadow-lg hover:shadow-amber-600/50 active:scale-95">
            <i class="fa fa-key mr-2"></i> Change Password
          </button>
        </div>
      </div>

      <!-- Login History Section -->
      <div class="bg-gradient-to-br from-slate-900 to-black backdrop-blur-md border border-amber-500/30 rounded-2xl p-8">
        <h2 class="text-2xl font-black text-white mb-8 flex items-center gap-3">
          <i class="fa fa-history text-amber-400"></i> Login History
        </h2>

        <!-- Responsive Table -->
        <div class="overflow-x-auto">
          <table class="w-full">
            <thead>
              <tr class="border-b border-amber-500/30">
                <th class="text-left px-6 py-4">
                  <span class="text-amber-400 font-black text-sm uppercase tracking-wider">Time</span>
                </th>
                <th class="text-left px-6 py-4">
                  <span class="text-amber-400 font-black text-sm uppercase tracking-wider">IP Address</span>
                </th>
                <th class="text-left px-6 py-4">
                  <span class="text-amber-400 font-black text-sm uppercase tracking-wider">Device</span>
                </th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="(log, index) in paginatedHistory" 
                :key="index"
                class="border-b border-slate-800 hover:bg-slate-800/30 transition-all duration-200">
                <td class="px-6 py-4">
                  <div class="flex items-center gap-2">
                    <span class="text-slate-300 font-semibold text-sm">{{ log.logged_time }}</span>
                  </div>
                </td>
                <td class="px-6 py-4">
                  <code class="bg-slate-800/50 text-amber-300 px-3 py-1 rounded font-mono text-xs">{{ log.logged_ip ? log.logged_ip : "Suspicious login" }}</code>
                </td>
                <td class="px-6 py-4">
                  <div class="flex items-center gap-2">
                    <span class="text-slate-300 font-semibold text-sm">{{ getBrowserInfo(log.logged_device) }}</span>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination Controls -->
        <div v-if="totalPages > 1" class="flex items-center justify-between mt-8 pt-8 border-t border-slate-800">
          <div class="text-slate-400 text-sm font-semibold">
            Showing <span class="text-amber-300">{{ (currentPage - 1) * itemsPerPage + 1 }}</span> - <span class="text-amber-300">{{ Math.min(currentPage * itemsPerPage, loginHistory.length) }}</span> of <span class="text-amber-300">{{ loginHistory.length }}</span> records
          </div>

          <div class="flex items-center gap-2">
            <!-- Previous Button -->
            <button 
              @click="goToPage(currentPage - 1)"
              :disabled="currentPage === 1"
              class="px-4 py-2 rounded-lg border border-amber-500/30 text-amber-300 hover:bg-amber-500/10 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 font-semibold">
              <i class="fa fa-chevron-left mr-1"></i> Previous
            </button>

            <!-- Page Numbers -->
            <div class="flex items-center gap-1">
              <button 
                v-for="page in totalPages"
                :key="page"
                @click="goToPage(page)"
                :class="{
                  'bg-gradient-to-r from-amber-600 to-orange-600 text-white': currentPage === page,
                  'border border-amber-500/30 text-amber-300 hover:bg-amber-500/10': currentPage !== page
                }"
                class="w-10 h-10 rounded-lg transition-all duration-200 font-bold text-sm">
                {{ page }}
              </button>
            </div>

            <!-- Next Button -->
            <button 
              @click="goToPage(currentPage + 1)"
              :disabled="currentPage === totalPages"
              class="px-4 py-2 rounded-lg border border-amber-500/30 text-amber-300 hover:bg-amber-500/10 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200 font-semibold">
              Next <i class="fa fa-chevron-right ml-1"></i>
            </button>
          </div>
        </div>

        <!-- Empty State -->
        <div v-if="loginHistory.length === 0" class="text-center py-12">
          <i class="fa fa-inbox text-slate-600 text-4xl mb-4 block"></i>
          <p class="text-slate-400 font-semibold">No login history available</p>
        </div>
      </div>
    </div>
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

/* Table hover effects */
table tbody tr:hover {
  transform: translateX(4px);
}

/* Smooth animations */
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.space-y-4 > div {
  animation: slideIn 0.3s ease-out;
}
</style>
