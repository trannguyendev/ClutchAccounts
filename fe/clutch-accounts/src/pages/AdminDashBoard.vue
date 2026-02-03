<script setup>
    /* ===== Sidebar.vue ===== */
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import { useI18n } from 'vue-i18n'


const { t } = useI18n()
const router = useRouter();
const route = useRoute();
const activeLi = ref(0);
let pollingInterval = null;
const sidebarItems = [
  { name: t('admin.dashboard'), icon: '<i class="fa-solid fa-chart-line"></i>', route: '/admin' },
  { name: t('admin.transaction'), icon: '<i class="fa-solid fa-money-bill-transfer"></i>', route: '/admin/transaction' },
  { name: t('admin.accountManagement'), icon: '<i class="fa-solid fa-people-roof"></i>', route: '/admin/account' },
  { name: t('admin.news'), icon: '<i class="fa-solid fa-newspaper"></i>', route: '/admin/news' },
  { name: t('admin.valorantAccountManagement'), icon: '<i class="fa-solid fa-gamepad"></i>', route: '/admin/vlr_account' },
  { name: t('admin.adminFAQ'), icon: '<i class="fa-solid fa-question"></i>', route: '/admin/faq' },
  { name: t('admin.adminVoucher'), icon: '<i class="fa-solid fa-ticket"></i>', route: '/admin/voucher' }
];

const routeToIndex = {
  '/admin': 0,
  '/admin/transaction': 1,
  '/admin/account': 2,
  '/admin/news': 3,
  '/admin/vlr_account': 4,
  '/admin/faq': 5,
  '/admin/voucher': 6
};

function syncActiveFromRoute() {
  activeLi.value = routeToIndex[route.path] ?? 0;
}

onMounted(() => { syncActiveFromRoute(); });
watch(() => route.path, syncActiveFromRoute);

const handleNavigation = async (item, index) => {
  try {
    await router.push(item.route);
    // only update active when navigation succeeds
    activeLi.value = index;
  } catch (err) {
    // navigation failed/blocked by guard — keep current active
    console.warn('navigation cancelled', err);
  }
};

/* ===== Dashboard.vue ===== */
const stats = ref([
  {
    title: 'Total Revenue',
    value: '$45,231.89',
    change: '+20.1%'
  },
  {
    title: 'Subscriptions',
    value: '+2350',
    change: '+180.1%'
  },
  {
    title: 'Sales',
    value: '+12,234',
    change: '+19%'
  },
  {
    title: 'Active Now',
    value: '573',
    change: '-2.1%'
  }
]);

// Bar chart data (12 months)
const chartData = [
  { label: 'January', value: 95, color: '#fbbf24' },
  { label: 'February', value: 65, color: '#fbbf24' },
  { label: 'March', value: 50, color: '#fbbf24' },
  { label: 'April', value: 55, color: '#fbbf24' },
  { label: 'May', value: 70, color: '#fbbf24' },
  { label: 'June', value: 68, color: '#fbbf24' },
  { label: 'July', value: 72, color: '#fbbf24' },
  { label: 'August', value: 60, color: '#fbbf24' },
  { label: 'September', value: 48, color: '#fbbf24' },
  { label: 'October', value: 32, color: '#fbbf24' },
  { label: 'November', value: 38, color: '#fbbf24' },
  { label: 'December', value: 50, color: '#fbbf24' }
];

const maxChart = computed(() => Math.max(...chartData.map(c => c.value)) || 1);

// SVG layout for bar chart
const svgW = 100;
const svgH = 50;
const pad = { left: 8, right: 8, top: 6, bottom: 10 };

const bars = computed(() => {
  const n = chartData.length;
  const available = svgW - pad.left - pad.right;
  const step = available / n;
  const barW = Math.max(3, step * 0.6);
  return chartData.map((d, i) => {
    const x = pad.left + i * step + (step - barW) / 2;
    const height = (d.value / maxChart.value) * (svgH - pad.top - pad.bottom);
    const y = svgH - pad.bottom - height;
    return { x, y, width: barW, height, label: d.label, value: d.value, color: d.color };
  });
});

// Recent deposits (sample data) and pagination
const recentDeposits = ref([]);

const fetchRecentDeposit = async () => {
  await axios.get("/api/payment/transactions-all")
  .then((res) => {
    recentDeposits.value = res.data
    console.log(res.data)
  })
  .catch((err) => {
    console.error(err)
  })
}
// status update functions removed — statuses are read-only in UI

const page = ref(1);
const pageSize = ref(5);

