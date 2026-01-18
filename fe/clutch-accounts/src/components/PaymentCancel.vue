<script setup>
import { ref, onMounted } from 'vue'

const isAnimated = ref(false)

onMounted(() => {
  setTimeout(() => {
    isAnimated.value = true
  }, 100)
})
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#0a0a0a] via-[#1a1a1a] to-[#0d0d0d] p-5 relative overflow-hidden">
    <!-- Background decorative elements -->
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
      <div class="absolute -top-1/2 -left-1/2 w-[200%] h-[200%] animate-pulse opacity-50">
        <div class="absolute top-[30%] left-[30%] w-96 h-96 bg-[#d4af37]/5 rounded-full blur-3xl"></div>
        <div class="absolute bottom-[30%] right-[30%] w-96 h-96 bg-red-600/10 rounded-full blur-3xl"></div>
      </div>
    </div>

    <!-- Main Card -->
    <div 
      class="relative z-10 w-full max-w-lg bg-gradient-to-br from-[#1a1a1a] to-[#0f0f0f] border border-[#d4af37]/20 rounded-3xl p-12 text-center shadow-2xl transform transition-all duration-700"
      :class="isAnimated ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-8 scale-95'"
    >
      <!-- Gold corner accents -->
      <div class="absolute top-0 left-0 w-16 h-16 border-l-2 border-t-2 border-[#d4af37]/40 rounded-tl-3xl"></div>
      <div class="absolute bottom-0 right-0 w-16 h-16 border-r-2 border-b-2 border-[#d4af37]/40 rounded-br-3xl"></div>

      <!-- Animated X Icon -->
      <div class="mb-8 relative">
        <div class="w-28 h-28 mx-auto relative">
          <!-- Pulsing glow background -->
          <div class="absolute inset-0 flex items-center justify-center">
            <div class="w-24 h-24 bg-red-500/20 rounded-full animate-ping"></div>
          </div>
          <div class="absolute inset-0 flex items-center justify-center">
            <div class="w-20 h-20 bg-gradient-radial from-red-500/30 to-transparent rounded-full animate-pulse"></div>
          </div>
          
          <!-- X Icon SVG -->
          <svg viewBox="0 0 52 52" class="w-full h-full relative z-10 -rotate-90">
            <circle 
              cx="26" 
              cy="26" 
              r="24" 
              fill="none" 
              stroke="#dc3545"
              stroke-width="2"
              class="drop-shadow-[0_0_10px_rgba(220,53,69,0.6)]"
              :class="isAnimated ? 'animate-circle-draw' : ''"
              style="stroke-dasharray: 151; stroke-dashoffset: 151;"
            />
            <path 
              d="M17 17 L35 35 M35 17 L17 35" 
              fill="none" 
              stroke="#dc3545"
              stroke-width="3"
              stroke-linecap="round"
              class="rotate-90 origin-center drop-shadow-[0_0_8px_rgba(220,53,69,0.7)]"
              :class="isAnimated ? 'animate-x-draw' : ''"
              style="stroke-dasharray: 50; stroke-dashoffset: 50;"
            />
          </svg>
        </div>
      </div>

      <!-- Title -->
      <h1 
        class="text-3xl md:text-4xl font-bold text-red-500 mb-6 uppercase tracking-widest transition-all duration-500 delay-300"
        :class="isAnimated ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'"
        style="text-shadow: 0 0 30px rgba(220, 53, 69, 0.4);"
      >
        Payment Cancelled
      </h1>

      <!-- Description -->
      <p 
        class="text-gray-400 text-base md:text-lg leading-relaxed mb-8 transition-all duration-500 delay-500"
        :class="isAnimated ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'"
      >
        Your payment has been cancelled. No charges have been made to your account.
        If you have any questions or need assistance, please don't hesitate to contact our support team.
      </p>

      <!-- Divider -->
      <div 
        class="h-px bg-gradient-to-r from-transparent via-[#d4af37]/50 to-transparent my-8 transition-all duration-700 delay-700"
        :class="isAnimated ? 'scale-x-100' : 'scale-x-0'"
      ></div>

      <!-- Return Button -->
      <router-link 
        to="/main" 
        class="group inline-flex items-center justify-center gap-3 px-10 py-4 bg-gradient-to-r from-[#d4af37] via-[#f0d060] to-[#d4af37] bg-[length:200%_100%] text-[#0a0a0a] font-semibold text-base uppercase tracking-wider rounded-xl shadow-lg shadow-[#d4af37]/20 transition-all duration-500 delay-[900ms] hover:bg-right hover:shadow-xl hover:shadow-[#d4af37]/40 hover:scale-105 hover:-translate-y-1 active:scale-95"
        :class="isAnimated ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'"
      >
        <span class="transition-transform duration-300 group-hover:-translate-x-1">
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
            <polyline points="9 22 9 12 15 12 15 22"></polyline>
          </svg>
        </span>
        <span class="relative">
          Return to Home
          <span class="absolute bottom-0 left-0 w-0 h-0.5 bg-[#0a0a0a] transition-all duration-300 group-hover:w-full"></span>
        </span>
      </router-link>

      <!-- Additional info -->
      <p 
        class="mt-8 text-gray-500 text-sm transition-all duration-500 delay-[1100ms]"
        :class="isAnimated ? 'opacity-100' : 'opacity-0'"
      >
        Need help? 
        <a href="#" class="text-[#d4af37] hover:text-[#f0d060] transition-colors duration-300 underline underline-offset-2">
          Contact Support
        </a>
      </p>
    </div>
  </div>
</template>

<style scoped>
/* Custom animations for SVG drawing */
@keyframes circle-draw {
  to {
    stroke-dashoffset: 0;
  }
}

@keyframes x-draw {
  to {
    stroke-dashoffset: 0;
  }
}

.animate-circle-draw {
  animation: circle-draw 0.8s ease-out 0.3s forwards;
}

.animate-x-draw {
  animation: x-draw 0.5s ease-out 1s forwards;
}

/* Smooth background animation */
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}
</style>
