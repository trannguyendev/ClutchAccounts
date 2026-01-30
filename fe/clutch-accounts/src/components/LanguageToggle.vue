<script setup>
import { useI18n } from 'vue-i18n'
import { ref, computed } from 'vue'
import enFlag from '../img/flags.png'
import viFlag from '../img/vietnam.png'

const { locale, t } = useI18n()
const isDropdownOpen = ref(false)

const currentLanguage = computed({
  get: () => locale.value,
  set: (newLocale) => {
    locale.value = newLocale
    localStorage.setItem('language', newLocale)
    isDropdownOpen.value = false
  }
})

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const selectLanguage = (lang) => {
  currentLanguage.value = lang
}

const flagImages = {
  en: enFlag,
  vi: viFlag
}
</script>

<template>
  <div class="relative">
    <!-- Language Toggle Button -->
    <button
      @click="toggleDropdown"
      type="button"
      class="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-amber-900/30 border border-amber-600/50 text-amber-200 hover:text-amber-300 hover:bg-amber-900/40 font-semibold transition-all duration-200"
      :aria-label="t('common.language')"
    >
      <i class="fa-solid fa-globe text-base"></i>
      <span class="hidden sm:inline uppercase text-sm">{{ currentLanguage }}</span>
      <svg
        class="w-4 h-4 transition-transform duration-200"
        :class="{ 'rotate-180': isDropdownOpen }"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
      </svg>
    </button>

    <!-- Dropdown Menu -->
    <div
      v-show="isDropdownOpen"
      class="absolute right-0 top-full mt-2 w-40 bg-black/90 border border-amber-600/50 rounded-lg shadow-lg backdrop-blur-sm z-50"
    >
      <div
        v-for="lang in ['en', 'vi']"
        :key="lang"
        @click="selectLanguage(lang)"
        :class="[
          'px-4 py-3 cursor-pointer transition-colors duration-150 flex items-center gap-2',
          currentLanguage === lang
            ? 'bg-amber-900/40 text-amber-300 border-l-2 border-amber-300'
            : 'text-amber-200 hover:bg-amber-900/20 hover:text-amber-300'
        ]"
      >
        <img
          :src="flagImages[lang]"
          :alt="lang === 'en' ? 'English' : 'Vietnamese'"
          class="rounded"
        />
        <span class="whitespace-nowrap">{{ lang === 'en' ? t('common.english') : t('common.vietnamese') }}</span>
        <i v-if="currentLanguage === lang" class="fa-solid fa-check ml-auto text-amber-300"></i>
      </div>
    </div>

    <!-- Overlay to close dropdown when clicking outside -->
    <div
      v-if="isDropdownOpen"
      @click="isDropdownOpen = false"
      class="fixed inset-0 z-40"
    ></div>
  </div>
</template>

<style scoped>
/* Smooth transitions */
button {
  transition: all 0.2s ease;
}

button:hover {
  box-shadow: 0 4px 12px rgba(255, 184, 28, 0.1);
}
</style>
