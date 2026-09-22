<template>
  <div>
    <PageBanner :image="bg('bg_industry_banner', '/images/industry-banner.jpeg')" title="集团产业" />
    <section class="section sub-detail-section">
      <div class="wrap">
        <div class="sub-detail" v-if="sub">
          <div class="sub-detail-hero">
            <div class="sub-hero-visual" :style="{ backgroundImage: `url(${sub.background || '/images/sub-bg-1.png'})` }">
              <div class="sub-hero-mask"></div>
              <div class="sub-hero-inner">
                <div v-if="sub.logo" class="sub-hero-logo"><img :src="sub.logo" :alt="sub.name"></div>
                <span v-if="sub.englishName" class="sub-hero-en">{{ sub.englishName }}</span>
                <h2>{{ sub.name }}</h2>
              </div>
            </div>
            <div class="sub-hero-info">
              <div class="sub-hero-crumb">
                <router-link to="/">首页</router-link> / <router-link to="/industry">集团产业</router-link> / <span>{{ sub.name }}</span>
              </div>
              <span v-if="sub.category" class="sub-hero-cat">{{ sub.category }}</span>
              <h1>{{ sub.name }}</h1>
              <span v-if="sub.englishName" class="sub-hero-en2">{{ sub.englishName }}</span>
              <div class="sub-hero-desc"><RichContent :content="sub.description" /></div>
            </div>
          </div>
        </div>

        <div class="sub-detail-empty" v-else>
          <p>{{ loading ? '正在加载…' : '未找到对应的成员企业。' }}</p>
        </div>

        <div v-if="sub && assignedCoreBusinesses.length" class="sub-core">
          <div class="sec-head">
            <span class="en">CORE BUSINESS</span>
            <h3>核心业务领域</h3>
          </div>
          <div class="sub-core-list">
          <div v-for="business in assignedCoreBusinesses" :key="business.id" class="sub-core-card">
            <div v-if="business.coverImage" class="sub-core-cover"><img :src="business.coverImage" :alt="business.name"></div>
            <div v-else class="sub-core-ico"><i :class="coreBusinessIcon(business)"></i></div>
            <div class="sub-core-body">
              <h5>{{ business.name }}</h5>
              <RichContent :content="business.description" />
            </div>
          </div>
          </div>
        </div>

        <div class="sub-related" v-if="sub && others.length">
          <div class="sec-head">
            <span class="en">MORE COMPANIES</span>
            <h3>其他成员企业</h3>
          </div>
          <div class="sub-related-grid">
            <router-link v-for="item in others" :key="item.id" class="sub-rel-card" :to="'/industry/' + item.id">
              <div class="sub-rel-bg" :style="{ backgroundImage: `url(${item.background || '/images/sub-bg-1.png'})` }"></div>
              <div class="sub-rel-mask"></div>
              <div class="sub-rel-inner">
                <div v-if="item.logo" class="sub-rel-logo"><img :src="item.logo" :alt="item.name"></div>
                <h5>{{ item.name }}</h5>
                <small v-if="item.category">{{ item.category }}</small>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '@/api'
import { loadContent, pick } from '@/utils/content'

const route = useRoute()
const sub = ref(null)
const list = ref([])
const coreBusinesses = ref([])
const content = ref({})
const loading = ref(false)
const bg = (key, fallback) => pick(content.value, key, fallback)

const others = computed(() => list.value.filter(item => String(item.id) !== String(route.params.id)))

// 核心业务领域图标，与首页「核心业务领域」卡片保持同序同款
const CORE_ICONS = ['fas fa-building', 'fas fa-microchip', 'fas fa-chart-line', 'fas fa-gears', 'fas fa-city']

const assignedCoreBusinesses = computed(() => {
  const ids = Array.isArray(sub.value?.coreBusinessIds) ? sub.value.coreBusinessIds.map(String) : []
  return ids.length ? coreBusinesses.value.filter(item => ids.includes(String(item.id))) : []
})
const coreBusinessIcon = business => {
  const index = business ? coreBusinesses.value.indexOf(business) : -1
  return CORE_ICONS[(index < 0 ? 0 : index) % CORE_ICONS.length]
}

