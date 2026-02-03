<template>
  <div class="min-h-screen bg-black relative overflow-hidden" style="background: linear-gradient(135deg, #0a0e27 0%, #1a1f3a 50%, #0f1a2e 100%)">
    <!-- Premium Background Pattern with Gold Accents -->
    <div class="fixed inset-0 opacity-10 pointer-events-none" style="background-image: repeating-linear-gradient(45deg, #d4af37 0px, #d4af37 2px, transparent 2px, transparent 10px), repeating-linear-gradient(-45deg, #d4af37 0px, #d4af37 2px, transparent 2px, transparent 10px); background-size: 40px 40px;"></div>

    <!-- Gradient Overlay Lines -->
    <div class="fixed top-0 right-0 w-1/2 h-1/3 opacity-[0.03] pointer-events-none" style="background: radial-gradient(circle at top right, #d4af37, transparent); border-radius: 50%;"></div>
    
    <!-- Content -->
    <div class="relative z-10">
      <!-- Top Navigation / Back Button -->
      <div class="px-4 sm:px-8 lg:px-12 py-6 flex justify-start">
        <RouterLink to="/">
          <button class="group inline-flex items-center gap-2 px-5 py-2.5 bg-gradient-to-r from-amber-600 to-yellow-500 hover:from-amber-500 hover:to-yellow-400 text-white font-bold text-sm uppercase tracking-wider rounded-lg transition-all duration-300 transform hover:scale-110 hover:shadow-2xl shadow-lg hover:shadow-amber-600/50 border border-amber-400/30">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" class="w-4 h-4">
              <polyline points="15 18 9 12 15 6"></polyline>
            </svg>
            <span>Quay lại</span>
          </button>
        </RouterLink>
      </div>

      <!-- Hero Section -->
      <div class="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12 py-12 sm:py-16">
        <!-- Section Title -->
        <div class="text-center mb-20">
          <div class="inline-block mb-6">
            <span class="text-amber-500 font-bold text-sm uppercase tracking-[3px] drop-shadow-lg">Premium Support</span>
          </div>
          <h1 class="text-6xl sm:text-7xl lg:text-8xl font-black tracking-tighter mb-6 leading-none">
            <span class="text-white">CÂU HỎI</span>
            <br />
            <span class="bg-gradient-to-r from-amber-400 via-yellow-300 to-amber-500 bg-clip-text text-transparent drop-shadow-xl">THƯỜNG GẶP</span>
          </h1>
          <p class="text-gray-400 text-lg sm:text-xl max-w-2xl mx-auto font-light">Tìm hiểu về dịch vụ của chúng tôi</p>
          
          <!-- Decorative Line -->
          <div class="flex items-center justify-center gap-4 mt-8">
            <div class="w-12 h-1 bg-gradient-to-r from-transparent to-amber-500"></div>
            <div class="w-2 h-2 bg-amber-500 rounded-full"></div>
            <div class="w-12 h-1 bg-gradient-to-l from-transparent to-amber-500"></div>
          </div>
        </div>

        <!-- FAQ Grid -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 auto-rows-max max-w-5xl mx-auto">
          <div
            v-for="(item, index) in faqItems"
            :key="index"
            class="faq-card group relative bg-gradient-to-br from-slate-900/90 to-slate-950/90 border border-amber-600/20 rounded-lg overflow-hidden backdrop-blur-xl transition-all duration-500 hover:border-amber-500/60 hover:shadow-2xl hover:shadow-amber-900/40"
            :class="{ 'border-amber-500/100 bg-gradient-to-br from-slate-900 to-slate-950 shadow-2xl shadow-amber-900/50': openedIndex === index }"
          >
            <!-- Card Glow Effect -->
            <div class="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-500 pointer-events-none" style="background: radial-gradient(circle at var(--mouse-x, 50%) var(--mouse-y, 50%), rgba(212, 175, 55, 0.1), transparent 80%);"></div>

            <!-- Content -->
            <button
              @click="toggleFAQ(index)"
              :aria-expanded="openedIndex === index"
              class="relative w-full px-6 sm:px-8 py-7 text-left flex items-center justify-between gap-4 transition-all duration-300 hover:pl-8"
              :class="{ 'bg-amber-950/20 pl-10': openedIndex === index }"
            >
              <!-- Question Text -->
              <span class="text-base sm:text-lg font-bold text-gray-100 group-hover:text-amber-300 transition-colors duration-300 tracking-wide" :class="{ 'text-amber-400': openedIndex === index }">
                {{ item.question }}
              </span>
              
              <!-- Toggle Icon -->
              <span class="flex-shrink-0 w-8 h-8 flex items-center justify-center text-amber-400 group-hover:text-amber-300 transition-all duration-500" :class="{ 'rotate-45 scale-125 text-amber-300': openedIndex === index }">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" class="w-full h-full">
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
              </span>
            </button>

            <!-- Answer -->
            <transition
              name="slide-down"
              @enter="onEnter"
              @leave="onLeave"
            >
            <div v-if="openedIndex === index" class="relative px-6 sm:px-8 pb-7 pt-2 border-t border-amber-600/30">
                <div class="absolute inset-0 bg-gradient-to-b from-amber-950/10 to-transparent pointer-events-none"></div>
                <p class="text-sm sm:text-base text-gray-300 leading-relaxed font-light relative z-10">{{ item.answer }}</p>
                
                <!-- Helpful Buttons -->
                <div class="absolute bottom-4 right-4 flex items-center gap-3">
                  <span class="text-xs text-gray-500">Hữu ích?</span>
                  <button
                    @click="voteHelpful(index, true)"
                    :class="{ 
                      'bg-green-600/20 border-green-500 text-green-400': getVoteState(index, true),
                      'hover:bg-green-900/20 hover:border-green-600/50 text-gray-400 hover:text-green-400': !getVoteState(index, true)
                    }"
                    class="p-2 border border-gray-600/30 rounded-lg transition-all duration-300 hover:scale-110"
                    title="Useful"
                    aria-label="Helpful"
                  >
                    <i class="fa fa-thumbs-up" aria-hidden="true"></i>
                  </button>
                  <button
                    @click="voteHelpful(index, false)"
                    :class="{ 
                      'bg-red-600/20 border-red-500 text-red-400': getVoteState(index, false),
                      'hover:bg-red-900/20 hover:border-red-600/50 text-gray-400 hover:text-red-400': !getVoteState(index, false)
                    }"
                    class="p-2 border border-gray-600/30 rounded-lg transition-all duration-300 hover:scale-110"
                    title="Not useful"
                    aria-label="Not helpful"
                  >
                    <i class="fa fa-thumbs-down" aria-hidden="true"></i>
                  </button>
                </div>
            </div>
            </transition>
            
          </div>
        </div>
      </div>

      <!-- Bottom Accent -->
      <div class="mt-20 px-4">
        <div class="max-w-5xl mx-auto h-1 bg-gradient-to-r from-transparent via-amber-600/40 to-transparent"></div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { RouterLink } from 'vue-router';

