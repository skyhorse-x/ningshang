<template>
  <div class="home-page">
    <h1 class="sr-only">安徽宁商科技集团有限公司</h1>
    <HeroCarousel />
    <NewsSection />
    <IndustryGrid />
    <section class="section advantages-section">
      <div class="wrap">
        <div class="sec-head">
          <span class="en">OUR ADVANTAGES</span>
          <h3>五大核心优势</h3>
          <p>以专业能力推动城市建设与数字化转型</p>
        </div>
        <div class="adv-grid">
          <div v-for="item in advantages" :key="item.title" class="adv-card">
            <div class="adv-ico"><i :class="item.icon"></i></div>
            <h5>{{ item.title }}</h5>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>
    <section class="section business-section">
      <div class="wrap">
        <div class="sec-head">
          <span class="en">CORE BUSINESS</span>
          <h3>核心业务领域</h3>
          <p>科技赋能产业，服务贯穿全程</p>
        </div>
        <div class="biz-grid">
          <div v-for="(item, index) in businessList" :key="item.id || item.name" class="biz-card">
            <div class="biz-media">
              <img :src="item.coverImage || fallbackImages[index % fallbackImages.length]" :alt="item.name" loading="lazy">
            </div>
            <div class="biz-body">
              <h5>{{ item.name }}</h5>
              <p>{{ richTextPreview(item.description) }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section v-if="partners.length" class="section partners-section">
      <div class="wrap">
        <div class="sec-head">
          <span class="en">PARTNERS</span>
          <h3>合作伙伴</h3>
          <p>携手优质伙伴，共建开放共赢的产业生态</p>
        </div>
        <div class="partner-grid">
          <a v-for="item in partners" :key="item.id || item.name" class="partner-card" :href="item.link || 'javascript:;'" :target="item.link && item.link !== '#' ? '_blank' : '_self'" :title="item.name">
            <img :src="item.logo" :alt="item.name">
          </a>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import HeroCarousel from '@/components/business/HeroCarousel.vue'
import NewsSection from '@/components/business/NewsSection.vue'
import IndustryGrid from '@/components/business/IndustryGrid.vue'
// import CoverageMap from '@/components/business/CoverageMap.vue'
import { ref, onMounted, computed } from 'vue'
import api from '@/api'
import { richTextPreview } from '@/utils/richText'

const fallbackImages = ['/images/biz-1.png', '/images/biz-2.png', '/images/biz-3.png', '/images/biz-4.png', '/images/biz-5.png']
const advantages = [
  { title: '专业团队', desc: '汇聚行业精英，提供全周期项目解决方案。', icon: 'fas fa-building' },
  { title: '技术创新', desc: '融合数字化技术，驱动产业智能化升级。', icon: 'fas fa-gear' },
  { title: '品质保障', desc: '严守质量管理体系，打造优质精品工程。', icon: 'fas fa-shield-halved' },
  { title: '绿色发展', desc: '践行低碳环保理念，建设可持续未来。', icon: 'fas fa-leaf' },
  { title: '合作共赢', desc: '开放协作，携手客户与伙伴共创成长。', icon: 'fas fa-handshake' }
]
const partners = ref([])

const defaultBusinesses = [
  { name: '建设工程', description: '建筑施工、市政配套与城市基础设施服务，匠心铸就品质工程。', coverImage: '/images/biz-1.png' },
  { name: '数字科技', description: '建筑数字化、人工智能与算法软件开发，打造科创服务核心引擎。', coverImage: '/images/biz-2.png' },
  { name: '信息咨询', description: '企业全周期科创赋能与专业咨询，助力规范化高质量发展。', coverImage: '/images/biz-3.png' },
  { name: '智能装备', description: '智能装备研发智造与数字技术应用，赋能多领域数字化转型。', coverImage: '/images/biz-4.png' },
  { name: '物业管理', description: '物业运营评估与城市综合配套服务，深耕多元城市服务板块。', coverImage: '/images/biz-5.png' }
]
const coreBusinesses = ref([])
const businessList = computed(() => coreBusinesses.value.length ? coreBusinesses.value : defaultBusinesses)

onMounted(async () => {
  const [bizRes, partnerRes] = await Promise.all([api.getCoreBusinesses(), api.getPartners()])
  if (bizRes.code === 200 && Array.isArray(bizRes.data)) coreBusinesses.value = bizRes.data
  if (partnerRes.code === 200 && Array.isArray(partnerRes.data)) partners.value = partnerRes.data.filter(item => item.status === 1 && item.logo)
})
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.home-page { overflow: hidden; background: #fff; }
.sr-only { position: absolute; width: 1px; height: 1px; padding: 0; margin: -1px; overflow: hidden; clip: rect(0,0,0,0); white-space: nowrap; border: 0; }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 12px; color: var(--c-accent-dark); letter-spacing: 5px; text-transform: uppercase; display: block; margin-bottom: 10px; font-weight: 600; }
.sec-head h3 { display: inline-flex; align-items: center; gap: 18px; font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head h3::before, .sec-head h3::after { content: ''; width: 38px; height: 1px; background: linear-gradient(90deg, transparent, var(--c-accent)); }
.sec-head h3::after { background: linear-gradient(90deg, var(--c-accent), transparent); }
.sec-head p { color: var(--c-text-light); margin-top: 12px; font-size: 15px; }
.business-section { position: relative; background: linear-gradient(180deg, #fff 0%, #f7f9fc 100%); }
.business-section::before { content: ''; position: absolute; width: 420px; height: 420px; right: -210px; top: 40px; border-radius: 50%; background: radial-gradient(circle, rgba(30,90,168,.08), transparent 68%); pointer-events: none; }
.biz-grid { position: relative; display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 18px; }
.biz-card { background: rgba(255,255,255,.96); border: 1px solid rgba(13,58,114,.10); border-radius: 12px; overflow: hidden; display: flex; flex-direction: column; transition: transform .3s ease, box-shadow .3s ease, border-color .3s ease; position: relative; box-shadow: 0 8px 24px rgba(13,58,114,.04); }
.biz-card::after { content: ''; position: absolute; left: 0; right: 0; bottom: 0; height: 3px; background: linear-gradient(90deg, var(--c-primary), var(--c-accent)); transform: scaleX(0); transform-origin: left; transition: transform .3s ease; }
.biz-card:hover { box-shadow: 0 16px 36px rgba(13,58,114,.13); transform: translateY(-7px); border-color: rgba(200,164,92,.45); }
.biz-card:hover::after { transform: scaleX(1); }
.biz-media { position: relative; overflow: hidden; height: 128px; margin: 14px 14px 0; border-radius: 8px; background: var(--c-bg-soft); }
.biz-media img { width: 100%; height: 100%; object-fit: cover; display: block; transition: transform .6s; }
.biz-card:hover .biz-media img { transform: scale(1.06); }
  .biz-body { flex: 1; display: flex; flex-direction: column; padding: 18px 16px 20px; }
  .biz-card h5 { font-size: 17px; color: var(--c-primary); margin-bottom: 8px; line-height: 1.45; }
  .biz-card p { font-size: 13px; color: var(--c-text-light); line-height: 1.7; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 3; overflow: hidden; }
.advantages-section { background: linear-gradient(135deg, #f4f8fd 0%, #fff 52%, #f8fafc 100%); position: relative; overflow: hidden; }
.advantages-section::before { content: ''; position: absolute; left: 0; right: 0; bottom: 0; height: 170px; background: linear-gradient(0deg, rgba(13,58,114,.07), transparent); pointer-events: none; }
.adv-grid { position: relative; z-index: 1; display: grid; grid-template-columns: repeat(5, 1fr); gap: 22px; }
.adv-card { text-align: center; padding: 28px 16px 24px; border: 1px solid rgba(13,58,114,.08); border-radius: 12px; background: rgba(255,255,255,.78); transition: transform .3s ease, box-shadow .3s ease, background .3s ease; }
.adv-card:hover { transform: translateY(-6px); background: #fff; box-shadow: 0 14px 34px rgba(13,58,114,.10); }
.adv-ico { width: 68px; height: 68px; border-radius: 50%; margin: 0 auto 18px; display: flex; align-items: center; justify-content: center; color: #256ce1; font-size: 28px; background: radial-gradient(circle at 35% 35%, #fff, #dce9ff); box-shadow: 0 10px 24px rgba(37,108,225,.12); }
.adv-card h5 { font-size: 18px; color: var(--c-primary); margin-bottom: 10px; }
.adv-card p { margin: 0 auto; max-width: 170px; color: var(--c-text-light); font-size: 13px; line-height: 1.7; }
.partners-section { background: linear-gradient(180deg, #f6f8fb 0%, #eef3f8 100%); }
.partner-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 18px; }
.partner-card { min-height: 118px; border: 1px solid rgba(13,58,114,.08); background: rgba(255,255,255,.92); border-radius: 10px; display: flex; align-items: center; justify-content: center; padding: 20px; box-shadow: 0 8px 24px rgba(13,58,114,.05); transition: transform .3s ease, box-shadow .3s ease, border-color .3s ease; }
.partner-card:hover { transform: translateY(-5px); border-color: rgba(200,164,92,.5); box-shadow: 0 14px 30px rgba(13,58,114,.11); }
.partner-card img { width: 100%; max-width: 150px; height: 58px; object-fit: contain; filter: grayscale(18%); transition: filter .3s ease, transform .3s ease; }
.partner-card:hover img { filter: grayscale(0); transform: scale(1.04); }
  @media (max-width: 1000px) {
  .section { padding: 52px 0; }
  .sec-head { margin-bottom: 30px; }
  .sec-head h3 { font-size: 26px; }
  .sec-head h3::before, .sec-head h3::after { width: 24px; }
    .biz-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
    .adv-grid, .partner-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .biz-grid { gap: 20px; }
  .biz-body { padding: 22px 22px 24px; }
  }
  @media (max-width: 640px) {
    .biz-grid { grid-template-columns: 1fr; }
    .adv-grid, .partner-grid { grid-template-columns: 1fr; }
    .sec-head h3 { gap: 10px; }
    .sec-head h3::before, .sec-head h3::after { width: 18px; }
  }
</style>
