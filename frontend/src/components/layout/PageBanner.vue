<template>
  <div class="pagebanner" :style="bannerStyle">
    <div class="wrap cap">
      <div v-if="breadcrumb.length" class="crumb">
        <span v-for="(item, idx) in breadcrumb" :key="idx">
          <router-link v-if="item.to" :to="item.to">{{ item.text }}</router-link>
          <span v-else>{{ item.text }}</span>
          <span v-if="idx < breadcrumb.length - 1"> / </span>
        </span>
      </div>
      <span v-if="en" class="en">{{ en }}</span>
      <h2>{{ title }}</h2>
    </div>
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
.wrap {
  width: 1200px;
  max-width: 94%;
  margin: 0 auto;
  position: relative;
}
.cap {
  position: relative;
  z-index: 2;
  color: #fff;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.crumb {
  position: absolute;
  right: 0;
  top: 24px;
  font-size: 13px;
  color: rgba(255,255,255,.75);
}
.crumb a {
  color: rgba(255,255,255,.75);
  text-decoration: none;
}
.crumb a:hover { color: var(--c-accent); }
.cap .en {
  font-size: 13px;
  letter-spacing: 4px;
  color: var(--c-accent);
  text-transform: uppercase;
}
.cap h2 {
  font-size: 40px;
  font-weight: 700;
  margin: 6px 0;
}
@media (max-width: 1000px) {
  .pagebanner { height: 220px; }
  .cap h2 { font-size: 28px; }
}
</style>
