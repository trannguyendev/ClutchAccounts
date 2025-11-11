<script setup></script>
<template>
    <nav
        class="bg-[rgba(10,10,10,0.7)] backdrop-blur-sm fixed w-full z-20 top-0 start-0 border-b border-amber-900/20">
        <div class="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
            <a href="/" class="flex items-center space-x-3 rtl:space-x-reverse">
                <img src="smth" class="h-8" alt="Team Logo">
                <span class="self-center text-2xl font-semibold whitespace-nowrap dark:text-white">Flowbite</span>
            </a>
            <div class="flex md:order-2 space-x-3 md:space-x-0 rtl:space-x-reverse">
                <button type="button"
                    class="text-black bg-gradient-to-r from-amber-400 to-amber-600 hover:from-amber-500 hover:to-amber-700 focus:ring-4 focus:outline-none focus:ring-amber-300 font-medium rounded-md text-sm px-4 py-2 text-center shadow-sm">Get
                    started</button>
                <button data-collapse-toggle="navbar-sticky" type="button"
                    class="inline-flex items-center p-2 w-10 h-10 justify-center text-sm text-amber-200 rounded-lg md:hidden hover:bg-amber-900/10 focus:outline-none focus:ring-2 focus:ring-amber-300"
                    aria-controls="navbar-sticky" aria-expanded="false">
                    <span class="sr-only">Open main menu</span>
                    <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none"
                        viewBox="0 0 17 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M1 1h15M1 7h15M1 13h15" />
                    </svg>
                </button>
            </div>
            <div class="items-center justify-between hidden w-full md:flex md:w-auto md:order-1" id="navbar-sticky">
                <ul
                    class="flex flex-col p-4 md:p-0 mt-4 font-medium rounded-lg md:space-x-8 rtl:space-x-reverse md:flex-row md:mt-0 md:border-0">
                    <li>
                        <a href="#"
                            class="block py-2 px-3 rounded-sm md:p-0 text-amber-200 hover:text-amber-50 md:hover:text-amber-100"
                            aria-current="page">Home</a>
                    </li>
                    <li>
                        <a href="#"
                            class="block py-2 px-3 rounded-sm md:p-0 text-amber-200 hover:text-amber-50 md:hover:text-amber-100">About</a>
                    </li>
                    <li>
                        <a href="#"
                            class="block py-2 px-3 rounded-sm md:p-0 text-amber-200 hover:text-amber-50 md:hover:text-amber-100">Services</a>
                    </li>
                    <li>
                        <a href="#"
                            class="block py-2 px-3 rounded-sm md:p-0 text-amber-200 hover:text-amber-50 md:hover:text-amber-100">Contact</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

</template>
<style scoped>
nav {
    /* subtle golden glow and glass */
    box-shadow: 0 6px 30px rgba(255, 184, 28, 0.05);
    border-bottom: 1px solid rgba(214, 153, 7, 0.12);
}

.max-w-screen-xl a img {
    filter: drop-shadow(0 2px 8px rgba(255, 184, 28, 0.06));
}

a {
    transition: color 0.15s ease, text-shadow 0.15s ease;
}

a:hover {
    text-shadow: 0 0 12px rgba(255, 184, 28, 0.12);
}

button.shadow-sm {
    box-shadow: 0 8px 24px rgba(255, 165, 0, 0.08);
}

.bg-amber-900\/10:hover {
    background-color: rgba(255, 184, 28, 0.06) !important;
}
</style>