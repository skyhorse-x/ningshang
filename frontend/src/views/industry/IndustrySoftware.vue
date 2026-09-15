<template>
  <div>
    <PageBanner image="/images/industry-banner.jpeg" title="软件科技" />
    <SubNav title="集团产业">
      <router-link to="/industry/construction">建筑工程</router-link>
      <router-link to="/industry/software" class="on">软件科技</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>软件科技板块</h1><div class="meta">SOFTWARE TECHNOLOGY SECTOR</div></div>
        <RichContent class="detail-body" :content="body" />
      </div>
    </div></section>
    <section class="section soft"><div class="wrap">
      <div class="sec-head"><span class="en">CORE BUSINESS</span><h3>核心业务领域</h3><p>科技赋能产业，创新驱动发展</p></div>
      <div class="content-list">
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-brain"></i></div></div><div class="info"><h4>AI定制软件开发</h4><div class="meta">AI CUSTOM SOFTWARE</div><p>人工智能算法应用与定制化软件开发。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-satellite"></i></div></div><div class="info"><h4>卫星信息系统集成</h4><div class="meta">SATELLITE INFORMATION</div><p>卫星数据应用与地理信息系统集成。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-graduation-cap"></i></div></div><div class="info"><h4>智慧校园基建</h4><div class="meta">SMART CAMPUS</div><p>智慧校园系统建设与基础设施升级。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-briefcase"></i></div></div><div class="info"><h4>政企数字化转型</h4><div class="meta">DIGITAL TRANSFORMATION</div><p>数字化咨询、系统集成与技术支持。</p></div></div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import RichContent from '@/components/business/RichContent.vue'
import { loadContent, pick } from '@/utils/content'
const content = ref({})
const body = computed(() => pick(content.value, 'industry_software_body', '<p>软件科技板块是安徽宁商科技集团数字产业战略的核心引擎，由安徽陆洲科技有限责任公司与安徽合州信息咨询有限责任公司共同构成。</p><p>两家成员企业协同发力，专注AI定制软件开发、卫星信息系统集成及政企数字化转型咨询。</p>'))
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.section.soft { background: var(--c-bg-soft); }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
.detail-body { font-size: 15px; color: var(--c-text); line-height: 1.9; }
.detail-body p { margin-bottom: 18px; text-indent: 2em; }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.content-list { list-style: none; padding: 0; }
.list-item { display: flex; gap: 32px; padding: 28px 0; border-bottom: 1px solid var(--c-line); }
.list-item .thumb { flex: 0 0 80px; }
.list-item .ico { width: 64px; height: 64px; border-radius: 50%; background: var(--c-bg-soft); color: var(--c-primary); display: flex; align-items: center; justify-content: center; font-size: 24px; }
.info h4 { font-size: 19px; color: var(--c-primary); margin-bottom: 10px; font-weight: 600; }
.meta { font-size: 13px; color: var(--c-accent); margin-bottom: 10px; }
.info p { font-size: 14px; color: var(--c-text-light); }
</style>
