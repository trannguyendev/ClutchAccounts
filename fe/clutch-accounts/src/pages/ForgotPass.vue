<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// Form state
const step = ref(1) // Step 1: Email entry, Step 2: OTP verification
const particles = ref([])
const mouse = ref({ x: 0, y: 0 })
const isLoading = ref(false)

// Form data
const email = ref('')
const otp = ref('')
const newPassword = ref('')
const confirmPassword = ref('')

// UI state
const isShowingPassword = ref(false)
const isShowingConfirmPassword = ref(false)
const errorMessage = ref('')

const togglePasswordVisibility = () => {
    isShowingPassword.value = !isShowingPassword.value
}

const toggleConfirmPasswordVisibility = () => {
    isShowingConfirmPassword.value = !isShowingConfirmPassword.value
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

    animationFrame = requestAnimationFrame(animate)
}

const handleMouseMove = (e) => {
    mouse.value = { x: e.clientX, y: e.clientY }
}

const validateEmail = (emailValue) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    return emailRegex.test(emailValue)
}

const validatePassword = (password) => {
    // Minimum 6 characters
    return password.length >= 6
}

const handleSendOTP = async () => {
    errorMessage.value = ''

    if (!email.value.trim()) {
        toast.error('Please enter your email', { multiple: false })
        errorMessage.value = 'Email is required'
        return
    }

    if (!validateEmail(email.value)) {
        toast.error('Please enter a valid email', { multiple: false })
        errorMessage.value = 'Invalid email format'
        return
    }

    isLoading.value = true

    try {
        await axios.post(`/api/auth/sendOTP/${email.value}`)
        toast.success('OTP code has been sent to your email', { multiple: false })
        step.value = 2
    } catch (err) {
        console.error('Error sending OTP:', err)
        const errorMsg = err.response.data.error || 'Failed to send OTP. Please check your email and try again.'
        toast.error(errorMsg, { multiple: false })
        errorMessage.value = errorMsg
    } finally {
        isLoading.value = false
    }
}

const handleVerifyOTP = async () => {
    errorMessage.value = ''

    if (!otp.value || otp.value.trim() === '') {
        toast.error('Please enter the OTP code', { multiple: false })
        errorMessage.value = 'OTP is required'
        return
    }

    if (!newPassword.value || newPassword.value.trim() === '') {
        toast.error('Please enter your new password', { multiple: false })
        errorMessage.value = 'New password is required'
        return
    }

    if (!validatePassword(newPassword.value)) {
        toast.error('Password must be at least 6 characters', { multiple: false })
        errorMessage.value = 'Password must be at least 6 characters'
        return
    }

    if (newPassword.value !== confirmPassword.value) {
        toast.error('Passwords do not match', { multiple: false })
        errorMessage.value = 'Passwords do not match'
        return
    }

    isLoading.value = true

    try {
        const forgotData = {
            email: email.value,
            otp: otp.value,
            newPass: newPassword.value
        }

        await axios.post('/api/auth/verifyOTP', forgotData)
        toast.success('Password changed successfully! Redirecting to login...', { multiple: false })
        setTimeout(() => {
            router.push('/auth')
        }, 2000)
    } catch (err) {
        console.error('Error verifying OTP:', err)
        const errorMsg = err.response.data.error || 'Failed to verify OTP. Please try again.'
        toast.error(errorMsg, { multiple: false })
        errorMessage.value = errorMsg
    } finally {
        isLoading.value = false
    }
}

const goBack = () => {
    router.push('/auth')
}