const totalPages = computed(() => Math.max(1, Math.ceil(recentDeposits.value.length / pageSize.value)));

const pagedDeposits = computed(() => {
  const start = (page.value - 1) * pageSize.value;
  return recentDeposits.value.slice(start, start + pageSize.value);
});

function goToPage(n) {
  if (n < 1) n = 1;
  if (n > totalPages.value) n = totalPages.value;
  page.value = n;
}

function prevPage() { if (page.value > 1) page.value -= 1; }
function nextPage() { if (page.value < totalPages.value) page.value += 1; }

// Visible pages with sliding window + ellipsis
const visiblePages = computed(() => {
  const total = totalPages.value;
  const current = page.value;
  const siblingCount = 1; // number of pages to show on each side of current

  // total page numbers to show (first + last + current +- siblings)
  const totalNumbers = siblingCount * 2 + 3;
  const totalBlocks = totalNumbers + 2; // including 2 ellipses

  if (total <= totalBlocks) {
    return Array.from({ length: total }, (_, i) => i + 1);
  }

  const left = Math.max(2, current - siblingCount);
  const right = Math.min(total - 1, current + siblingCount);
  const pages = [];

  pages.push(1);

  if (left > 2) {
    pages.push('...');
  } else {
    for (let i = 2; i < left; i++) pages.push(i);
  }

  for (let i = left; i <= right; i++) pages.push(i);

  if (right < total - 1) {
    pages.push('...');
  } else {
    for (let i = right + 1; i < total; i++) pages.push(i);
  }

  pages.push(total);
  return pages;
});

// preserve table height by rendering invisible placeholder rows when a page has fewer items than pageSize
const emptyRowCount = computed(() => Math.max(0, pageSize.value - pagedDeposits.value.length));
const emptyRows = computed(() => Array.from({ length: emptyRowCount.value }));

// Date formatting helpers (localized to Vietnamese)
function formatDate(dateString) {
  if (!dateString) return '-';
  try {
    return new Date(dateString).toLocaleString('vi-VN', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    });
  } catch (e) {
    return dateString;
  }
}
function formatDateLong(dateString) {
  if (!dateString) return '-';
  try {
    return new Date(dateString).toLocaleString('vi-VN', { dateStyle: 'long', timeStyle: 'short' });
  } catch (e) {
    return dateString;
  }
}

onMounted(() => {
  // Fetch recent deposits on mount
  fetchRecentDeposit();

  pollingInterval = setInterval(() => {
    fetchRecentDeposit();
  }, 10000); 
})

