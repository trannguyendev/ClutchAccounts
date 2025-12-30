<template>
  <aside class="w-64 border-r-2 border-amber-900/60">
    <div class="p-6 flex items-center justify-between gap-4">
      <div class="text-lg font-semibold text-amber-400">
        Gemini Admin
      </div>
      <div class="w-12 h-12 rounded-lg bg-amber-900/40 border border-amber-600/50 flex items-center justify-center flex-shrink-0 overflow-hidden">
        <img src="../img/Logo.png" alt="Logo" class="w-full h-full object-cover" />
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
        <span class="text-xl">{{ item.icon }}</span>
        <span>{{ item.name }}</span>
      </div>
    </div>
  </aside>
</template>

<script setup>
defineProps({
  activeLi: {
    type: Number,
    default: 0
  }
});

defineEmits(['navigate']);

const sidebarItems = [
  { name: 'Dashboard', icon: '📊', route: '/admin' },
  { name: 'Transaction', icon: '💳', route: '/admin/transaction' },
  { name: 'Account Management', icon: '🌐', route: '/admin/account' },
  { name: 'News', icon: '📰', route: '/admin/news' },
  { name: 'Valorant Account Management', icon: '⚙️', route: '/admin/vlr_account' }
];
</script>

<style scoped>
</style>
