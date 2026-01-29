<script setup>
import { ref, onMounted } from 'vue';
import { devToolsDetector } from '../utils/devToolsDetector';

const isDevToolsOpen = ref(false);

onMounted(() => {
  // Initialize the detector
  devToolsDetector.init();

  // Subscribe to dev tools state changes
  devToolsDetector.subscribe((state) => {
    isDevToolsOpen.value = state;
  });
});
</script>

<template>
  <!-- Overlay to block interaction when dev tools are open -->
  <div v-if="isDevToolsOpen" class="dev-tools-blocker">
    <div class="blocker-content">
      <div class="blocker-icon">⚠️</div>
      <h1>Developer Tools Detected</h1>
      <p>Please close your developer tools to continue using this website.</p>
      <p class="instruction">Press <code>F12</code>, <code>Ctrl+Shift+I</code>, or <code>Right-click → Inspect</code> to close the developer tools.</p>
    </div>
  </div>
</template>

<style scoped>
.dev-tools-blocker {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(20, 20, 40, 0.98), rgba(40, 20, 60, 0.98));
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999999;
  pointer-events: all;
  backdrop-filter: blur(5px);
}

.blocker-content {
  text-align: center;
  color: white;
  animation: slideIn 0.3s ease-out;
  padding: 40px;
  max-width: 500px;
}

.blocker-icon {
  font-size: 80px;
  margin-bottom: 20px;
  animation: bounce 2s infinite;
}

h1 {
  font-size: 32px;
  margin-bottom: 15px;
  font-weight: bold;
}

p {
  font-size: 16px;
  margin: 10px 0;
  line-height: 1.6;
}

.instruction {
  margin-top: 20px;
  font-size: 14px;
  color: #aaa;
  margin-bottom: 0;
}

code {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  color: #00ff00;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}
</style>
