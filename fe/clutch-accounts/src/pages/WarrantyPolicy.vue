<script setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import Navbar from '@/components/Navbar.vue'
import Footer from '@/components/Footer.vue'

const { t } = useI18n()

// FAQ accordion state
const openFaqIndex = ref(null)

const toggleFaq = (index) => {
  openFaqIndex.value = openFaqIndex.value === index ? null : index
}

// Smooth scroll to section
const scrollToSection = (sectionId) => {
  const element = document.getElementById(sectionId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

// Warranty sections data
const warrantySections = [
  {
    id: 'coverage',
    icon: '🛡️',
    titleKey: 'warranty.coverageTitle',
    descKey: 'warranty.coverageDesc'
  },
  {
    id: 'duration',
    icon: '⏱️',
    titleKey: 'warranty.durationTitle',
    descKey: 'warranty.durationDesc'
  },
  {
    id: 'process',
    icon: '📋',
    titleKey: 'warranty.processTitle',
    descKey: 'warranty.processDesc'
  },
  {
    id: 'exclusions',
    icon: '⚠️',
    titleKey: 'warranty.exclusionsTitle',
    descKey: 'warranty.exclusionsDesc'
  }
]
</script>

<template>
  <div class="min-h-screen bg-black">
    <Navbar />

    <!-- Hero Section -->
    <section class="relative py-20 overflow-hidden">
      <!-- Background Effects -->
      <div class="absolute inset-0">
        <div class="absolute top-0 left-1/4 w-96 h-96 bg-gold-500/10 rounded-full blur-3xl animate-pulse"></div>
        <div class="absolute bottom-0 right-1/4 w-80 h-80 bg-yellow-600/10 rounded-full blur-3xl animate-pulse delay-1000"></div>
      </div>

      <div class="relative container mx-auto px-4 text-center">
        <h1 class="text-5xl md:text-6xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-gold-400 via-yellow-500 to-gold-600 mb-6 animate-fade-in">
          {{ t('warranty.heroTitle') }}
        </h1>
        <p class="text-xl text-gray-300 max-w-3xl mx-auto animate-fade-in-up">
          {{ t('warranty.heroSubtitle') }}
        </p>
        <button
          @click="scrollToSection('coverage')"
          class="mt-8 px-8 py-3 bg-gradient-to-r from-gold-500 to-yellow-600 text-black font-semibold rounded-lg hover:from-gold-400 hover:to-yellow-500 transition-all duration-300 transform hover:scale-105 hover:shadow-lg hover:shadow-gold-500/25"
        >
          {{ t('warranty.learnMore') }}
        </button>
      </div>
    </section>

    <!-- Warranty Coverage Cards -->
    <section id="coverage" class="py-16 relative">
      <div class="container mx-auto px-4">
        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div
            v-for="(section, index) in warrantySections"
            :key="section.id"
            class="group bg-gradient-to-br from-gray-900/80 to-black/80 border border-gold-500/20 rounded-xl p-6 hover:border-gold-500/50 transition-all duration-500 hover:transform hover:-translate-y-2 hover:shadow-xl hover:shadow-gold-500/10"
            :style="{ animationDelay: `${index * 100}ms` }"
          >
            <div class="text-4xl mb-4 transform group-hover:scale-110 group-hover:rotate-3 transition-transform duration-300">
              {{ section.icon }}
            </div>
            <h3 class="text-xl font-bold text-gold-400 mb-3 group-hover:text-gold-300 transition-colors">
              {{ t(section.titleKey) }}
            </h3>
            <p class="text-gray-400 text-sm group-hover:text-gray-300 transition-colors">
              {{ t(section.descKey) }}
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- Detailed Warranty Information -->
    <section class="py-16 relative">
      <div class="container mx-auto px-4">
        <!-- Coverage Details -->
        <div class="mb-16">
          <h2 class="text-3xl font-bold text-center text-gold-400 mb-12">
            <span class="border-b-2 border-gold-500 pb-2">{{ t('warranty.whatCovered') }}</span>
          </h2>
          <div class="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto">
            <div
              v-for="(item, index) in [
                { icon: '✅', title: 'warranty.coveredItems.accountLock', desc: 'warranty.coveredItems.accountLockDesc' },
                { icon: '✅', title: 'warranty.coveredItems.security', desc: 'warranty.coveredItems.securityDesc' },
                { icon: '✅', title: 'warranty.coveredItems.delivery', desc: 'warranty.coveredItems.deliveryDesc' },
                { icon: '✅', title: 'warranty.coveredItems.access', desc: 'warranty.coveredItems.accessDesc' }
              ]"
              :key="index"
              class="flex items-start gap-4 p-4 bg-gray-900/50 border border-gold-500/10 rounded-lg hover:border-gold-500/30 transition-all duration-300 hover:bg-gray-900/70"
            >
              <span class="text-2xl">{{ item.icon }}</span>
              <div>
                <h4 class="text-gold-400 font-semibold mb-1">{{ t(item.title) }}</h4>
                <p class="text-gray-400 text-sm">{{ t(item.desc) }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- What's Not Covered -->
        <div class="max-w-4xl mx-auto mb-16">
          <h2 class="text-3xl font-bold text-center text-gold-400 mb-12">
            <span class="border-b-2 border-gold-500 pb-2">{{ t('warranty.notCovered') }}</span>
          </h2>
          <div class="grid md:grid-cols-2 gap-6">
            <div
              v-for="(item, index) in [
                { icon: '❌', title: 'warranty.notCoveredItems.userAction', desc: 'warranty.notCoveredItems.userActionDesc' },
                { icon: '❌', title: 'warranty.notCoveredItems.gameUpdate', desc: 'warranty.notCoveredItems.gameUpdateDesc' },
                { icon: '❌', title: 'warranty.notCoveredItems.thirdParty', desc: 'warranty.notCoveredItems.thirdPartyDesc' },
                { icon: '❌', title: 'warranty.notCoveredItems.misuse', desc: 'warranty.notCoveredItems.misuseDesc' }
              ]"
              :key="index"
              class="flex items-start gap-4 p-4 bg-red-900/10 border border-red-500/20 rounded-lg hover:border-red-500/40 transition-all duration-300 hover:bg-red-900/20"
            >
              <span class="text-2xl">{{ item.icon }}</span>
              <div>
                <h4 class="text-red-400 font-semibold mb-1">{{ t(item.title) }}</h4>
                <p class="text-gray-400 text-sm">{{ t(item.desc) }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Claim Process -->
        <div class="max-w-5xl mx-auto mb-16">
          <h2 class="text-3xl font-bold text-center text-gold-400 mb-12">
            <span class="border-b-2 border-gold-500 pb-2">{{ t('warranty.claimProcess') }}</span>
          </h2>
          <div class="grid md:grid-cols-4 gap-6">
            <div
              v-for="(step, index) in [
                { num: 1, icon: '📧', title: 'warranty.steps.contact', desc: 'warranty.steps.contactDesc' },
                { num: 2, icon: '📝', title: 'warranty.steps.provide', desc: 'warranty.steps.provideDesc' },
                { num: 3, icon: '⏳', title: 'warranty.steps.review', desc: 'warranty.steps.reviewDesc' },
                { num: 4, icon: '🎉', title: 'warranty.steps.resolve', desc: 'warranty.steps.resolveDesc' }
              ]"
              :key="index"
              class="relative text-center"
            >
              <div class="absolute top-8 left-1/2 w-full h-12 bg-gradient-to-r from-gold-500/30 to-yellow-500/30 transform -translate-x-1/2 hidden md:block" v-if="index < 3"></div>
              <div class="w-16 h-16 mx-auto mb-4 bg-gradient-to-br from-gold-500 to-yellow-600 rounded-full flex items-center justify-center text-2xl font-bold text-black relative z-10 transform hover:scale-110 hover:rotate-12 transition-all duration-300 shadow-lg shadow-gold-500/30">
                {{ step.num }}
              </div>
              <div class="text-3xl mb-2">{{ step.icon }}</div>
              <h4 class="text-gold-400 font-semibold mb-2">{{ t(step.title) }}</h4>
              <p class="text-gray-400 text-sm">{{ t(step.desc) }}</p>
            </div>
          </div>
        </div>

        <!-- Duration Table -->
        <div class="max-w-4xl mx-auto mb-16">
          <h2 class="text-3xl font-bold text-center text-gold-400 mb-12">
            <span class="border-b-2 border-gold-500 pb-2">{{ t('warranty.warrantyDuration') }}</span>
          </h2>
          <div class="overflow-x-auto">
            <table class="w-full bg-gray-900/50 border border-gold-500/20 rounded-xl overflow-hidden">
              <thead>
                <tr class="bg-gradient-to-r from-gold-500/20 to-yellow-500/20">
                  <th class="py-4 px-6 text-left text-gold-400 font-semibold">{{ t('warranty.table.accountType') }}</th>
                  <th class="py-4 px-6 text-center text-gold-400 font-semibold">{{ t('warranty.table.duration') }}</th>
                  <th class="py-4 px-6 text-center text-gold-400 font-semibold">{{ t('warranty.table.coverage') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(row, index) in [
                    { type: 'warranty.table.rows.warranty', duration: '30 ' + t('warranty.table.days'), coverage: t('warranty.table.rows.fullCoverage') },
                    { type: 'warranty.table.rows.regular', duration: '7 ' + t('warranty.table.days'), coverage: t('warranty.table.rows.basicCoverage') },
                    { type: 'warranty.table.rows.vip', duration: '60 ' + t('warranty.table.days'), coverage: t('warranty.table.rows.extendedCoverage') }
                  ]"
                  :key="index"
                  class="border-t border-gold-500/10 hover:bg-gold-500/5 transition-colors"
                >
                  <td class="py-4 px-6 text-gray-300">{{ row.type }}</td>
                  <td class="py-4 px-6 text-center text-gold-400 font-semibold">{{ row.duration }}</td>
                  <td class="py-4 px-6 text-center text-gray-400">{{ row.coverage }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>

    <!-- FAQ Section -->
    <section class="py-16 relative bg-gradient-to-b from-black via-gray-900 to-black">
      <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center text-gold-400 mb-12">
          <span class="border-b-2 border-gold-500 pb-2">{{ t('warranty.faqTitle') }}</span>
        </h2>
        <div class="max-w-3xl mx-auto space-y-4">
          <div
            v-for="(faq, index) in [
              { question: 'warranty.faq.q1', answer: 'warranty.faq.a1' },
              { question: 'warranty.faq.q2', answer: 'warranty.faq.a2' },
              { question: 'warranty.faq.q3', answer: 'warranty.faq.a3' },
              { question: 'warranty.faq.q4', answer: 'warranty.faq.a4' }
            ]"
            :key="index"
            class="border border-gold-500/20 rounded-lg overflow-hidden bg-gray-900/50"
          >
            <button
              @click="toggleFaq(index)"
              class="w-full px-6 py-4 text-left flex items-center justify-between hover:bg-gold-500/5 transition-colors"
            >
              <span class="text-gold-400 font-semibold">{{ t(faq.question) }}</span>
              <span
                class="text-gold-500 transform transition-transform duration-300"
                :class="{ 'rotate-180': openFaqIndex === index }"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                </svg>
              </span>
            </button>
            <div
              v-show="openFaqIndex === index"
              class="px-6 py-4 text-gray-400 bg-gray-900/30 border-t border-gold-500/10 animate-slide-down"
            >
              {{ t(faq.answer) }}
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Support -->
    <section class="py-16 relative">
      <div class="container mx-auto px-4">
        <div class="max-w-4xl mx-auto text-center">
          <h2 class="text-3xl font-bold text-gold-400 mb-6">{{ t('warranty.needHelp') }}</h2>
          <p class="text-gray-300 mb-8">{{ t('warranty.contactDesc') }}</p>
          <div class="grid md:grid-cols-3 gap-6">
            <a
              href="#"
              class="group p-6 bg-gradient-to-br from-gray-900/80 to-black/80 border border-gold-500/20 rounded-xl hover:border-gold-500/50 transition-all duration-300 hover:transform hover:-translate-y-2 hover:shadow-xl hover:shadow-gold-500/10"
            >
              <div class="text-4xl mb-4 transform group-hover:scale-110 transition-transform">💬</div>
              <h4 class="text-gold-400 font-semibold mb-2">{{ t('warranty.contact.liveChat') }}</h4>
              <p class="text-gray-400 text-sm">{{ t('warranty.contact.liveChatDesc') }}</p>
            </a>
            <a
              href="#"
              class="group p-6 bg-gradient-to-br from-gray-900/80 to-black/80 border border-gold-500/20 rounded-xl hover:border-gold-500/50 transition-all duration-300 hover:transform hover:-translate-y-2 hover:shadow-xl hover:shadow-gold-500/10"
            >
              <div class="text-4xl mb-4 transform group-hover:scale-110 transition-transform">📧</div>
              <h4 class="text-gold-400 font-semibold mb-2">{{ t('warranty.contact.email') }}</h4>
              <p class="text-gray-400 text-sm">{{ t('warranty.contact.emailDesc') }}</p>
            </a>
            <a
              href="#"
              class="group p-6 bg-gradient-to-br from-gray-900/80 to-black/80 border border-gold-500/20 rounded-xl hover:border-gold-500/50 transition-all duration-300 hover:transform hover:-translate-y-2 hover:shadow-xl hover:shadow-gold-500/10"
            >
              <div class="text-4xl mb-4 transform group-hover:scale-110 transition-transform">📱</div>
              <h4 class="text-gold-400 font-semibold mb-2">{{ t('warranty.contact.social') }}</h4>
              <p class="text-gray-400 text-sm">{{ t('warranty.contact.socialDesc') }}</p>
            </a>
          </div>
        </div>
      </div>
    </section>

    <!-- Back to Top Button -->
    <button
      @click="scrollToSection('top')"
      class="fixed bottom-8 right-8 w-12 h-12 bg-gradient-to-r from-gold-500 to-yellow-600 rounded-full flex items-center justify-center text-black font-bold hover:from-gold-400 hover:to-yellow-500 transition-all duration-300 transform hover:scale-110 hover:shadow-lg hover:shadow-gold-500/30 z-50"
      id="top"
    >
      <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
      </svg>
    </button>
  </div>
</template>

<style scoped>
/* Custom Gold Colors */
.bg-gold-500 {
  background-color: #d4af37;
}

.bg-gold-400 {
  background-color: #e5c158;
}

.bg-gold-600 {
  background-color: #c5a028;
}

.bg-gradient-to-r from-gold-500 {
  background-image: linear-gradient(to right, #d4af37, #c5a028);
}

.bg-gradient-to-br from-gold-500 {
  background-image: linear-gradient(to bottom right, #d4af37, #c5a028);
}

.text-gold-400 {
  color: #e5c158;
}

.text-gold-500 {
  color: #d4af37;
}

.border-gold-500 {
  border-color: #d4af37;
}

.hover\:from-gold-400:hover {
  background-image: linear-gradient(to right, #e5c158, #d4af37);
}

/* Animations */
@keyframes fade-in {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slide-down {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fade-in 0.8s ease-out forwards;
}

.animate-fade-in-up {
  animation: fade-in-up 0.8s ease-out 0.2s forwards;
  opacity: 0;
}

.animate-slide-down {
  animation: slide-down 0.3s ease-out forwards;
}

/* Hover Effects */
.group:hover .group-hover\:rotate-3 {
  transform: rotate(3deg);
}

.group:hover .group-hover\:scale-110 {
  transform: scale(1.1);
}

/* Smooth scroll behavior */
html {
  scroll-behavior: smooth;
}

/* Pulse animation for hero elements */
@keyframes pulse {
  0%, 100% {
    opacity: 0.5;
  }
  50% {
    opacity: 0.8;
  }
}

.animate-pulse {
  animation: pulse 3s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

.delay-1000 {
  animation-delay: 1s;
}
</style>
