<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-black via-neutral-900 to-black px-4 py-8">
    <div class="text-center space-y-6 max-w-md">
      <!-- Large 403 Text -->
      <div class="space-y-2">
        <h1 class="text-8xl md:text-9xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-amber-400 to-amber-600">403</h1>
        <p class="text-2xl md:text-3xl font-bold text-amber-300">Access Denied</p>
      </div>

      <!-- Description -->
      <p class="text-amber-200/80 text-base md:text-lg leading-relaxed">
        You don't have permission to access this resource. Your account may lack the required privileges.
      </p>

      <!-- Back Button -->
      <button @click="goBack"
        class="inline-flex items-center space-x-2 px-6 py-3 rounded-lg bg-gradient-to-r from-amber-500 to-amber-600 hover:from-amber-600 hover:to-amber-700 text-black font-bold transition-all duration-300 transform hover:scale-105 shadow-lg hover:shadow-amber-500/50">
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
        </svg>
        <span>Go Back</span>
      </button>

      <!-- Decorative elements -->
      <div class="pt-12 space-y-3 text-amber-300/40">
        <div class="h-px bg-gradient-to-r from-transparent via-amber-500 to-transparent"></div>
        <p class="text-xs tracking-widest uppercase">Error • Forbidden</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
button {
  box-shadow: 0 8px 24px rgba(255, 184, 28, 0.15);
}

button:hover {
  box-shadow: 0 12px 32px rgba(255, 184, 28, 0.25);
}

/* Optional: subtle animation on load */
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

div {
  animation: slideUp 0.6s ease-out;
}
</style>