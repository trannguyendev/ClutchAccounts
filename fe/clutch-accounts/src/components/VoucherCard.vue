<template>
  <div class="voucher-card bg-[#0E0E0E] border border-amber-900/40 rounded-lg p-4 flex flex-col gap-3 transition-transform duration-300 hover:scale-105 hover:shadow-xl hover:shadow-amber-900/30 cursor-pointer" @click="onApply">
    <div class="flex items-start gap-3">
      <div class="w-14 h-14 rounded-md bg-amber-800/20 flex items-center justify-center text-amber-300 font-mono text-lg">%{{ voucher.discount_percent ?? voucher.discount ?? 0 }}</div>
      <div class="flex-1">
        <div class="flex items-center gap-2">
          <div class="font-semibold text-amber-200">{{ voucher.voucher_code || voucher.code }}</div>
          <div v-if="isExpiring" class="text-xs bg-red-600/80 text-black px-2 py-0.5 rounded-full ml-2">{{ expiringLabel }}</div>
        </div>
        <div class="text-sm text-amber-400/60 mt-1">{{ voucherDescription }}</div>
      </div>
      <div class="flex flex-col items-end gap-2">
        <button @click.stop="copy" class="px-3 py-1 bg-amber-400 text-black rounded text-sm shadow-sm">Copy</button>
        <button @click.stop="onApply" class="px-3 py-1 border border-amber-900/30 rounded text-sm text-amber-200 hover:bg-amber-900/20">Use</button>
      </div>
    </div>
    <div class="flex items-center justify-between text-xs text-amber-400/60">
      <div>Max: <span class="font-mono text-amber-300">{{ voucher.max_usage ?? '-' }}</span></div>
      <div>Expires: <span class="font-mono text-amber-300">{{ expireText }}</span></div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
const props = defineProps({ voucher: { type: Object, required: true } });
const emits = defineEmits(['apply', 'copied']);
const { t } = useI18n();

const voucherDescription = computed(() => {
  const p = props.voucher;
  return p.description || t('voucher.defaultDescription') || 'Áp dụng cho đơn hàng phù hợp';
});
const expireText = computed(() => {
  const d = props.voucher.expire_at || props.voucher.expireAt;
  return d ? new Date(d).toLocaleString() : '-';
});
const isExpiring = computed(() => {
  const d = props.voucher.expire_at || props.voucher.expireAt;
  if (!d) return false;
  const diff = new Date(d) - new Date();
  return diff > 0 && diff < 1000 * 60 * 60 * 24 * 2; // < 48h
});
const expiringLabel = computed(() => isExpiring.value ? t('voucher.expiringSoon') || 'Expiring' : '');

function copy() {
  navigator.clipboard?.writeText(props.voucher.voucher_code || props.voucher.code || '')
    .then(() => emits('copied', props.voucher))
    .catch(() => emits('copied', props.voucher));
}
function onApply() { emits('apply', props.voucher); }
</script>

<style scoped>
.voucher-card { will-change: transform; }
</style>
