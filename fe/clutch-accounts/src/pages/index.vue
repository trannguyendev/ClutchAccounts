<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { RouterLink } from 'vue-router'

// Drag state tracking
const isDragging = ref(false)
const startX = ref(0)
const startY = ref(0)
const rotationX = ref(0)
const rotationY = ref(0)
const currentX = ref(0)
const currentY = ref(0)

// Handle 3D card effect
const handleMouseDown = (e) => {
    isDragging.value = true
    startX.value = e.clientX - currentX.value
    startY.value = e.clientY - currentY.value
    const card = e.target.closest('.card')
    if (card) {
        card.style.transition = 'none'
    }
}

const handleMouseUp = () => {
    isDragging.value = false
    rotationX.value = currentX.value
    rotationY.value = currentY.value
    const cards = document.querySelectorAll('.card')
    cards.forEach(card => {
        card.style.transition = 'transform 0.8s cubic-bezier(0.23, 1, 0.32, 1)'
    })
}

const handle3DCard = (e) => {
    const cards = document.querySelectorAll('.card')
    cards.forEach(card => {
        const cardRect = card.getBoundingClientRect()
        
        if (isDragging.value) {
            // Calculate rotation based on drag
            currentX.value = (e.clientX - startX.value)
            currentY.value = (e.clientY - startY.value)
            const dragRotationY = currentX.value / 2
            const dragRotationX = -currentY.value / 2
            
            card.style.setProperty('--rotate-x', `${dragRotationX}deg`)
            card.style.setProperty('--rotate-y', `${dragRotationY}deg`)
        } else {
            // Normal hover effect
            const cardCenterX = cardRect.left + cardRect.width / 2
            const cardCenterY = cardRect.top + cardRect.height / 2
            const angleX = -(e.clientY - cardCenterY) / 30
            const angleY = (e.clientX - cardCenterX) / 20
            
            card.style.setProperty('--rotate-x', `${angleX}deg`)
            card.style.setProperty('--rotate-y', `${angleY}deg`)
        }
        
        // Calculate glare position
        const mouseX = ((e.clientX - cardRect.left) / cardRect.width) * 100
        const mouseY = ((e.clientY - cardRect.top) / cardRect.height) * 100
        card.style.setProperty('--mouse-x', `${mouseX}%`)
        card.style.setProperty('--mouse-y', `${mouseY}%`)
    })
}

const resetCard = () => {
    const cards = document.querySelectorAll('.card')
    cards.forEach(card => {
        card.style.setProperty('--rotate-x', '0deg')
        card.style.setProperty('--rotate-y', '0deg')
    })
}

// Parallax scroll effect
const handleScroll = () => {
    const scrolled = window.pageYOffset || document.documentElement.scrollTop
    const parallaxElements = document.querySelectorAll('.scroll-parallax')
    
    parallaxElements.forEach(element => {
        const speed = element.getAttribute('data-scroll-speed')
        const yPos = -(scrolled * speed)
        element.style.transform = `translate3d(0px, ${yPos}px, 0px)`
    })
}

// Mouse movement effect
const handleMouseMove = (e) => {
    const parallaxElements = document.querySelectorAll('.mouse-parallax')
    const mouseX = e.clientX
    const mouseY = e.clientY

    parallaxElements.forEach(element => {
        const speed = element.getAttribute('data-speed')
        const x = (window.innerWidth - mouseX * speed) / 100
        const y = (window.innerHeight - mouseY * speed) / 100
        element.style.transform = `translate3d(${x}px, ${y}px, 0)`
    })
}

// Reveal elements on scroll
const handleReveal = () => {
    const reveals = document.querySelectorAll('.reveal')
    reveals.forEach(element => {
        const windowHeight = window.innerHeight
        const elementTop = element.getBoundingClientRect().top
        const elementVisible = 150

        if (elementTop < windowHeight - elementVisible) {
            element.classList.add('active')
        }
    })
}