async function loadList() {
  const res = await api.getSubsidiaries()
  if (res.code === 200 && Array.isArray(res.data)) list.value = res.data
}

watch(() => route.params.id, async (id, previous, onCleanup) => {
  let stale = false
  onCleanup(() => { stale = true })
  sub.value = null
  loading.value = true
  try {
    const res = await api.getSubsidiary(id)
    if (!stale) sub.value = res.data
  } catch (e) {
    // 详情接口异常时退回列表匹配，避免整页空白
    if (!stale && list.value.length) {
      sub.value = list.value.find(item => String(item.id) === String(id)) || null
    }
  } finally {
    if (!stale) loading.value = false
  }
}, { immediate: true })

onMounted(async () => {
  content.value = await loadContent()
  await loadList()
  const res = await api.getCoreBusinesses()
  if (res.code === 200 && Array.isArray(res.data)) coreBusinesses.value = res.data
})
</script>

<style scoped>
.section { padding: 60px 0 70px; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.sub-detail-hero { display: grid; grid-template-columns: minmax(0, 1fr) 420px; gap: 52px; align-items: start; }
.sub-hero-visual { grid-column: 2; grid-row: 1; position: relative; height: 460px; border-radius: 8px; overflow: hidden; background-size: cover; background-position: center; box-shadow: var(--shadow); }
.sub-hero-mask { position: absolute; inset: 0; background: linear-gradient(180deg, rgba(8,32,64,.16) 0%, rgba(8,32,64,.74) 100%); }
.sub-hero-inner { position: relative; z-index: 1; height: 100%; padding: 28px 24px; display: flex; flex-direction: column; justify-content: flex-end; text-align: center; color: #fff; }
.sub-hero-logo { width: 76px; height: 76px; border-radius: 6px; background: rgba(255,255,255,.94); padding: 10px; margin: 0 auto 16px; display: flex; align-items: center; justify-content: center; }
.sub-hero-logo img { max-width: 100%; max-height: 100%; object-fit: contain; }
.sub-hero-en { font-size: 11px; letter-spacing: 3px; color: var(--c-accent); text-transform: uppercase; }
.sub-hero-inner h2 { font-size: 20px; color: #fff; margin-top: 8px; line-height: 1.45; }
.sub-hero-info { grid-column: 1; grid-row: 1; padding-top: 6px; }
.sub-hero-crumb { font-size: 13px; color: var(--c-text-light); margin-bottom: 20px; }
.sub-hero-crumb a { color: var(--c-text-light); text-decoration: none; }
.sub-hero-crumb a:hover { color: var(--c-accent); }
.sub-hero-cat { display: inline-block; padding: 5px 14px; border-radius: 20px; background: rgba(200,164,92,.16); color: var(--c-accent-dark); font-size: 13px; font-weight: 600; margin-bottom: 16px; }
.sub-hero-info h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; line-height: 1.4; margin-bottom: 10px; }
.sub-hero-en2 { display: block; font-size: 12px; letter-spacing: 3px; color: var(--c-text-light); text-transform: uppercase; margin-bottom: 26px; }
.sub-hero-desc { margin-top: 24px; padding-top: 24px; border-top: 1px solid var(--c-line); font-size: 15px; line-height: 2; color: var(--c-text); overflow: visible; max-height: none; white-space: normal; word-break: break-word; }
.sub-hero-desc :deep(p) { margin-bottom: 16px; text-indent: 2em; }
.sub-hero-desc :deep(p:last-child) { margin-bottom: 0; }
.sub-hero-desc :deep(p:empty), .sub-hero-desc :deep(p:has(> br:only-child)) { display: none; }
.sub-detail-empty { padding: 60px 0; text-align: center; color: var(--c-text-light); }
.sub-detail-empty .sub-btn { margin-top: 22px; }
.sub-core { margin-top: 80px; padding-top: 60px; border-top: 1px solid var(--c-line); }
.sub-core-list { display: grid; gap: 22px; }
.sub-core-card { display: flex; align-items: flex-start; gap: 26px; padding: 34px 38px; background: var(--c-bg-soft); border-left: 3px solid var(--c-accent); border-radius: 6px; }
.sub-core-ico { flex: 0 0 64px; width: 64px; height: 64px; border-radius: 50%; background: rgba(13,58,114,.08); color: var(--c-primary); display: flex; align-items: center; justify-content: center; font-size: 26px; line-height: 1; }
.sub-core-cover { flex: 0 0 220px; width: 220px; height: 150px; border-radius: 6px; overflow: hidden; background: #fff; }
.sub-core-cover img { width: 100%; height: 100%; object-fit: cover; }
.sub-core-body { flex: 1; min-width: 0; }
.sub-core-body h5 { font-size: 20px; font-weight: 600; color: var(--c-primary); margin-bottom: 12px; }
.sub-core-body :deep(.rich-content) { font-size: 14px; line-height: 1.9; color: var(--c-text-light); margin: 0; white-space: normal; word-break: break-word; }
.sub-core-body :deep(.rich-content p) { margin: 0 0 12px; }
.sub-core-body :deep(.rich-content p:last-child) { margin-bottom: 0; }
.sub-related { margin-top: 80px; padding-top: 60px; border-top: 1px solid var(--c-line); }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sub-related-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
.sub-rel-card { position: relative; display: block; height: 230px; border-radius: 6px; overflow: hidden; text-decoration: none; box-shadow: var(--shadow); }
.sub-rel-bg { position: absolute; inset: 0; background-size: cover; background-position: center; transition: .5s; }
.sub-rel-card:hover .sub-rel-bg { transform: scale(1.07); }
.sub-rel-mask { position: absolute; inset: 0; background: linear-gradient(180deg, rgba(8,32,64,.28) 0%, rgba(8,32,64,.80) 100%); }
.sub-rel-inner { position: relative; z-index: 1; height: 100%; padding: 22px 18px; display: flex; flex-direction: column; justify-content: flex-end; text-align: center; color: #fff; }
.sub-rel-logo { width: 52px; height: 52px; border-radius: 5px; background: rgba(255,255,255,.94); padding: 7px; margin: 0 auto 12px; display: flex; align-items: center; justify-content: center; }
.sub-rel-logo img { max-width: 100%; max-height: 100%; object-fit: contain; }
.sub-rel-inner h5 { font-size: 15px; color: #fff; line-height: 1.5; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.sub-rel-inner small { display: block; font-size: 11px; color: rgba(255,255,255,.72); margin-top: 6px; letter-spacing: 1px; }
@media (max-width: 1000px) {
  .section { padding: 36px 0 48px; }
  .sub-detail-hero { grid-template-columns: 1fr; gap: 28px; }
  .sub-hero-info { grid-column: 1; grid-row: 1; }
  .sub-hero-visual { grid-column: 1; grid-row: 2; height: 320px; }
  .sub-hero-info h1 { font-size: 24px; }
  .sub-related { margin-top: 48px; padding-top: 40px; }
  .sub-core { margin-top: 48px; padding-top: 40px; }
  .sub-core-card { flex-direction: column; gap: 18px; padding: 26px 22px; }
  .sub-core-cover { width: 100%; height: auto; aspect-ratio: 2 / 1; flex: none; }
  .sub-core-ico { width: 54px; height: 54px; flex: 0 0 54px; font-size: 22px; }
  .sub-core-body h5 { font-size: 18px; }
  .sec-head { margin-bottom: 30px; }
  .sec-head h3 { font-size: 26px; }
  .sub-related-grid { grid-template-columns: repeat(2, 1fr); gap: 14px; }
  .sub-rel-card { height: 190px; }
}
</style>
