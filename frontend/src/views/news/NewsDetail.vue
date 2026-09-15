<template>
  <div>
    <PageBanner image="/images/news-center-banner.jpeg" title="新闻中心" />
    <section class="section"><div class="wrap">
      <div class="content-detail" v-if="news">
        <div class="detail-head">
          <h1>{{ news.title }}</h1>
          <div class="meta">
            <span class="cat">{{ news.categoryName }}</span>
            <span v-if="news.author"> | {{ news.author }}</span>
            <span v-if="news.source"> | {{ news.source }}</span>
          </div>
        </div>
        <RichContent class="detail-body" :content="news.body" />
        <div class="detail-foot" style="margin-top:40px;padding-top:24px;border-top:1px solid #e6e6e6;text-align:center;">
          <router-link to="/news" style="display:inline-block;padding:10px 28px;background:#c8a45c;color:#fff;border-radius:4px;text-decoration:none;font-size:14px;">返回新闻中心</router-link>
        </div>
      </div>
      <div class="content-detail" v-else><p style="text-align:center;color:#999;">{{ loading ? '正在加载…' : errorMessage }}</p></div>
    </div></section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import PageBanner from '@/components/layout/PageBanner.vue'
import api from '@/api'
const route = useRoute()
const news = ref(null)
const loading = ref(false)
const errorMessage = ref('文章未找到')
// 渲染前二次清洗，防存储型 XSS
watch(() => route.params.id, async (id, previous, onCleanup) => {
  let stale = false
  onCleanup(() => { stale = true })
  news.value = null
  loading.value = true
  errorMessage.value = '文章未找到'
  try {
    const res = await api.getNewsDetail(id)
    if (!stale) news.value = res.data
  } catch (e) {
    if (!stale) errorMessage.value = e.response?.status === 404 ? '文章未找到' : '加载失败，请稍后重试'
  } finally {
    if (!stale) loading.value = false
  }
}, { immediate: true })
</script>

<style scoped>
.section { padding: 50px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); margin-bottom: 16px; font-weight: 700; line-height: 1.4; }
.meta { font-size: 13px; color: var(--c-text-light); }
.meta .cat { color: var(--c-accent); font-weight: 600; }
.detail-body { font-size: 16px; line-height: 2; color: #3a3a3a; }
.detail-body :deep(p) { margin-bottom: 22px; text-indent: 2em; }
.detail-body :deep(img) { max-width: 100%; border-radius: 6px; margin: 20px auto; }
</style>
