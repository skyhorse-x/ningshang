<template>
  <div>
    <PageBanner image="/images/industry-banner.jpeg" title="建筑工程" />
    <SubNav title="集团产业">
      <router-link to="/industry/construction" class="on">建筑工程</router-link>
      <router-link to="/industry/software">软件科技</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>安徽利至高建设工程有限公司</h1><div class="meta">ANHUI LIZHIGAO CONSTRUCTION ENGINEERING CO., LTD.</div></div>
        <RichContent class="detail-body" :content="body" />
      </div>
    </div></section>
    <section class="section soft"><div class="wrap">
      <div class="sec-head"><span class="en">CORE BUSINESS</span><h3>核心业务领域</h3><p>匠心铸就品质，专业服务城市</p></div>
      <div class="content-list">
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-building"></i></div></div><div class="info"><h4>建筑工程施工</h4><div class="meta">CONSTRUCTION ENGINEERING</div><p>房屋建筑、工业厂房、公共建筑等施工总承包。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-road"></i></div></div><div class="info"><h4>市政公用工程</h4><div class="meta">MUNICIPAL ENGINEERING</div><p>城市道路、桥梁、给排水等市政基础设施建设。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-city"></i></div></div><div class="info"><h4>基础设施配套</h4><div class="meta">INFRASTRUCTURE</div><p>城市基础设施配套建设与服务。</p></div></div>
        <div class="list-item"><div class="thumb"><div class="ico"><i class="fas fa-clipboard-check"></i></div></div><div class="info"><h4>工程技术咨询</h4><div class="meta">ENGINEERING CONSULTING</div><p>工程咨询、项目管理、技术支持等全过程服务。</p></div></div>
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
const body = computed(() => pick(content.value, 'industry_construction_body', '<p>安徽利至高建设工程有限公司是安徽宁商科技集团旗下建筑工程板块的核心企业，承载集团工程建设与基础设施配套服务的核心职能。</p><p>公司业务覆盖房屋建筑、工业厂房、公共建筑施工总承包，以及城市道路、桥梁、给排水等市政公用工程。</p>'))
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