export default {
  name: 'FAQ',
  components: { RouterLink },
  data() {
    return {
      openedIndex: null,
      votes: {}, // Track votes: { faqIndex: { liked: boolean/null, count: number } }
      faqItems: []
    }
  },
  async mounted() {
    await this.fetchFAQs();
  },
  methods: {
    async fetchFAQs() {
        await axios.get('/api/faq/info')
    .then((res) => {
      const data = res.data;
      this.faqItems = data.map(item => ({
      id: item.id,
      question: item.title,
      answer: item.content,
      like_amount: item.like_amount,
      dislike_amount: item.dislike_amount
    }))})
    .catch((error) => {
      console.error('Error fetching FAQs:', error);
    });
    },
    toggleFAQ(index) {
      this.openedIndex = this.openedIndex === index ? null : index;
    },
    onEnter(el) {
      el.style.height = '0';
      el.style.opacity = '0';
      el.offsetHeight; // Force reflow
      el.style.height = el.scrollHeight + 'px';
      el.style.opacity = '1';
    },
    onLeave(el) {
      el.style.height = '0';
      el.style.opacity = '0';
    },
    async voteHelpful(index, isHelpful) {
      const faq = this.faqItems[index];
      try {
        const response = await fetch(`/api/faq/vote/${faq.id}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ isHelpful })
        });
        if (response.ok) {
          // Update local data
          if (isHelpful) {
            faq.like_amount += 1;
          } else {
            faq.dislike_amount += 1;
          }
        } else {
          console.error('Failed to vote');
        }
      } catch (error) {
        console.error('Error voting:', error);
      }

      // Local vote tracking for UI
      if (!this.votes[index]) {
        this.$set(this.votes, index, { liked: null, count: 0 });
      }
      
      const vote = this.votes[index];
      
      // If already voted, toggle or change vote
      if (vote.liked === isHelpful) {
        // Remove vote
        vote.liked = null;
      } else if (vote.liked === null) {
        // First vote
        vote.liked = isHelpful;
      } else {
        // Change vote
        vote.liked = isHelpful;
      }
    },
    hasVoted(index) {
      return this.votes[index] && this.votes[index].liked !== null;
    },
    getVoteState(index, isHelpful) {
      if (!this.votes[index]) return null;
      return this.votes[index].liked === isHelpful;
    }
  }
};
</script>

<style scoped>
/* Smooth slide-down transition for answer expansion */
.slide-down-enter-active,
.slide-down-leave-active {
  overflow: hidden;
  transition: height 0.5s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.4s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  height: 0 !important;
  opacity: 0 !important;
}

/* Entry animation for cards - Staggered */
@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.faq-card {
  animation: slideInUp 0.7s cubic-bezier(0.34, 1.56, 0.64, 1) var(--card-delay, 0s) both;
}

.faq-card:nth-child(1) { --card-delay: 0.1s; }
.faq-card:nth-child(2) { --card-delay: 0.2s; }
.faq-card:nth-child(3) { --card-delay: 0.3s; }
.faq-card:nth-child(4) { --card-delay: 0.4s; }

/* Premium card hover glow */
.faq-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(212, 175, 55, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.5s ease;
  pointer-events: none;
}

.faq-card:hover::before {
  opacity: 1;
}
</style>
