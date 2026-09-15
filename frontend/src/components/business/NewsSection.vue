<template>
  <div class="section home-news-sec">
    <div class="wrap">
      <div class="home-news">
        <router-link v-if="featNews" :to="'/news/' + featNews.newsId" class="hn-feat">
          <img :src="featNews.image || '/images/news-left-banner.png'" :alt="featNews.categoryName">
          <div class="hn-feat-mask"><h4>{{ featNews.title }}</h4></div>
          <div class="hn-feat-meta"><span class="cat">{{ featNews.categoryName }}</span></div>
        </router-link>
        <router-link v-else to="/news" class="hn-feat">
          <img src="/images/news-left-banner.png" alt="集团新闻">
          <div class="hn-feat-mask"><h4>安徽宁商科技集团正式成立 扎根合肥赋能区域科创</h4></div>
          <div class="hn-feat-meta"><span class="cat">集团新闻</span></div>
        </router-link>
        <div class="hn-list">
          <el-tabs v-model="activeTab">
            <el-tab-pane v-for="cat in categories" :key="cat.value" :label="cat.label" :name="cat.value" />
          </el-tabs>
          <ul class="hn-items">
            <li v-for="item in filteredNews" :key="item.id">
              <router-link :to="'/news/' + item.newsId">
                <span class="dot"></span>
                <span class="title">{{ item.title }}</span>
              </router-link>
            </li>
          </ul>
        </div>
        <div class="hn-side">
          <router-link to="/about/intro" class="hn-video">
            <img src="/images/news-staff-training.jpeg" alt="宁商宣传片">
            <div class="hn-video-mask">
              <div class="play-btn">▶</div>
              <span>宁商集团宣传片</span>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '@/api'
const newsList = ref([])
const activeTab = ref('group')
const categories = [
  { label: '集团新闻', value: 'group' },
  { label: '产业动态', value: 'industry' },
  { label: '行业资讯', value: 'trend' },
  { label: '员工风采', value: 'staff' }
]
const filteredNews = computed(() => newsList.value.filter(n => n.category === activeTab.value))
const featNews = computed(() => newsList.value.find(n => n.category === 'group') || newsList.value[0] || null)
onMounted(async () => {
  try {
    const res = await api.getNews()
    if (res.code === 200) newsList.value = res.data
  } catch (e) {
    console.error('Failed to load news:', e)
  }
})
</script>

<style scoped>
.section { padding: 50px 0 40px; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.home-news { display: grid; grid-template-columns: 1.3fr 1fr 0.8fr; gap: 24px; }
.hn-feat { position: relative; display: block; border-radius: 4px; overflow: hidden; text-decoration: none; color: inherit; }
.hn-feat img { width: 100%; height: 300px; object-fit: cover; transition: .5s; display: block; }
.hn-feat:hover img { transform: scale(1.04); }
.hn-feat-mask { position: absolute; left: 0; right: 0; bottom: 0; padding: 20px 24px 44px; background: linear-gradient(transparent, rgba(8,28,56,.85)); color: #fff; }
.hn-feat-mask h4 { font-size: 16px; line-height: 1.5; font-weight: 500; }
.hn-feat-meta { position: absolute; left: 0; right: 0; bottom: 0; padding: 10px 24px; background: #fff; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--c-line); }
.hn-feat-meta .cat { font-size: 12px; color: var(--c-primary); font-weight: 500; }
.hn-list { border: 1px solid var(--c-line); border-radius: 4px; display: flex; flex-direction: column; background: #fff; }
.hn-list ::v-deep(.el-tabs__nav-wrap::after) { background: var(--c-line); }
.hn-list ::v-deep(.el-tabs__item) { font-size: 14px; padding: 0 16px; }
.hn-list ::v-deep(.el-tabs__item.is-active) { color: var(--c-primary); }
.hn-list ::v-deep(.el-tabs__active-bar) { background: var(--c-accent); }
.hn-items { list-style: none; padding: 4px 0; margin: 0; flex: 1; }
.hn-items li a { display: flex; align-items: center; gap: 10px; padding: 12px 18px; transition: .25s; font-size: 13px; text-decoration: none; color: inherit; }
.hn-items li a:hover { background: var(--c-bg-soft); }
.dot { width: 5px; height: 5px; border-radius: 50%; background: var(--c-accent); flex-shrink: 0; }
.title { flex: 1; color: var(--c-text); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.hn-items li:hover .title { color: var(--c-primary); }

.hn-side { display: flex; flex-direction: column; gap: 18px; }
.hn-video { position: relative; display: block; border-radius: 4px; overflow: hidden; text-decoration: none; }
.hn-video img { width: 100%; height: 300px; object-fit: cover; transition: .5s; display: block; }
.hn-video:hover img { transform: scale(1.04); }
.hn-video-mask { position: absolute; inset: 0; background: rgba(8,28,56,.40); display: flex; flex-direction: column; align-items: center; justify-content: center; color: #fff; }
.play-btn { width: 50px; height: 50px; border-radius: 50%; background: rgba(200,164,92,.92); display: flex; align-items: center; justify-content: center; font-size: 16px; color: #fff; margin-bottom: 10px; transition: .3s; }
.hn-video:hover .play-btn { background: var(--c-accent-dark); transform: scale(1.08); }
.hn-video-mask span { font-size: 14px; letter-spacing: 1px; }
@media (max-width: 1000px) {
  .home-news { grid-template-columns: 1fr; }
  .hn-feat img, .hn-video img { height: 200px; }
}
</style>
