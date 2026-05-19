<template>
  <div class="app">
    <div class="app-bg">
      <div class="bg-gradient"></div>
      <div class="bg-pattern"></div>
    </div>
    <NavBar v-if="!isAuthPage" />
    <main :class="{ 'with-nav': !isAuthPage, 'full-width': isAuthPage }">
      <router-view v-slot="{ Component }">
        <Transition name="page" mode="out-in">
          <component :is="Component" />
        </Transition>
      </router-view>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from './components/NavBar.vue'

const route = useRoute()
const isAuthPage = computed(() => {
  return route.path === '/login' || route.path === '/register'
})
</script>

<style scoped>
.app {
  min-height: 100vh;
  position: relative;
}

/* Background */
.app-bg {
  position: fixed;
  inset: 0;
  z-index: -1;
  background: linear-gradient(135deg, #e0f7fa 0%, #f3e5f5 25%, #fff3e0 50%, #fce4ec 75%, #e8f5e9 100%);
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
}

@keyframes gradientBG {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* GPU加速 */
.app-bg {
  will-change: transform;
  transform: translateZ(0);
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 80%, rgba(102, 126, 234, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(250, 112, 154, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(79, 172, 254, 0.1) 0%, transparent 40%);
}

.bg-pattern {
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23667eea' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.5;
}

/* Main Content */
main {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px;
  position: relative;
  z-index: 1;
}

main.with-nav {
  padding-top: 100px;
}

main.full-width {
  max-width: none;
  padding: 0;
}

/* Page Transitions */
.page-enter-active,
.page-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Responsive */
@media (max-width: 768px) {
  main.with-nav {
    padding-top: 80px;
    padding-left: 16px;
    padding-right: 16px;
  }
}
</style>
