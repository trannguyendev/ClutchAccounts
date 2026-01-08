<script setup>
import { ref, computed } from 'vue';

// --------------------------------------------------------------------------
// 2. Props & Emits
// --------------------------------------------------------------------------
const props = defineProps({
    prizes: {
        type: Array,
        required: true,
        // Dạng của mỗi phần tử trong mảng prizes (tham khảo):
        // {
        //     id: number | string,
        //     label: string,
        //     color: string,
        //     text: string,
        //     image?: string,
        //     value?: any
        // }
    },
    size: {
        type: [Number, String],
        default: 500
    },
    duration: {
        type: Number,
        default: 6
    },
    borderWidth: {
        type: Number,
        default: 10
    }
});

const emit = defineEmits(['spin-end', 'spin-start']);

// --------------------------------------------------------------------------
// 3. State Management
// --------------------------------------------------------------------------
const isSpinning = ref(false);
const currentRotation = ref(0); // Tổng số độ đã quay
const wheelRef = ref(null);

// --------------------------------------------------------------------------
// 4. Computed Logic (SVG Calculation)
// --------------------------------------------------------------------------
const totalPrizes = computed(() => props.prizes.length);
const segmentAngle = computed(() => 360 / totalPrizes.value); // Góc của 1 miếng (độ)

// Bán kính & Tâm của SVG (ViewBox 0 0 100 100)
const SVG_SIZE = 100;
const CENTER = SVG_SIZE / 2;
const RADIUS = SVG_SIZE / 2;

// Hàm tính đường dẫn SVG (d hình rẻ quạt) cho miếng thứ i
const getSectorPath = (index) => {
    const startAngle = index * segmentAngle.value;
    const endAngle = (index + 1) * segmentAngle.value;

    // Chuyển đổi sang radian (trừ 90 độ để bắt đầu từ 12h)
    const startRad = (startAngle - 90) * (Math.PI / 180);
    const endRad = (endAngle - 90) * (Math.PI / 180);

    // Tính tọa độ điểm trên đường tròn
    const x1 = CENTER + RADIUS * Math.cos(startRad);
    const y1 = CENTER + RADIUS * Math.sin(startRad);
    const x2 = CENTER + RADIUS * Math.cos(endRad);
    const y2 = CENTER + RADIUS * Math.sin(endRad);

    // SVG Path Command: Move to Center -> Line to Start -> Arc to End -> Close
    // Large Arc Flag: 0 nếu góc < 180, 1 nếu góc > 180
    const largeArcFlag = segmentAngle.value > 180 ? 1 : 0;

    return `M ${CENTER} ${CENTER} L ${x1} ${y1} A ${RADIUS} ${RADIUS} 0 ${largeArcFlag} 1 ${x2} ${y2} Z`;
};

// Hàm tính rotation cho text để nó nằm giữa miếng và hướng về tâm
const getTextRotation = (index) => {
    // Góc giữa của miếng
    const angle = (index * segmentAngle.value) + (segmentAngle.value / 2);
    // Text rotation: xoay theo góc slice
    return `rotate(${angle}, ${CENTER}, ${CENTER})`;
};

// --------------------------------------------------------------------------
// 5. Spin Logic (Core Feature)
// --------------------------------------------------------------------------
const spin = (targetIndex) => {
    if (isSpinning.value) return;
    if (targetIndex < 0 || targetIndex >= totalPrizes.value) {
        console.warn("Invalid target index");
        return;
    }

    isSpinning.value = true;

    // Tính toán góc cần đến
    // Mục tiêu: Đưa miếng targetIndex về vị trí kim (12h - góc 0 độ hoặc 360 độ)
    // Góc tâm của miếng target:
    const targetSegmentAngle = (targetIndex * segmentAngle.value) + (segmentAngle.value / 2);

    // Kim cố định ở 12h.
    // Miếng số 0 bắt đầu từ 12h (do logic vẽ SVG start -90 radian).
    // Miếng i nằm ở góc (i * angle).
    // Để miếng i đến kim, ta cần xoay đĩa một góc alpha sao cho điểm giữa miếng i trùng 12h.
    // Góc hiện tại của miếng i (tuyệt đối so với div cha) = Rotation + Angle_i.
    // Ta cần: (Rotation_Mới + Angle_i) % 360 = 0 (hoặc = 360*k).
    // => Rotation_Mới = (360 * k) - Angle_i.

    // Để tạo hiệu ứng quay tiếp (không quay lùi), ta cộng thêm số vòng (SPINS).
    const MIN_SPINS = 5;
    const currentTotal = currentRotation.value;

    // Tính góc thô cần đạt tới (bỏ qua số vòng) -> mục tiêu là -targetSegmentAngle
    // Tuy nhiên để mượt, ta lấy phần dư hiện tại
    const currentMod = currentTotal % 360;

    // Tính khoảng cách cần quay thêm để đến đích
    // Đích (trên vòng tròn 360): (360 - targetSegmentAngle) % 360.
    // Ví dụ: Target ở 90 độ. Cần quay div sao cho 90 độ về 0. Tức là quay -90 độ hay +270 độ.
    // Đích = (360 - targetSegmentAngle).

    let distance = (360 - targetSegmentAngle) - currentMod;

    // Đảm bảo distance luôn dương để quay tới
    while (distance < 0) {
        distance += 360;
    }

    // Cộng thêm số vòng quay tối thiểu
    const finalRotation = currentTotal + (MIN_SPINS * 360) + distance;

    // Thực hiện Animation
    currentRotation.value = finalRotation;

    // Reset flag khi quay xong
    setTimeout(() => {
        isSpinning.value = false;
        emit('spin-end', props.prizes[targetIndex]);
    }, props.duration * 1000); // Khớp với transition duration
};

