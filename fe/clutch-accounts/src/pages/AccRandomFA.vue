<script setup>
import Navbar from '@/components/Navbar.vue';
import LuckyWheel from '@/components/LuckyWheel.vue';
import { ref, onMounted } from 'vue';
import axios from 'axios';

// State cho thông báo (Toast)
const toast = ref({
        show: false,
        message: '',
        type: 'success'
});

const showToast = (message, type = 'success') => {
        toast.value = { show: true, message, type };
        setTimeout(() => {
                toast.value.show = false;
        }, 3000);
};

// User State
const userBalance = ref(0);
const SPIN_COST = 20000;

const formatCurrency = (value) => {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

// Fetch User Balance from API
const fetchUserBalance = async () => {
        const currentUserStr = localStorage.getItem('currentuser');
        if (currentUserStr) {
                try {
                        const currentUser = JSON.parse(currentUserStr);
                        const token = currentUser.token;

                        if (token) {
                                const res = await axios.get('/api/auth/me', {
                                        headers: {
                                                Authorization: `Bearer ${token}`
                                        }
                                });
                                userBalance.value = res.data.balance;
                        }
                } catch (err) {
                        console.error("Failed to fetch user balance:", err);
                        // Optional: Redirect to login or show warning
                        if (err.response && (err.response.status === 401 || err.response.status === 403)) {
                                showToast('Phiên đăng nhập hết hạn, vui lòng đăng nhập lại.', 'error');
                        }
                }
        } else {
                showToast('Bạn chưa đăng nhập!', 'error');
        }
};

// Mock Data cho phần thưởng - Black & Gold Theme
// Thêm account/password cho các giải thưởng là nick game
const prizes = ref([
        { id: 1, label: 'AccRandom', color: '#1a1a1a', text: '#FFD700', value: '10_skin', account: 'acc10skin_vip', password: 'password123' },
        { id: 2, label: 'AccRandom', color: '#C5A009', text: '#000000', value: '20_skin', account: 'acc20skin_pro', password: 'superpass789' },
        { id: 3, label: 'AccRandom', color: '#333333', text: '#F0E68C', value: 'trang', account: 'empty_acc_01', password: 'nopassword' },
        { id: 4, label: 'AccRandom', color: '#B8860B', text: '#000000', value: 'miss' },
        { id: 5, label: 'AccRandom', color: '#000000', text: '#FFD700', value: 'vip', account: 'vip_accord_99', password: 'goldenvip!!!' },
        { id: 6, label: 'AccRandom', color: '#D4AF37', text: '#000000', value: 'voucher' },
        { id: 7, label: 'AccRandom', color: '#2C2C2C', text: '#F0E68C', value: 'cui', account: 'bacon_hair_123', password: '123456abc' },
        { id: 8, label: 'AccRandom', color: '#DAA520', text: '#000000', value: 'miss_2' },
]);

const wheelRef = ref(null);
const showConfirmModal = ref(false);
const showRewardModal = ref(false);
const receivedPrize = ref(null);

onMounted(() => {
        fetchUserBalance();
});

const onSpinStart = () => {
        showConfirmModal.value = true;
};

const confirmSpin = () => {
        // 1. Kiểm tra số dư
        if (userBalance.value < SPIN_COST) {
                showToast('Số dư không đủ để quay! Vui lòng nạp thêm.', 'error');
                showConfirmModal.value = false;
                return;
        }

        // 2. Trừ tiền (Tạm thời trừ ở Client để demo, thực tế cần API Spin)
        userBalance.value -= SPIN_COST;
        showConfirmModal.value = false;

        // 3. Random kết quả (index) giả lập từ API
        const winningIndex = Math.floor(Math.random() * prizes.value.length);
        console.log("Dự kiến trúng:", prizes.value[winningIndex].label);

        // 4. Gọi method spin của component con
        if (wheelRef.value) {
                wheelRef.value.spin(winningIndex);
        }
};

const onSpinEnd = (prize) => {
        receivedPrize.value = prize;
        showRewardModal.value = true;

        // Nếu trúng giải có account, có thể tự động copy hoặc thông báo
        if (prize.account) {
                // Optional: showToast('Chúc mừng đã trúng Acc!', 'success');
        }
};

const copyCredentials = async () => {
        if (receivedPrize.value && receivedPrize.value.account) {
                const textToCopy = `${receivedPrize.value.account}|${receivedPrize.value.password}`;
                try {
                        await navigator.clipboard.writeText(textToCopy);
                        showToast('Đã sao chép Tài khoản | Mật khẩu!', 'success');
                } catch (err) {
                        console.error('Failed to copy: ', err);
                        showToast('Không thể sao chép, vui lòng thử lại.', 'error');
                }
        }
};
</script>

<template>
        <div
                class="min-h-screen bg-black text-yellow-500 font-sans overflow-hidden relative selection:bg-yellow-500 selection:text-black">

                <!-- Toast Notification (Top Center) -->
                <transition enter-active-class="transform ease-out duration-300 transition"
                        enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
                        enter-to-class="translate-y-0 opacity-100 sm:translate-x-0"
                        leave-active-class="transition ease-in duration-100" leave-from-class="opacity-100"
                        leave-to-class="opacity-0">
                        <div v-if="toast.show"
                                class="fixed top-4 left-1/2 -translate-x-1/2 z-[120] flex items-center w-full max-w-xs p-4 rounded-lg shadow dark:text-gray-400 dark:bg-gray-800"
                                :class="toast.type === 'error' ? 'bg-red-900/90 text-red-200 border border-red-500' : 'bg-green-900/90 text-green-200 border border-green-500'"
                                role="alert">
                                <div class="inline-flex items-center justify-center flex-shrink-0 w-8 h-8 rounded-lg">
                                        <span v-if="toast.type === 'error'">⚠️</span>
                                        <span v-else>✅</span>
                                </div>
                                <div class="ml-3 text-sm font-normal">{{ toast.message }}</div>
                                <button type="button"
                                        class="ml-auto -mx-1.5 -my-1.5 rounded-lg focus:ring-2 p-1.5 inline-flex h-8 w-8 hover:bg-opacity-20 hover:bg-white"
                                        @click="toast.show = false">
                                        <span class="sr-only">Close</span>
                                        <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg"
                                                fill="none" viewBox="0 0 14 14">
                                                <path stroke="currentColor" stroke-linecap="round"
                                                        stroke-linejoin="round" stroke-width="2"
                                                        d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6" />
                                        </svg>
                                </button>
                        </div>
                </transition>


                <!-- Confirmation Modal -->
                <transition enter-active-class="transition duration-200 ease-out"
                        enter-from-class="transform scale-95 opacity-0" enter-to-class="transform scale-100 opacity-100"
                        leave-active-class="transition duration-150 ease-in"
                        leave-from-class="transform scale-100 opacity-100"
                        leave-to-class="transform scale-95 opacity-0">
                        <div v-if="showConfirmModal"
                                class="fixed inset-0 z-[100] flex items-center justify-center bg-black/80 backdrop-blur-sm px-4">
                                <div
                                        class="bg-zinc-900 border-2 border-yellow-600/50 rounded-2xl p-6 md:p-8 max-w-sm w-full text-center shadow-[0_0_50px_rgba(234,179,8,0.15)] relative overflow-hidden">
                                        <!-- Decorative glow -->
                                        <div
                                                class="absolute top-0 left-1/2 -translate-x-1/2 w-32 h-1 bg-yellow-500 shadow-[0_0_20px_rgba(234,179,8,1)]">
                                        </div>

                                        <h3
                                                class="text-2xl md:text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-br from-yellow-300 to-yellow-600 mb-4 uppercase font-serif tracking-wide">
                                                Xác nhận</h3>

                                        <div class="space-y-4 mb-8">
                                                <p class="text-gray-300">Bạn có chắc chắn muốn quay?</p>
                                                <div
                                                        class="inline-block px-4 py-2 bg-yellow-900/20 rounded-lg border border-yellow-500/20">
                                                        Phí: <span
                                                                class="text-yellow-400 font-bold text-lg">20.000đ</span>
                                                </div>
                                                <div class="text-sm text-gray-400">
                                                        Số dư hiện tại: <span
                                                                :class="userBalance < 20000 ? 'text-red-500' : 'text-green-500'"
                                                                class="font-bold">{{ formatCurrency(userBalance)
                                                                }}</span>
                                                </div>
                                        </div>

                                        <div class="flex gap-4 justify-center">
                                                <button @click="showConfirmModal = false"
                                                        class="px-6 py-2.5 rounded-xl border border-zinc-700 text-zinc-400 hover:text-white hover:bg-zinc-800 transition-all font-medium">
                                                        Để sau
                                                </button>
                                                <button @click="confirmSpin"
                                                        class="px-8 py-2.5 rounded-xl bg-gradient-to-r from-yellow-600 via-yellow-500 to-yellow-400 text-black font-bold hover:shadow-[0_0_20px_rgba(234,179,8,0.4)] hover:scale-105 transition-all transform uppercase tracking-wider text-sm">
                                                        Quay Ngay
                                                </button>
                                        </div>
                                </div>
                        </div>
                </transition>

                <!-- Reward Popup Modal -->
                <transition enter-active-class="transition duration-500 cubic-bezier(0.19, 1, 0.22, 1)"
                        enter-from-class="transform scale-50 opacity-0 translate-y-10"
                        enter-to-class="transform scale-100 opacity-100 translate-y-0"
                        leave-active-class="transition duration-200 ease-in"
                        leave-from-class="transform scale-100 opacity-100"
                        leave-to-class="transform scale-95 opacity-0">
                        <div v-if="showRewardModal"
                                class="fixed inset-0 z-[110] flex items-center justify-center bg-black/90 backdrop-blur-md px-4"
                                @click.self="showRewardModal = false">
                                <div
                                        class="bg-gradient-to-b from-zinc-800 to-black border border-yellow-500 rounded-2xl p-8 max-w-md w-full text-center shadow-[0_0_100px_rgba(234,179,8,0.3)] relative overflow-visible">
                                        <!-- Floating Badge/Icon -->
                                        <div class="absolute -top-16 left-1/2 -translate-x-1/2 w-32 h-32">
                                                <!-- Simple CSS Gift Icon/Glow -->
                                                <div
                                                        class="absolute inset-0 bg-yellow-500/50 rounded-full blur-[40px] animate-pulse">
                                                </div>
                                                <div
                                                        class="relative w-full h-full flex items-center justify-center text-7xl drop-shadow-[0_10px_10px_rgba(0,0,0,0.5)] animate-bounce">
                                                        🎁
                                                </div>
                                        </div>

                                        <div class="mt-12 space-y-2">
                                                <h2
                                                        class="text-3xl md:text-4xl font-black bg-clip-text text-transparent bg-gradient-to-r from-yellow-200 via-yellow-500 to-yellow-200 uppercase tracking-widest drop-shadow-sm font-serif">
                                                        CHÚC MỪNG
                                                </h2>
                                                <p class="text-yellow-100/60 text-sm font-medium tracking-wider">BẠN ĐÃ
                                                        NHẬN ĐƯỢC</p>
                                        </div>

                                        <div class="my-6 py-4 bg-yellow-900/10 border-y border-yellow-500/20">
                                                <h3
                                                        class="text-2xl md:text-3xl font-bold text-yellow-400 drop-shadow-[0_2px_4px_rgba(0,0,0,0.8)] px-2">
                                                        {{ receivedPrize?.label || 'PHẦN QUÀ BÍ ẨN' }}
                                                </h3>
                                        </div>

                                        <!-- Credentials Info Section -->
                                        <div v-if="receivedPrize?.account"
                                                class="mb-6 p-4 bg-zinc-900/80 rounded-xl border border-zinc-700">
                                                <div class="text-left space-y-3">
                                                        <div>
                                                                <span
                                                                        class="text-xs uppercase text-zinc-500 font-bold block mb-1">Tài
                                                                        khoản</span>
                                                                <div
                                                                        class="font-mono text-yellow-100 bg-zinc-800 p-2 rounded border border-zinc-700/50 select-all truncate">
                                                                        {{ receivedPrize.account }}
                                                                </div>
                                                        </div>
                                                        <div>
                                                                <span
                                                                        class="text-xs uppercase text-zinc-500 font-bold block mb-1">Mật
                                                                        khẩu</span>
                                                                <div class="relative group">
                                                                        <div
                                                                                class="font-mono text-yellow-100 bg-zinc-800 p-2 rounded border border-zinc-700/50 select-all truncate">
                                                                                {{ receivedPrize.password }}
                                                                        </div>
                                                                </div>
                                                        </div>
                                                </div>

                                                <button @click="copyCredentials"
                                                        class="mt-4 w-full flex items-center justify-center gap-2 px-4 py-2 bg-zinc-800 hover:bg-zinc-700 border border-zinc-600 rounded-lg transition-colors text-sm text-zinc-300 font-bold uppercase tracking-wide">
                                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4"
                                                                fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                                <path stroke-linecap="round" stroke-linejoin="round"
                                                                        stroke-width="2"
                                                                        d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
                                                        </svg>
                                                        Copy Tài Khoản | Mật Khẩu
                                                </button>
                                        </div>

                                        <button @click="showRewardModal = false"
                                                class="w-full px-8 py-3.5 rounded-xl bg-gradient-to-r from-yellow-600 via-yellow-500 to-yellow-400 text-black text-lg font-bold hover:shadow-[0_0_30px_rgba(234,179,8,0.6)] hover:-translate-y-1 transition-all transform uppercase tracking-widest shadow-lg">
                                                NHẬN QUÀ NGAY
                                        </button>
                                </div>
                        </div>
                </transition>

                <Navbar></Navbar>

                <!-- Background Effects - Gold & Dark Ambience -->
                <div class="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none z-0">
                        <!-- Radial gradient center -->
                        <div
                                class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-yellow-600/10 rounded-full blur-[100px]">
                        </div>

                        <div
                                class="absolute top-[-10%] right-[-10%] w-[600px] h-[600px] bg-yellow-500/5 rounded-full blur-[120px] animate-pulse">
                        </div>
                        <div
                                class="absolute bottom-[-10%] left-[-10%] w-[600px] h-[600px] bg-slate-800/20 rounded-full blur-[120px] animate-pulse delay-1000">
                        </div>

                        <!-- Grid pattern overlay (optional luxury touch) -->
                        <div
                                class="absolute inset-0 bg-[url('https://grainy-gradients.vercel.app/noise.svg')] opacity-10 mix-blend-overlay">
                        </div>
                </div>

                <div
                        class="container mx-auto px-4 py-12 relative z-10 flex flex-col items-center justify-center min-h-[calc(100vh-80px)]">

                        <h1
                                class="text-4xl md:text-5xl font-bold bg-clip-text text-transparent bg-gradient-to-b from-yellow-300 via-yellow-500 to-yellow-700 mb-8 drop-shadow-[0_2px_10px_rgba(234,179,8,0.3)] text-center uppercase tracking-[0.2em] pb-2 leading-relaxed font-serif">
                                FA Account Random
                        </h1>

                        <!-- Sử dụng Component LuckyWheel -->
                        <LuckyWheel ref="wheelRef" :prizes="prizes" size="500px" @spin-start="onSpinStart"
                                @spin-end="onSpinEnd" />

                        <div class="mt-12 text-center text-slate-400 text-sm">
                                <p>Số dư của bạn: <span class="text-yellow-400 font-bold text-lg">{{
                                        formatCurrency(userBalance) }}</span></p>
                                <p>Mỗi lượt quay tốn <span class="text-yellow-400 font-bold">20.000đ</span></p>
                                <p class="mt-2 text-xs opacity-60">Chúc bạn may mắn!</p>
                        </div>

                </div>
        </div>
</template>

<style scoped>
/* Hiệu ứng xoay chậm cho viền ngoài */
@keyframes spin-slow {
        from {
                transform: rotate(0deg);
        }

        to {
                transform: rotate(360deg);
        }
}

.animate-spin-slow {
        animation: spin-slow 10s linear infinite;
}
</style>