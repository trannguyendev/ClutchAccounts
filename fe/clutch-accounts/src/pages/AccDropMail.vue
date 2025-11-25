<script setup>
import { ref } from 'vue'

// const currentUser = ref([]); //this section for fetch user info from server <declare here for using later>
const userTest =
{
  id: 1,
  name: "Erik Tran",
  role: "USER",
  balance: 90726
}

 const accs = ref([
   {
     id: 1,
     name: "Acc Valorant Drop Mail 1",
     image: "https://cdn-offer-photos.zeusx.com/b2b9d6cb-cbd6-4a51-a142-4a62a69734ba.png",
     description: "This is a Valorant account with drop mail 1.",
     price: 50000
   },
   
 ])
</script>
<template>
  <main class="min-h-screen main-background relative">
    <!-- Background Overlay for gradient and theme -->
    <div class="absolute inset-0 bg-gradient-to-b from-slate-950 via-slate-900 to-slate-950 opacity-50"></div>
    <!-- Hero Banner Section -->
    <section class="relative z-10 w-full h-96 overflow-hidden">

      <!-- Background Glow -->
      <div class="absolute inset-0 opacity-20">
        <div class="absolute bottom-0 right-1/4 w-96 h-96 bg-amber-500 rounded-full blur-3xl"></div>
      </div>

      <!-- Content -->
      <div class="relative z-10 flex items-center justify-between h-full px-8">
        <div class="flex gap-6">
          <!-- Balance Card -->
          <div
            class="bg-gradient-to-br from-amber-400/10 to-orange-600/10 backdrop-blur-md border border-amber-500/30 rounded-2xl p-6 min-w-max">
            <p class="text-amber-200 text-sm font-semibold mb-2">SỐ DƯ HIỆN TẠI</p>
            <p class="text-3xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">{{userTest.balance}} VND</p>
          </div>
          <!-- Level Card -->
          <div
            class="bg-gradient-to-br from-purple-400/10 to-pink-600/10 backdrop-blur-md border border-purple-500/30 rounded-2xl p-6 min-w-max">
            <div class="flex items-center gap-2.5">
              <img
                class="w-10 h-10 rounded-full font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]"
                src="" alt="userAvt">
              <div class="font-medium text-heading">
                <div class="text-purple-200 text-sm font-semibold mb-2">Have a good day!</div>
                <div class="text-2xl font-black text-purple-300 drop-shadow-[0_3px_10px_rgba(168,85,247,0.3)]">{{
                  userTest.name }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Decorative Lines -->
      <div class="absolute top-20 left-8 w-1 h-20 bg-gradient-to-b from-amber-400 to-transparent"></div>
      <div class="absolute bottom-20 right-8 w-1 h-20 bg-gradient-to-t from-purple-400 to-transparent"></div>
    </section>

    <!-- Main Content -->
    <section class="max-w-7xl mx-auto px-8 py-16 relative z-10">
      <!-- Products Section -->
      <div class="mb-20">
        <h2 class="text-3xl font-black text-white mb-8 flex items-center gap-3">
          <span class="text-purple-400">⚡</span> ACC VALORANT DROP MAIL
          <div class="flex-1 h-1 bg-gradient-to-r from-purple-400 to-transparent ml-4"></div>
        </h2>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div v-for="item in accs" :key="item.id"
            class="group relative bg-gradient-to-br from-black/80 to-slate-950/80 backdrop-blur-md border border-amber-900/50 rounded-2xl overflow-hidden hover:border-amber-500/70 transition-all duration-300 cursor-pointer hover:shadow-2xl hover:shadow-amber-600/40">
            <div class="p-6">
              <h3 class="text-xl font-bold text-amber-300 mb-2">{{ item.name }}</h3>
              <img :src="item.image" alt="Product Image" class="w-full h-40 object-cover rounded-lg mb-4 shimmer glow-amber">
              <p class="text-sm text-amber-200 mb-4">{{ item.description }}</p>
              <span class="text-2xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)]">Price: {{item.price }} VND</span>
              <div class="text-2xl font-black text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.3)] ml-1 mt-2 border-2 border-amber-300 rounded-2xl hover:text-black/80 hover:bg-amber-300 transition-all delay-75" title="Add to Cart">
                <button class="ml-7 p-2"><i class="fa fa-cart-plus" aria-hidden="true"></i> Buy now</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer Spacer -->
    <div class="h-20"></div>
  </main>
</template>

<style scoped>
main {
  position: relative;
}

.main-background {
  background-image: url('../img/main-background.jpg');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}

/* Shimmer animation for premium feel */
@keyframes shimmer {
  0% {
    background-position: -1000px 0;
  }

  100% {
    background-position: 1000px 0;
  }
}

.shimmer {
  animation: shimmer 3s infinite;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 0.1) 50%, rgba(255, 255, 255, 0) 100%);
  background-size: 1000px 100%;
}

/* Glow effects */
.glow-amber {
  box-shadow: 0 0 30px rgba(255, 184, 28, 0.3), inset 0 0 30px rgba(255, 184, 28, 0.1);
}

.glow-purple {
  box-shadow: 0 0 30px rgba(168, 85, 247, 0.3), inset 0 0 30px rgba(168, 85, 247, 0.1);
}

/* Hover effects */
.card-hover {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card-hover:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
}
</style>
