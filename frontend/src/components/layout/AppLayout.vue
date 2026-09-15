<template>
  <div class="app-layout">
    <TopBar />
    <NavBar />
    <main class="main-content">
      <router-view />
    </main>
    <SiteFooter />
  </div>
</template>

<script setup>
import TopBar from './TopBar.vue'
import NavBar from './NavBar.vue'
import SiteFooter from './SiteFooter.vue'
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.main-content {
  flex: 1;
}
.main-content :deep(.section) { padding-top: 72px; padding-bottom: 72px; }
.main-content :deep(.sec-head) { margin-bottom: 40px; }
@media (max-width: 1000px) {
  .main-content :deep(.section) { padding-top: 44px; padding-bottom: 44px; }
  .main-content :deep(.sec-head) { margin-bottom: 30px; }
}
</style>
