<template>
  <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">
    <AdminSidebar :activeLi="activeLi" @navigate="handleNavigation" />

    <div class="flex-1 p-6">
      <h1 class="text-2xl font-bold mb-6 text-amber-300">💳 Transaction Management</h1>

      <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] overflow-auto">
        <table class="w-full text-left text-sm">
          <thead>
            <tr class="border-b border-amber-900/30 text-amber-300">
              <th class="pb-3 px-4">#</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="n in pageSize" :key="n" class="border-t border-amber-900/20 hover:bg-amber-900/10 transition-all">
              <td class="py-3 px-4 text-amber-200">{{ (currentPage - 1) * pageSize + n }}</td>
            </tr>
          </tbody>
        </table>

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
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import AdminSidebar from './AdminSidebar.vue';

const router = useRouter();
const activeLi = ref(1);

const currentPage = ref(1);
const totalPages = ref(5);
const pageSize = 10;

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
</script>

<style scoped>
</style>
