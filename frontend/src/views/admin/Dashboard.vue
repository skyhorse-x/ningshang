<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6" v-for="card in cards" :key="card.key">
        <el-card shadow="hover" class="stat-card" :style="{ borderTop: '3px solid ' + card.color }" @click="router.push(card.path)">
          <div class="stat-item">
            <div class="stat-icon" :style="{ background: card.bg, color: card.color }">
              <el-icon :size="26"><component :is="card.icon" /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-num">{{ stats[card.key] || 0 }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <template #header>
            <div class="chart-head"><span>新闻分类分布</span></div>
          </template>
          <div ref="pieRef" class="chart" v-loading="loading"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <template #header>
            <div class="chart-head"><span>各模块数据量</span></div>
          </template>
          <div ref="barRef" class="chart" v-loading="loading"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近新闻 -->
    <el-card class="recent-card" shadow="never">
      <template #header>
        <div class="chart-head"><span>最近新闻</span><el-button size="small" text type="primary" @click="$router.push('/ningshang-admin/news')">管理新闻 →</el-button></div>
      </template>
      <el-table :data="recentNews" stripe>
        <el-table-column prop="title" label="标题" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="date" label="日期" width="120" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, markRaw } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import api from '@/api'
import { Document, Message, Briefcase, User, TrophyBase, Flag, Setting } from '@element-plus/icons-vue'

const cards = [
  { key: 'news', label: '新闻数量', icon: markRaw(Document), color: '#409eff', bg: '#ecf5ff', path: '/ningshang-admin/news' },
  { key: 'messages', label: '留言数量', icon: markRaw(Message), color: '#e6a23c', bg: '#fdf6ec', path: '/ningshang-admin/messages' },
  { key: 'jobs', label: '招聘岗位', icon: markRaw(Briefcase), color: '#67c23a', bg: '#f0f9eb', path: '/ningshang-admin/jobs' },
  { key: 'team', label: '团队成员', icon: markRaw(User), color: '#9b59f6', bg: '#f5f0ff', path: '/ningshang-admin/team' },
]
const router = useRouter()

const loading = ref(true)
const stats = ref({})
const recentNews = ref([])

const pieRef = ref(null)
const barRef = ref(null)
let pieChart = null
let barChart = null

const CATS = { group: '集团新闻', industry: '产业动态', trend: '行业资讯', staff: '员工风采' }
const PIE_COLORS = ['#0d3a72', '#2f7fe0', '#e6a23c', '#67c23a']

const renderCharts = () => {
  // 饼图：新闻分类分布
  const newsList = cached.news || []
  const catCount = {}
  newsList.forEach(n => {
    const name = CATS[n.category] || n.categoryName || '其他'
    catCount[name] = (catCount[name] || 0) + 1
  })
  if (pieRef.value) {
    pieChart = pieChart || echarts.init(pieRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}：{c} 条（{d}%）' },
      legend: { bottom: 0, icon: 'circle', itemWidth: 8, itemHeight: 8 },
      color: PIE_COLORS,
      series: [{
        type: 'pie',
        radius: ['42%', '66%'],
        center: ['50%', '44%'],
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: true, formatter: '{b}\n{c} 条' },
        data: Object.keys(catCount).length
          ? Object.entries(catCount).map(([name, value]) => ({ name, value }))
          : [{ name: '暂无数据', value: 0, itemStyle: { color: '#e0e0e0' } }]
      }]
    })
  }
  // 柱状图：各模块数据量
  if (barRef.value) {
    barChart = barChart || echarts.init(barRef.value)
    barChart.setOption({
      tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
      grid: { left: 40, right: 20, top: 30, bottom: 30 },
      xAxis: { type: 'category', data: ['新闻', '留言', '招聘', '团队', '荣誉', '大事记'], axisTick: { show: false } },
      yAxis: { type: 'value', minInterval: 1, splitLine: { lineStyle: { type: 'dashed' } } },
      series: [{
        type: 'bar',
        barWidth: 26,
        itemStyle: { borderRadius: [6, 6, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#2f7fe0' }, { offset: 1, color: '#0d3a72' }]) },
        label: { show: true, position: 'top' },
        data: ['news', 'messages', 'jobs', 'team', 'honors', 'milestones'].map(k => stats.value[k] || 0)
      }]
    })
  }
}

// 独立取数：权限不足（403）或接口异常时静默跳过，对应图表显示为 0
const cached = {}
const safeCount = async (module) => {
  try {
    const res = await api.adminList(module)
    if (res.code === 200) { cached[module] = res.data; return (res.data || []).length }
  } catch (e) { /* 无权限或接口异常 */ }
  return 0
}

onMounted(async () => {
  try {
    const res = await api.getHome()
    if (res.code === 200) {
      recentNews.value = (res.data.news || []).slice(0, 6)
      cached.news = res.data.news || []
      stats.value.news = cached.news.length
    }
  } catch (e) { /* ignore */ }

  const [messages, jobs, team, honors, milestones] = await Promise.all([
    safeCount('messages'), safeCount('jobs'), safeCount('team'),
    safeCount('honors'), safeCount('milestones')
  ])
  stats.value.messages = messages
  stats.value.jobs = jobs
  stats.value.team = team
  stats.value.honors = honors
  stats.value.milestones = milestones

  loading.value = false
  renderCharts()
})

const onResize = () => { pieChart && pieChart.resize(); barChart && barChart.resize() }
onMounted(() => window.addEventListener('resize', onResize))
onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  pieChart && pieChart.dispose()
  barChart && barChart.dispose()
})
</script>

<style scoped>
.stat-cards { margin-bottom: 16px; }
.stat-card { cursor: pointer; transition: transform .18s ease, box-shadow .18s ease; }
.stat-card:hover { transform: translateY(-2px); }
.stat-card :deep(.el-card__body) { padding: 18px 20px; }
.stat-item { display: flex; align-items: center; gap: 16px; }
.stat-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-num { font-size: 28px; font-weight: 700; color: #1f2d3d; font-family: Georgia, serif; line-height: 1.2; }
.stat-label { font-size: 13px; color: #8a94a6; margin-top: 4px; }
.chart-row { margin-bottom: 16px; }
.chart { height: 300px; }
.chart-head { display: flex; justify-content: space-between; align-items: center; font-weight: 600; color: #1f2d3d; }
.recent-card :deep(.el-card__header) { padding: 12px 20px; }
</style>
