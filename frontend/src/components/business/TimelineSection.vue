<template>
  <div class="timeline">
    <el-timeline>
      <el-timeline-item
        v-for="(item, idx) in milestones"
        :key="item.id"
        :timestamp="item.year"
        placement="top"
        :color="'var(--c-accent)'"
      >
        <div class="card">
          <h5>{{ item.title }}</h5>
          <RichContent :content="item.description" />
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import api from '@/api'
const milestones = ref([])
onMounted(async () => {
  const res = await api.getMilestones()
  if (res.code === 200) milestones.value = res.data
})
</script>

<style scoped>
.timeline { margin: 30px 0; }
.timeline .card { background: #fff; border: 1px solid var(--c-line); padding: 20px 24px; border-radius: 6px; box-shadow: 0 4px 14px rgba(0,0,0,.05); }
.timeline .card h5 { font-size: 16px; color: var(--c-text); margin-bottom: 6px; }
.timeline .card p { font-size: 13px; color: var(--c-text-light); text-indent: 0; }
.timeline ::v-deep(.el-timeline-item__timestamp) { font-size: 24px; color: var(--c-primary); font-weight: 700; font-family: Georgia, serif; }
</style>
