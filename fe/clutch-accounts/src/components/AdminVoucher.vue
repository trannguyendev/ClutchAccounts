<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <div class="flex-1 p-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-extrabold text-amber-300">{{ t('admin.voucherPage.title') }}</h1>
          <p class="text-sm text-amber-400/70">{{ t('admin.voucherPage.subtitle') }}</p>
        </div>
        <div class="flex items-center gap-3">
          <input v-model="search" :placeholder="t('admin.voucherPage.searchPlaceholder')" class="bg-[#0f0f0f] border border-amber-900/40 px-3 py-2 rounded-md text-amber-200 placeholder:amber-600/40" />
          <button @click="openCreate" class="bg-amber-500 text-black px-4 py-2 rounded-md shadow">+ {{ t('admin.voucherPage.create') }}</button>
        </div>
      </div>

      <!-- runtime error banner: prevents white-screen and offers retry -->
      <div v-if="runtimeError" class="mb-4 rounded-lg border border-red-700/40 bg-red-900/10 p-3 text-red-200 flex items-start justify-between gap-4">
        <div class="text-sm">{{ t('admin.voucherPage.loadError') || 'Lỗi khi tải voucher:' }} <span class="font-mono">{{ runtimeError }}</span></div>
        <div class="flex items-center gap-2">
          <button @click="fetchAll" class="px-3 py-1 bg-amber-500 text-black rounded">{{ t('common.retry') || 'Retry' }}</button>
          <button @click="() => { runtimeError = null }" class="px-3 py-1 border rounded text-amber-200">{{ t('common.dismiss') || 'Dismiss' }}</button>
        </div>
      </div>

      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#0E0E0E] shadow-inner">
        <div class="flex items-center justify-between mb-4">
          <div class="text-amber-300/70">{{ t('admin.voucherPage.showing', {count: filtered.length}) }}</div>
          <div class="text-sm text-amber-400/60">API: <span class="font-mono text-amber-300">{{ apiHealthy ? t('admin.voucherPage.apiOk') : t('admin.voucherPage.apiDown') }}</span></div>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full text-left text-amber-100">
            <thead>
              <tr class="border-b border-amber-900/50 text-amber-400/70 text-sm">
                <th class="px-3 py-2">#</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.code') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.discount') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.max') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.used') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.expires') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.active') }}</th>
                <th class="px-3 py-2">{{ t('admin.voucherPage.table.actions') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(v, idx) in paged" :key="v.voucher_id || v.id" class="border-b border-amber-900/20 transition-transform duration-300 transform-gpu hover:scale-[1.01] hover:shadow-xl hover:shadow-amber-900/30">
                <td class="px-3 py-3 text-amber-300">{{ startIndex + idx + 1 }}</td>

                <td class="px-3 py-3 font-mono text-amber-200 flex items-center gap-3">
                  <div class="truncate max-w-[10rem]">{{ v.voucher_code }}</div>
                  <button @click.stop="copyCode(v)" title="Copy code" class="ml-2 px-2 py-1 bg-amber-400 text-black rounded text-xs">Copy</button>
                </td>

                <td class="px-3 py-3">{{ v.discount_percent ?? v.discount ?? 0 }}%</td>
                <td class="px-3 py-3">{{ v.max_usage ?? v.maxUsage ?? '-' }}</td>
                <td class="px-3 py-3">{{ v.used_count ?? v.usedCount ?? 0 }}</td>

                <td class="px-3 py-3">
                  <div class="flex items-center gap-2">
                    <div>{{ formatDate(v.expire_at || v.expireAt) }}</div>
                    <div v-if="isExpiringSoon(v)" class="text-xs bg-red-600/80 text-black px-2 py-0.5 rounded">{{ t('admin.voucherPage.expiringSoon') || 'Expiring' }}</div>
                  </div>
                </td>

                <td class="px-3 py-3">
                  <label class="inline-flex items-center gap-2 cursor-pointer">
                    <input type="checkbox" :checked="(v.isActive || v.is_active)" @change="toggleActive(v)" class="accent-amber-500 w-4 h-4" />
                    <span class="text-amber-200 text-sm">{{ (v.isActive || v.is_active) ? t('common.yes') : t('common.no') }}</span>
                  </label>
                </td>

                <td class="px-3 py-3 space-x-2 flex items-center">
                  <div class="ml-auto flex items-center gap-2">
                    <button @click="openEdit(v)" class="px-2 py-1 bg-amber-400 text-black rounded text-sm">{{ t('admin.voucherPage.edit') }}</button>
                    <button @click="confirmDelete(v)" class="px-2 py-1 bg-red-600 rounded text-sm">{{ t('admin.voucherPage.delete') }}</button>
                  </div>
                </td>
              </tr>

              <tr v-if="!filtered.length">
                <td colspan="8" class="px-3 py-6 text-amber-400/60 text-center">{{ t('admin.voucherPage.empty') }}</td>
              </tr>
            </tbody>
          </table>
        </div>

      </div>

      <!-- modal -->
      <transition name="fade-scale">
        <div v-if="modalOpen" class="fixed inset-0 z-40 flex items-center justify-center bg-black/60" @click.self="closeModal" @keydown.window.esc="closeModal" tabindex="-1">
          <div class="w-11/12 md:w-2/5 bg-[#0b0b0b] border border-amber-900/40 rounded-xl p-5" role="dialog" aria-modal="true">
            <div class="flex items-center justify-between">
              <div>
                <h3 class="text-lg font-bold text-amber-200">{{ editing.id ? t('admin.voucherPage.editTitle') : t('admin.voucherPage.createTitle') }}</h3>
                <p class="text-sm text-amber-400/60">{{ t('admin.voucherPage.createHint') }}</p>
              </div>
              <div class="text-amber-400/50">ID: <span class="font-mono">{{ editing.id || '-' }}</span></div>
            </div>

            <div class="mt-4 space-y-3 text-amber-100">
              <div>
                <label class="text-amber-300 text-sm">{{ t('admin.voucherPage.form.code') }}</label>
                <input v-model="form.voucher_code" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />
              </div>

              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="text-amber-300 text-sm">{{ t('admin.voucherPage.form.discount') }}</label>
                  <input type="number" v-model.number="form.discount_percent" min="0" max="100" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />
                </div>
                <div>
                  <label class="text-amber-300 text-sm">{{ t('admin.voucherPage.form.max') }}</label>
                  <input type="number" v-model.number="form.max_usage" min="1" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />
                </div>
              </div>

              <div>
                <label class="text-amber-300 text-sm">Max Discount</label>
                <p class="text-xs text-amber-400/60 mt-1">default 0 is unlimited</p>
                <input type="number" v-model.number="form.max_discount" min="0" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />
              </div>

              <div>
                <label class="text-amber-300 text-sm">{{ t('admin.voucherPage.form.expires') }}</label>
                <input type="datetime-local" v-model="form.expire_at_local" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />
              </div>

              <div class="flex items-center gap-3">
                <label class="flex items-center gap-2"><input type="checkbox" v-model="form.isActive" class="accent-amber-500"/> <span class="text-amber-300">{{ t('admin.voucherPage.form.active') }}</span></label>
                <div class="ml-auto text-sm text-amber-400/60">{{ t('admin.voucherPage.form.usedHint') }}: <span class="font-mono">{{ form.used_count ?? 0 }}</span></div>
              </div>

              <div class="flex items-center justify-end gap-3">
                <button @click="save" class="bg-amber-500 text-black px-4 py-2 rounded">{{ t('admin.voucherPage.form.save') }}</button>
                <button @click="closeModal" class="px-4 py-2 border rounded">{{ t('common.cancel') }}</button>
              </div>
            </div>

          </div>
        </div>
      </transition>

      <!-- toasts -->
      <div class="fixed right-6 bottom-6 z-50 flex flex-col gap-2 pointer-events-none">
        <div v-for="s in toasts" :key="s.id" class="pointer-events-auto px-4 py-2 rounded shadow-md" :class="s.type==='ok' ? 'bg-emerald-500 text-black' : 'bg-amber-800 text-amber-100'">{{ s.msg }}</div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, onErrorCaptured } from 'vue';
import { useI18n } from 'vue-i18n';
import axios from 'axios';
import AdminSidebar from './AdminSidebar.vue';
import { useRouter, useRoute } from 'vue-router';
import { useUserStore } from '../stores/user';


const { t } = useI18n();
const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

const routeToIndex = {
  '/admin': 0,
  '/admin/transaction': 1,
  '/admin/account': 2,
  '/admin/news': 3,
  '/admin/vlr_account': 4,
  '/admin/faq': 5,
  '/admin/voucher': 6
};

const activeLi = ref(routeToIndex[route.path] ?? 6);
function syncActive() { activeLi.value = routeToIndex[route.path] ?? 6; }

onMounted(syncActive);
watch(() => route.path, syncActive);

const handleNavigation = async (item, index) => {
  try {
    await router.push(item.route);
    activeLi.value = index;
  } catch (e) {
    /* navigation aborted */
  }
};

const localKey = 'admin.voucher.cache.v1';

const items = ref([]);
const loading = ref(false);
const apiHealthy = ref(true);
const modalOpen = ref(false);
const editing = reactive({ id: null });
const previewItem = reactive({});
const runtimeError = ref(null);

const form = reactive({ voucher_code: '', discount_percent: 0, max_usage: 1, max_discount: 0, used_count: 0, expire_at: null, expire_at_local: '', isActive: true });
const search = ref('');

const startIndex = computed(() => (page.value - 1) * perPage.value);
const page = ref(1);
const perPage = ref(10);

const normalize = (r) => ({
  voucher_id: r.voucher_id ?? r.id,
  voucher_code: r.voucher_code ?? r.code ?? r.voucherCode ?? '',
  discount_percent: r.discount_percent ?? r.discount ?? 0,
  max_usage: r.max_usage ?? r.maxUsage ?? null,
  used_count: r.used_count ?? r.usedCount ?? 0,
  expire_at: r.expire_at ?? r.expireAt ?? null,
  isActive: r.isActive ?? r.is_active ?? false,
  ...r
});

const filtered = computed(() => {
  const q = (search.value || '').trim().toLowerCase();
  return items.value.filter(i => !q || (i.voucher_code || '').toLowerCase().includes(q));
});
const totalPages = computed(() => Math.max(1, Math.ceil(filtered.value.length / perPage.value)));
const paged = computed(() => filtered.value.slice(startIndex.value, startIndex.value + perPage.value));

function toLocalDatetime(iso) {
  if (!iso) return '';
  const d = new Date(iso);
  const off = d.getTimezoneOffset();
  const local = new Date(d.getTime() - off * 60000);
  return local.toISOString().slice(0,16);
}

function fromLocalDatetime(local) {
  if (!local) return null;
  const iso = new Date(local);
  return iso.toISOString().slice(0, -1);
}

async function fetchAll() {
  loading.value = true;
  runtimeError.value = null
    axios.get("/api/vouchers/getAllVouchers")
    .then((res) => {
      if (Array.isArray(res.data)) {
      items.value = res.data.map(normalize);
      apiHealthy.value = true;
    } else {
      throw new Error('unexpected payload');
    }})
   .catch(err => {
    console.error('fetchAll error', err);
    runtimeError.value = (err && err.message) ? err.message : 'Failed to load vouchers';
    apiHealthy.value = false;
   }) 
   .finally(() => {
    loading.value = false;
   })
}

onMounted(async () => {
  try {
    // Ensure user is initialized and has auth token
    await new Promise(resolve => {
      if (userStore.token) {
        resolve();
      } else {
        const unwatch = watch(() => userStore.token, () => {
          unwatch();
          resolve();
        });
        // Timeout after 2 seconds
        setTimeout(resolve, 2000);
      }
    });
    await fetchAll();
  } catch (e) {
    console.error('onMounted error', e);
    runtimeError.value = e?.message || String(e) || 'Unexpected error';
  }
});

onErrorCaptured((err) => {
  console.error('AdminVoucher captured error', err);
  runtimeError.value = err?.message || String(err) || 'Unexpected runtime error';
  // return false to stop propagation to global handler (we handled it)
  return false;
});

function openCreate() {
  editing.id = null;
  form.voucher_code = '';
  form.discount_percent = 0;
  form.max_usage = 1;
  form.max_discount = 0;
  form.used_count = 0;
  form.expire_at = null;
  form.expire_at_local = '';
  form.isActive = true;
  modalOpen.value = true;
}

function openEdit(v) {
  editing.id = v.voucher_id ?? v.id;
  form.voucher_code = v.voucher_code || v.code || '';
  form.discount_percent = v.discount_percent ?? v.discount ?? 0;
  form.max_usage = v.max_usage ?? v.maxUsage ?? null;
  form.max_discount = v.max_discount ?? v.maxDiscount ?? 0;
  form.used_count = v.used_count ?? v.usedCount ?? 0;
  form.expire_at = v.expire_at ?? v.expireAt ?? null;
  form.expire_at_local = toLocalDatetime(form.expire_at);
  form.isActive = !!(v.isActive ?? v.is_active);
  modalOpen.value = true;
}

function validateForm() {
  if (!form.voucher_code || !form.voucher_code.trim()) { alert(t('admin.voucherPage.errors.code')); return false; }
  if (form.discount_percent == null || form.discount_percent < 0 || form.discount_percent > 100) { alert(t('admin.voucherPage.errors.discount')); return false; }
  if (form.max_usage != null && form.max_usage < 1) { alert(t('admin.voucherPage.errors.max')); return false; }
  return true;
}

async function save() {
  if (!validateForm()) return;
  const payload = {
    voucher_code: form.voucher_code.trim(),
    discount_percent: Number(form.discount_percent) || 0,
    max_usage: form.max_usage ? Number(form.max_usage) : null,
    expired_at: fromLocalDatetime(form.expire_at_local),
    isActive: !!form.isActive,
    max_discount: Number(form.max_discount) || 0
  };

  try {
    if (editing.id) {
      await axios.put('/api/vouchers/updateVoucher/'+editing.id, payload);
      const idx = items.value.findIndex(x => (x.voucher_id || x.id) === editing.id);
      if (idx !== -1) items.value[idx] = normalize({ ...items.value[idx], ...payload, updated_at: new Date().toISOString() });
    } else {
      console.log(payload)
      const res = await axios.post('/api/vouchers/addVoucher', payload);
      const created = normalize(res?.data || { voucher_id: Date.now(), ...payload, used_count: 0, expire_at: payload.expire_at });
      items.value.unshift(created);
    }
    modalOpen.value = false;
    apiHealthy.value = true;
  } catch (e) {
    console.error(e);
    apiHealthy.value = false;
    if (!editing.id) {
      const fake = normalize({ voucher_id: Date.now(), ...payload, used_count: 0, expire_at: payload.expire_at });
      items.value.unshift(fake);
      modalOpen.value = false;
      alert(t('admin.voucherPage.msg.savedLocal'));
    } else {
      alert(t('admin.voucherPage.msg.updateFail'));
    }
  }
}

const toasts = ref([]);
function toast(msg, type = 'info'){
  const id = Date.now() + Math.random();
  toasts.value.push({ id, msg, type });
  setTimeout(() => { toasts.value = toasts.value.filter(x => x.id !== id); }, 3000);
}

async function confirmDelete(v) {
  if (!confirm(t('admin.voucherPage.confirmDelete'))) return;
  await remove(v.voucher_id || v.id);
}
async function remove(id) {
  try {
    await axios.delete('/api/vouchers/deleteVoucher/'+id);
    items.value = items.value.filter(x => (x.voucher_id || x.id) !== id);
    toast(t('admin.voucherPage.msg.deleted') || 'Đã xóa', 'ok');
  } catch (e) {
    console.warn(e);
    // optimistic fallback: remove locally
    items.value = items.value.filter(x => (x.voucher_id || x.id) !== id);
    toast(t('admin.voucherPage.msg.deletedLocal') || 'Xóa cục bộ (offline)', 'info');
  }
}

async function toggleActive(v){
  const id = v.voucher_id || v.id;
  const prev = v.isActive ?? v.is_active;
  // optimistic UI
  v.isActive = !prev;
  try {
    await axios.put(`/api/vouchers/updateVoucher/${id}`, { isActive: v.isActive });
    toast(t('admin.voucherPage.msg.updated') || 'Cập nhật thành công', 'ok');
  } catch (e) {
    v.isActive = prev; // revert
    toast(t('admin.voucherPage.msg.updateFail') || 'Cập nhật thất bại', 'err');
  }
}

function copyCode(v){
  const code = v.voucher_code || v.code || '';
  navigator.clipboard?.writeText(code).then(() => {
    toast(t('admin.voucherPage.msg.copied') || 'Mã đã sao chép', 'ok');
  }).catch(() => {
    toast(t('admin.voucherPage.msg.copyFail') || 'Không thể sao chép', 'err');
  });
}

function isExpiringSoon(v){
  const d = v.expire_at || v.expireAt;
  if (!d) return false;
  const ms = new Date(d) - new Date();
  return ms > 0 && ms < 1000 * 60 * 60 * 24 * 2;
}

function formatDate(d) { if (!d) return '-'; try { return new Date(d).toLocaleString(); } catch (e) { return d; } }

function prevPage() { if (page.value > 1) page.value--; }
function nextPage() { if (page.value < totalPages.value) page.value++; }

function closeModal() { modalOpen.value = false; }
</script>

<style scoped>
.fade-scale-enter-active, .fade-scale-leave-active { transition: all 220ms cubic-bezier(.2,.9,.2,1); }
.fade-scale-enter-from { opacity: 0; transform: translateY(6px) scale(.995); }
.fade-scale-enter-to { opacity: 1; transform: translateY(0) scale(1); }
.fade-scale-leave-to { opacity: 0; transform: translateY(6px) scale(.995); }

.prose a { color: #FFD166; }
</style>