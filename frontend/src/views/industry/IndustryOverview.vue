<template>
  <div>
    <PageBanner :image="bg('bg_industry_banner', '/images/industry-banner.jpeg')" title="集团产业" />
    <section class="section text-bg-industry" :style="sectionBg('bg_industry_page', '/images/2945347C3CC652EA1119F1A7F09DC2A9.jpg')"><div class="wrap">
      <div class="sec-head">
        <span class="en">SUBSIDIARIES</span><h3>{{ activeCategory || '成员企业' }}</h3><p>多元产业协同发展，构建覆盖多领域的产业服务生态</p>
      </div>
      <div class="content-list">
        <router-link class="list-item" v-for="sub in filteredSubsidiaries" :key="sub.id" :to="'/industry/' + sub.id">
          <div class="thumb"><img :src="sub.logo" alt=""></div>
          <div class="info">
            <h4>{{ sub.name }}</h4>
            <div class="meta">{{ sub.englishName }} · {{ sub.category }}</div>
            <p>{{ sub.summary || plainText(sub.description) }}</p>
          </div>
        </router-link>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import PageBanner from '@/components/layout/PageBanner.vue'
import api from '@/api'
import { loadContent, pick } from '@/utils/content'
import { richTextPreview } from '@/utils/richText'
const subsidiaries = ref([])
const plainText = value => richTextPreview(value || '')
const route = useRoute()
const activeCategory = computed(() => typeof route.query.category === 'string' ? route.query.category.trim() : '')
const filteredSubsidiaries = computed(() => activeCategory.value
  ? subsidiaries.value.filter(item => (item.category || '').trim() === activeCategory.value)
  : subsidiaries.value)
const content = ref({})
const bg = (key, fallback) => pick(content.value, key, fallback)
const sectionBg = (key, fallback) => ({ backgroundImage: `url(${bg(key, fallback)})` })
onMounted(async () => {
  content.value = await loadContent()
  const res = await api.getSubsidiaries()
  if (res.code === 200) subsidiaries.value = res.data
})
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-industry { background-image: url(/images/2945347C3CC652EA1119F1A7F09DC2A9.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head p { color: var(--c-text-light); margin-top: 12px; }
.content-list { list-style: none; padding: 0; }
.list-item { display: flex; gap: 32px; padding: 28px 0; border-bottom: 1px solid var(--c-line); transition: .3s; color: inherit; text-decoration: none; }
.list-item:hover { background: var(--c-bg-soft); margin: 0 -20px; padding: 28px 20px; }
.thumb { flex: 0 0 260px; height: 170px; border-radius: 4px; overflow: hidden; display: flex; align-items: center; justify-content: center; background: var(--c-bg-soft); }
.thumb img { max-width: 200px; max-height: 120px; object-fit: contain; }
.info { flex: 1; display: flex; flex-direction: column; justify-content: center; }
.info h4 { font-size: 19px; color: var(--c-primary); margin-bottom: 10px; font-weight: 600; }
.meta { font-size: 13px; color: var(--c-accent); margin-bottom: 10px; }
.info p { font-size: 14px; color: var(--c-text-light); line-height: 1.7; }
@media (max-width: 1000px) {
  .list-item { flex-direction: column; gap: 16px; }
  .thumb { flex: none; width: 100%; height: 150px; }
}
</style>
