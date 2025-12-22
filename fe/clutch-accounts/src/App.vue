<script setup>
import { RouterView } from 'vue-router';
import Footer from './components/Footer.vue';
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();

const token = localStorage.getItem('token');
if (token){
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            const status = error.response.status;
            if (status >= 400 && status < 500) {
                toast.error("Error: Your token might be expired, pls login again", {
                    position: toast.POSITION.TOP_CENTER
                });
                route.push('/auth');
            } else if (status >= 500) {
                toast.error('Server Error: Please try again later.', {
                    position: toast.POSITION.TOP_RIGHT
                });
            }
        } else {
            toast.error('Network Error: Please check your connection.', {
                position: toast.POSITION.TOP_RIGHT
            });
        }
        return Promise.reject(error);
    }
);
</script>

<template>
    <router-view></router-view>
    <Footer></Footer>
</template>
<style scoped></style>
