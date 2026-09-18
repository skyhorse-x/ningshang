<template>
  <div>
    <PageBanner :image="bg('bg_about_banner', '/images/1.jpg')" title="集团简介" />
    <SubNav title="集团概况">
      <router-link to="/about/intro" class="on">集团简介</router-link>
      <router-link to="/about/speech">董事长致词</router-link>
      <router-link to="/about/events">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor">企业荣誉</router-link>
      <router-link to="/about/party">党建工作</router-link>
      <router-link to="/about/culture">企业文化</router-link>
    </SubNav>
    <section class="section about-text-section" :style="sectionBg('bg_about_page', '/images/about-pic.jpeg')">
      <div class="wrap">
        <div class="content-detail">
          <div class="detail-head"><h1>安徽宁商科技集团有限公司</h1><div class="meta">{{ c('about_intro_meta', '一徽藏一城，一潮见格局') }}</div></div>
          <div class="detail-body">
            <div class="intro-section">
              <h3>{{ c('about_intro_s1_title', '徽韵承城，扎根本土根基') }}</h3>
              <RichContent :content="c('about_intro_s1_body', '安徽宁商科技集团有限公司扎根安徽合肥，深度契合合肥&quot;城湖共生&quot;的城市格局与科创引领的发展脉络。')" />
            </div>
            <div class="intro-section">
              <h3>{{ c('about_intro_s2_title', '潮涌科创，锚定主业航向') }}</h3>
              <RichContent :content="c('about_intro_s2_body', '集团以数字科技为核心发展主线，奔涌拓展业务边界，形成覆盖技术研发、系统集成、数字文创的多元核心业务体系。')" />
            </div>
            <div class="intro-section">
              <h3>{{ c('about_intro_s3_title', '五子联动，共筑产业潮头') }}</h3>
              <div class="sub-grid-new">
                <div class="sub-item" v-for="sub in subsidiaries" :key="sub.id">
                  <div class="logo-area"><img :src="sub.logo" alt=""></div>
                  <div class="info-area"><h4>{{ sub.name }}</h4><RichContent :content="sub.description" /></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <div class="stats">
      <div class="wrap">
        <div class="item"><div class="num">{{ c('stat_founded', '2018') }}</div><div class="lbl">成立年份</div></div>
        <div class="item"><div class="num">{{ c('stat_companies', '4') }}</div><div class="lbl">成员企业</div></div>
        <div class="item"><div class="num">{{ c('stat_ip', '10+') }}</div><div class="lbl">知识产权</div></div>
        <div class="item"><div class="num">{{ c('stat_fields', '4') }}</div><div class="lbl">业务领域</div></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import api from '@/api'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)
const bg = (key, fallback) => pick(content.value, key, fallback)
const sectionBg = (key, fallback) => ({ backgroundImage: `url(${bg(key, fallback)})` })
const subsidiaries = ref([])
onMounted(async () => {
  content.value = await loadContent()
  const res = await api.getSubsidiaries()
  if (res.code === 200) subsidiaries.value = res.data
})
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.about-text-section { background-image: url(/images/about-pic.jpeg); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; }
.detail-head .meta { font-size: 13px; color: var(--c-text-light); }
.detail-body { font-size: 15px; color: var(--c-text); line-height: 1.9; }
.intro-section { margin-bottom: 40px; }
.intro-section h3 { font-size: 22px; color: var(--c-primary); margin-bottom: 16px; padding-left: 14px; border-left: 4px solid var(--c-primary); }
.intro-section p { font-size: 15px; line-height: 2; color: var(--c-text); margin-bottom: 14px; }
.sub-grid-new { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; margin-top: 20px; }
.sub-item { background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,.06); transition: .3s; }
.sub-item:hover { transform: translateY(-4px); box-shadow: 0 8px 30px rgba(0,0,0,.12); }
.logo-area { display: flex; align-items: center; justify-content: center; height: 140px; background: var(--c-bg-soft); }
.logo-area img { max-width: 120px; max-height: 100px; object-fit: contain; }
.info-area { padding: 18px 20px; }
.info-area h4 { font-size: 16px; color: var(--c-primary); margin-bottom: 8px; }
.info-area p { font-size: 13px; color: var(--c-text-light); line-height: 1.7; margin: 0; text-indent: 0; }
.stats { background: var(--c-primary); color: #fff; }
.stats .wrap { display: flex; justify-content: space-around; padding: 56px 0; text-align: center; }
.stats .item .num { font-size: 48px; font-weight: 700; color: var(--c-accent); font-family: Georgia, serif; }
.stats .item .num small { font-size: 22px; }
.stats .item .lbl { font-size: 15px; color: #c7d6ec; margin-top: 6px; letter-spacing: 1px; }
@media (max-width: 1000px) {
  .sub-grid-new { grid-template-columns: 1fr; }
  .stats .wrap { flex-wrap: wrap; gap: 24px; padding: 40px 0; }
}
</style>
