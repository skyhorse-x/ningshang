<template>
  <div>
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
    <section class="section">
      <div class="wrap">
        <div class="sec-head">
          <span class="en">CORE BUSINESS</span>
          <h3>核心业务领域</h3>
          <p>科技赋能产业，服务贯穿全程</p>
        </div>
        <div class="biz-grid">
          <div v-for="(item, index) in businessList" :key="item.id || item.name" class="biz-card" :style="{ backgroundImage: `url(${item.coverImage || fallbackImages[index % fallbackImages.length]})` }">
            <div class="ico"><i :class="businessIcons[index % businessIcons.length]"></i></div>
            <h5>{{ item.name }}</h5>
            <p>{{ richTextPreview(item.description) }}</p>
          </div>
        </div>
      </div>
    </section>
    <section class="section partners-section">
      <div class="wrap">
        <div class="sec-head">
          <span class="en">PARTNERS</span>
          <h3>合作伙伴</h3>
          <p>携手优质伙伴，共建开放共赢的产业生态</p>
        </div>
        <div class="partner-grid">
          <div v-for="name in partners" :key="name" class="partner-card">{{ name }}</div>
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
const businessIcons = ['fas fa-building', 'fas fa-microchip', 'fas fa-chart-line', 'fas fa-gears', 'fas fa-city']
const advantages = [
  { title: '专业团队', desc: '汇聚行业精英，提供全周期项目解决方案。', icon: 'fas fa-building' },
  { title: '技术创新', desc: '融合数字化技术，驱动产业智能化升级。', icon: 'fas fa-gear' },
  { title: '品质保障', desc: '严守质量管理体系，打造优质精品工程。', icon: 'fas fa-shield-halved' },
  { title: '绿色发展', desc: '践行低碳环保理念，建设可持续未来。', icon: 'fas fa-leaf' },
  { title: '合作共赢', desc: '开放协作，携手客户与伙伴共创成长。', icon: 'fas fa-handshake' }
]
const partners = ['安徽利至高建设工程', '安徽陆洲科技', '安徽合州信息咨询', '安徽玉彤智能装备', '合肥南峰建设投资']

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
  const res = await api.getCoreBusinesses()
  if (res.code === 200 && Array.isArray(res.data)) coreBusinesses.value = res.data
})
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head p { color: var(--c-text-light); margin-top: 12px; font-size: 15px; }
.biz-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; }
.biz-card { background: #fff; background-size: cover; background-position: center; border: 1px solid var(--c-line); padding: 38px 26px; transition: .3s; position: relative; overflow: hidden; }
.biz-card:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-6px); border-color: transparent; }
.ico { width: 64px; height: 64px; border-radius: 50%; background: rgba(255,255,255,.15); display: flex; align-items: center; justify-content: center; font-size: 25px; color: #fff; margin-bottom: 20px; }
.biz-card h5 { font-size: 19px; color: #fff; margin-bottom: 10px; }
.biz-card p { font-size: 14px; color: rgba(255,255,255,.85); }
.advantages-section { background: linear-gradient(180deg, #f7fbff 0%, #fff 100%); position: relative; overflow: hidden; }
.advantages-section::before { content: ''; position: absolute; left: 0; right: 0; bottom: 0; height: 150px; background: linear-gradient(0deg, rgba(13,58,114,.06), transparent); pointer-events: none; }
.adv-grid { position: relative; z-index: 1; display: grid; grid-template-columns: repeat(5, 1fr); gap: 22px; }
.adv-card { text-align: center; padding: 14px 12px 0; }
.adv-ico { width: 68px; height: 68px; border-radius: 50%; margin: 0 auto 18px; display: flex; align-items: center; justify-content: center; color: #256ce1; font-size: 28px; background: radial-gradient(circle at 35% 35%, #fff, #dce9ff); box-shadow: 0 10px 24px rgba(37,108,225,.12); }
.adv-card h5 { font-size: 18px; color: var(--c-primary); margin-bottom: 10px; }
.adv-card p { margin: 0 auto; max-width: 170px; color: var(--c-text-light); font-size: 13px; line-height: 1.7; }
.partners-section { background: #f7f9fc; }
.partner-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 18px; }
.partner-card { min-height: 82px; border: 1px solid var(--c-line); background: #fff; border-radius: 6px; display: flex; align-items: center; justify-content: center; text-align: center; padding: 16px; color: var(--c-primary); font-weight: 600; box-shadow: 0 8px 24px rgba(13,58,114,.05); }
@media (max-width: 1000px) {
  .section { padding: 52px 0; }
  .sec-head { margin-bottom: 30px; }
  .sec-head h3 { font-size: 26px; }
  .biz-grid, .adv-grid, .partner-grid { grid-template-columns: 1fr; }
  .biz-card { min-height: 190px; padding: 30px 22px; }
}
</style>
