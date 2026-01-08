<script setup>
import Navbar from '@/components/Navbar.vue';
import axios from 'axios';
import { onMounted } from 'vue';
import { ref } from 'vue';
import { useUserStore } from '@/stores/user';

let isSpinning = ref(false);
const currentRotation = ref(0);
// Configuration
let prizes = ref([]);
let size = 400;
let duration = 3; // 3s for spin animation
let borderWidth = 8;
const user = useUserStore();
const showConfirmModal = ref(false);
const showErrorModal = ref(false);
const showResultModal = ref(false);
let errorMessage = ref('');
let resultPrize = ref({});
const SVG_SIZE = 100;
const CENTER = SVG_SIZE / 2;
const RADIUS = SVG_SIZE / 2;

const fetchData = async () => {
    try {
        const res = await axios.get("/api/accounts/random/acc-fa/list-acc");
        prizes.value = res.data;
        console.log("Prizes fetched:", prizes.value);
    } catch (err) {
        console.error("Error fetching prizes:", err);
    }
};

//Calculate segment angle
const getSegmentAngle = () => {
    return prizes.value.length > 0 ? 360 / prizes.value.length : 0;
}

// Calculate SVG path for sector
const getSectorPath = (index) => {
    const segmentAngle = getSegmentAngle();
    const startAngle = index * segmentAngle;
    const endAngle = (index + 1) * segmentAngle;
    const startRad = (startAngle - 90) * (Math.PI / 180);
    const endRad = (endAngle - 90) * (Math.PI / 180);
    const x1 = CENTER + RADIUS * Math.cos(startRad);
    const y1 = CENTER + RADIUS * Math.sin(startRad);
    const x2 = CENTER + RADIUS * Math.cos(endRad);
    const y2 = CENTER + RADIUS * Math.sin(endRad);
    const largeArcFlag = segmentAngle > 180 ? 1 : 0;

    return `M ${CENTER} ${CENTER} L ${x1} ${y1} A ${RADIUS} ${RADIUS} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`;
}

// Calculate text rotation to center of segment
const getTextRotation = (index) => {
    const segmentAngle = getSegmentAngle();
    const angle = (index * segmentAngle) + (segmentAngle / 2);
    return `rotate(${angle}, ${CENTER}, ${CENTER})`;
}

//Spin animation
const spin = (targetIndex) => {
    if (isSpinning.value) return;
    isSpinning.value = true;

    const segmentAngle = 360 / prizes.value.length;
    const targetAngle = targetIndex * segmentAngle + segmentAngle / 2;

    const MIN_SPINS = 5;
    const current = currentRotation.value % 360;

    let delta = 360 - targetAngle - current;
    if (delta < 0) delta += 360;

    currentRotation.value += MIN_SPINS * 360 + delta;

    setTimeout(() => {
        isSpinning.value = false;
        showResultModal.value = true;
    }, duration * 1000);
};

// Step 1: Show confirmation modal
const onSpinButtonClick = () => {
    if (isSpinning.value) {
        console.warn("Wheel is already spinning...");
        console.log("State isSpin: ", isSpinning.value);
        return;
    }
    
    if (prizes.value.length === 0) {
        errorMessage.value = "No prizes available";
        showErrorModal.value = true;
        return;
    }

    showConfirmModal.value = true;
}

const cancelSpin = () => {
    showConfirmModal.value = false;
}

// Step 3: Confirm and fetch from API
const confirmSpin = async () => {
    showConfirmModal.value = false;
    try {
        console.log("Calling API to get random prize...");
        // Call backend API to get random result
        const response = await axios.post("/api/accounts/random/acc-fa", {
            email: user.username
        });
        spin(3);
        resultPrize.value = response.data;
        console.log("Backend returned prize:", resultPrize.value);
        
    } catch (error) {
        console.error("Error fetching random prize:", error);
        errorMessage.value = error.response.data.error;
        showErrorModal.value = true;
    }
}

