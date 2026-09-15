<template>
  <div>
    <PageBanner image="/images/1.jpg" title="企业荣誉" />
    <SubNav title="集团概况">
      <router-link to="/about/intro">集团简介</router-link>
      <router-link to="/about/speech">董事长致词</router-link>
      <router-link to="/about/events">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor" class="on">企业荣誉</router-link>
      <router-link to="/about/party">党建工作</router-link>
      <router-link to="/about/culture">企业文化</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>企业荣誉</h1><div class="meta">以专业资质铸就品牌 以自主创新赢得信赖</div></div>
        <div class="detail-body"><p>集团始终坚持以专业资质铸就品牌、以自主创新赢得信赖，在工程建设、数字软件、智能装备等领域取得了多项资质与荣誉。</p></div>
      </div>
      <HonorGrid />
    </div></section>
  </div>
</template>

<script setup>
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import HonorGrid from '@/components/business/HonorGrid.vue'
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto 40px; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
.detail-body p { font-size: 15px; color: var(--c-text); line-height: 1.9; }
</style>
