<template>
  <div class="p-6 bg-[#0A0A0A] min-h-screen text-amber-100">
    <div class="max-w-6xl mx-auto">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-2xl font-extrabold text-amber-300">{{ t('voucher.pageTitle') || 'Vouchers' }}</h1>
          <p class="text-sm text-amber-400/70">{{ t('voucher.pageSubtitle') || 'Voucher available for your account' }}</p>
        </div>
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        <VoucherCard v-for="v in items" :key="v.voucher_id || v.id" :voucher="v" @apply="applyVoucher" @copied="onCopied" />
      </div>

      <div v-if="!items.length" class="mt-8 text-center text-amber-400/60">{{ t('voucher.empty') || 'No vouchers found' }}</div>

      <!-- toast -->
      <div class="fixed right-6 bottom-6 z-50 flex flex-col gap-2">
        <div v-for="(s, idx) in toasts" :key="s.id" class="px-4 py-2 rounded shadow-md" :class="s.type==='ok' ? 'bg-emerald-500 text-black' : 'bg-amber-800 text-amber-100'">{{ s.msg }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import VoucherCard from '@/components/VoucherCard.vue';
import { useI18n } from 'vue-i18n';
const { t } = useI18n();

const items = ref([]);
const toasts = ref([]);

function toast(msg, type = 'info'){
  const id = Date.now() + Math.random();
  toasts.value.push({ id, msg, type });
  setTimeout(() => { toasts.value = toasts.value.filter(x => x.id !== id); }, 3000);
}

onMounted(async () => {
  try {
    const res = await axios.get('/api/voucher');
    items.value = Array.isArray(res.data) ? res.data : [];
  } catch (e) {
    console.warn(e);
    toast(t('voucher.fetchFail') || 'Không thể tải voucher — hiển thị cache');
  }
});

function onCopied(v){ toast((t('voucher.copied') || 'Mã đã được sao chép') + ` — ${v.voucher_code || v.code}` , 'ok'); }

async function applyVoucher(v){
  // optimistic UX: copy to clipboard and show feedback; real apply endpoint may vary
  try {
    await navigator.clipboard.writeText(v.voucher_code || v.code || '');
    toast(t('voucher.codeCopiedApply') || 'Mã đã sao chép — dán vào ô thanh toán', 'ok');
    // try backend apply if endpoint exists
    try { await axios.post('/api/voucher/apply', { code: v.voucher_code || v.code }); } catch (e) { /* ignore */ }
  } catch (e) {
    toast(t('voucher.copyFail') || 'Không thể sao chép mã', 'err');
  }
}
</script>

<style scoped>
</style>
