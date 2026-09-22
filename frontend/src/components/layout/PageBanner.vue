<template>
  <div class="pagebanner" :style="bannerStyle">
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  image: { type: String, required: true },
  title: { type: String, required: true },
  en: { type: String, default: '' },
  breadcrumb: { type: Array, default: () => [] }
})
const bannerStyle = computed(() => ({
  backgroundImage: 'url(' + props.image + ')'
}))
</script>

<style scoped>
.pagebanner {
  height: 450px;
  background-size: cover;
  background-position: center;
  position: relative;
}
.pagebanner.with-overlay::after {
  content: "";
  position: absolute;
  inset: 0;
  display: block;
  background: linear-gradient(180deg, rgba(8,28,56,.55), rgba(8,28,56,.65));
}
@media (max-width: 1000px) {
  .pagebanner { height: 220px; }
}
</style>
