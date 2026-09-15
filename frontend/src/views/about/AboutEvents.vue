<template>
  <div>
    <PageBanner image="/images/dashiji.jpg" title="发展大事记" en="MILESTONES" :breadcrumb="[{text:'首页',to:'/'},{text:'集团概况',to:'/about/intro'},{text:'发展大事记'}]" />
    <SubNav title="集团概况">
      <router-link to="/about/intro">集团简介</router-link>
      <router-link to="/about/speech">董事长致词</router-link>
      <router-link to="/about/events" class="on">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor">企业荣誉</router-link>
      <router-link to="/about/party">党建工作</router-link>
      <router-link to="/about/culture">企业文化</router-link>
    </SubNav>
    <section class="section text-bg-events"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>集团大事记</h1><div class="meta">记录宁商发展历程中的每一个重要时刻</div></div>
        <TimelineSection />
      </div>
    </div></section>
  </div>
</template>

<script setup>
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import TimelineSection from '@/components/business/TimelineSection.vue'
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-events { background-image: url(/images/C1F2DFC9578B6FF62C261218F7688AC0.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
</style>