onMounted(() => {
    window.addEventListener('mousemove', handleMouseMove)
    window.addEventListener('mousemove', handle3DCard)
    window.addEventListener('mousedown', handleMouseDown)
    window.addEventListener('mouseup', handleMouseUp)
    window.addEventListener('mouseleave', resetCard)
    window.addEventListener('scroll', handleScroll)
    window.addEventListener('scroll', handleReveal)
    handleReveal() // Initial check for elements in view
})

onUnmounted(() => {
    window.removeEventListener('mousemove', handleMouseMove)
    window.removeEventListener('mousemove', handle3DCard)
    window.removeEventListener('mousedown', handleMouseDown)
    window.removeEventListener('mouseup', handleMouseUp)
    window.removeEventListener('mouseleave', resetCard)
    window.removeEventListener('scroll', handleScroll)
    window.removeEventListener('scroll', handleReveal)
})

const features = ref([
    {
        title: 'Premium Accounts',
        description: 'High-quality Valorant accounts with rare skins and exclusive content'
    },
    {
        title: 'Instant Delivery',
        description: 'Get your account credentials immediately after purchase'
    },
    {
        title: 'Secure Payment',
        description: 'Safe and protected transaction methods'
    },
    {
        title: '24/7 Support',
        description: 'Round-the-clock customer service for all your needs'
    }
])

const advantages = ref([
    'Competitive Prices',
    'Verified Accounts',
    'Full Access Guaranteed',
    'Regular Stock Updates'
])
</script>

