<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <div class="flex-1 p-6">
      <h1 class="text-2xl font-bold mb-6 text-amber-300">💳 Transaction Management</h1>

      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] overflow-auto">
        <div v-if="loading" class="text-center py-8">
          <p class="text-amber-300">Loading transactions...</p>
        </div>
        <div v-else-if="error" class="text-center py-8">
          <p class="text-red-400">{{ error }}</p>
        </div>
        <table v-else class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-amber-900/30 text-amber-300">
              <th class="pb-3 px-4">ID</th>
              <th class="pb-3 px-4">User</th>
              <th class="pb-3 px-4">Amount</th>
              <th class="pb-3 px-4">Type</th>
              <th class="pb-3 px-4">Description</th>
              <th class="pb-3 px-4">Status</th>
              <th class="pb-3 px-4">Date</th>
              <th class="pb-3 px-4">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="transaction in paginatedTransactions" :key="transaction.id"
              class="border-t border-amber-900/20 hover:bg-amber-900/10 transition-all">
              <td class="py-3 px-4 text-amber-200">{{ transaction.id }}</td>
              <td class="py-3 px-4 text-amber-200">{{ transaction.username || 'N/A' }}</td>
              <td class="py-3 px-4 text-amber-200">{{ transaction.amount }}</td>
              <td class="py-3 px-4 text-amber-200">{{ transaction.type }}</td>
              <td class="py-3 px-4 text-amber-200">{{ transaction.descrp }}</td>
              <td class="py-3 px-4">
                <span :class="[
                  'px-3 py-1 rounded-full text-xs font-semibold',
                  transaction.status === 'pending' ? 'bg-yellow-900/30 text-yellow-300' :
                    transaction.status === 'completed' ? 'bg-green-900/30 text-green-300' :
                      'bg-red-900/30 text-red-300'
                ]">
                  {{ transaction.status }}
                </span>
              </td>
              <td class="py-3 px-4 text-amber-200">{{ formatDate(transaction.created_at) }}</td>
              <td class="py-3 px-4 flex gap-2">
                <button v-if="transaction.status === 'PENDING'" @click="approveTransaction(transaction.id)"
                  :disabled="processing[transaction.id]"
                  class="px-3 py-1 bg-green-900/30 text-green-300 rounded hover:bg-green-900/50 disabled:opacity-50 transition-all text-xs font-semibold">
                  {{ processing[transaction.id] ? 'Approving...' : 'Approve' }}
                </button>
                <button v-if="transaction.status === 'PENDING'" @click="rejectTransaction(transaction.id)"
                  :disabled="processing[transaction.id]"
                  class="px-3 py-1 bg-red-900/30 text-red-300 rounded hover:bg-red-900/50 disabled:opacity-50 transition-all text-xs font-semibold">
                  {{ processing[transaction.id] ? 'Rejecting...' : 'Reject' }}
                </button>
              </td>
            </tr>
            <tr v-if="paginatedTransactions.length === 0">
              <td colspan="8" class="py-8 text-center text-amber-200/60">No pending transactions found</td>
            </tr>
          </tbody>
        </table>

        <div v-if="totalPages > 1" class="mt-4 flex items-center justify-between">
          <div class="text-sm text-amber-200/60">
            Trang {{ currentPage }} / {{ totalPages }}
          </div>
          <div class="flex items-center gap-2">
            <button @click="previousPage"
              class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all">Prev</button>
            <div class="flex gap-1">
              <button v-for="n in totalPages" :key="n" @click="currentPage = n" :class="[
                'px-2 py-1 rounded transition-all',
                currentPage === n
                  ? 'bg-amber-600 text-black font-semibold'
                  : 'bg-amber-900/20 text-amber-200 hover:bg-amber-900/30'
              ]">
                {{ n }}
              </button>
            </div>
            <button @click="nextPage"
              class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all">Next</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import AdminSidebar from './AdminSidebar.vue';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

const router = useRouter();
const activeLi = ref(1);

const currentPage = ref(1);
const pageSize = 10;
const transactions = ref([]);
const loading = ref(false);
const error = ref(null);
const processing = ref({});
let pollingInterval = null;

const totalPages = computed(() => {
  return Math.ceil(transactions.value.length / pageSize);
});

const paginatedTransactions = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  const end = start + pageSize;
  return transactions.value.slice(start, end);
});

const handleNavigation = (item, index) => {
  activeLi.value = index;
  router.push(item.route);
};

const previousPage = () => {
  if (currentPage.value > 1) currentPage.value--;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value++;
};

const fetchPendingTransactions = async () => {
  loading.value = true;
  error.value = null;

  await axios.get('/api/payment/transaction-pending')
    .then((res) => {
      transactions.value = res.data;
      console.log('Fetched transactions:', transactions.value);
    })
    .catch((err) => {
      error.value = err.response.data.error || 'Failed to fetch transactions';
      console.error('Error fetching transactions:', err);
    })  
    .finally(() => {
      loading.value = false;
    });
    
  
};

const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
};

const approveTransaction = async (transactionId) => {
  processing.value[transactionId] = true;
  try {
    const response = await axios.post(`/api/payment/approve/${transactionId}`);
    toast.success(response.data.message);
    await fetchPendingTransactions();
    currentPage.value = 1;
  } catch (err) {
    toast.error(err.response.data.error || 'Failed to approve transaction')
    console.error('Error approving transaction:', err);
  } finally {
    processing.value[transactionId] = false;
  }
};

const rejectTransaction = async (transactionId) => {
  processing.value[transactionId] = true;
  try {
    const response = await axios.post(`/api/payment/reject/${transactionId}`);
    toast.success(response.data.message);
    await fetchPendingTransactions();
    currentPage.value = 1;
  } catch (err) {
    toast.error(err.response.data.error || 'Failed to reject transaction')
    console.error('Error rejecting transaction:', err);
  } finally {
    processing.value[transactionId] = false;
  }
};

onMounted(() => {
  fetchPendingTransactions();

  pollingInterval = setInterval(fetchPendingTransactions, 10000);
});

onUnmounted(() => {
  if (pollingInterval) {
    clearInterval(pollingInterval);
  }
});
</script>

<style scoped></style>
