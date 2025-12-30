<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <!-- Sidebar (reusable) -->
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <!-- Main Content -->
    <div class="flex-1 p-6">
      <h1 class="text-2xl font-bold mb-6 text-amber-300">⚙️ Valorant Account Management</h1>

      <div class="rounded-xl border border-amber-900/50 p-6 bg-[#141414]">
        <p class="text-amber-200">Valorant account management page content goes here.</p>
        <p class="text-amber-200/60 mt-4">This is where you can manage Valorant accounts.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import AdminSidebar from './AdminSidebar.vue';

const router = useRouter();
const activeLi = ref(4);

const handleNavigation = (item, index) => {
  activeLi.value = index;
  router.push(item.route);
};
</script>

<style scoped>
</style>
