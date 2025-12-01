<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import mainBackground from '@/img/main-background.jpg'

const route = useRoute()
const transactionContent = route.params.transaction_content

const amount = ref(0)
const loading = ref(false)
const qrVisible = ref(false)
const qrSrc = ref('')
const error = ref('')

const confirmPayment = async () => {
    error.value = ''
    // basic validation
    const num = parseFloat(amount.value)
    if (!num || num < 10000) {
        error.value = 'Please enter a valid amount greater than 10000.'
        return
    }
    
    loading.value = true
    qrVisible.value = true
    qrSrc.value = ''
    
    try {
        // Construct QR URL
        const qrUrl = `https://api.vietqr.io/image/970422-0867514526-bbP8BGH.jpg?accountName=TRAN%20DO%20KHOI%20NGUYEN&amount=${amount.value}&addInfo=${transactionContent}`
        
        // Fetch and validate image loads properly
        await new Promise((resolve, reject) => {
            const img = new Image()
            img.onload = () => {
                qrSrc.value = qrUrl
                loading.value = false
                resolve()
            }
            img.onerror = () => {
                loading.value = false
                error.value = 'Failed to load QR code. Please try again.'
                qrVisible.value = false
                reject(new Error('Image load failed'))
            }
            img.src = qrUrl
        })
        
        console.log("QR SRC: ", qrSrc.value)
    } catch (err) {
        console.error('Error generating QR:', err)
        error.value = error.value || 'Failed to generate QR code.'
        qrVisible.value = false
        loading.value = false
    }
}
const checkPaymentStatus = () => {
    // Placeholder for payment status check logic
}
const hideQr = () => {
    qrVisible.value = false
    qrSrc.value = ''
    loading.value = false
}
</script>

<template>
    <div class="min-h-[70vh] flex items-start justify-center py-12 px-4 bg-cover bg-center bg-no-repeat" 
         :style="{ backgroundImage: `url(${mainBackground})` }">
        <div class="absolute inset-0 bg-black/50"></div>
        <div
            class="relative w-full max-w-2xl rounded-2xl p-8 bg-gradient-to-br from-neutral-900/70 to-black/60 border border-amber-700/10 backdrop-blur-md shadow-xl">
            <h2 class="text-2xl font-extrabold text-amber-300 mb-4">Add Money</h2>
            <p class="text-sm text-amber-200/80 mb-6">Enter the amount you want to add to your account. Maintain the
                black & gold luxury theme.</p>

            <div class="space-y-4">
                <label class="block">
                    <span class="text-amber-200 text-sm font-semibold">Amount</span>
                    <div class="mt-2 flex items-center gap-3">
                        <input v-model="amount" type="number" min="10000" step="10000" placeholder="Enter amount greater than 10000"
                            class="w-full bg-black/40 border border-amber-700/30 rounded-md px-4 py-3 text-amber-100 placeholder-amber-400 focus:outline-none focus:ring-2 focus:ring-amber-500 transition" />
                        <button @click="confirmPayment" :disabled="loading"
                            class="inline-flex items-center space-x-2 px-4 py-3 rounded-md bg-amber-500 hover:bg-amber-600 text-black font-semibold disabled:opacity-60 disabled:cursor-not-allowed">
                            <span v-if="!loading">Confirm</span>
                            <span v-else>Generated!</span>
                        </button>
                    </div>
                </label>

                <p v-if="error" class="text-red-400 text-sm">{{ error }}</p>

                <!-- QR card (hidden until generated) -->
                <div v-if="qrVisible"
                    class="mt-6 bg-black/60 border border-amber-700/20 rounded-xl p-4 flex flex-col items-center">
                    <div class="w-full flex justify-between items-start mb-3">
                        <div>
                            <div class="text-amber-300 font-bold">Scan to Pay</div>
                            <div class="text-amber-200 text-sm">Amount: <span class="font-semibold">{{ amount }}</span>
                            </div>
                        </div>
                        <button @click="hideQr" class="text-amber-200 hover:text-white"><i class="fa fa-times-circle" aria-hidden="true"></i></button>
                    </div>
                    <div class="bg-black/80 p-4 rounded-lg">
                        <!-- Loading skeleton -->
                        <div v-if="loading" class="w-64 h-64 flex items-center justify-center">
                            <div class="space-y-4 w-full px-4">
                                <div class="flex justify-center">
                                    <div class="animate-spin rounded-full h-16 w-16 border-4 border-amber-600 border-t-amber-300"></div>
                                </div>
                                <p class="text-center text-amber-300 text-sm font-semibold">Generating QR code...</p>
                            </div>
                        </div>
                        <!-- QR image -->
                        <img v-else-if="qrSrc" :src="qrSrc" alt="QR code" class="w-64 h-64 object-contain" />
                        <!-- Fallback -->
                        <div v-else class="w-64 h-64 flex items-center justify-center text-amber-400">Unable to load QR</div>
                    </div>
                </div>

                <!-- Placeholder when no QR yet -->
                <div v-else class="mt-6 text-amber-300/70 text-sm">Press "Confirm" to generate your payment QR.</div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* keep styling in the luxury black/gold theme */
.rounded-2xl {
    border-radius: 16px;
}

.text-amber-300 {
    color: #FFD966;
}

.bg-amber-500 {
    background: #FFB41B;
}
</style>