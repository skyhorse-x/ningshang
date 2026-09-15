<template>
  <div>
    <PageBanner image="/images/1.jpg" title="企业文化" />
    <SubNav title="集团概况">
      <router-link to="/about/intro">集团简介</router-link>
      <router-link to="/about/speech">董事长致词</router-link>
      <router-link to="/about/events">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor">企业荣誉</router-link>
      <router-link to="/about/party">党建工作</router-link>
      <router-link to="/about/culture" class="on">企业文化</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>企业文化理念</h1><div class="meta">以文化铸牢企魂 · 以理念凝聚共识</div></div>
        <div class="culture-content">
          <div class="culture-section">
            <h3>扎根本土，做有根的事业</h3>
            <RichContent class="lead" :content="c('culture_lead', '安徽宁商科技集团生长于合肥科创热土，扎根巢湖之滨。')" />
          </div>
          <div class="culture-section">
            <h3>核心价值理念</h3>
            <div class="culture-values">
              <div class="cv-item"><div class="cv-label">MISSION</div><RichContent class="cv-text" :content="c('culture_mission', '科创赋能城乡，数字服务实业')" /></div>
              <div class="cv-item"><div class="cv-label">CORE VALUES</div><RichContent class="cv-text" :content="c('culture_values', '徽商固本 · 科创致远 · 协同共生 · 开放共赢')" /></div>
              <div class="cv-item"><div class="cv-label">VISION</div><RichContent class="cv-text" :content="c('culture_vision', '城湖共生处，笃行向远方')" /></div>
              <div class="cv-item"><div class="cv-label">SPIRIT</div><RichContent class="cv-text" :content="c('culture_spirit', '务实笃行 · 创新赋能 · 诚信致远')" /></div>
            </div>
          </div>
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
.culture-content { font-size: 15px; }
.culture-section { margin-bottom: 40px; padding: 30px; background: var(--c-bg-soft); border-radius: 10px; }
.culture-section h3 { font-size: 22px; color: var(--c-primary); margin-bottom: 18px; display: flex; align-items: center; gap: 12px; }
.culture-section h3::before { content: ''; display: inline-block; width: 4px; height: 22px; background: var(--c-primary); border-radius: 2px; }
.lead { font-size: 17px; color: var(--c-primary); font-weight: 600; line-height: 1.8; margin-bottom: 20px; }
.culture-values { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-top: 30px; }
.cv-item { padding: 24px; border-radius: 8px; background: #fff; border: 1px solid var(--c-line); }
.cv-label { font-size: 13px; color: var(--c-accent); letter-spacing: 1px; margin-bottom: 8px; }
.cv-text { font-size: 16px; color: var(--c-primary); font-weight: 600; line-height: 1.6; }
@media (max-width: 1000px) {
  .culture-values { grid-template-columns: 1fr; }
}
</style>
