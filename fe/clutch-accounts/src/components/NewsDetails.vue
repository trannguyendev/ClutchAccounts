<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const news = ref(null)
const loading = ref(true)
const error = ref(null)

const fetchNews = async () => {
  try {
    const response = await axios.get('/api/news/info')
    console.log('Fetched news data:', response.data)
    news.value = response.data
  } catch (err) {
    error.value = 'Failed to load news. Please try again later.'
    console.error('Error fetching news:', err)
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/main')
}

onMounted(() => {
  fetchNews()
})
</script>

<template>
  <div class="min-h-screen bg-gray-900 text-white">
    <!-- Header -->
    <div class="bg-black py-8 px-4 sm:px-6 lg:px-8 flex items-center justify-between">
      <!-- Back Button -->
      <button
        @click="goBack"
        class="text-gold-400 hover:text-gold-300 transition-colors"
      >
        <span class="material-icons mr-2">arrow_back</span>
        Back
      </button>
      <h1 class="text-3xl md:text-4xl font-bold text-gold-400 mb-2">
        News & Updates
      </h1>
      <!-- Empty div to push back button to left -->
      <div></div>
    </div>

    <!-- Content Area -->
    <div class="container mx-auto px-4 py-12">
      <!-- Loading State -->
      <div v-if="loading" class="flex justify-center items-center py-12">
        <div class="space-y-4">
          <div class="w-full h-8 bg-gray-800 rounded"></div>
          <div class="w-3/4 h-6 bg-gray-800 rounded"></div>
          <div class="w-full h-64 bg-gray-800 rounded"></div>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="text-center py-12">
        <p class="text-red-500 font-semibold mb-4">{{ error }}</p>
        <button 
          @click="fetchNews()"
          class="bg-gold-400 text-black px-6 py-2 rounded-lg hover:bg-gold-300 transition-colors"
        >
          Retry
        </button>
      </div>

      <!-- News Content -->
      <div v-else class="space-y-8">
        <div class="bg-gray-800 rounded-lg p-8 shadow-xl">
          <h2 class="text-3xl font-bold text-gold-400 mb-6">{{ news.title }}</h2>
          <div class="prose prose-lg max-w-none text-gray-300">
            <p>{{ news.content }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.prose p {
  color: #d1d5db;
}

.prose a {
  color: #fbbf24;
}

.prose a:hover {
  color: #f59e0b;
}

.bg-gold-400 {
  background-color: #fbbf24;
}

.text-gold-400 {
  color: #fbbf24;
}

.hover\:bg-gold-300:hover {
  background-color: #f59e0b;
}

.text-gold-300:hover {
  color: #f59e0b;
}
</style>