<template>
    <div class="min-h-screen bg-black text-white">
        <!-- Hero Section -->
        <div class="relative min-h-screen">
            <!-- Dynamic Background Pattern -->
            <div class="absolute inset-0 bg-[#0A0A0A] overflow-hidden">
                <div class="absolute inset-0 opacity-20 scroll-parallax" data-scroll-speed="0.1">
                    <div class="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(255,215,0,0.1),transparent_50%)]"></div>
                    <div class="absolute top-0 left-0 right-0 h-px bg-gradient-to-r from-transparent via-amber-500/50 to-transparent mouse-parallax" data-speed="2"></div>
                    <div class="absolute bottom-0 left-0 right-0 h-px bg-gradient-to-r from-transparent via-amber-500/50 to-transparent mouse-parallax" data-speed="2"></div>
                    <!-- Circuit pattern lines -->
                    <div class="absolute inset-0 opacity-20 scroll-parallax" data-scroll-speed="0.2"
                         style="background-image: linear-gradient(to right, transparent 49px, rgba(255,215,0,0.1) 50px),
                                linear-gradient(to bottom, transparent 49px, rgba(255,215,0,0.1) 50px);
                                background-size: 50px 50px;">
                    </div>
                </div>
            </div>

            <!-- Chamber Background -->
            <div class="absolute inset-0 flex items-center justify-end overflow-hidden">
                <div class="relative w-[90%] h-[90%] md:w-[70%] md:h-[80%]">
                    <img src="../img/mateusz-dabrowski-chamber-4k.jpg" 
                         alt="Chamber" 
                         class="absolute right-0 h-full object-contain opacity-90 mouse-parallax scroll-parallax"
                         data-speed="3"
                         data-scroll-speed="0.15">
                    <!-- Glowing effects -->
                    <div class="absolute top-1/2 right-1/4 w-64 h-64 bg-amber-500/20 rounded-full blur-3xl mouse-parallax scroll-parallax" 
                         data-speed="4"
                         data-scroll-speed="0.25"></div>
                    <div class="absolute bottom-1/3 right-1/3 w-48 h-48 bg-amber-400/10 rounded-full blur-2xl mouse-parallax scroll-parallax" 
                         data-speed="5"
                         data-scroll-speed="0.3"></div>
                </div>
            </div>

            <!-- Content -->
            <div class="relative z-20 container mx-auto px-4 py-32">
                <div class="max-w-2xl">
                    <h1 class="text-5xl md:text-7xl font-bold mb-6">
                        <span class="text-amber-400">Premium</span> Valorant Accounts
                    </h1>
                    <p class="text-xl md:text-2xl mb-8 text-gray-300">
                        Elevate your game with exclusive Chamber-focused accounts and rare collections
                    </p>
                    <div class="space-x-4">
                        <RouterLink to="/auth" class="bg-amber-400 hover:bg-amber-500 text-black px-8 py-3 rounded-full font-bold transition-all">
                            Login to shop now
                        </RouterLink>
                        <RouterLink to="/auth" class="border-2 border-amber-400 text-amber-400 hover:bg-amber-400 hover:text-black px-8 py-3 rounded-full font-bold transition-all">
                            SignUp
                        </RouterLink>
                    </div>
                </div>
            </div>
        </div>

        <!-- Features Section -->
        <div class="bg-gradient-to-b from-black to-amber-900/20 py-20 reveal">
            <div class="container mx-auto px-4">
                <h2 class="text-3xl md:text-4xl font-bold text-center mb-12 scroll-parallax" data-scroll-speed="0.1">
                    <span class="text-amber-400">Why Choose</span> Our Service
                </h2>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                    <div v-for="(feature, index) in features" :key="feature.title" 
                         class="bg-black/50 p-6 rounded-lg border border-amber-400/30 hover:border-amber-400 transition-all reveal"
                         :style="{ transitionDelay: index * 200 + 'ms' }">
                        <h3 class="text-xl font-bold text-amber-400 mb-3">{{ feature.title }}</h3>
                        <p class="text-gray-300">{{ feature.description }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Advantages Section -->
        <div class="bg-black/80 py-20">
            <div class="container mx-auto px-4">
                <div class="flex flex-col md:flex-row items-center justify-between">
                    <div class="md:w-1/2 mb-8 md:mb-0">
                        <h2 class="text-3xl md:text-4xl font-bold mb-6">
                            The <span class="text-amber-400">Golden Standard</span>
                            <br />in Valorant Accounts
                        </h2>
                        <ul class="space-y-4">
                            <li v-for="advantage in advantages" :key="advantage" 
                                class="flex items-center space-x-3">
                                <svg class="w-5 h-5 text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                </svg>
                                <span>{{ advantage }}</span>
                            </li>
                        </ul>
                    </div>
                    <div class="md:w-1/2 flex justify-center">
                        <div class="card-container floating">
                            <div class="card" @mousedown.prevent>
                                <div class="card-inner">
                                    <img src="../img/ChamCard.png" 
                                         alt="Chamber Abilities" 
                                         class="max-w-md w-full select-none">
                                    <div class="card-glare"></div>
                                    <div class="card-shine"></div>
                                    <div class="card-glow"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Call to Action -->
        <div class="bg-gradient-to-t from-black to-amber-900/20 py-20">
            <div class="container mx-auto px-4 text-center">
                <h2 class="text-3xl md:text-4xl font-bold mb-6">
                    Ready to <span class="text-amber-400">Join the Elite?</span>
                </h2>
                <p class="text-xl text-gray-300 mb-8 max-w-2xl mx-auto">
                    Get access to premium Valorant accounts featuring Chamber and exclusive content
                </p>
                <button class="bg-amber-400 hover:bg-amber-500 text-black px-12 py-4 rounded-full font-bold text-lg transition-all">
                    Browse Accounts
                </button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.bg-gradient-custom {
    background: linear-gradient(135deg, #000000 0%, #B7791F 100%);
}

/* Smooth transitions for mouse parallax elements */
.mouse-parallax {
    transition: transform 0.2s cubic-bezier(0.33, 1, 0.68, 1);
}

/* Smooth transitions for scroll parallax elements */
.scroll-parallax {
    transition: transform 0.5s cubic-bezier(0.33, 1, 0.68, 1);
    will-change: transform;
}

/* Reveal animation */
.reveal {
    opacity: 0;
    transform: translateY(50px);
    transition: all 0.9s cubic-bezier(0.38, 1, 0.68, 1);
}

.reveal.active {
    opacity: 1;
    transform: translateY(0);
}

/* Add gradient animation */
@keyframes gradientFlow {
    0% {
        background-position: 0% 50%;
    }
    50% {
        background-position: 100% 50%;
    }
    100% {
        background-position: 0% 50%;
    }
}

.animate-gradient {
    animation: gradientFlow 15s ease infinite;
    background-size: 400% 400%;
}

/* Optimize performance */
.scroll-parallax, .mouse-parallax {
    backface-visibility: hidden;
    -webkit-backface-visibility: hidden;
    transform-style: preserve-3d;
    -webkit-transform-style: preserve-3d;
}

/* 3D Card Effect */
@keyframes float {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
}

.card-container {
    perspective: 2000px;
    transform-style: preserve-3d;
    width: 100%;
    max-width: 28rem;
    cursor: pointer;
}

.floating {
    animation: float 6s ease-in-out infinite;
}

.card {
    position: relative;
    width: 100%;
    height: 100%;
    transition: all 0.8s cubic-bezier(0.23, 1, 0.32, 1);
    transform-style: preserve-3d;
}

.card-inner {
    position: relative;
    width: 100%;
    height: 100%;
    transform-style: preserve-3d;
}

.card::after {
    content: '';
    position: absolute;
    inset: -2px;
    background: linear-gradient(125deg, #ffd700 0%, #b8860b 50%, #daa520 100%);
    z-index: -1;
    opacity: 0;
    transition: all 0.6s;
    border-radius: 0.5rem;
    filter: blur(1px);
}

.card-glare {
    position: absolute;
    inset: 0;
    background: radial-gradient(
        circle at var(--mouse-x, 50%) var(--mouse-y, 50%),
        rgba(255, 215, 0, 0.6) 0%,
        transparent 60%
    );
    opacity: 0;
    transition: all 0.3s;
    pointer-events: none;
    mix-blend-mode: overlay;
    transform: translateZ(20px);
}

.card-shine {
    position: absolute;
    inset: 0;
    background: linear-gradient(
        125deg,
        rgba(255, 215, 0, 0.5) 0%,
        transparent 60%
    );
    opacity: 0;
    transition: all 0.3s;
    mix-blend-mode: overlay;
    transform: translateZ(20px);
}

.card-glow {
    position: absolute;
    inset: -20px;
    background: radial-gradient(
        circle at center,
        rgba(255, 215, 0, 0.4) 0%,
        transparent 70%
    );
    opacity: 0;
    transition: all 0.3s;
    pointer-events: none;
    mix-blend-mode: screen;
    transform: translateZ(20px);
}

.card {
    transform: rotateX(var(--rotate-x, 0deg)) rotateY(var(--rotate-y, 0deg)) scale(1);
}

.card:hover:not(:active) {
    transform: rotateX(var(--rotate-x, 0deg)) rotateY(var(--rotate-y, 0deg)) scale(1.02);
}

.card:active {
    cursor: grabbing;
    transform: rotateX(var(--rotate-x, 0deg)) rotateY(var(--rotate-y, 0deg)) scale(0.98);
}

.card:hover::after {
    opacity: 1;
    filter: blur(2px);
}

.card:hover .card-glare,
.card:hover .card-shine,
.card:hover .card-glow {
    opacity: 1;
}

.card img {
    transform: translateZ(50px);
    border-radius: 0.5rem;
    box-shadow: 0 0 30px rgba(255, 215, 0, 0.3);
    transition: all 0.3s;
}

.card:hover img {
    box-shadow: 0 0 50px rgba(255, 215, 0, 0.5);
    filter: brightness(1.1);
}
</style>