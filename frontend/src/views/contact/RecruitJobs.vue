<template>
  <div>
    <PageBanner image="/images/contact-banner.jpeg" title="招聘岗位" />
    <SubNav title="联系宁商">
      <router-link to="/recruit">人才理念</router-link>
      <router-link to="/recruit/jobs" class="on">招聘岗位</router-link>
      <router-link to="/contact">联系方式</router-link>
      <router-link to="/contact/message">在线留言</router-link>
    </SubNav>
    <section class="section text-bg-job"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>招聘岗位</h1></div>
        <div class="detail-body">
          <p>诚邀志同道合者，与宁商同心同行、共赴远方</p>
          <div class="job-list" v-if="jobs.length">
            <div class="job-item" v-for="job in jobs" :key="job.id">
              <div class="job-head"><h4>{{ job.title }}</h4><span class="job-dept">{{ job.department }}</span></div>
              <div class="job-tags">
                <el-tag size="small">✦ 招聘人数：{{ job.headcount }}</el-tag>
                <el-tag size="small">✦ 工作地点：{{ job.location }}</el-tag>
                <el-tag size="small">✦ 学历要求：{{ job.education }}</el-tag>
              </div>
              <div class="job-desc"><b>岗位描述：</b><RichContent :content="job.description" /></div>
            </div>
          </div>
          <el-empty v-else description="暂无在招岗位" />
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
import api from '@/api'

const jobs = ref([])
onMounted(async () => {
  const res = await api.getJobs()
  if (res.code === 200) jobs.value = res.data
})
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-job { background-image: url(/images/job-text-bg.png); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.detail-body { font-size: 15px; color: var(--c-text); line-height: 1.9; }
.detail-body > p { margin-bottom: 20px; }
.job-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; margin-top: 30px; }
.job-item { background: #fff; border: 1px solid var(--c-line); padding: 30px 28px; transition: .3s; position: relative; overflow: hidden; }
.job-item::before { content: ''; position: absolute; top: 0; left: 0; width: 100%; height: 3px; background: var(--c-accent); transform: scaleX(0); transform-origin: left; transition: .3s; }
.job-item:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-4px); border-color: transparent; }
.job-item:hover::before { transform: scaleX(1); }
.job-head { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px dashed var(--c-line); padding-bottom: 14px; margin-bottom: 16px; }
.job-head h4 { font-size: 20px; color: var(--c-primary); font-weight: 600; }
.job-dept { display: inline-block; background: var(--c-bg-soft); color: var(--c-primary); font-size: 13px; padding: 4px 14px; border-radius: 3px; }
.job-tags { display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 16px; }
.job-desc { font-size: 14px; color: var(--c-text-light); line-height: 1.8; }
.job-desc b { color: var(--c-text); font-weight: 500; }
@media (max-width: 1000px) {
  .job-list { grid-template-columns: 1fr; }
}
</style>