// Expose method spin ra cho component cha dùng
defineExpose({
    spin
});

</script>

<template>
    <div class="relative flex items-center justify-center font-sans select-none"
        :style="{ width: typeof size === 'number' ? `${size}px` : size, height: typeof size === 'number' ? `${size}px` : size }">

        <!-- 1. Vòng quay chính (Wheel) -->
        <div ref="wheelRef" class="w-full h-full rounded-full relative z-10 overflow-hidden shadow-2xl" :style="{
            transform: `rotate(${currentRotation}deg)`,
            transition: `transform ${duration}s cubic-bezier(0.25, 0.1, 0.25, 1)` /* Ease-out cubic bezier for natural spin */
        }">

            <!-- SVG Render -->
            <!-- ViewBox 0 0 100 100 để dễ scale theo % -->
            <svg viewBox="0 0 100 100" class="w-full h-full transform drop-shadow-md"
                :class="{ 'pointer-events-none': isSpinning }">
                <!-- Render từng miếng (Slice) -->
                <g v-for="(prize, index) in prizes" :key="prize.id" class="group">
                    <!-- Đường path rẻ quạt (Normal State) -->
                    <path :d="getSectorPath(index)" :fill="prize.color" stroke="#FDB931" stroke-width="0.5" />

                    <!-- Text và Icon (Normal) - Adjusted for compatibility with Images -->
                    <g :transform="getTextRotation(index)">
                        <foreignObject x="38" y="4" width="24" height="42">
                            <div
                                class="flex flex-col items-center justify-start h-full w-full pt-2 pointer-events-none">
                                <!-- Image (if exists) -->
                                <img v-if="prize.image" :src="prize.image"
                                    class="w-4 h-4 object-contain mb-1 drop-shadow-sm" alt="prize-icon" />

                                <!-- Label -->
                                <div class="text-[0.22rem] md:text-[0.25rem] font-bold uppercase tracking-widest text-center leading-none shadow-sm pb-1"
                                    style="writing-mode: vertical-rl; text-orientation: mixed;"
                                    :style="{ color: prize.text }">
                                    {{ prize.label }}
                                </div>

                                <!-- Decorative Icon (only if no image) -->
                                <div v-if="!prize.image" class="text-[0.3rem] mt-1 opacity-80"
                                    :style="{ color: prize.text }">♦</div>
                            </div>
                        </foreignObject>
                    </g>
                </g>


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

            <button @click="$emit('spin-start')"
                class="w-16 h-16 md:w-20 md:h-20 rounded-full bg-gradient-to-br from-yellow-300 via-yellow-500 to-yellow-700 shadow-[inset_0_2px_5px_rgba(255,255,255,0.4),0_4px_10px_rgba(0,0,0,0.5)] flex items-center justify-center border-4 border-yellow-900 group active:scale-95 hover:scale-110 hover:shadow-[0_0_20px_rgba(255,215,0,0.6)] transition-all duration-300 ease-out">

                <span
                    class="font-serif font-bold text-yellow-950 text-sm md:text-base tracking-widest drop-shadow-sm group-hover:text-black transition-colors">
                    QUAY
                </span>
            </button>
        </div>

    </div>
</template>

<style scoped>
/* Gradient Text Style nếu cần */
.text-gold {
    color: #FFD700;
}
</style>