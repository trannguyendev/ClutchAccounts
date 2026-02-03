<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <div class="flex-1 p-6">
      <div class="flex items-center justify-between mb-6">
        <div class="flex items-center space-x-3">
          <!-- Valorant-inspired emblem (generic) -->
          <div class="w-12 h-12 flex items-center justify-center rounded-lg bg-gradient-to-br from-amber-800/10 to-amber-800/5 border border-amber-900/30 shadow-md">
            <svg class="w-7 h-7 text-amber-400" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden>
              <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-opacity="0.15" stroke-width="1.5" />
              <path d="M12 6v12M6 12h12" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" />
              <path d="M4.6 4.6L9 9" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round" opacity="0.6"/>
            </svg>
          </div>
          <div>
            <h1 class="text-2xl font-extrabold text-amber-300">{{ t('admin.faqPage.pageTitle') }}</h1>
            <p class="text-sm text-amber-400/70">{{ t('admin.faqPage.pageSubtitle') }}</p>
          </div>
        </div>

        <div class="flex items-center space-x-3">
          <input v-model="search" :placeholder="t('admin.faqPage.searchPlaceholder')" class="bg-[#0f0f0f] border border-amber-900/40 px-3 py-2 rounded-md text-amber-200 placeholder:amber-600/40 shadow-sm transition-all focus:outline-none focus:ring-2 focus:ring-amber-600/30" />
          <button @click="openCreate" class="bg-amber-500 text-black px-4 py-2 rounded-md shadow hover:scale-[1.01] transition-transform">{{ t('admin.faqPage.createButton') }}</button>
        </div>
      </div>

      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#0E0E0E] shadow-inner">
        <div class="flex items-center justify-between mb-4">
          <div class="text-amber-300/70">{{ t('admin.faqPage.showingCount', { count: filtered.length }) }}</div>
          <div class="text-sm text-amber-400/60">API: <span class="font-mono text-amber-300">{{ apiHealthy ? t('admin.faqPage.apiStatusOk') : t('admin.faqPage.apiStatusUnreachable') }}</span></div>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full text-left text-amber-100 border-separate" style="border-spacing: 0 8px;">
            <thead>
              <tr class="text-amber-400/70 text-sm">
                <th class="px-3 py-2">{{ t('admin.faqPage.table.number') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.title') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.content') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.createdAt') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.updatedAt') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.likes') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.dislikes') }}</th>
                <th class="px-3 py-2">{{ t('admin.faqPage.table.actions') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(f, idx) in paged" :key="f.id" class="bg-gradient-to-r from-[#0b0b0b] to-[#0f0f0f] border border-amber-900/20 rounded-xl align-top transition-transform hover:scale-[1.005]">
                <td class="px-3 py-3 align-middle w-12 text-amber-300">{{ startIndex + idx + 1 }}</td>

                <td class="px-3 py-3 align-middle max-w-xs break-words">
                  <div class="flex items-start gap-3">
                    <div class="w-10 h-10 flex items-center justify-center rounded-md bg-amber-900/10 border border-amber-900/30 text-amber-300/90">T</div>
                    <div>
                      <div class="font-semibold text-amber-100">{{ f.title }}</div>
                      <div class="text-xs text-amber-400/60 mt-1">{{ t('admin.faqPage.idLabel') }} <span class="font-mono">{{ f.id }}</span></div>
                    </div>
                  </div>
                </td>

                <td class="px-3 py-3 align-middle text-amber-300/70 max-w-[36ch] truncate">{{ f.content }}</td>

                <td class="px-3 py-3 align-middle text-amber-300/60">{{ formatDate(f.created_at) }}</td>
                <td class="px-3 py-3 align-middle text-amber-300/60">{{ formatDate(f.updated_at) }}</td>

                <td class="px-3 py-3 align-middle text-amber-300/80 font-mono">{{ f.like_amount || 0 }}</td>
                <td class="px-3 py-3 align-middle text-amber-300/80 font-mono">{{ f.dislike_amount || 0 }}</td>

                <td class="px-3 py-3 align-middle text-sm">
                  <div class="flex items-center gap-2">
                    <button @click="openEdit(f)" class="px-3 py-1 bg-amber-400 text-black rounded shadow-sm">{{ t('admin.faqPage.edit') }}</button>
                    <button @click="confirmDelete(f)" class="px-3 py-1 bg-red-600 rounded shadow-sm">{{ t('admin.faqPage.delete') }}</button>
                    <button @click="moveUp(f)" :disabled="isFirst(f)" class="px-2 py-1 bg-amber-800/10 border border-amber-900/30 rounded disabled:opacity-40">{{ t('admin.faqPage.moveUpShort') }}</button>
                    <button @click="moveDown(f)" :disabled="isLast(f)" class="px-2 py-1 bg-amber-800/10 border border-amber-900/30 rounded disabled:opacity-40">{{ t('admin.faqPage.moveDownShort') }}</button>
                    <button @click="preview(f)" class="px-2 py-1 bg-amber-700/10 border border-amber-900/30 rounded">{{ t('admin.faqPage.previewShort') }}</button>
                  </div>
                </td>
              </tr>

              <tr v-if="!filtered.length">
                <td colspan="8" class="px-3 py-6 text-amber-400/60 text-center">{{ t('admin.faqPage.emptyState') }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- pagination -->
        <div class="flex items-center justify-between mt-4 text-amber-400/70">
          <div>{{ t('admin.faqPage.pagination.pageLabel') }} <strong class="text-amber-200">{{ page }}</strong> / {{ totalPages }}</div>
          <div class="flex items-center gap-2">
            <button @click="prevPage" :disabled="page===1" class="px-3 py-1 border rounded disabled:opacity-40">{{ t('admin.faqPage.pagination.prev') }}</button>
            <button @click="nextPage" :disabled="page===totalPages" class="px-3 py-1 border rounded disabled:opacity-40">{{ t('admin.faqPage.pagination.next') }}</button>
          </div>
        </div>
      </div>

      <!-- Modal: create / edit -->
      <transition name="fade-scale">
        <div v-if="modalOpen" class="fixed inset-0 z-40 flex items-center justify-center bg-black/60">
          <div class="w-11/12 md:w-3/5 bg-[#0b0b0b] border border-amber-900/40 rounded-xl p-5 shadow-2xl transform-gpu">
            <div class="flex items-start justify-between gap-4">
              <div>
                <h3 class="text-lg font-bold text-amber-200">{{ editing.id ? t('admin.faqPage.modal.editTitle') : t('admin.faqPage.modal.createTitle') }}</h3>
                <p class="text-sm text-amber-400/60">{{ t('admin.faqPage.modal.description') }}</p>
              </div>
              <div class="text-amber-400/50">{{ t('admin.faqPage.idLabel') }} <span class="font-mono text-amber-300">{{ editing.id || '-' }}</span></div>
            </div>

            <div class="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4 text-amber-100">
              <div>
                <label class="text-amber-300 text-sm">{{ t('admin.faqPage.labels.title') }}</label>
                <input v-model="form.title" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded" />

                <div v-if="editing.id" class="mt-3 text-sm text-amber-400/60">
                  <div>{{ t('admin.faqPage.labels.createdAt') }}: <span class="font-mono">{{ formatDate(form.created_at) }}</span></div>
                  <div class="mt-1">{{ t('admin.faqPage.labels.updatedAt') }}: <span class="font-mono">{{ formatDate(form.updated_at) }}</span></div>
                </div>

                <div class="mt-4 flex items-center gap-2">
                  <button @click="save" class="bg-amber-500 text-black px-4 py-2 rounded">{{ t('admin.faqPage.labels.save') }}</button>
                  <button @click="closeModal" class="px-4 py-2 border rounded">{{ t('common.cancel') }}</button>
                </div>
              </div>

              <div>
                <label class="text-amber-300 text-sm">{{ t('admin.faqPage.labels.content') }}</label>
                <textarea v-model="form.content" rows="8" class="w-full mt-2 bg-[#121212] border border-amber-900/40 px-3 py-2 rounded font-sans"></textarea>

                <div class="mt-3 text-sm text-amber-400/60">{{ t('admin.faqPage.labels.preview') }}</div>
                <div class="mt-2 p-3 bg-gradient-to-b from-[#0b0b0b] to-[#0d0d0d] border border-amber-900/20 rounded min-h-[6rem] text-amber-100 prose prose-invert max-h-40 overflow-auto">
                  <div v-html="previewHtml"></div>
                </div>
              </div>
            </div>

            <div v-if="apiHealthy === false" class="mt-3 text-sm text-amber-200/70">{{ t('admin.faqPage.apiOfflineWarning') }}</div>
          </div>
        </div>
      </transition>

      <!-- Preview drawer -->
      <transition name="slide-up-fade">
        <div v-if="drawerOpen" class="fixed right-6 bottom-6 z-50 w-96 bg-[#0b0b0b] border border-amber-900/40 rounded-xl p-4 shadow-2xl">
          <div class="flex items-start justify-between gap-3">
            <div>
              <div class="text-amber-300 font-semibold">{{ previewItem.title }}</div>
              <div class="text-xs text-amber-400/70 mt-1">{{ t('admin.faqPage.idLabel') }}: <span class="font-mono">{{ previewItem.id }}</span></div>
              <div class="text-xs text-amber-400/60 mt-1">{{ t('admin.faqPage.labels.createdAt') }}: <span class="font-mono">{{ formatDate(previewItem.created_at) }}</span></div>
            </div>
            <div>
              <button @click="drawerOpen = false" class="text-amber-400/60">✕</button>
            </div>
          </div>

          <div class="mt-3 text-amber-100 prose prose-invert max-h-64 overflow-auto" v-html="renderMarkdown(previewItem.content)"></div>
        </div>
      </transition>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import axios from 'axios';
import AdminSidebar from './AdminSidebar.vue';
import { useUserStore } from '@/stores/user';

import { useRouter, useRoute } from 'vue-router';
const user = useUserStore();
const router = useRouter();
const route = useRoute();

const routeToIndex = {
  '/admin': 0,
  '/admin/transaction': 1,
  '/admin/account': 2,
  '/admin/news': 3,
  '/admin/vlr_account': 4,
  '/admin/faq': 5,
  '/admin/voucher': 6
};

const activeLi = ref(routeToIndex[route.path] ?? 5);
const { t } = useI18n();

function syncActive() { activeLi.value = routeToIndex[route.path] ?? 5; }

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

const apiBase = '/api/faq';
const listKey = 'admin.faq.cache.v1';

const items = ref([]);
const loading = ref(false);
const apiHealthy = ref(true);
const modalOpen = ref(false);
const drawerOpen = ref(false);
const previewItem = reactive({});

const editing = reactive({ id: null });
const form = reactive({ title: '', content: '', like_amount: 0, dislike_amount: 0, created_at: null, updated_at: null });
const search = ref('');

// pagination
const page = ref(1);
const perPage = ref(8);
const startIndex = computed(() => (page.value - 1) * perPage.value);
const normalize = (i) => ({
  id: i.id,
  title: i.title || i.question || '',
  content: i.content || i.answer || '',
  like_amount: i.like_amount ?? i.likeAmount ?? 0,
  dislike_amount: i.dislike_amount ?? i.dislikeAmount ?? 0,
  created_at: i.created_at || i.createdAt || null,
  updated_at: i.updated_at || i.updatedAt || null,
  // keep other fields if present for future-proofing
  ...i
});

const filtered = computed(() => {
  const q = search.value.trim().toLowerCase();
  return items.value.filter(i => {
    if (!q) return true;
    return (i.title || '').toLowerCase().includes(q) || (i.content || '').toLowerCase().includes(q);
  });
});
const totalPages = computed(() => Math.max(1, Math.ceil(filtered.value.length / perPage.value)));
const paged = computed(() => filtered.value.slice(startIndex.value, startIndex.value + perPage.value));

const previewHtml = computed(() => renderMarkdown(form.content || ''));

function persistLocal() {
  try { localStorage.setItem(listKey, JSON.stringify(items.value || [])); } catch (e) { /* ignore */ }
}
function loadLocal() {
  try { const s = localStorage.getItem(listKey); if (s) items.value = JSON.parse(s).map(normalize); } catch (e) { /* ignore */ }
}

async function fetchAll() {
  loading.value = true;
  try {
    const res = await axios.get(apiBase);
    if (Array.isArray(res.data)) {
      items.value = res.data.map(normalize);
      apiHealthy.value = true;
      persistLocal();
    } else {
      throw new Error('unexpected payload');
    }
  } catch (err) {
    console.warn('FAQ fetch failed, using local cache', err);
    apiHealthy.value = false;
    loadLocal();
  } finally {
    loading.value = false;
  }
}

onMounted(() => { loadLocal(); fetchAll(); });

function openCreate() {
  editing.id = null;
  form.title = '';
  form.content = '';
  form.like_amount = 0;
  form.dislike_amount = 0;
  form.created_at = null;
  form.updated_at = null;
  modalOpen.value = true;
}

function openEdit(f) {
  editing.id = f.id;
  form.title = f.title || '';
  form.content = f.content || '';
  form.like_amount = f.like_amount || 0;
  form.dislike_amount = f.dislike_amount || 0;
  form.created_at = f.created_at || null;
  form.updated_at = f.updated_at || null;
  modalOpen.value = true;
}

async function save() {
  const payload = { title: form.title, content: form.content, like_amount: form.like_amount ?? 0, dislike_amount: form.dislike_amount ?? 0 };
  try {
    if (editing.id) {
      await axios.put(`${apiBase}/${editing.id}`, payload);
      const idx = items.value.findIndex(x => x.id === editing.id);
      if (idx !== -1) items.value[idx] = { ...items.value[idx], ...payload, updated_at: new Date().toISOString() };
    } else {
      const res = await axios.post(apiBase, payload);
      const created = normalize(res?.data || { id: Date.now(), ...payload, created_at: new Date().toISOString() });
      items.value.unshift(created);
    }

    persistLocal();
    modalOpen.value = false;
    apiHealthy.value = true;
  } catch (e) {
    console.error(e);
    apiHealthy.value = false;
    if (!editing.id) {
      const fake = normalize({ id: Date.now(), ...payload, created_at: new Date().toISOString() });
      items.value.unshift(fake);
      persistLocal();
      modalOpen.value = false;
      alert(t('admin.faqPage.localSaveAlert'));
    } else {
      alert(t('admin.faqPage.updateFailAlert'));
    }
  }
}

async function confirmDelete(f) {
  if (!confirm(t('admin.faqPage.confirmDelete'))) return;
  await remove(f.id);
}
async function remove(id) {
  try {
    await axios.delete(`${apiBase}/${id}`);
    items.value = items.value.filter(i => i.id !== id);
    persistLocal();
    apiHealthy.value = true;
  } catch (e) {
    console.warn('delete failed, removing locally', e);
    items.value = items.value.filter(i => i.id !== id);
    persistLocal();
    apiHealthy.value = false;
  }
}

function preview(f) {
  previewItem.id = f.id;
  previewItem.title = f.title;
  previewItem.content = f.content;
  previewItem.created_at = f.created_at;
  drawerOpen.value = true;
}

function renderMarkdown(md = '') {
  const esc = (s) => String(s)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
  const html = esc(md)
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\n{2,}/g, '</p><p>')
    .replace(/\n/g, '<br/>');
  return `<p>${html}</p>`;
}

function formatDate(d) {
  if (!d) return '-';
  try { return new Date(d).toLocaleString(); } catch (e) { return d; }
}

function isFirst(f) { const idx = items.value.findIndex(i => i.id === f.id); return idx <= 0; }
function isLast(f) { const idx = items.value.findIndex(i => i.id === f.id); return idx === items.value.length - 1; }

async function moveUp(f) {
  const idx = items.value.findIndex(i => i.id === f.id);
  if (idx > 0) { items.value.splice(idx, 1); items.value.splice(idx - 1, 0, f); await pushOrder(); }
}
async function moveDown(f) { const idx = items.value.findIndex(i => i.id === f.id); if (idx < items.value.length - 1) { items.value.splice(idx, 1); items.value.splice(idx + 1, 0, f); await pushOrder(); } }
async function pushOrder() { try { await axios.post(`${apiBase}/reorder`, { order: items.value.map(i => i.id) }); persistLocal(); apiHealthy.value = true; } catch (e) { console.warn('reorder failed — saved locally', e); persistLocal(); apiHealthy.value = false; } }

function closeModal() { modalOpen.value = false; }
</script>

<style scoped>
.fade-scale-enter-to { opacity: 1; transform: translateY(0) scale(1); }
.fade-scale-leave-to { opacity: 0; transform: translateY(6px) scale(.995); }

.slide-up-fade-enter-active { transition: all 260ms ease; }
.slide-up-fade-enter-from { opacity: 0; transform: translateY(8px); }
.slide-up-fade-enter-to { opacity: 1; transform: translateY(0); }

.prose a { color: #FFD166; }
</style>
