<template>
  <div class="team-grid">
    <div class="team-card" v-for="member in members" :key="member.id">
      <div class="avatar" :style="'background:' + member.gradient">
        <img :src="member.avatar" :alt="member.name">
      </div>
      <div class="info">
        <h5>{{ member.name }}</h5>
        <span class="pos">{{ member.position }}</span>
        <RichContent :content="member.description" />
      </div>
    </div>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import api from '@/api'
const members = ref([])
onMounted(async () => {
  const res = await api.getTeam()
  if (res.code === 200) members.value = res.data
})
</script>

<style scoped>
.team-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 28px; margin-top: 30px; }
.team-card { background: #fff; border: 1px solid var(--c-line); padding: 40px 36px; display: flex; align-items: center; gap: 26px; transition: .3s; }
.team-card:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-4px); border-color: transparent; }
.avatar { flex-shrink: 0; width: 108px; height: 108px; border-radius: 50%; overflow: hidden; }
.avatar img { width: 100%; height: 100%; object-fit: cover; }
.info h5 { font-size: 22px; color: var(--c-primary); margin-bottom: 4px; }
.info .pos { display: inline-block; font-size: 13px; color: var(--c-accent); letter-spacing: 1px; margin-bottom: 12px; }
.info p { font-size: 14px; color: var(--c-text-light); }
@media (max-width: 1000px) {
  .team-grid { grid-template-columns: 1fr; }
}
</style>
