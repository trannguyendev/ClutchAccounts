<script setup>
import { RouterView } from 'vue-router';
import Footer from './components/Footer.vue';
import Navbar from './components/Navbar.vue';
</script>

<template>
    <Navbar></Navbar>
    <router-view></router-view>
    <Footer></Footer>
</template>
<style scoped></style>
