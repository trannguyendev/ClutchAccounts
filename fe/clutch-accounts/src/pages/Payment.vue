<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import mainBackground from '@/img/main-background.jpg'
import { useUserStore } from '@/stores/user'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
const userDeposit = useUserStore()
const route = useRoute()
const router = useRouter()
const transactionContent = route.params.transaction_content
const amount = ref(0)
const loading = ref(false)
const requestLoading = ref(false)
const qrVisible = ref(false)
const qrSrc = ref('')
const error = ref('')
const orderCode = ref(null)
// Methods
const confirmPayment = async () => {
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
        await axios.post("/api/payment/request-deposit", {
            email: userDeposit.username,
            amount: amount.value,
            descrp: transactionContent
        })
        .then((res) => {
            console.log(res)
            qrSrc.value = res.data.checkoutUrl;
            console.log("QR URL: ", qrSrc.value);
            console.log("length of QR: ", qrSrc.value.length)
            orderCode.value = res.data.orderCode;
        })
        .catch(err => console.log(err))
        loading.value = false
        console.log("QR SRC: ", qrSrc.value)
    } catch (err) {
        console.error('Error generating QR:', err)
        error.value = error.value || 'Failed to generate QR code.'
        qrVisible.value = false
        loading.value = false
    }
}
const checkPaymentStatus = async () => {
    requestLoading.value = true
    let dataPayLoad = {
            email: userDeposit.username,
            amount: amount.value,
            descrp: transactionContent
    }
    console.log("Data PayLoad: ", dataPayLoad)
    await axios.post("/api/payment/request-deposit", dataPayLoad)
    .then((res) => {
        console.log("Response: ", res)
        toast.success("Your payment has been successfully confirmed, pls waiting for admin to verify!", {
            position: "top-right",
            closeOnClick: true,
            pauseOnHover: false,
        });
    })
    .catch((err) => {
        console.error('Error submitting payment:', err)
        error.value = 'Failed to submit payment. Please try again.'
        requestLoading.value = false
    })
    .finally(() => {
        requestLoading.value = false
    })
        
    
}
const hideQr = () => {
    qrVisible.value = false
    qrSrc.value = ''
    loading.value = false
    axios.put(`/api/payment/cancel/${orderCode.value}`)
    .then(console.log("Cancelled order: ", orderCode.value))
}
const goBack = () => {
    router.back()
}
</script>

