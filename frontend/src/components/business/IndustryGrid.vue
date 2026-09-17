<template>
  <div class="ind-section">
    <div class="wrap">
      <div class="sec-head">
        <span class="en">SUBSIDIARIES</span>
        <h3>成员企业</h3>
        <p>集团旗下子公司协同发展，形成科创服务与产业运营合力</p>
      </div>
      <div v-if="subsidiaries.length" class="ind-row">
        <article v-for="item in subsidiaries" :key="item.id" class="sub-card">
          <img v-if="item.background" class="sub-bg" :src="item.background" alt="">
          <div class="sub-mask"></div>
          <div class="sub-content">
            <img v-if="item.logo" class="sub-logo" :src="item.logo" :alt="item.name">
            <span class="sub-category">{{ item.category }}</span>
            <h4>{{ item.name }}</h4>
            <small>{{ item.englishName }}</small>
            <p>{{ plainText(item.description) }}</p>
          </div>
        </article>
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
.ind-section { padding: 72px 0 20px; background: #f7f9fc; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.sec-head { text-align: center; margin-bottom: 36px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head p { color: var(--c-text-light); margin-top: 12px; font-size: 15px; }
.ind-row { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 22px; }
.sub-card { min-height: 260px; position: relative; overflow: hidden; border-radius: 6px; background: var(--c-primary); color: #fff; }
.sub-bg { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover; transition: transform .35s ease; }
.sub-mask { position: absolute; inset: 0; background: linear-gradient(90deg, rgba(8, 32, 64, .9), rgba(8, 32, 64, .52)); }
.sub-card:hover .sub-bg { transform: scale(1.04); }
.sub-content { position: relative; z-index: 1; min-height: 260px; padding: 32px; display: flex; flex-direction: column; justify-content: flex-end; }
.sub-logo { width: 60px; height: 60px; object-fit: contain; background: rgba(255,255,255,.92); border-radius: 4px; padding: 8px; margin-bottom: 18px; }
.sub-category { font-size: 13px; color: var(--c-accent); font-weight: 600; }
.sub-content h4 { margin: 8px 0 4px; font-size: 22px; line-height: 1.35; }
.sub-content small { color: rgba(255,255,255,.74); text-transform: uppercase; letter-spacing: 1px; }
.sub-content p { margin: 14px 0 0; max-width: 520px; color: rgba(255,255,255,.86); line-height: 1.7; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
@media (max-width: 1000px) {
  .ind-row { grid-template-columns: 1fr; }
  .sub-card, .sub-content { min-height: 220px; }
}
</style>
