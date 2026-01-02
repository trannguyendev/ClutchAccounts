<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import mainBackground from '@/img/main-background.jpg'

const router = useRouter()

// Mock data - replace with actual API calls
const transactionLogs = ref([
  { id: 1, date: '2026-01-02', amount: '50,000', type: 'Deposit', status: 'Success', description: 'Add Money' },
  { id: 2, date: '2026-01-01', amount: '100,000', type: 'Withdrawal', status: 'Pending', description: 'Withdraw' },
  { id: 3, date: '2025-12-31', amount: '25,000', type: 'Deposit', status: 'Success', description: 'Add Money' },
  { id: 4, date: '2025-12-30', amount: '75,000', type: 'Purchase', status: 'Cancelled', description: 'Account Purchase' },
  { id: 5, date: '2025-12-29', amount: '150,000', type: 'Deposit', status: 'Success', description: 'Add Money' },
])

const boughtLogs = ref([
  { id: 1, date: '2026-01-02', accountType: 'Acc Drop Mail', amount: '150,000', status: 'Completed', seller: 'Admin' },
  { id: 2, date: '2026-01-01', accountType: 'Acc Sieu Sale', amount: '200,000', status: 'Completed', seller: 'Admin' },
  { id: 3, date: '2025-12-30', accountType: 'Acc Random FA', amount: '100,000', status: 'Pending', seller: 'Admin' },
  { id: 4, date: '2025-12-28', accountType: 'Acc Random NFA', amount: '75,000', status: 'Completed', seller: 'Admin' },
  { id: 5, date: '2025-12-25', accountType: 'Acc Drop Mail', amount: '125,000', status: 'Completed', seller: 'Admin' },
])

// Filter states
const transactionFilterType = ref('all')
const transactionFilterStatus = ref('all')
const boughtFilterType = ref('all')
const boughtFilterStatus = ref('all')

// Pagination states
const transactionCurrentPage = ref(1)
const transactionItemsPerPage = ref(5)
const boughtCurrentPage = ref(1)
const boughtItemsPerPage = ref(5)

// Get unique values for dropdown filters
const transactionTypes = computed(() => {
  const types = new Set(transactionLogs.value.map(log => log.type))
  return Array.from(types).sort()
})

const transactionStatuses = computed(() => {
  const statuses = new Set(transactionLogs.value.map(log => log.status))
  return Array.from(statuses).sort()
})

const boughtAccountTypes = computed(() => {
  const types = new Set(boughtLogs.value.map(log => log.accountType))
  return Array.from(types).sort()
})

const boughtStatuses = computed(() => {
  const statuses = new Set(boughtLogs.value.map(log => log.status))
  return Array.from(statuses).sort()
})

// Filter transaction logs
const filteredTransactionLogs = computed(() => {
  return transactionLogs.value.filter(log => {
    const typeMatch = transactionFilterType.value === 'all' || log.type === transactionFilterType.value
    const statusMatch = transactionFilterStatus.value === 'all' || log.status === transactionFilterStatus.value
    return typeMatch && statusMatch
  })
})

// Paginate transaction logs
const paginatedTransactionLogs = computed(() => {
  const filtered = filteredTransactionLogs.value
  const start = (transactionCurrentPage.value - 1) * transactionItemsPerPage.value
  const end = start + transactionItemsPerPage.value
  return filtered.slice(start, end)
})

const transactionTotalPages = computed(() => {
  return Math.ceil(filteredTransactionLogs.value.length / transactionItemsPerPage.value)
})

// Filter bought logs
const filteredBoughtLogs = computed(() => {
  return boughtLogs.value.filter(log => {
    const typeMatch = boughtFilterType.value === 'all' || log.accountType === boughtFilterType.value
    const statusMatch = boughtFilterStatus.value === 'all' || log.status === boughtFilterStatus.value
    return typeMatch && statusMatch
  })
})

// Paginate bought logs
const paginatedBoughtLogs = computed(() => {
  const filtered = filteredBoughtLogs.value
  const start = (boughtCurrentPage.value - 1) * boughtItemsPerPage.value
  const end = start + boughtItemsPerPage.value
  return filtered.slice(start, end)
})

