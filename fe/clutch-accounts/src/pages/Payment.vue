<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

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
    if (!num || num <= 0) {
        error.value = 'Please enter a valid amount greater than 0.'
        return
    }
    else {
        loading.value = true
        qrVisible.value = false
        qrSrc.value = `https://api.vietqr.io/image/970422-0867514526-bbP8BGH.jpg?accountName=TRAN%20DO%20KHOI%20NGUYEN&amount=${amount.value}&addInfo=${transactionContent}`
        console.log("QR SRC: ", qrSrc.value)
        qrVisible.value = true
    }

}

const hideQr = () => {
    qrVisible.value = false
    qrSrc.value = ''
}
</script>

<template>
    <div class="min-h-[70vh] flex items-start justify-center py-12 px-4">
        <div
            class="w-full max-w-2xl rounded-2xl p-8 bg-gradient-to-br from-neutral-900/70 to-black/60 border border-amber-700/10 backdrop-blur-md shadow-xl">
            <h2 class="text-2xl font-extrabold text-amber-300 mb-4">Add Money</h2>
            <p class="text-sm text-amber-200/80 mb-6">Enter the amount you want to add to your account. Maintain the
                black & gold luxury theme.</p>

            <div class="space-y-4">
                <label class="block">
                    <span class="text-amber-200 text-sm font-semibold">Amount</span>
                    <div class="mt-2 flex items-center gap-3">
                        <input v-model="amount" type="number" min="0" step="10" placeholder="0.00"
                            class="w-full bg-black/40 border border-amber-700/30 rounded-md px-4 py-3 text-amber-100 placeholder-amber-400 focus:outline-none focus:ring-2 focus:ring-amber-500 transition" />
                        <button @click="confirmPayment" :disabled="loading"
                            class="inline-flex items-center space-x-2 px-4 py-3 rounded-md bg-amber-500 hover:bg-amber-600 text-black font-semibold disabled:opacity-60 disabled:cursor-not-allowed">
                            <span v-if="!loading">Confirm</span>
                            <span v-else>Generating...</span>
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
                        <button @click="hideQr" class="text-amber-200 hover:text-white">Close</button>
                    </div>
                    <div class="bg-black/80 p-4 rounded-lg">
                        <img v-if="qrSrc" :src="qrSrc" alt="QR code" class="w-64 h-64 object-contain" />
                        <div v-else class="w-64 h-64 flex items-center justify-center text-amber-400">No image</div>
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