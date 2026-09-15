<template>
  <div>
    <PageBanner image="/images/dangjian.jpg" title="党建工作" en="PARTY BUILDING" :breadcrumb="[{text:'首页',to:'/'},{text:'集团概况',to:'/about/intro'},{text:'党建工作'}]" />
    <SubNav title="集团概况">
      <router-link to="/about/intro">集团简介</router-link>
      <router-link to="/about/speech">董事长致词</router-link>
      <router-link to="/about/events">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor">企业荣誉</router-link>
      <router-link to="/about/party" class="on">党建工作</router-link>
      <router-link to="/about/culture">企业文化</router-link>
    </SubNav>
    <section class="section text-bg-party"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>{{ c('party_title', '党建领航聚合力 实干奋进启新程') }}</h1><div class="meta">{{ c('party_meta', '安徽宁商科技集团筑牢红色根基引领高质量发展') }}</div></div>
        <div class="detail-body">
          <RichContent :content="bodyBlocks" />
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, computed, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)

const bodyBlocks = computed(() =>
  c('party_body', '以高质量党建引领集团高质量发展，传承红色基因，凝聚奋进力量。\n\n一、冲锋在前、实干担当\n安徽宁商科技集团2026年7月启动党支部筹备工作，4名党员全程亮身份、作表率。\n\n二、凝心铸魂、氛围浓厚\n党支部的组织成立过程，是安徽宁商科技集团发展历程中的重要里程碑。\n\n三、深度融合、同频奋进\n站在新的起点，安徽宁商科技集团将以党支部组织成立为契机，始终坚持党建领航。')

)

onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-party { background-image: url(/images/a.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
.detail-body { font-size: 15px; color: var(--c-text); line-height: 1.9; }
.detail-body p { margin-bottom: 18px; text-indent: 2em; }
.detail-body h3 { font-size: 22px; color: var(--c-primary); margin: 32px 0 16px; font-weight: 600; }
</style>
