<script setup>
import { ref } from 'vue'

const isMenuOpen = ref(false)

const toggleMenu = () => {
    isMenuOpen.value = !isMenuOpen.value
}
</script>
<template>
    <nav class="sticky top-0 z-30 w-full bg-black backdrop-blur-md border-b border-amber-700/20">
        <!-- top-edge shimmer -->
        <div class="nav-shimmer" aria-hidden="true">
            <div class="nav-shimmer-inner"></div>
        </div>
        <div class="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto px-4 py-2 md:p-4">
                <a href="/" class="flex items-center space-x-2 md:space-x-3 rtl:space-x-reverse py-2">
                <img src="#" class="h-8 md:h-9" alt="Team Logo">
                <span class="self-center text-lg md:text-2xl font-extrabold tracking-tight text-amber-300 drop-shadow-[0_3px_10px_rgba(255,184,28,0.12)]">Team name</span>
            </a>
            <div class="flex md:order-2 space-x-2 md:space-x-3 rtl:space-x-reverse items-center">
                <button @click="toggleMenu" type="button"
                    class="inline-flex items-center p-2.5 md:p-2 w-10 h-10 justify-center text-sm text-amber-200 rounded-lg md:hidden hover:bg-amber-900/12 focus:outline-none focus:ring-2 focus:ring-amber-300 transition-colors duration-200"
                    aria-controls="navbar-sticky">
                    <span class="sr-only">Open main menu</span>
                    <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                        viewBox="0 0 17 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M1 1h15M1 7h15M1 13h15" />
                    </svg>
                </button>
            </div>
            <div :class="[isMenuOpen ? 'block' : 'hidden', 'w-full md:flex md:w-auto md:order-1']" id="navbar-sticky">
                <ul
                    class="flex flex-col p-3 md:p-0 mt-3 md:mt-0 font-medium rounded-lg md:space-x-8 rtl:space-x-reverse md:flex-row md:border-0 bg-black/50 md:bg-transparent md:rounded-none space-y-2 md:space-y-0">
                    <li>
                        <a href="#"
                            class="nav-link block py-2 px-4 rounded-md md:p-0 font-semibold transition-colors duration-200 text-md"
                            aria-current="page">Home</a>
                    </li>
                    <li>
                        <a href="#"
                            class="nav-link block py-2 px-4 rounded-md md:p-0 font-semibold transition-colors duration-200 text-md">About</a>
                    </li>
                    <li>
                        <a href="#"
                            class="nav-link block py-2 px-4 rounded-md md:p-0 font-semibold transition-colors duration-200 text-md">Services</a>
                    </li>
                    <li>
                        <a href="#"
                            class="nav-link block py-2 px-4 rounded-md md:p-0 font-semibold transition-colors duration-200 text-md">Contact</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

</template>
<style scoped>
nav {
    /* warmer golden glow and softer glass */
    box-shadow: 0 12px 40px rgba(255, 184, 28, 0.06);
    border-bottom: 1px solid rgba(214, 153, 7, 0.12);
    /* ensure backdrop blur works across browsers */
    -webkit-backdrop-filter: blur(8px);
    backdrop-filter: blur(8px);
}

.max-w-screen-xl a img {
    filter: drop-shadow(0 3px 10px rgba(255, 184, 28, 0.08));
}

.nav-link {
    color: #FFD966; /* brighter gold */
    transition: color 0.18s ease, text-shadow 0.18s ease, transform 0.12s ease;
    margin: 0px 4rem;
}

.nav-link:hover {
    color: #fff;
    text-shadow: 0 4px 18px rgba(255, 184, 28, 0.14);
    transform: translateY(-1px);
}

/* subtle underline bar on focus/hover (desktop) */
.nav-link::after {
    content: '';
    display: block;
    height: 2px;
    width: 0%;
    background: linear-gradient(90deg, rgba(255,215,102,1), rgba(255,184,28,0.9));
    transition: width 220ms ease;
}

.nav-link:hover::after,
.nav-link:focus::after {
    width: 100%;
}

/* subtle underline bar on focus/hover (desktop) */
.nav-link::after {
    content: '';
    display: block;
    height: 2px;
    width: 0%;
    background: linear-gradient(90deg, rgba(255,215,102,1), rgba(255,184,28,0.9));
    transition: width 220ms ease;
}

.nav-link:hover::after,
.nav-link:focus::after {
    width: 100%;
}

button.shadow-lg {
    box-shadow: 0 14px 38px rgba(255, 160, 0, 0.10);
}

/* mobile menu toggle hover */
.md\:hidden:hover {
    background-color: rgba(255, 184, 28, 0.035);
}

</style>
<style scoped>
/* shimmer effect */
.nav-shimmer {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    height: 4px;
    overflow: hidden;
    pointer-events: none;
}
.nav-shimmer-inner {
    position: absolute;
    left: -40%;
    top: 0;
    height: 100%;
    width: 40%;
    background: linear-gradient(90deg, rgba(255,215,102,0) 0%, rgba(255,215,102,0.95) 50%, rgba(255,215,102,0) 100%);
    filter: blur(6px);
    opacity: 0.9;
    transform: translateX(0);
    animation: navShimmer 3.6s ease-in-out infinite;
}

@keyframes navShimmer {
    0% { transform: translateX(-100%); opacity: 0.0 }
    10% { opacity: 0.6 }
    50% { transform: translateX(150%); opacity: 0.9 }
    90% { opacity: 0.6 }
    100% { transform: translateX(300%); opacity: 0 }
}
</style>