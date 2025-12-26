<script setup>
import Navbar from '@/components/Navbar.vue';
import LuckyWheel from '@/components/LuckyWheel.vue';
import { ref, onMounted } from 'vue';

// Mock Data cho phần thưởng - Black & Gold Theme
const prizes = ref([
        { id: 1, label: 'Accord 10 Skin', color: '#1a1a1a', text: '#FFD700', value: '10_skin' }, // Đen - Chữ Vàng
        { id: 2, label: 'Accord 20 Skin', color: '#C5A009', text: '#000000', value: '20_skin' }, // Vàng đậm - Chữ Đen
        { id: 3, label: 'Acc Trắng', color: '#333333', text: '#F0E68C', value: 'trang' }, // Xám đen - Chữ Vàng nhạt
        { id: 4, label: 'Mất Lượt', color: '#B8860B', text: '#000000', value: 'miss' }, // Vàng đồng - Chữ Đen
        { id: 5, label: 'Accord VIP', color: '#000000', text: '#FFD700', value: 'vip' }, // Đen tuyền - Chữ Vàng
        { id: 6, label: 'Voucher 50%', color: '#D4AF37', text: '#000000', value: 'voucher' }, // Vàng kim - Chữ Đen
        { id: 7, label: 'Nick Cùi', color: '#2C2C2C', text: '#F0E68C', value: 'cui' }, // Xám - Chữ Vàng nhạt
        { id: 8, label: 'May mắn lần sau', color: '#DAA520', text: '#000000', value: 'miss_2' }, // Vàng nghệ - Chữ Đen
]);

const wheelRef = ref(null);
const showConfirmModal = ref(false);
const showRewardModal = ref(false);
const receivedPrize = ref(null);

const onSpinStart = () => {
        showConfirmModal.value = true;
};

const confirmSpin = () => {
        showConfirmModal.value = false;

        // Random kết quả (index) giả lập từ API
        const winningIndex = Math.floor(Math.random() * prizes.value.length);
        console.log("Dự kiến trúng:", prizes.value[winningIndex].label);

        // Gọi method spin của component con
        if (wheelRef.value) {
                wheelRef.value.spin(winningIndex);
        }
};

const onSpinEnd = (prize) => {
        receivedPrize.value = prize;
        showRewardModal.value = true;
};
</script>

<template>
        <div
                class="min-h-screen bg-black text-yellow-500 font-sans overflow-hidden relative selection:bg-yellow-500 selection:text-black">

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
                                        class="bg-gradient-to-b from-zinc-800 to-black border border-yellow-500 rounded-2xl p-8 max-w-sm w-full text-center shadow-[0_0_100px_rgba(234,179,8,0.3)] relative overflow-visible">
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

                                        <div class="my-8 py-4 bg-yellow-900/10 border-y border-yellow-500/20">
                                                <h3
                                                        class="text-2xl md:text-3xl font-bold text-yellow-400 drop-shadow-[0_2px_4px_rgba(0,0,0,0.8)] px-2">
                                                        {{ receivedPrize?.label || 'PHẦN QUÀ BÍ ẨN' }}
                                                </h3>
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
                                VÒNG QUAY ĐỊNH MỆNH
                        </h1>

                        <!-- Sử dụng Component LuckyWheel -->
                        <LuckyWheel ref="wheelRef" :prizes="prizes" size="500px" @spin-start="onSpinStart"
                                @spin-end="onSpinEnd" />

                        <div class="mt-12 text-center text-slate-400 text-sm">
                                <p>Mỗi lượt quay tốn <span class="text-yellow-400 font-bold">20.000đ</span></p>
                                <p>Chúc bạn may mắn!</p>
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