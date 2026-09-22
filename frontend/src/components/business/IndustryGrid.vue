<template>
  <div class="ind-section">
    <div class="wrap">
      <div class="sec-head">
        <span class="en">SUBSIDIARIES</span>
        <h3>集团子公司</h3>
        <p>集团旗下子公司协同发展，形成科创服务与产业运营合力</p>
      </div>
      <div v-if="subsidiaries.length" class="ind-row">
        <router-link v-for="item in subsidiaries" :key="item.id" class="sub-card" :to="'/industry/' + item.id">
          <img v-if="item.background" class="sub-bg" :src="item.background" alt="">
          <div class="sub-mask"></div>
          <div class="sub-content">
            <img v-if="item.logo" class="sub-logo" :src="item.logo" :alt="item.name">
            <span class="sub-category">{{ item.category }}</span>
            <h4>{{ item.name }}</h4>
            <small>{{ item.englishName }}</small>
            <p>{{ plainText(item.description) }}</p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'
import { richTextPreview } from '@/utils/richText'

const subsidiaries = ref([])
const plainText = value => richTextPreview(value || '')

onMounted(async () => {
  const res = await api.getSubsidiaries()
  if (res.code === 200 && Array.isArray(res.data)) subsidiaries.value = res.data
})
</script>

<style scoped>
.ind-section { padding: 72px 0 20px; background: #fff; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.sec-head { text-align: center; margin-bottom: 36px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head p { color: var(--c-text-light); margin-top: 12px; font-size: 15px; }
.ind-row { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 16px; align-items: stretch; }
.sub-card { display: block; min-height: 330px; height: 100%; position: relative; overflow: hidden; border-radius: 6px; background: var(--c-primary); color: #fff; text-decoration: none; box-shadow: 0 6px 24px rgba(13,58,114,.06); transition: box-shadow .35s ease; }
.sub-card:hover { box-shadow: 0 14px 34px rgba(13,58,114,.18); }
.sub-bg { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; transition: transform .35s ease; }
.sub-mask { position: absolute; inset: 0; background: linear-gradient(90deg, rgba(8, 32, 64, .9), rgba(8, 32, 64, .52)); }
.sub-card:hover .sub-bg { transform: scale(1.04); }
.sub-content { position: relative; z-index: 1; min-height: 330px; height: 100%; padding: 24px 18px; display: flex; flex-direction: column; justify-content: flex-end; min-width: 0; }
.sub-logo { width: 56px; height: 56px; object-fit: contain; background: rgba(255,255,255,.92); border-radius: 4px; padding: 7px; margin: 0 auto 16px; }
.sub-category { font-size: 12px; line-height: 1.5; color: var(--c-accent); font-weight: 600; overflow-wrap: anywhere; }
.sub-content h4 { margin: 8px 0 4px; font-size: 17px; line-height: 1.45; min-height: 50px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; overflow-wrap: anywhere; }
.sub-content small { color: rgba(255,255,255,.74); text-transform: uppercase; letter-spacing: .5px; font-size: 10px; line-height: 1.5; overflow-wrap: anywhere; }
.sub-content p { margin: 12px 0 0; color: rgba(255,255,255,.86); font-size: 12px; line-height: 1.65; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }
@media (max-width: 1000px) {
  .ind-row { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .sub-card, .sub-content { min-height: 220px; }
}
@media (max-width: 640px) {
  .ind-row { grid-template-columns: 1fr; }
}
</style>
