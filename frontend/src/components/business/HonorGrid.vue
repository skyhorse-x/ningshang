<template>
  <div class="honor-grid">
    <div class="honor-card" v-for="honor in honors" :key="honor.id">
      <img :src="honor.image" alt="" class="honor-bg">
      <div class="honor-mask"></div>
      <div class="honor-content">
        <div class="ico"><i class="fas fa-medal"></i></div>
        <h5>{{ honor.title }}</h5>
        <RichContent :content="honor.description" />
      </div>
    </div>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import api from '@/api'
const honors = ref([])
onMounted(async () => {
  const res = await api.getHonors()
  if (res.code === 200) honors.value = res.data
})
</script>

<style scoped>
.honor-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 26px; }
.honor-card { border: 1px solid var(--c-line); transition: .3s; position: relative; overflow: hidden; }
.honor-card::before { content: ""; position: absolute; top: 0; left: 0; width: 100%; height: 3px; background: var(--c-accent); transform: scaleX(0); transform-origin: center; transition: .3s; z-index: 3; }
.honor-card:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-6px); border-color: transparent; }
.honor-card:hover::before { transform: scaleX(1); }
.honor-bg { width: 100%; display: block; height: 200px; object-fit: cover; }
.honor-mask { position: absolute; inset: 0; background: rgba(255,255,255,.85); z-index: 1; transition: .3s; top: 0; height: 200px; }
.honor-card:hover .honor-mask { background: rgba(255,255,255,.15); }
.honor-content { position: absolute; inset: 0; z-index: 2; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center; padding: 24px; height: 200px; }
.ico { width: 56px; height: 56px; border-radius: 50%; background: var(--c-bg-soft); color: var(--c-accent); font-size: 24px; display: flex; align-items: center; justify-content: center; margin: 0 auto 16px; }
.honor-card h5 { font-size: 16px; color: var(--c-primary); margin-bottom: 8px; }
.honor-card p { font-size: 13px; color: #222; text-shadow: 0 1px 3px rgba(255,255,255,.5); }
@media (max-width: 1000px) {
  .honor-grid { grid-template-columns: 1fr; }
}
</style>