const goBackToEmailStep = () => {
    step.value = 1
    otp.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
    errorMessage.value = ''
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
        <div class="w-full max-w-md relative px-4 sm:px-6 md:px-8">
            <!-- Back Button -->
            <button @click="goBack"
                class="mb-6 flex items-center text-amber-400 hover:text-amber-300 transition-colors relative z-10">
                <i class="fas fa-arrow-left mr-2"></i>
                Back to Login
            </button>

            <!-- Step Indicator -->
            <div class="mb-6 flex items-center justify-center gap-2 relative z-10">
                <div :class="[
                    'w-12 h-12 rounded-full flex items-center justify-center font-bold transition-all duration-300',
                    step >= 1 ? 'bg-gradient-to-r from-amber-500 to-amber-600 text-black' : 'bg-gray-700 text-gray-300'
                ]">
                    1
                </div>
                <div :class="[
                    'h-1 w-8 transition-all duration-300',
                    step >= 2 ? 'bg-gradient-to-r from-amber-500 to-amber-600' : 'bg-gray-700'
                ]"></div>
                <div :class="[
                    'w-12 h-12 rounded-full flex items-center justify-center font-bold transition-all duration-300',
                    step >= 2 ? 'bg-gradient-to-r from-amber-500 to-amber-600 text-black' : 'bg-gray-700 text-gray-300'
                ]">
                    2
                </div>
            </div>

            <!-- Form Container -->
            <div class="relative">
                <!-- Step 1: Email Entry -->
                <form @submit.prevent="handleSendOTP" v-if="step === 1"
                    class="space-y-6 bg-black/40 p-8 rounded-lg border border-amber-900/30 relative z-10 animate-fadeIn">
                    <div>
                        <h2 class="text-2xl font-bold text-center mb-2 text-amber-400">Reset Password</h2>
                        <p class="text-center text-gray-400 text-sm">Enter your email to receive a verification code</p>
                    </div>

                    <div class="space-y-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-300 mb-2" for="email">Email Address</label>
                            <input v-model="email" type="email"
                                class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                placeholder="Enter your email" id="email" required>
                        </div>
                    </div>

                    <div v-if="errorMessage" class="text-red-500 text-sm text-center">
                        {{ errorMessage }}
                    </div>

                    <button type="submit" :disabled="isLoading"
                        class="w-full bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold py-3 rounded-lg hover:from-amber-600 hover:to-amber-700 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
                        <span v-if="!isLoading">Send OTP Code</span>
                        <span v-else class="flex items-center gap-2">
                            <i class="fas fa-spinner fa-spin"></i> Sending...
                        </span>
                    </button>
                </form>

                <!-- Step 2: OTP Verification -->
                <form @submit.prevent="handleVerifyOTP" v-if="step === 2"
                    class="space-y-6 bg-black/40 p-8 rounded-lg border border-amber-900/30 relative z-10 animate-fadeIn">
                    <div>
                        <h2 class="text-2xl font-bold text-center mb-2 text-amber-400">Verify & Reset</h2>
                        <p class="text-center text-gray-400 text-sm">Enter the code sent to {{ email }}</p>
                    </div>

                    <div class="space-y-4">
                        <div>
                            <label class="block text-sm font-medium text-gray-300 mb-2" for="otp">OTP Code</label>
                            <input v-model="otp" type="text" inputmode="numeric" pattern="[0-9]*"
                                class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors text-center tracking-widest"
                                placeholder="XXXXXX" id="otp" maxlength="6" required>
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-300 mb-2" for="new-password">New
                                Password</label>
                            <div class="relative">
                                <input v-model="newPassword"
                                    :type="isShowingPassword ? 'text' : 'password'"
                                    class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 pr-12 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                    placeholder="Create a new password" id="new-password" required>
                                <button type="button" @click="togglePasswordVisibility"
                                    class="absolute right-3 top-1/2 transform -translate-y-1/2 text-amber-200">
                                    <i :class="isShowingPassword ? 'fas fa-eye' : 'fas fa-eye-slash'"></i>
                                </button>
                            </div>
                            <p class="text-xs text-gray-400 mt-1">At least 6 characters</p>
                        </div>

                        <div>
                            <label class="block text-sm font-medium text-gray-300 mb-2" for="confirm-password">Confirm
                                Password</label>
                            <div class="relative">
                                <input v-model="confirmPassword"
                                    :type="isShowingConfirmPassword ? 'text' : 'password'"
                                    class="w-full bg-black/50 border border-amber-900/30 rounded-lg px-4 py-3 pr-12 text-gray-100 focus:outline-none focus:border-amber-500 transition-colors"
                                    placeholder="Confirm your password" id="confirm-password" required>
                                <button type="button" @click="toggleConfirmPasswordVisibility"
                                    class="absolute right-3 top-1/2 transform -translate-y-1/2 text-amber-200">
                                    <i :class="isShowingConfirmPassword ? 'fas fa-eye' : 'fas fa-eye-slash'"></i>
                                </button>
                            </div>
                        </div>
                    </div>

                    <div v-if="errorMessage" class="text-red-500 text-sm text-center">
                        {{ errorMessage }}
                    </div>

                    <div class="flex gap-3">
                        <button type="button" @click="goBackToEmailStep"
                            class="flex-1 bg-gray-700 text-white font-bold py-3 rounded-lg hover:bg-gray-600 transition-all duration-300">
                            Back
                        </button>
                        <button type="submit" :disabled="isLoading"
                            class="flex-1 bg-gradient-to-r from-amber-500 to-amber-600 text-black font-bold py-3 rounded-lg hover:from-amber-600 hover:to-amber-700 transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center gap-2">
                            <span v-if="!isLoading">Reset Password</span>
                            <span v-else class="flex items-center gap-2">
                                <i class="fas fa-spinner fa-spin"></i> Verifying...
                            </span>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Password visibility toggle */
i {
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

/* Form transition smoothing */
form {
    backface-visibility: hidden;
    -webkit-backface-visibility: hidden;
}

/* Fade animation */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: scale(0.98);
    }
    to {
        opacity: 1;
        transform: scale(1);
    }
}

.animate-fadeIn {
    animation: fadeIn 0.3s ease-in-out;
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
</style>