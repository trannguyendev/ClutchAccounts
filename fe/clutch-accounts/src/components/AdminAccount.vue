<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <!-- Sidebar (reusable) -->
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <!-- Main Content -->
    <div class="flex-1 p-6">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-2xl font-bold text-amber-300"><i class="fa-solid fa-people-roof"></i> Account Management</h1>
        <button @click="showAddModal = true" class="px-4 py-2 bg-amber-600 text-black rounded-lg font-semibold hover:bg-amber-500 transition-all">
          + Thêm Account
        </button>
      </div>

      <!-- Search and Filter -->
      <div class="mb-6 flex gap-4">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Tìm kiếm theo username hoặc email..."
          class="flex-1 px-4 py-2 rounded-lg bg-[#1a1a1a] border border-amber-900/50 text-amber-100 placeholder-amber-200/40 focus:outline-none focus:border-amber-600"
        />
        <select
          v-model="filterStatus"
          class="px-4 py-2 rounded-lg bg-[#1a1a1a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
        >
          <option value="">Tất cả trạng thái</option>
          <option value="active">Hoạt động</option>
          <option value="locked">Bị khóa</option>
          <option value="banned">Bị cấm</option>
        </select>
      </div>

      <!-- Accounts Table -->
      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] overflow-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-amber-900/30 text-amber-300">
              <th class="pb-3 px-4">ID</th>
              <th class="pb-3 px-4">Email</th>
              <th class="pb-3 px-4">Balance</th>
              <th class="pb-3 px-4">Trạng thái</th>
              <th class="pb-3 px-4">Ngày tạo</th>
              <th class="pb-3 px-4">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="account in filteredAccounts" :key="account.id" class="border-t border-amber-900/20 hover:bg-amber-900/10 transition-all">
              <td class="py-3 px-4 text-amber-200">{{ account.id }}</td>
              <td class="py-3 px-4 text-amber-200/80">{{ account.email }}</td>
              <td class="py-3 px-4">
                <span class="px-2 py-1 bg-amber-900/30 text-amber-300 rounded text-xs font-semibold">$ {{ account.balance }}</span>
              </td>
              <td class="py-3 px-4">
                <span v-if="account.status === 'active'" class="inline-block px-3 py-1 rounded text-xs font-semibold bg-emerald-500/20 text-emerald-300">✓ Hoạt động</span>
                <span v-else-if="account.status === 'locked'" class="inline-block px-3 py-1 rounded text-xs font-semibold bg-yellow-500/20 text-yellow-300">🔒 Bị khóa</span>
                <span v-else-if="account.status === 'banned'" class="inline-block px-3 py-1 rounded text-xs font-semibold bg-red-500/20 text-red-300">⛔ Bị cấm</span>
              </td>
              <td class="py-3 px-4 text-amber-200/60 text-xs">{{ account.createdDate }}</td>
              <td class="py-3 px-4">
                <div class="flex gap-2">
                  <button
                    @click="editAccount(account)"
                    class="px-2 py-1 text-xs bg-blue-900/30 text-blue-300 rounded hover:bg-blue-900/50 transition-all"
                  >
                    ✎ Sửa
                  </button>
                  <button
                    @click="toggleLock(account)"
                    :class="[
                      'px-2 py-1 text-xs rounded transition-all',
                      account.status === 'active'
                        ? 'bg-yellow-900/30 text-yellow-300 hover:bg-yellow-900/50'
                        : 'bg-emerald-900/30 text-emerald-300 hover:bg-emerald-900/50'
                    ]"
                  >
                    {{ account.status === 'active' ? '🔒 Khóa' : '🔓 Mở' }}
                  </button>
                  <button
                    @click="deleteAccount(account.id)"
                    class="px-2 py-1 text-xs bg-red-900/30 text-red-300 rounded hover:bg-red-900/50 transition-all"
                  >
                    🗑 Xóa
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- Empty State -->
        <div v-if="filteredAccounts.length === 0" class="text-center py-8 text-amber-200/60">
          Không có account nào
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="totalPages > 1" class="mt-4 flex items-center justify-between">
        <div class="text-sm text-amber-200/60">
          Trang {{ currentPage }} / {{ totalPages }}
        </div>
        <div class="flex items-center gap-2">
          <button @click="previousPage" class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all">Prev</button>
          <div class="flex gap-1">
            <button
              v-for="n in totalPages"
              :key="n"
              @click="currentPage = n"
              :class="[
                'px-2 py-1 rounded transition-all',
                currentPage === n
                  ? 'bg-amber-600 text-black font-semibold'
                  : 'bg-amber-900/20 text-amber-200 hover:bg-amber-900/30'
              ]"
            >
              {{ n }}
            </button>
          </div>
          <button @click="nextPage" class="px-3 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 transition-all">Next</button>
        </div>
      </div>
    </div>

    <!-- Add/Edit Account Modal -->
    <div v-if="showAddModal" class="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
      <div class="bg-[#1a1a1a] border border-amber-900/50 rounded-xl p-6 w-full max-w-md">
        <h2 class="text-xl font-bold text-amber-300 mb-4">
          {{ editingAccount ? 'Chỉnh sửa Account' : 'Thêm Account Game Mới' }}
        </h2>

        <div class="space-y-4 mb-6">
          <div>
            <label class="block text-sm text-amber-200 mb-1">Username</label>
            <input
              v-model="formData.username"
              type="text"
              class="w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
            />
          </div>

          <div>
            <label class="block text-sm text-amber-200 mb-1">Email</label>
            <input
              v-model="formData.email"
              type="email"
              class="w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
            />
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm text-amber-200 mb-1">Balance</label>
              <input
                v-model.number="formData.balance"
                type="number"
                min="0"
                max="999999"
                class="w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
              />
            </div>

            <div>
              <label class="block text-sm text-amber-200 mb-1">Server</label>
              <select
                v-model="formData.server"
                class="w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
              >
                <option value="Server 1">Server 1</option>
                <option value="Server 2">Server 2</option>
                <option value="Server 3">Server 3</option>
              </select>
            </div>
          </div>

          <div>
            <label class="block text-sm text-amber-200 mb-1">Trạng thái</label>
            <select
              v-model="formData.status"
              class="w-full px-3 py-2 rounded bg-[#0a0a0a] border border-amber-900/50 text-amber-100 focus:outline-none focus:border-amber-600"
            >
              <option value="active">Hoạt động</option>
              <option value="locked">Bị khóa</option>
              <option value="banned">Bị cấm</option>
            </select>
          </div>
        </div>

        <div class="flex gap-3">
          <button
            @click="closeModal"
            class="flex-1 px-4 py-2 rounded border border-amber-900/50 text-amber-200 hover:bg-amber-900/10 transition-all"
          >
            Hủy
          </button>
          <button
            @click="saveAccount"
            class="flex-1 px-4 py-2 rounded bg-amber-600 text-black font-semibold hover:bg-amber-500 transition-all"
          >
            {{ editingAccount ? 'Cập nhật' : 'Tạo' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import AdminSidebar from './AdminSidebar.vue';

const router = useRouter();
const activeLi = ref(2);

const handleNavigation = (item, index) => {
  activeLi.value = index;
  router.push(item.route);
};

// Account data
const accounts = ref([
  { id: 1, username: 'player_123', email: 'player1@gmail.com', balance: 4500, server: 'Server 1', status: 'active', createdDate: '2024-12-15' },
  { id: 2, username: 'warrior_king', email: 'warrior@gmail.com', balance: 6000, server: 'Server 2', status: 'active', createdDate: '2024-12-10' },
  { id: 3, username: 'mage_master', email: 'mage@gmail.com', balance: 3800, server: 'Server 1', status: 'locked', createdDate: '2024-12-08' },
  { id: 4, username: 'rogue_shadow', email: 'rogue@gmail.com', balance: 5200, server: 'Server 3', status: 'active', createdDate: '2024-12-05' },
  { id: 5, username: 'paladin_light', email: 'paladin@gmail.com', balance: 4000, server: 'Server 2', status: 'banned', createdDate: '2024-12-01' },
  { id: 6, username: 'archer_swift', email: 'archer@gmail.com', balance: 3500, server: 'Server 1', status: 'active', createdDate: '2024-11-28' },
  { id: 7, username: 'dragon_slayer', email: 'dragon@gmail.com', balance: 7000, server: 'Server 3', status: 'active', createdDate: '2024-11-25' },
  { id: 8, username: 'mystic_void', email: 'mystic@gmail.com', balance: 4800, server: 'Server 2', status: 'active', createdDate: '2024-11-20' },
]);

// Search and filter
const searchQuery = ref('');
const filterStatus = ref('');
const currentPage = ref(1);
const pageSize = ref(5);

// Modal
const showAddModal = ref(false);
const editingAccount = ref(null);
const formData = ref({
  username: '',
  email: '',
  balance: 0,
  server: 'Server 1',
  status: 'active'
});

// Computed properties
const filteredAccounts = computed(() => {
  let filtered = accounts.value.filter(acc => {
    const matchSearch = !searchQuery.value || 
      acc.username.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      acc.email.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchStatus = !filterStatus.value || acc.status === filterStatus.value;
    return matchSearch && matchStatus;
  });
  return filtered;
});

const paginatedAccounts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredAccounts.value.slice(start, start + pageSize.value);
});

