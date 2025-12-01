<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
// Form state
const isLogin = ref(true)
const isAnimating = ref(false)
const particles = ref([])
const mouse = ref({ x: 0, y: 0 })
// Password visibility
const isShowingPswLogin = ref(false)
const isShowingPswSignup = ref(false)


//Check if has error stores error value
const errorLoginMessage = ref('')
const errorSignupMessage = ref('')


const togglePswVisibility = () => {
    isShowingPswLogin.value = !isShowingPswLogin.value
}
const togglePswVisibilitySignup = () => {
    isShowingPswSignup.value = !isShowingPswSignup.value
}
const createParticle = () => ({
    x: Math.random() * window.innerWidth,
    y: Math.random() * window.innerHeight,
    size: Math.random() * 3 + 1,
    speedX: Math.random() * 1 - 0.5,
    speedY: Math.random() * 1 - 0.5,
    opacity: Math.random() * 0.5 + 0.3,
    hue: Math.random() * 30 + 30, // Golden hue range
    rotation: Math.random() * 360,
    rotationSpeed: (Math.random() * 2 - 1) * 0.5
})

const initParticles = () => {
    particles.value = Array.from({ length: 50 }, createParticle)
}

let animationFrame = null

const animate = () => {
    particles.value.forEach(particle => {
        particle.x += particle.speedX
        particle.y += particle.speedY
        particle.rotation += particle.rotationSpeed

        // Bounce off edges
        if (particle.x < 0 || particle.x > window.innerWidth) particle.speedX *= -1
        if (particle.y < 0 || particle.y > window.innerHeight) particle.speedY *= -1
    })

    // Continue animation
    animationFrame = requestAnimationFrame(animate)
}

const handleMouseMove = (e) => {
    mouse.value = { x: e.clientX, y: e.clientY }
}

onMounted(() => {
    initParticles()
    animate()
    window.addEventListener('mousemove', handleMouseMove)
    window.addEventListener('resize', initParticles)
})

onUnmounted(() => {
    cancelAnimationFrame(animationFrame)
    window.removeEventListener('mousemove', handleMouseMove)
    window.removeEventListener('resize', initParticles)
})

const toggleForm = () => {
    if (isAnimating.value) return
    isAnimating.value = true
    isLogin.value = !isLogin.value
    setTimeout(() => {
        isAnimating.value = false
    }, 600)
}

// Form data
const loginForm = ref({
    email: '',
    password: ''
})

const signupForm = ref({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
})

const handleLogin = () => {
    // Handle login logic here
    console.log('Login:', loginForm.value)
}

const handleSignup = () => {
    // Handle signup logic here
    console.log('Signup:', signupForm.value)
}
</script>