const boughtTotalPages = computed(() => {
  return Math.ceil(filteredBoughtLogs.value.length / boughtItemsPerPage.value)
})

// Methods
const goBack = () => {
  router.back()
}

const refreshTransactionLogs = () => {
  // TODO: Fetch from API
  console.log('Refreshing transaction logs...')
}

const refreshBoughtLogs = () => {
  // TODO: Fetch from API
  console.log('Refreshing bought logs...')
}

const resetTransactionPagination = () => {
  transactionCurrentPage.value = 1
}

const resetBoughtPagination = () => {
  boughtCurrentPage.value = 1
}

const getStatusClass = (status) => {
  switch(status) {
    case 'Success':
    case 'Completed':
      return 'text-green-400 bg-green-500/10 px-3 py-1 rounded-full text-xs font-semibold'
    case 'Pending':
      return 'text-yellow-400 bg-yellow-500/10 px-3 py-1 rounded-full text-xs font-semibold'
    case 'Cancelled':
      return 'text-red-400 bg-red-500/10 px-3 py-1 rounded-full text-xs font-semibold'
    default:
      return 'text-amber-300 bg-amber-500/10 px-3 py-1 rounded-full text-xs font-semibold'
  }
}

const getTypeClass = (type) => {
  switch(type) {
    case 'Deposit':
      return 'text-blue-400'
    case 'Withdrawal':
      return 'text-orange-400'
    case 'Purchase':
      return 'text-violet-400'
    default:
      return 'text-amber-300'
  }
}
</script>

