<template>
  <div class="job-list">
    <div class="job-item" v-for="job in jobs" :key="job.id">
      <div class="job-head">
        <h4>{{ job.title }}</h4>
        <span class="job-dept">{{ job.department }}</span>
      </div>
      <div class="job-tags">
        <el-tag size="small">✦ 招聘人数：{{ job.headcount }}</el-tag>
        <el-tag size="small">✦ 工作地点：{{ job.location }}</el-tag>
        <el-tag size="small">✦ 学历要求：{{ job.education }}</el-tag>
      </div>
      <div class="job-desc"><b>岗位描述：</b><RichContent :content="job.description" /></div>
    </div>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
const props = defineProps({ jobs: { type: Array, default: () => [] } })
</script>

<style scoped>
.job-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24px; margin-top: 40px; }
.job-item { background: #fff; border: 1px solid var(--c-line); padding: 30px 28px; transition: .3s; position: relative; overflow: hidden; }
.job-item::before { content: ""; position: absolute; top: 0; left: 0; width: 100%; height: 3px; background: var(--c-accent); transform: scaleX(0); transform-origin: left; transition: .3s; }
.job-item:hover { box-shadow: 0 6px 24px rgba(13,58,114,.10); transform: translateY(-4px); border-color: transparent; }
.job-item:hover::before { transform: scaleX(1); }
.job-head { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px dashed var(--c-line); padding-bottom: 14px; margin-bottom: 16px; }
.job-head h4 { font-size: 20px; color: var(--c-primary); font-weight: 600; }
.job-dept { display: inline-block; background: var(--c-bg-soft); color: var(--c-primary); font-size: 13px; padding: 4px 14px; border-radius: 3px; white-space: nowrap; }
.job-tags { display: flex; flex-wrap: wrap; gap: 10px; margin-bottom: 16px; }
.job-desc { font-size: 14px; color: var(--c-text-light); line-height: 1.8; }
.job-desc b { color: var(--c-text); font-weight: 500; }
@media (max-width: 1000px) {
  .job-list { grid-template-columns: 1fr; }
}
</style>