<template>
    <div class="min-h-[70vh] flex items-start justify-center py-12 px-4 bg-cover bg-center bg-no-repeat"
        :style="{ backgroundImage: `url(${mainBackground})` }">
        <div class="absolute inset-0 bg-black/50"></div>
        <div
            class="relative w-full max-w-2xl rounded-2xl p-8 bg-gradient-to-br from-neutral-900/70 to-black/60 border border-amber-700/10 backdrop-blur-md shadow-xl hover:border-amber-700/30 hover:shadow-2xl transition duration-300">
            <h2 class="text-2xl font-extrabold text-amber-300 mb-4">Add Money</h2>
            <p class="text-sm text-amber-200/80 mb-6">Enter the amount you want to add to your account</p>

            <div class="space-y-4">
                <label class="block">
                    <span class="text-amber-200 text-sm font-semibold">Amount</span>
                    <div class="mt-2 flex items-center gap-3">
                            <input v-model="amount" type="number" min="10000" step="10000"
                            placeholder="Enter amount greater than 10000"
                            class="w-full bg-black/40 border border-amber-700/30 rounded-md px-4 py-3 text-amber-100 placeholder-amber-400 focus:outline-none focus:ring-2 focus:ring-amber-500 hover:border-amber-700/60 hover:bg-black/50 transition" />
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
                    class="mt-6 bg-black/60 border border-amber-700/20 rounded-xl p-4 flex flex-col items-center hover:border-amber-700/50 hover:shadow-lg transition duration-300">
                    <div class="w-full flex justify-between items-start mb-3">
                        <div>
                            <div class="text-amber-300 font-bold">Scan to Pay</div>
                            <div class="text-amber-200 text-sm">Amount: <span class="font-semibold">{{ amount }}</span>
                            </div>
                        </div>
                        <button @click="hideQr" class="text-amber-200 hover:text-white"><i class="fa fa-times-circle"
                                aria-hidden="true"></i></button>
                    </div>
                    <div class="bg-black/80 p-4 rounded-lg">
                        <!-- Loading frame -->
                        <div v-if="loading" class="w-64 h-64 flex items-center justify-center">
                            <div class="space-y-4 w-full px-4">
                                <div class="flex justify-center">
                                    <div
                                        class="animate-spin rounded-full h-16 w-16 border-4 border-amber-600 border-t-amber-300">
                                    </div>
                                </div>
                                <p class="text-center text-amber-300 text-sm font-semibold">Generating QR code...</p>
                            </div>
                        </div>
                        <!-- QR image -->
                        <iframe v-else-if="qrSrc" :src="qrSrc" width="400" height="400" frameborder="0"
                            class="mx-auto"></iframe>
                    </div>
                    <button @click="checkPaymentStatus" :disabled="requestLoading"
                        class="mt-4 w-full px-4 py-3 rounded-md bg-amber-600 hover:bg-amber-700 text-black font-semibold transition disabled:opacity-60 disabled:cursor-not-allowed">
                        <span v-if="!requestLoading">Check Payment</span>
                        <span v-else class="inline-flex items-center gap-2">
                            <div class="animate-spin rounded-full h-4 w-4 border-2 border-black border-t-amber-300"></div>
                            Processing...
                        </span>
                    </button>
                </div>

                <!-- Placeholder when no QR yet -->
                <div v-else class="mt-6 text-amber-300/70 text-sm">Press "Confirm" to generate your payment QR.</div>
                <button @click="goBack"
                    class="relative inline-flex items-center space-x-2 px-4 py-2 rounded-md bg-amber-600 hover:bg-amber-700 text-black font-semibold transition duration-200 hover:shadow-lg">
                    <i class="fa fa-arrow-left"></i>
                    <span>Back</span>
                </button>
            </div>
        </div>
        <div
            class="ml-4 relative w-full max-w-4xl rounded-2xl p-8 bg-gradient-to-br from-red-950/70 to-red-900/60 border border-red-700/30 backdrop-blur-md shadow-xl mt-6 hover:border-red-700/60 hover:shadow-2xl transition duration-300">
            <div class="flex items-center gap-3 mb-6">
                <i class="fa fa-exclamation-triangle text-red-400 text-2xl"></i>
                <h3 class="text-2xl font-extrabold text-red-300">Important Notes!</h3>
            </div>

            <div class="space-y-4 text-red-100/90 text-sm leading-relaxed">
                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Attention:</span> Only click the "Confirm Payment" button
                    after your transfer has been successfully completed. (Remember to take a screenshot of your
                    transaction).
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Minimum Amount:</span> The transfer amount must be at least
                    10,000 VND. Otherwise, the system will flag it as spam and the funds will NOT be credited to your
                    account.
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Transaction Content:</span> Do NOT edit the transfer
                    note/content field when scanning the QR code.
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Supported Wallets:</span> All e-wallets, such as MOMO, ZALO
                    PAY, VIETTEL MONEY, etc., are supported.
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Verification:</span> Double-check that your transaction
                    details match the payment information provided above (especially the Transfer Content).
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Processing Time:</span> After clicking "Confirm Payment,"
                    please wait for the system to finish loading (approx. 1–3 minutes).
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Error Handling:</span> If you receive a "System Busy" or
                    "Transaction Not Found" error, wait 5 minutes, refresh the page, and click "Confirm" again. Please
                    repeat this process up to 3 times. If the funds are still not credited, contact our Page.
                </div>

                <div class="p-3 rounded-lg hover:bg-red-800/30 transition duration-200 cursor-default">
                    <span class="font-semibold text-red-300">Support:</span> If you encounter any issues, please message
                    our FanPage or Group. Provide your account information and a screenshot of your payment history.
                </div>
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