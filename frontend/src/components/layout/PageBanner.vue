<template>
  <div class="pagebanner" :style="bannerStyle">
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { loadContent, pick } from '@/utils/content'
const props = defineProps({
  image: { type: String, required: true },
  title: { type: String, required: true },
  en: { type: String, default: '' },
  breadcrumb: { type: Array, default: () => [] }
})
const route = useRoute()
const content = ref({})
const resolvedImage = computed(() => {
  if (route.path.startsWith('/about')) return pick(content.value, 'bg_about_banner', props.image)
  if (route.path.startsWith('/contact') || route.path.startsWith('/recruit')) {
    return pick(content.value, 'bg_contact_banner', props.image)
  }
  return props.image
})
const bannerStyle = computed(() => ({ backgroundImage: 'url(' + resolvedImage.value + ')' }))
onMounted(async () => { content.value = await loadContent() })
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