<template>
    <div class="min-h-screen bg-[#0A0A0A] flex items-center justify-center px-4 relative overflow-hidden">
        <!-- Interactive Particles -->
        <div class="absolute inset-0">
            <svg class="w-full h-full">
                <g v-for="(particle, index) in particles" :key="index"
                    :transform="`translate(${particle.x}, ${particle.y}) rotate(${particle.rotation})`">
                    <circle :r="particle.size" :fill="`hsla(${particle.hue}, 80%, 50%, ${particle.opacity})`"
                        class="transition-transform duration-200 ease-out">
                        <animate attributeName="opacity"
                            :values="`${particle.opacity};${particle.opacity * 0.5};${particle.opacity}`" dur="2s"
                            repeatCount="indefinite" />
                    </circle>
                </g>
            </svg>
        </div>

        <!-- Background Pattern -->
        <div class="absolute inset-0 opacity-5">
            <div class="absolute inset-0" style="background-image: linear-gradient(to right, transparent 49px, rgba(255,215,0,0.1) 50px),
                        linear-gradient(to bottom, transparent 49px, rgba(255,215,0,0.1) 50px);
                        background-size: 50px 50px;">
            </div>
        </div>

        <!-- Glowing Orbs -->
        <div class="absolute top-1/4 -left-20 w-80 h-80 bg-amber-500/20 rounded-full blur-[100px]"></div>
        <div class="absolute bottom-1/4 -right-20 w-80 h-80 bg-amber-500/20 rounded-full blur-[100px]"></div>

        <!-- Main Container -->
        <div class="w-full max-w-md md:max-w-lg lg:max-w-xl relative px-4 sm:px-6 md:px-8">
            <!-- Form Toggle Buttons -->
            <div class="flex mb-8 bg-black/40 p-1 rounded-lg border border-amber-900/30 relative z-10">
                <button @click="toggleForm" :class="[
                    'flex-1 py-3 text-center rounded-md transition-all duration-500',
                    isLogin ? 'bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold' : 'text-gray-400'
                ]">
                    Login
                </button>
                <button @click="toggleForm" :class="[
                    'flex-1 py-3 text-center rounded-md transition-all duration-500',
                    !isLogin ? 'bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold' : 'text-gray-400'
                ]">
                    Sign Up
                </button>
            </div>

            <!-- Forms Container -->
            <div class="relative" style="min-height: 400px; height: auto">
                <!-- Login Form -->
                <form @submit.prevent="handleLogin" :class="[
                    'absolute w-full transition-all duration-500 ease-in-out',
                    isLogin ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-full'
                ]" :style="{ pointerEvents: isLogin ? 'auto' : 'none' }">
                    <div class="space-y-6 bg-black/40 p-8 rounded-lg border border-amber-900/30">
                        <h2 class="text-2xl font-bold text-center mb-8 text-amber-400">Welcome Back</h2>

                        <div class="space-y-4">
                            <div>
                                <label class="block text-sm font-medium text-gray-300 mb-2"
                                    for="email-login">Email</label>
                                <input v-model="loginForm.email" type="email"
                                    class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                    placeholder="Enter your email" id="email-login" required>
                            </div>
                            <div>
                                <label class="block text-sm font-medium text-gray-300 mb-2"
                                    for="psw-login">Password</label>
                                <div class="relative">
                                    <input v-model="loginForm.password" :type="isShowingPswLogin ? 'text' : 'password'"
                                        class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 pr-12 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                        placeholder="Enter your password" id="psw-login" required>
                                    <button type="button" @click="togglePswVisibility"
                                        class="absolute right-3 top-1/2 transform -translate-y-1/2 text-amber-200">
                                        <i :class="isShowingPswLogin ? 'fa fa-eye' : 'fa fa-eye-slash'"
                                            id="icon-show-psw"></i>
                                    </button>
                                </div>
                                <div v-if="errorLoginMessage">
                                    <span id="show-error" class="text-wrap text-red-800 block">{{ errorLoginMessage}}</span>
                                </div>
                            </div>
                        </div>

                        <div class="flex items-center justify-between text-sm">
                            <label class="flex items-center text-gray-400">
                                <input type="checkbox" class="mr-2 accent-amber-500">
                                Remember me
                            </label>
                            <a href="#" class="text-amber-400 hover:text-amber-300 transition-colors">Forgot
                                Password?</a>
                        </div>

                        <button type="submit"
                            class="w-full bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold py-3 rounded-lg hover:from-amber-600 hover:to-amber-700 transition-all duration-300">
                            Login
                        </button>
                    </div>
                </form>

                <!-- Sign Up Form -->
                <form @submit.prevent="handleSignup" :class="[
                    'absolute w-full transition-all duration-500 ease-in-out',
                    !isLogin ? 'opacity-100 translate-x-0' : 'opacity-0 translate-x-full'
                ]" :style="{ pointerEvents: !isLogin ? 'auto' : 'none' }">
                    <div class="space-y-6 bg-black/40 p-8 rounded-lg border border-amber-900/30">
                        <h2 class="text-2xl font-bold text-center mb-8 text-amber-400">Create Account</h2>

                        <div class="space-y-4">
                            <div class="relative">
                                <label class="block text-sm font-medium text-gray-300 mb-2" for="email">Email</label>
                                <input v-model="signupForm.email" type="email"
                                    class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                    placeholder="Enter your email" id="email" required>
                                <button>

                                </button>
                            </div>
                            <div class="relative">
                                <label class="block text-sm font-medium text-gray-300 mb-2"
                                    for="pw-reg">Password</label>
                                <input v-model="signupForm.password" :type="isShowingPswSignup ? 'text' : 'password'"
                                    class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 pr-12 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                    placeholder="Create a password" id="pw-reg" required>
                                <button type="button" @click="togglePswVisibilitySignup"
                                    class="absolute right-3 top-1/2 transform translate-y-[0.5] text-amber-200">
                                    <i :class="isShowingPswSignup ? 'fa fa-eye' : 'fa fa-eye-slash'"
                                        id="icon-show-psw"></i>
                                </button>
                            </div>
                            <div v-if="errorSignupMessage">
                                <span id="show-error"
                                    class="text-wrap text-red-800 block">{{errorSignupMessage}}</span>
                            </div>
                        </div>

                        <div class="text-sm text-gray-400">
                            <label class="flex items-center">
                                <input type="checkbox" class="mr-2 accent-amber-500" required>
                                <span> I agree to the <a href="#" class="text-amber-500 transition-all delay-150 hover:underline"> Terms of Service and Privacy Policy
                                    </a></span>
                            </label>
                        </div>

                        <button type="submit"
                            class="w-full bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold py-3 rounded-lg hover:from-amber-600 hover:to-amber-700 transition-all duration-500">
                            Create Account
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>

#icon-show-psw {
    color: aliceblue;
    transition: color 0.2s ease-in-out, transform 0.15s ease;
}

/* Custom scrollbar for inputs */
input {
    scrollbar-width: thin;
    scrollbar-color: #b45309 transparent;
}

input::-webkit-scrollbar {
    width: 6px;
}

input::-webkit-scrollbar-track {
    background: transparent;
}

input::-webkit-scrollbar-thumb {
    background-color: #b45309;
    border-radius: 20px;
}

/* Focus effects */
input:focus {
    box-shadow: 0 0 0 2px rgba(217, 119, 6, 0.2);
}

/* Checkbox custom style */
input[type="checkbox"] {
    cursor: pointer;
    position: relative;
    width: 16px;
    height: 16px;
    border: 2px solid #b45309;
    border-radius: 3px;
    background: transparent;
    -webkit-appearance: none;
    -moz-appearance: none;
    appearance: none;
}

input[type="checkbox"]:checked {
    background: #b45309;
}

input[type="checkbox"]:checked::before {
    content: '✓';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: black;
    font-size: 12px;
}

/* Form transition smoothing */
form {
    backface-visibility: hidden;
    -webkit-backface-visibility: hidden;
}

/* Responsive adjustments */
@media (max-width: 640px) {
    .space-y-6 {
        margin-top: 1rem;
        margin-bottom: 1rem;
    }

    input,
    button {
        font-size: 0.875rem;
    }
}

/* Particle effects */
svg circle {
    mix-blend-mode: screen;
    filter: blur(1px);
    will-change: transform;
}

/* Glass effect enhancement */
.bg-black\/40 {
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
}
</style>