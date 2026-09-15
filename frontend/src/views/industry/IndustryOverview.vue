<template>
  <div>
    <PageBanner image="/images/industry-banner.jpeg" title="集团产业" />
    <SubNav title="集团产业">
      <router-link to="/industry/construction">建筑工程</router-link>
      <router-link to="/industry/software">软件科技</router-link>
    </SubNav>
    <section class="section text-bg-industry"><div class="wrap">
      <div class="sec-head">
        <span class="en">SUBSIDIARIES</span><h3>成员企业</h3><p>多元产业协同发展，构建覆盖多领域的产业服务生态</p>
      </div>
      <div class="content-list">
        <div class="list-item" v-for="sub in subsidiaries" :key="sub.id">
          <div class="thumb"><img :src="sub.logo" alt=""></div>
          <div class="info">
            <h4>{{ sub.name }}</h4>
            <div class="meta">{{ sub.englishName }} · {{ sub.category }}</div>
            <RichContent :content="sub.description" />
          </div>
        </div>
      </div>
    </div></section>
    <section class="section soft"><div class="wrap">
      <div class="sec-head"><span class="en">CORE BUSINESS</span><h3>核心业务领域</h3><p>科技赋能产业，服务贯穿全程</p></div>
      <div class="biz-grid">
        <div class="biz-card" style="background-image:url(/images/biz-1.png)"><div class="ico"><i class="fas fa-building"></i></div><h5>建设工程</h5><p>建筑施工、市政配套与城市基础设施服务。</p></div>
        <div class="biz-card" style="background-image:url(/images/biz-2.png)"><div class="ico"><i class="fas fa-microchip"></i></div><h5>数字科技</h5><p>建筑数字化、人工智能与算法软件开发。</p></div>
        <div class="biz-card" style="background-image:url(/images/biz-3.png)"><div class="ico"><i class="fas fa-chart-line"></i></div><h5>信息咨询</h5><p>企业全周期科创赋能与专业咨询。</p></div>
        <div class="biz-card" style="background-image:url(/images/biz-4.png)"><div class="ico"><i class="fas fa-gears"></i></div><h5>智能装备</h5><p>智能装备研发智造与数字技术应用。</p></div>
        <div class="biz-card" style="background-image:url(/images/biz-5.png)"><div class="ico"><i class="fas fa-city"></i></div><h5>物业管理</h5><p>物业运营评估与城市综合配套服务。</p></div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import api from '@/api'
const subsidiaries = ref([])
onMounted(async () => {
  const res = await api.getSubsidiaries()
  if (res.code === 200) subsidiaries.value = res.data
})
</script>

<style scoped>
.section { padding: 80px 0; }
.section.soft { background: var(--c-bg-soft); }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-industry { background-image: url(/images/2945347C3CC652EA1119F1A7F09DC2A9.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.sec-head { text-align: center; margin-bottom: 50px; }
.sec-head .en { font-size: 14px; color: var(--c-accent); letter-spacing: 4px; text-transform: uppercase; display: block; margin-bottom: 8px; }
.sec-head h3 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.sec-head p { color: var(--c-text-light); margin-top: 12px; }
.content-list { list-style: none; padding: 0; }
.list-item { display: flex; gap: 32px; padding: 28px 0; border-bottom: 1px solid var(--c-line); transition: .3s; }
.list-item:hover { background: var(--c-bg-soft); margin: 0 -20px; padding: 28px 20px; }
.thumb { flex: 0 0 260px; height: 170px; border-radius: 4px; overflow: hidden; display: flex; align-items: center; justify-content: center; background: var(--c-bg-soft); }
.thumb img { max-width: 200px; max-height: 120px; object-fit: contain; }
.info { flex: 1; display: flex; flex-direction: column; justify-content: center; }
.info h4 { font-size: 19px; color: var(--c-primary); margin-bottom: 10px; font-weight: 600; }
.meta { font-size: 13px; color: var(--c-accent); margin-bottom: 10px; }
.info p { font-size: 14px; color: var(--c-text-light); line-height: 1.7; }
.biz-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 20px; }
.biz-card { background: #fff; background-size: cover; border: 1px solid var(--c-line); padding: 38px 26px; transition: .3s; position: relative; overflow: hidden; }
.biz-card:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-6px); border-color: transparent; }
.ico { width: 64px; height: 64px; border-radius: 50%; background: var(--c-bg-soft); display: flex; align-items: center; justify-content: center; font-size: 28px; color: var(--c-primary); margin-bottom: 20px; }
.biz-card h5 { font-size: 19px; color: var(--c-primary); margin-bottom: 10px; }
.biz-card p { font-size: 14px; color: var(--c-text-light); }
@media (max-width: 1000px) {
  .list-item { flex-direction: column; gap: 16px; }
  .thumb { flex: none; width: 100%; height: 150px; }
  .biz-grid { grid-template-columns: 1fr; }
}
</style>