// Close error modal
function closeErrorModal() {
    showErrorModal.value = false;
}

// Close result modal
function closeResultModal() {
    showResultModal.value = false;
    resultPrize.value = null;
}

// Initialize on component mount
onMounted(() => {
    fetchData();
});

</script>

<template>
        <div class="min-h-screen bg-black text-yellow-500 font-sans overflow-hidden relative selection:bg-yellow-500 selection:text-black">
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
                                LUCKY WHEEL
                        </h1>

                        <!-- Lucky Wheel UI -->
                        <div class="relative flex items-center justify-center font-sans select-none"
                            :style="{ width: typeof size === 'number' ? `${size}px` : size, height: typeof size === 'number' ? `${size}px` : size }">

                            <!-- 1. Vòng quay chính (Wheel) -->
                            <div ref="wheelRef" class="w-full h-full rounded-full relative z-10 overflow-hidden shadow-2xl transition-transform duration-[4000ms] ease-out" :style="{
                                transform: `rotate(${currentRotation}deg)`                            }">

                                <!-- SVG Render -->
                                <!-- ViewBox 0 0 100 100 để dễ scale theo % -->
                                <svg viewBox="0 0 100 100" class="w-full h-full drop-shadow-md"
                                    :class="{ 'pointer-events-none': isSpinning }">
                                    <!-- Render từng miếng (Slice) -->
                                    <g v-for="(prize, index) in prizes" :key="prize.id" class="group">
                                        <!-- Đường path rẻ quạt (Normal State) -->
                                        <path :d="getSectorPath(index)" stroke="#FDB931" stroke-width="0.5" class="hover:bg-amber-400"/>

                                        <!-- Text và Icon (Normal) -->
                                        <g :transform="getTextRotation(index)">
                                            <foreignObject x="38" y="4" width="24" height="42">
                                                <div class="flex flex-col items-center justify-start h-full w-full pt-1 pointer-events-none">
                                                    <div class="text-[0.22rem] md:text-[0.25rem] font-bold uppercase tracking-widest text-center leading-none shadow-sm pb-1"
                                                        style="writing-mode: vertical-rl; text-orientation: mixed;">
                                                        {{ prize.account_type }}
                                                    </div>
                                                    <div class="text-[0.3rem] mt-1 opacity-80">♦</div>
                                                </div>
                                            </foreignObject>
                                        </g>
                                    </g>

                                    <!-- Highlighted Slice (Rendered on TOP) -->

                                </svg>
                            </div>

                            <!-- 2. Viền ngoài (Outer Border Ring) - Hiệu ứng Glow -->
                            <div class="absolute inset-0 rounded-full border-solid border-yellow-600 z-20 pointer-events-none shadow-[0_0_20px_rgba(234,179,8,0.5)]"
                                :style="{ borderWidth: `${borderWidth}px` }">
                                <!-- Gradient overlay cho border -->
                                <div class="absolute inset-[-10px] rounded-full border-[4px] border-yellow-300 opacity-50"></div>
                            </div>

                            <!-- 3. Kim chỉ thị (Pointer) - Luôn ở vị trí 12h -->
                            <div class="absolute top-[-10px] left-1/2 -translate-x-1/2 z-30 drop-shadow-lg">
                                <svg width="40" height="50" viewBox="0 0 40 55" fill="none">
                                    <path d="M20 55L4 10C2 5 5 0 10 0H30C35 0 38 5 36 10L20 55Z" fill="url(#pointerGradient)"
                                        stroke="#5B420C" stroke-width="2" />
                                    <defs>
                                        <linearGradient id="pointerGradient" x1="20" y1="0" x2="20" y2="55" gradientUnits="userSpaceOnUse">
                                            <stop stop-color="#FFD700" />
                                            <stop offset="0.5" stop-color="#FDB931" />
                                            <stop offset="1" stop-color="#BF953F" />
                                        </linearGradient>
                                    </defs>
                                </svg>
                            </div>

                            <!-- 4. Nút Quay ở giữa (Center Button) -->
                            <div class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 z-40">
                                <!-- Vòng sáng animate -->
                                <div class="absolute inset-[-6px] bg-yellow-400 rounded-full blur-md opacity-30 animate-pulse"></div>

                                <button @click="onSpinButtonClick()"
                                    class="w-16 h-16 md:w-20 md:h-20 rounded-full bg-gradient-to-br from-yellow-300 via-yellow-500 to-yellow-700 shadow-[inset_0_2px_5px_rgba(255,255,255,0.4),0_4px_10px_rgba(0,0,0,0.5)] flex items-center justify-center border-4 border-yellow-900 group active:scale-95 hover:scale-110 hover:shadow-[0_0_20px_rgba(255,215,0,0.6)] transition-all duration-300 ease-out" :disabled="isSpinning.value">

                                    <span
                                        class="font-serif font-bold text-yellow-950 text-sm md:text-base tracking-widest drop-shadow-sm group-hover:text-black transition-colors">
                                        ROLL NOW
                                    </span>
                                </button>
                            </div>
                        </div>

                        <div class="mt-12 text-center text-slate-400 text-sm">
                                <p>Each spin costs <span class="text-yellow-400 font-bold">20.000đ</span></p>
                                <p>Good luck!</p>
                        </div>

                </div>
        </div>

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
                                <div
                                        class="absolute top-0 left-1/2 -translate-x-1/2 w-32 h-1 bg-yellow-500 shadow-[0_0_20px_rgba(234,179,8,1)]">
                                </div>

                                <h3
                                        class="text-2xl md:text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-br from-yellow-300 to-yellow-600 mb-4 uppercase font-serif tracking-wide">
                                        Confirm Spin</h3>

                                <div class="space-y-4 mb-8">
                                        <p class="text-gray-300">Are you sure you want to spin?</p>
                                        <div
                                                class="inline-block px-4 py-2 bg-yellow-900/20 rounded-lg border border-yellow-500/20">
                                               <span
                                                        class="text-yellow-400 font-bold text-lg">Cost: 20.000 VND</span>
                                        </div>
                                </div>

                                <div class="flex gap-4 justify-center">
                                        <button @click="cancelSpin()"
                                                class="px-6 py-2.5 rounded-xl border border-zinc-700 text-zinc-400 hover:text-white hover:bg-zinc-800 transition-all font-medium">
                                                Cancel
                                        </button>
                                        <button @click="confirmSpin()"
                                                class="px-8 py-2.5 rounded-xl bg-gradient-to-r from-yellow-600 via-yellow-500 to-yellow-400 text-black font-bold hover:shadow-[0_0_20px_rgba(234,179,8,0.4)] hover:scale-105 transition-all transform uppercase tracking-wider text-sm">
                                                Spin Now
                                        </button>
                                </div>
                        </div>
                </div>
        </transition>

        <!-- Error Modal -->
        <transition enter-active-class="transition duration-200 ease-out"
                enter-from-class="transform scale-95 opacity-0" enter-to-class="transform scale-100 opacity-100"
                leave-active-class="transition duration-150 ease-in"
                leave-from-class="transform scale-100 opacity-100"
                leave-to-class="transform scale-95 opacity-0">
                <div v-if="showErrorModal"
                        class="fixed inset-0 z-[110] flex items-center justify-center bg-black/80 backdrop-blur-sm px-4">
                        <div
                                class="bg-zinc-900 border-2 border-red-600/50 rounded-2xl p-6 md:p-8 max-w-sm w-full text-center shadow-[0_0_50px_rgba(239,68,68,0.15)] relative overflow-hidden">
                                <div
                                        class="absolute top-0 left-1/2 -translate-x-1/2 w-32 h-1 bg-red-500 shadow-[0_0_20px_rgba(239,68,68,1)]">
                                </div>

                                <h3
                                        class="text-2xl md:text-3xl font-bold text-transparent bg-clip-text bg-gradient-to-br from-red-300 to-red-600 mb-4 uppercase font-serif tracking-wide">
                                        Error</h3>

                                <div class="space-y-4 mb-8">
                                        <p class="text-gray-300">{{ errorMessage }}</p>
                                </div>

                                <button @click="closeErrorModal()"
                                        class="w-full px-8 py-2.5 rounded-xl bg-gradient-to-r from-red-600 via-red-500 to-red-400 text-white font-bold hover:shadow-[0_0_20px_rgba(239,68,68,0.4)] hover:scale-105 transition-all transform uppercase tracking-wider text-sm">
                                        Close
                                </button>
                        </div>
                </div>
        </transition>

        <!-- Result Modal -->
        <transition enter-active-class="transition duration-500 cubic-bezier(0.19, 1, 0.22, 1)"
                enter-from-class="transform scale-50 opacity-0 translate-y-10"
                enter-to-class="transform scale-100 opacity-100 translate-y-0"
                leave-active-class="transition duration-200 ease-in"
                leave-from-class="transform scale-100 opacity-100"
                leave-to-class="transform scale-95 opacity-0">
                <div v-if="showResultModal"
                        class="fixed inset-0 z-[110] flex items-center justify-center bg-black/90 backdrop-blur-md px-4"
                        @click.self="closeResultModal()">
                        <div
                                class="bg-gradient-to-b from-zinc-800 to-black border border-yellow-500 rounded-2xl p-8 max-w-sm w-full text-center shadow-[0_0_100px_rgba(234,179,8,0.3)] relative overflow-visible">
                                <div class="absolute -top-16 left-1/2 -translate-x-1/2 w-32 h-32">
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
                                                class="text-xl md:text-lg font-black bg-clip-text text-transparent bg-gradient-to-r from-yellow-200 via-yellow-500 to-yellow-200 uppercase tracking-widest drop-shadow-sm font-serif">
                                                CONGRATULATIONS!
                                        </h2>
                                        <p class="text-yellow-100/60 text-sm font-medium tracking-wider">YOU HAVE RECEIVED</p>
                                </div>

                                <div class="my-8 py-4 bg-yellow-900/10 border-y border-yellow-500/20">
                                        <h3 v-if="resultPrize"
                                                class="text-2xl md:text-3xl font-bold text-yellow-400 drop-shadow-[0_2px_4px_rgba(0,0,0,0.8)] px-2 mb-2">
                                                {{ resultPrize.username }}
                                        </h3>
                                        <div v-if="resultPrize.username" class="space-y-2">
                                                <p class="text-lg text-yellow-300">
                                                        <span class="font-semibold">Username:</span> <code class="bg-slate-900">{{ resultPrize.username }}</code>
                                                </p>
                                                <p v-if="resultPrize.password" class="text-lg text-yellow-300">
                                                        <span class="font-semibold">Password:</span> <code class="bg-slate-900">{{ resultPrize.password }}</code> 
                                                </p>
                                                <p v-if="resultPrize.email" class="text-lg text-yellow-300">
                                                        <span class="font-semibold">Email:</span> <code class="bg-slate-900">{{ resultPrize.email }}</code>
                                                </p>
                                        </div>
                                </div>

                                <button @click="closeResultModal()"
                                        class="w-full px-8 py-3.5 rounded-xl bg-gradient-to-r from-yellow-600 via-yellow-500 to-yellow-400 text-black text-lg font-bold hover:shadow-[0_0_30px_rgba(234,179,8,0.6)] hover:-translate-y-1 transition-all transform uppercase tracking-widest shadow-lg">
                                        Close
                                </button>
                        </div>
                </div>
        </transition>
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