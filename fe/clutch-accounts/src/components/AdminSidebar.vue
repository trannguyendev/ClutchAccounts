<template>
  <aside class="w-64 border-r-2 border-amber-900/60">
    <div class="p-6 flex items-center justify-between gap-4">
      <div class="text-lg font-semibold text-amber-400">
        {{ t('admin.adminDashboard') }}
      </div>
      <div class="w-12 h-12 rounded-lg bg-amber-900/40 border border-amber-600/50 flex items-center justify-center flex-shrink-0 overflow-hidden cursor-pointer hover:bg-amber-900/60 transition-all duration-300" @click="goToMainPage">
        <img src="../img/Logo.png" alt="Logo" class="w-full h-full object-cover"/>
      </div>
    </div>

    <div class="px-4 space-y-4">
      <div
        v-for="(item, index) in sidebarItems"
        :key="index"
        @click="$emit('navigate', item, index)"
        :class="[
          'px-3 py-3 rounded-lg cursor-pointer transition-all duration-300 flex items-center gap-3',
          activeLi === index 
            ? 'bg-amber-900/30 font-medium border border-amber-600/50 text-amber-300' 
            : 'hover:bg-amber-900/20 hover:border hover:border-amber-600/30 text-amber-200 hover:text-amber-300'
        ]"
      >
        <span v-if="item.icon.includes('<')" v-html="item.icon" class="text-xl"></span>
        <span>{{ item.label }}</span>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
const router = useRouter();
const { t } = useI18n();
defineProps({
  activeLi: {
    type: Number,
    default: 0
  }
});

defineEmits(['navigate']);

const sidebarItems = [
  { label: t('admin.dashboard'), icon: '<i class="fa-solid fa-chart-line"></i>', route: '/admin' },
  { label: t('admin.transaction'), icon: '<i class="fa-solid fa-money-bill-transfer"></i>', route: '/admin/transaction' },
  { label: t('admin.accountManagement'), icon: '<i class="fa-solid fa-people-roof"></i>', route: '/admin/account' },
  { label: t('admin.news'), icon: '<i class="fa-solid fa-newspaper"></i>', route: '/admin/news' },
  { label: t('admin.valorantAccountManagement'), icon: '<i class="fa-solid fa-gamepad"></i>', route: '/admin/vlr_account' }
];

const goToMainPage = () => {
  router.push('/main');
}
</script>

<style scoped>
</style>