<template>
  <div class="min-h-screen bg-cover bg-center bg-no-repeat" :style="{ backgroundImage: `url(${mainBackground})` }">
    <div class="absolute inset-0 bg-black/50"></div>
    
    <div class="relative py-12 px-4 md:px-8">
      <!-- Header with Back Button -->
      <div class="max-w-7xl mx-auto mb-8">
        <button @click="goBack" 
          class="flex items-center gap-2 px-4 py-2 rounded-lg bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300 font-semibold transition-all duration-300 mb-6">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
          </svg>
          <span>Go Back</span>
        </button>

        <h1 class="text-4xl font-extrabold text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.12)]">
          Transaction & Bought Logs
        </h1>
      </div>

      <!-- Transaction Log Table -->
      <div class="max-w-7xl mx-auto mb-12">
        <div class="rounded-2xl p-6 bg-gradient-to-br from-neutral-900/70 to-black/60 border border-amber-700/10 backdrop-blur-md hover:border-amber-700/30 transition duration-300">
          <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4 mb-6">
            <h2 class="text-2xl font-bold text-amber-300">Transaction Logs</h2>
            
            <div class="flex flex-col md:flex-row gap-3 w-full md:w-auto">
              <!-- Filter by Type -->
              <div class="flex items-center gap-2">
                <select v-model="transactionFilterType" @change="resetTransactionPagination"
                  class="px-4 py-2 rounded-lg bg-black/40 border border-amber-700/30 text-amber-100 font-semibold hover:border-amber-700/60 hover:bg-black/50 focus:outline-none focus:ring-2 focus:ring-amber-500 transition cursor-pointer">
                  <option value="all">All Types</option>
                  <option v-for="type in transactionTypes" :key="type" :value="type">
                    {{ type }}
                  </option>
                </select>
              </div>

              <!-- Filter by Status -->
              <div class="flex items-center gap-2">
                <select v-model="transactionFilterStatus" @change="resetTransactionPagination"
                  class="px-4 py-2 rounded-lg bg-black/40 border border-amber-700/30 text-amber-100 font-semibold hover:border-amber-700/60 hover:bg-black/50 focus:outline-none focus:ring-2 focus:ring-amber-500 transition cursor-pointer">
                  <option value="all">All Status</option>
                  <option v-for="status in transactionStatuses" :key="status" :value="status">
                    {{ status }}
                  </option>
                </select>
              </div>

              <!-- Refresh Button -->
              <button @click="refreshTransactionLogs"
                class="px-4 py-2 rounded-lg bg-amber-500 hover:bg-amber-600 text-black font-semibold transition-all duration-200 flex items-center gap-2 whitespace-nowrap">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                </svg>
                Refresh
              </button>
            </div>
          </div>

          <!-- Transaction Table -->
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-amber-700/20">
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Date</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Amount</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Type</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Status</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Description</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="filteredTransactionLogs.length === 0" class="border-b border-amber-700/10">
                  <td colspan="5" class="text-center py-8 text-amber-200/60">
                    No transaction logs found
                  </td>
                </tr>
                <tr v-for="log in paginatedTransactionLogs" :key="log.id" 
                  class="border-b border-amber-700/10 hover:bg-amber-700/5 transition duration-200">
                  <td class="py-4 px-4 text-amber-100">{{ log.date }}</td>
                  <td class="py-4 px-4 text-amber-100 font-semibold">{{ log.amount }}</td>
                  <td class="py-4 px-4">
                    <span :class="getTypeClass(log.type)" class="font-semibold">{{ log.type }}</span>
                  </td>
                  <td class="py-4 px-4">
                    <span :class="getStatusClass(log.status)">{{ log.status }}</span>
                  </td>
                  <td class="py-4 px-4 text-amber-100/80">{{ log.description }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination Controls -->
          <div class="flex items-center justify-between mt-6 pt-6 border-t border-amber-700/10">
            <div class="text-sm text-amber-200/70">
              Showing <span class="font-semibold">{{ (transactionCurrentPage - 1) * transactionItemsPerPage + 1 }}</span> 
              to <span class="font-semibold">{{ Math.min(transactionCurrentPage * transactionItemsPerPage, filteredTransactionLogs.length) }}</span> 
              of <span class="font-semibold">{{ filteredTransactionLogs.length }}</span> entries
            </div>

            <div class="flex items-center gap-2">
              <button @click="transactionCurrentPage = Math.max(1, transactionCurrentPage - 1)" 
                :disabled="transactionCurrentPage === 1"
                class="px-3 py-2 rounded-lg bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300 font-semibold transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                </svg>
              </button>

              <div class="flex items-center gap-1">
                <button v-for="page in transactionTotalPages" :key="page" 
                  @click="transactionCurrentPage = page"
                  :class="[
                    'px-3 py-2 rounded-lg font-semibold transition-all duration-200',
                    transactionCurrentPage === page 
                      ? 'bg-amber-500 text-black border border-amber-400' 
                      : 'bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300'
                  ]">
                  {{ page }}
                </button>
              </div>

              <button @click="transactionCurrentPage = Math.min(transactionTotalPages, transactionCurrentPage + 1)" 
                :disabled="transactionCurrentPage === transactionTotalPages"
                class="px-3 py-2 rounded-lg bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300 font-semibold transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Bought Log Table -->
      <div class="max-w-7xl mx-auto">
        <div class="rounded-2xl p-6 bg-gradient-to-br from-neutral-900/70 to-black/60 border border-amber-700/10 backdrop-blur-md hover:border-amber-700/30 transition duration-300">
          <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-4 mb-6">
            <h2 class="text-2xl font-bold text-amber-300">Bought Logs</h2>
            
            <div class="flex flex-col md:flex-row gap-3 w-full md:w-auto">
              <!-- Filter by Account Type -->
              <div class="flex items-center gap-2">
                <select v-model="boughtFilterType" @change="resetBoughtPagination"
                  class="px-4 py-2 rounded-lg bg-black/40 border border-amber-700/30 text-amber-100 font-semibold hover:border-amber-700/60 hover:bg-black/50 focus:outline-none focus:ring-2 focus:ring-amber-500 transition cursor-pointer">
                  <option value="all">All Account Types</option>
                  <option v-for="type in boughtAccountTypes" :key="type" :value="type">
                    {{ type }}
                  </option>
                </select>
              </div>

              <!-- Filter by Status -->
              <div class="flex items-center gap-2">
                <select v-model="boughtFilterStatus" @change="resetBoughtPagination"
                  class="px-4 py-2 rounded-lg bg-black/40 border border-amber-700/30 text-amber-100 font-semibold hover:border-amber-700/60 hover:bg-black/50 focus:outline-none focus:ring-2 focus:ring-amber-500 transition cursor-pointer">
                  <option value="all">All Status</option>
                  <option v-for="status in boughtStatuses" :key="status" :value="status">
                    {{ status }}
                  </option>
                </select>
              </div>

              <!-- Refresh Button -->
              <button @click="refreshBoughtLogs"
                class="px-4 py-2 rounded-lg bg-amber-500 hover:bg-amber-600 text-black font-semibold transition-all duration-200 flex items-center gap-2 whitespace-nowrap">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
                </svg>
                Refresh
              </button>
            </div>
          </div>

          <!-- Bought Table -->
          <div class="overflow-x-auto">
            <table class="w-full">
              <thead>
                <tr class="border-b border-amber-700/20">
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Date</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Account Type</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Amount</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Status</th>
                  <th class="text-left py-4 px-4 text-amber-300 font-semibold">Seller</th>
                </tr>
              </thead>
              <tbody>
                <tr v-if="filteredBoughtLogs.length === 0" class="border-b border-amber-700/10">
                  <td colspan="5" class="text-center py-8 text-amber-200/60">
                    No bought logs found
                  </td>
                </tr>
                <tr v-for="log in paginatedBoughtLogs" :key="log.id" 
                  class="border-b border-amber-700/10 hover:bg-amber-700/5 transition duration-200">
                  <td class="py-4 px-4 text-amber-100">{{ log.date }}</td>
                  <td class="py-4 px-4 text-amber-100 font-semibold">{{ log.accountType }}</td>
                  <td class="py-4 px-4 text-amber-100 font-semibold">{{ log.amount }}</td>
                  <td class="py-4 px-4">
                    <span :class="getStatusClass(log.status)">{{ log.status }}</span>
                  </td>
                  <td class="py-4 px-4 text-amber-100/80">{{ log.seller }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination Controls -->
          <div class="flex items-center justify-between mt-6 pt-6 border-t border-amber-700/10">
            <div class="text-sm text-amber-200/70">
              Showing <span class="font-semibold">{{ (boughtCurrentPage - 1) * boughtItemsPerPage + 1 }}</span> 
              to <span class="font-semibold">{{ Math.min(boughtCurrentPage * boughtItemsPerPage, filteredBoughtLogs.length) }}</span> 
              of <span class="font-semibold">{{ filteredBoughtLogs.length }}</span> entries
            </div>

            <div class="flex items-center gap-2">
              <button @click="boughtCurrentPage = Math.max(1, boughtCurrentPage - 1)" 
                :disabled="boughtCurrentPage === 1"
                class="px-3 py-2 rounded-lg bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300 font-semibold transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                </svg>
              </button>

              <div class="flex items-center gap-1">
                <button v-for="page in boughtTotalPages" :key="page" 
                  @click="boughtCurrentPage = page"
                  :class="[
                    'px-3 py-2 rounded-lg font-semibold transition-all duration-200',
                    boughtCurrentPage === page 
                      ? 'bg-amber-500 text-black border border-amber-400' 
                      : 'bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300'
                  ]">
                  {{ page }}
                </button>
              </div>

              <button @click="boughtCurrentPage = Math.min(boughtTotalPages, boughtCurrentPage + 1)" 
                :disabled="boughtCurrentPage === boughtTotalPages"
                class="px-3 py-2 rounded-lg bg-amber-700/20 hover:bg-amber-700/40 border border-amber-700/30 hover:border-amber-700/60 text-amber-300 font-semibold transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Optional: Add smooth transitions */
select option {
  background-color: #1a1a1a;
  color: #fcd34d;
}

/* Ensure table rows have proper spacing on mobile */
@media (max-width: 768px) {
  table {
    font-size: 0.875rem;
  }

  td, th {
    padding: 0.75rem 0.5rem;
  }
}
</style>
