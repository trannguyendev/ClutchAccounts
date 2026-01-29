<script setup>
import { RouterView } from 'vue-router';
import Footer from './components/Footer.vue';
import DevToolsBlocker from './components/DevToolsBlocker.vue';

</script>

<template>
    <DevToolsBlocker />
    <router-view></router-view>
    <Footer></Footer>
</template>
<style scoped></style>