const totalPages = computed(() => {
  return Math.max(1, Math.ceil(filteredAccounts.value.length / pageSize.value));
});

// Methods
const editAccount = (account) => {
  editingAccount.value = account;
  formData.value = {
    username: account.username,
    email: account.email,
    balance: account.balance,
    server: account.server,
    status: account.status
  };
  showAddModal.value = true;
};

const saveAccount = () => {
  if (!formData.value.username || !formData.value.email) {
    alert('Vui lòng điền đầy đủ thông tin');
    return;
  }

  if (editingAccount.value) {
    // Update existing account
    Object.assign(editingAccount.value, formData.value);
  } else {
    // Add new account
    const newId = Math.max(...accounts.value.map(a => a.id), 0) + 1;
    accounts.value.push({
      id: newId,
      ...formData.value,
      createdDate: new Date().toISOString().split('T')[0]
    });
  }

  closeModal();
};

const closeModal = () => {
  showAddModal.value = false;
  editingAccount.value = null;
  formData.value = {
    username: '',
    email: '',
    balance: 0,
    server: 'Server 1',
    status: 'active'
  };
};

const deleteAccount = (id) => {
  if (confirm('Bạn có chắc muốn xóa account này?')) {
    accounts.value = accounts.value.filter(acc => acc.id !== id);
  }
};

const toggleLock = (account) => {
  account.status = account.status === 'active' ? 'locked' : 'active';
};

const previousPage = () => {
  if (currentPage.value > 1) currentPage.value -= 1;
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) currentPage.value += 1;
};

// Watch for page changes
import { watch } from 'vue';
watch(() => searchQuery.value, () => {
  currentPage.value = 1;
});
</script>

<style scoped>
</style>