onUnmounted(() => {
  clearInterval(pollingInterval);
});
</script>
<template>
    <div class="flex min-h-screen bg-[#0A0A0A] text-amber-100">

    <!-- ===== Sidebar.vue ===== -->
    <aside class="w-64 border-r-2 border-amber-900/60">
      <div class="p-6 flex items-center justify-between gap-4">
        <div class="text-lg font-semibold text-amber-400">
          Admin Dashboard
        </div>
        <div class="w-12 h-12 rounded-lg bg-amber-900/40 border border-amber-600/50 flex items-center justify-center flex-shrink-0 overflow-hidden hover:cursor-pointer" @click="router.push('/main')">
          <img src="../img/Logo.png" alt="Logo" class="w-full h-full object-cover" />
        </div>
      </div>

      <div class="px-4 space-y-4">
        <div
          v-for="(item, index) in sidebarItems"
          :key="index"
          @click="handleNavigation(item, index)"
          :class="[
            'px-3 py-3 rounded-lg cursor-pointer transition-all duration-300 flex items-center gap-3',
            activeLi === index 
              ? 'bg-amber-900/30 font-medium border border-amber-600/50 text-amber-300' 
              : 'hover:bg-amber-900/20 hover:border hover:border-amber-600/30 text-amber-200 hover:text-amber-300'
          ]"
        >
          <span v-if="item.icon.includes('<')" v-html="item.icon" class="text-xl"></span>
          <span v-else class="text-xl">{{ item.icon }}</span>
          <span>{{ item.name }}</span>
        </div>
      </div>
    </aside>

    <!-- ===== Dashboard.vue ===== -->
    <div class="flex-1 p-6">
      <h1 class="text-2xl font-bold mb-6 text-amber-300">Dashboard</h1>

      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div
          v-for="stat in stats"
          :key="stat.title"
          class="rounded-xl border border-amber-900/50 p-4 bg-[#1a1a1a] hover:bg-[#242424] hover:border-amber-600 transition-all duration-300 cursor-pointer hover:shadow-lg hover:shadow-amber-900/30"
        >
          <div class="text-sm text-amber-200/70">
            {{ stat.title }}
          </div>
          <div class="text-2xl font-bold text-amber-300">
            {{ stat.value }}
          </div>
          <div class="text-xs text-amber-200/60">
            {{ stat.change }}
          </div>
        </div>
      </div>

      <!-- Clean bar chart: left-aligned, occupies 1/3 width on md+ screens -->
      <div class="mt-6">
        <div class="w-full flex flex-col md:flex-row gap-4">
          <!-- Chart (left) -->
          <div class="w-full md:w-1/3">
            <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] h-full">
              <div class="mb-2">
                <h2 class="text-lg font-semibold text-amber-300">Biểu đồ</h2>
                <div class="text-sm text-amber-200/60">Success/ Fail rate</div>
              </div>

              <div class="w-full overflow-hidden">
                <svg :viewBox="`0 0 ${svgW} ${svgH}`" class="w-full h-48" xmlns="http://www.w3.org/2000/svg" shape-rendering="geometricPrecision">
                  <!-- subtle grid lines -->
                  <g stroke="#f3f4f6" stroke-opacity="0.06" stroke-width="0.4">
                    <line :x1="0" :x2="svgW" :y1="pad.top" :y2="pad.top" />
                    <line :x1="0" :x2="svgW" :y1="pad.top + (svgH - pad.top - pad.bottom) * 0.33" :y2="pad.top + (svgH - pad.top - pad.bottom) * 0.33" />
                    <line :x1="0" :x2="svgW" :y1="pad.top + (svgH - pad.top - pad.bottom) * 0.66" :y2="pad.top + (svgH - pad.top - pad.bottom) * 0.66" />
                    <line :x1="0" :x2="svgW" :y1="svgH - pad.bottom" :y2="svgH - pad.bottom" />
                  </g>

                  <!-- bars -->
                  <g>
                    <rect v-for="(b, i) in bars" :key="i"
                      :x="b.x"
                      :y="b.y"
                      :width="b.width"
                      :height="Math.max(0, b.height)"
                      :fill="b.color"
                      rx="1"
                      shape-rendering="crispEdges"
                    />
                  </g>

                  <!-- crisp top strokes to make bars 'net' -->
                  <g>
                    <line v-for="(b, i) in bars" :key="i" :x1="b.x" :x2="b.x + b.width" :y1="b.y" :y2="b.y" stroke="#00000022" stroke-width="0.6" stroke-linecap="round" />
                  </g>

                  <!-- x labels -->
                  <g>
                    <text v-for="(b, i) in bars" :key="i" :x="b.x + b.width / 2" :y="svgH - 1" fill="#e5e7eb" font-size="3" text-anchor="middle">{{ b.label.slice(0,3) }}</text>
                  </g>
                </svg>
              </div>
            </div>
          </div>

          <!-- Recent deposits table (right) -->
          <div class="w-full md:w-2/3">
            <div class="rounded-xl border border-amber-900/50 p-4 bg-[#141414] h-full flex flex-col min-w-0">
              <div class="mb-2">
                <h3 class="text-lg font-semibold text-amber-300">Recently request deposit</h3>
                <div class="text-sm text-amber-200/60">List of deposit</div>
              </div>

              <div class="flex-1 overflow-x-auto">
                <table class="w-full text-left table-fixed text-sm">
                  <thead>
                    <tr class="text-amber-200/70">
                      <th class="pb-2 break-words">ID</th>
                      <th class="pb-2 break-words">Username</th>
                      <th class="pb-2 break-words sticky-col sticky-col-amount" style="width:10rem;">Deposit Amount</th>
                      <th class="pb-2 break-words sticky-col sticky-col-request" style="width:9rem;">Request at</th>
                      <th class="pb-2 break-words">Status</th>
                      <th class="pb-2 break-words">Type</th>
                      <th class="pb-2 break-words">Description</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="d in pagedDeposits" :key="d.id" class="border-t border-amber-900/20">
                      <td class="py-2 truncate cell-limit">{{ d.id }}</td>
                      <td class="py-2 truncate cell-limit">{{ d.descrp.substring(4) }}</td>
                      <td class="py-2 truncate cell-limit sticky-col sticky-col-amount">{{ d.amount }}</td>
                      <td class="py-2 text-amber-200/60 truncate cell-limit sticky-col sticky-col-request" :title="formatDateLong(d.created_at)">{{ formatDate(d.created_at) }}</td> 
                      <td class="py-2 truncate cell-limit">
                        <div class="w-28">
                          <span v-if="d.status==='APPROVED'" class="inline-block w-full text-center px-2 py-0.5 rounded text-xs bg-emerald-500 text-black truncate">APPROVED</span>
                          <span v-else-if="d.status==='FAILED'" class="inline-block w-full text-center px-2 py-0.5 rounded text-xs bg-red-500 text-white truncate">FAILED</span>
                          <span v-else class="inline-block w-full text-center px-2 py-0.5 rounded text-xs bg-amber-900/20 text-amber-200 truncate">PENDING</span>
                        </div>
                      </td>
                      <td class="py-2 truncate cell-limit">{{ d.type }}</td>
                      <td class="py-2 truncate cell-limit">{{ d.descrp }}</td>
                    </tr>

                    <!-- invisible placeholder rows to preserve table height when page has fewer items -->
                    <tr v-for="(_, idx) in emptyRows" :key="`empty-${idx}`" class="border-t border-amber-900/20 invisible-row" aria-hidden="true">
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                      <td class="py-2 cell-limit">&nbsp;</td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- pagination -->
              <div class="mt-3 flex items-center justify-between flex-wrap gap-2 min-w-0 pagination-fixed">
                <div class="text-sm text-amber-200/60 min-w-0 truncate">Trang {{ page }} / {{ totalPages }}</div>
                <div class="flex items-center gap-2 min-w-0">
                  <button @click="prevPage" class="px-2 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 shrink-0">Prev</button>

                  <div class="flex items-center gap-1 overflow-x-auto no-scrollbar max-w-[18rem] min-w-0">
                    <template v-for="(p, idx) in visiblePages" :key="`p-${idx}`">
                      <button
                        v-if="p !== '...'
                        "
                        @click="goToPage(p)"
                        :aria-current="page===p ? 'page' : null"
                        :class="[
                          'px-2 py-1 text-sm rounded shrink-0',
                          page===p ? 'bg-amber-600 text-black' : 'bg-amber-900/20 text-amber-200 hover:bg-amber-900/30'
                        ]"
                      >
                        {{ p }}
                      </button>

                      <span v-else class="px-2 py-1 text-sm text-amber-200/60 shrink-0">…</span>
                    </template>
                  </div>

                  <button @click="nextPage" class="px-2 py-1 bg-amber-900/20 rounded text-amber-200 hover:bg-amber-900/30 shrink-0">Next</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>
