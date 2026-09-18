<template>
  <div class="section home-news-sec">
    <div class="wrap">
      <div class="home-news">
        <el-carousel v-if="featuredNews.length" class="hn-carousel" height="300px" :interval="4500" :autoplay="featuredNews.length > 1" :pause-on-hover="true" arrow="hover" :indicator-position="featuredNews.length > 1 ? '' : 'none'" aria-label="集团新闻轮播">
          <el-carousel-item v-for="item in featuredNews" :key="item.newsId">
            <router-link :to="'/news/' + item.newsId" class="hn-feat">
              <img :src="item.image || '/images/news-left-banner.png'" :alt="item.title">
              <div class="hn-feat-mask"><h4>{{ item.title }}</h4></div>
              <div class="hn-feat-meta"><span class="cat">集团新闻</span></div>
            </router-link>
          </el-carousel-item>
        </el-carousel>
        <router-link v-else to="/news" class="hn-feat">
          <img src="/images/news-left-banner.png" alt="集团新闻">
          <div class="hn-feat-mask"><h4>安徽宁商科技集团正式成立 扎根合肥赋能区域科创</h4></div>
          <div class="hn-feat-meta"><span class="cat">集团新闻</span></div>
        </router-link>
        <div class="hn-list">
          <el-tabs v-model="activeTab" stretch>
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
          <button type="button" class="hn-video" aria-label="播放宁商集团宣传片" @click="openVideo">
            <img src="/images/news-staff-training.jpeg" alt="宁商宣传片">
            <div class="hn-video-mask">
              <div class="play-btn">▶</div>
              <span>宁商集团宣传片</span>
            </div>
          </button>
        </div>
      </div>
    </div>
    <el-dialog v-model="videoVisible" title="宁商集团宣传片" width="min(960px, 94vw)" align-center append-to-body destroy-on-close @close="stopVideo">
      <video v-if="videoVisible && videoUrl" ref="videoPlayer" class="promo-player" :src="videoUrl" poster="/images/news-staff-training.jpeg" controls autoplay playsinline preload="metadata" @error="videoFailed = true" />
      <p v-if="!videoUrl || videoFailed" class="video-message" role="status">{{ videoFailed ? '视频暂时无法播放，请稍后重试。' : '宣传片即将上线，敬请期待。' }}</p>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import api from '@/api'
const newsList = ref([])
const activeTab = ref('group')
const categories = [
  { label: '集团新闻', value: 'group' },
  { label: '产业动态', value: 'industry' },
  { label: '行业资讯', value: 'trend' },
  { label: '员工风采', value: 'staff' }
]
const filteredNews = computed(() => newsList.value.filter(n => n.category === activeTab.value).slice(0, 5))
const featuredNews = computed(() => newsList.value.filter(n => n.category === 'group'))
const videoUrl = (import.meta.env.VITE_HOME_VIDEO_URL || 'https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4').trim()
const videoVisible = ref(false)
const videoFailed = ref(false)
const videoPlayer = ref(null)
function openVideo() {
  videoFailed.value = false
  videoVisible.value = true
}
function stopVideo() {
  videoPlayer.value?.pause()
}
onBeforeUnmount(stopVideo)
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
.home-news { display: grid; grid-template-columns: minmax(0, 1.3fr) minmax(0, 1fr) minmax(0, 0.8fr); gap: 24px; align-items: start; }
.home-news > * { min-width: 0; }
.hn-carousel { width: 100%; }
.hn-carousel :deep(.el-carousel__indicators--horizontal) { bottom: 36px; }
.hn-feat { position: relative; display: block; border-radius: 4px; overflow: hidden; text-decoration: none; color: inherit; }
.hn-feat img { width: 100%; height: 300px; object-fit: cover; transition: .5s; display: block; }
.hn-feat:hover img { transform: scale(1.04); }
.hn-feat-mask { position: absolute; left: 0; right: 0; bottom: 0; padding: 20px 24px 44px; background: linear-gradient(transparent, rgba(8,28,56,.85)); color: #fff; }
.hn-feat-mask h4 { font-size: 16px; line-height: 1.5; font-weight: 500; }
.hn-feat-meta { position: absolute; left: 0; right: 0; bottom: 0; padding: 10px 24px; background: #fff; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--c-line); }
.hn-feat-meta .cat { font-size: 12px; color: var(--c-primary); font-weight: 500; }
.hn-list { border: 1px solid var(--c-line); border-radius: 4px; display: flex; flex-direction: column; background: #fff; width: 420px; min-width: 420px; max-width: 420px; height: 300px; overflow: hidden; }
.hn-list :deep(.el-tabs) { min-width: 0; }
.hn-list :deep(.el-tabs__header) { margin-bottom: 0; }
.hn-list ::v-deep(.el-tabs__nav-wrap::after) { background: var(--c-line); }
.hn-list ::v-deep(.el-tabs__nav) { width: 100%; display: grid; grid-template-columns: repeat(4, 1fr); }
.hn-list ::v-deep(.el-tabs__item) { width: 100%; padding: 0; text-align: center; font-size: 14px; font-weight: 500; }
.hn-list ::v-deep(.el-tabs__item.is-active) { color: var(--c-primary); font-weight: 500; }
.hn-list ::v-deep(.el-tabs__active-bar) { background: var(--c-accent); }
.hn-items { list-style: none; padding: 4px 0; margin: 0; flex: 1; min-height: 0; overflow-y: auto; }
.hn-items li a { display: flex; align-items: center; gap: 10px; padding: 12px 18px; transition: .25s; font-size: 13px; text-decoration: none; color: inherit; }
.hn-items li a:hover { background: var(--c-bg-soft); }
.dot { width: 5px; height: 5px; border-radius: 50%; background: var(--c-accent); flex-shrink: 0; }
.title { flex: 1; min-width: 0; color: var(--c-text); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.hn-items li:hover .title { color: var(--c-primary); }

.hn-side { display: flex; flex-direction: column; gap: 18px; }
.hn-video { position: relative; display: block; width: 100%; padding: 0; border: 0; font: inherit; cursor: pointer; border-radius: 4px; overflow: hidden; text-decoration: none; }
.hn-video:focus-visible { outline: 3px solid var(--c-accent); outline-offset: 3px; }
.promo-player { display: block; width: 100%; max-height: 70vh; aspect-ratio: 16 / 9; background: #000; }
.video-message { padding: 32px 16px; text-align: center; color: var(--c-text-light); }
.hn-video img { width: 100%; height: 300px; object-fit: cover; transition: .5s; display: block; }
.hn-video:hover img { transform: scale(1.04); }
.hn-video-mask { position: absolute; inset: 0; background: rgba(8,28,56,.40); display: flex; flex-direction: column; align-items: center; justify-content: center; color: #fff; }
.play-btn { width: 50px; height: 50px; border-radius: 50%; background: rgba(200,164,92,.92); display: flex; align-items: center; justify-content: center; font-size: 16px; color: #fff; margin-bottom: 10px; transition: .3s; }
.hn-video:hover .play-btn { background: var(--c-accent-dark); transform: scale(1.08); }
.hn-video-mask span { font-size: 14px; letter-spacing: 1px; }
@media (max-width: 1000px) {
  .hn-list { width: 100%; min-width: 0; max-width: none; }
  .home-news { grid-template-columns: 1fr; }
  .hn-carousel :deep(.el-carousel__container) { height: 200px !important; }
  .hn-feat img, .hn-video img { height: 200px; }
}
</style>