<style scoped>
/* Limit cell content to 18 characters width */
.cell-limit {
  max-width: 144px;
  white-space: nowrap;
}

/* Smooth hover transitions */
div {
  transition: all 0.3s ease;
}

/* Subtle golden circuit pattern background */
.flex {
  position: relative;
}

.flex::before {
  content: '';
  position: absolute;
  inset: 0;
  opacity: 0.03;
  background-image: linear-gradient(to right, transparent 49px, rgba(255,215,0,0.1) 50px),
                    linear-gradient(to bottom, transparent 49px, rgba(255,215,0,0.1) 50px);
  background-size: 50px 50px;
  pointer-events: none;
}

/* small helper to hide native scrollbars for horizontal pagers */
.no-scrollbar {
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  -webkit-overflow-scrolling: touch;
}
.no-scrollbar::-webkit-scrollbar { display: none; }

/* placeholder rows: keep table/card height stable between pages */
.invisible-row {
  opacity: 0;
  pointer-events: none;
  user-select: none;
}

/* Sticky columns: keep Deposit Amount + Request at visible during horizontal scroll */
.sticky-col {
  position: -webkit-sticky;
  position: sticky;
  background: rgba(20,20,20,0.98); /* match card bg to avoid bleed-through */
  backdrop-filter: none;
  z-index: 20;
  box-shadow: -6px 0 8px -6px rgba(0,0,0,0.6);
  border-left: 1px solid rgba(255,215,0,0.03);
}

/* order: Request at sits at far right, Deposit Amount sits to its left */
.sticky-col-request { right: 0; min-width: 9rem; width: 9rem; }
.sticky-col-amount { right: 9rem; min-width: 10rem; width: 10rem; }

/* ensure header cells for sticky columns sit above body cells */
thead .sticky-col { z-index: 30; box-shadow: none; border-bottom: 1px solid rgba(255,255,255,0.02); }

/* ensure pagination region doesn't change layout height when its content changes */
.pagination-fixed { min-height: 2.5rem; }
</style>