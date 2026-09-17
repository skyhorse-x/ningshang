<template>
  <div>
    <PageBanner image="/images/news-center-banner.jpeg" title="新闻中心" />
    <div class="subnav-section"><div class="wrap"><span class="title">新闻中心</span></div></div>
    <div class="subnav-tabs news-tabs"><div class="wrap">
      <a href="#" :class="{on: activeCat==='all'}" @click.prevent="setCategory('all')">全部新闻</a>
      <a href="#" :class="{on: activeCat==='group'}" @click.prevent="setCategory('group')">集团新闻</a>
      <a href="#" :class="{on: activeCat==='industry'}" @click.prevent="setCategory('industry')">产业动态</a>
      <a href="#" :class="{on: activeCat==='trend'}" @click.prevent="setCategory('trend')">行业资讯</a>
      <a href="#" :class="{on: activeCat==='staff'}" @click.prevent="setCategory('staff')">员工风采</a>
    </div></div>
    <section class="section text-bg-news"><div class="wrap">
      <ul class="content-list news-waterfall">
        <li class="nitem" v-for="item in pagedNews" :key="item.id">
          <div class="list-item">
            <router-link class="thumb" :to="'/news/' + item.newsId"><img :src="item.image" alt=""></router-link>
            <div class="info">
              <h4><router-link :to="'/news/' + item.newsId">{{ item.title }}</router-link></h4>
              <div class="meta"><span class="cat">{{ item.categoryName }}</span></div>
              <p>{{ item.summary }}</p>
            </div>
          </div>
        </li>
      </ul>
      <div class="pagination-row" v-if="filteredNews.length > pageSize">
        <el-pagination background layout="prev, pager, next" :total="filteredNews.length" :page-size="pageSize" v-model:current-page="currentPage" />
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageBanner from '@/components/layout/PageBanner.vue'
import api from '@/api'
const route = useRoute()
const router = useRouter()
const newsList = ref([])
const activeCat = ref('all')
const currentPage = ref(1)
const pageSize = 8
const filteredNews = computed(() => activeCat.value === 'all' ? newsList.value : newsList.value.filter(n => n.category === activeCat.value))
const pagedNews = computed(() => filteredNews.value.slice((currentPage.value - 1) * pageSize, currentPage.value * pageSize))
const normalizeCategory = value => ['group', 'industry', 'trend', 'staff'].includes(value) ? value : 'all'
const setCategory = (category) => {
  activeCat.value = category
  currentPage.value = 1
  router.replace({ path: '/news', query: category === 'all' ? {} : { category } })
}
onMounted(async () => {
  activeCat.value = normalizeCategory(route.query.category)
  const res = await api.getNews()
  if (res.code === 200) newsList.value = res.data
})
watch(() => route.query.category, value => { activeCat.value = normalizeCategory(value); currentPage.value = 1 })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-news { background-image: url(/images/43B3F7AAFD4D74BF80FA30DFA7B129CE.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.subnav-section { background: var(--c-primary); color: #fff; padding: 18px 0; }
.subnav-section .title { font-size: 22px; font-weight: 600; position: relative; padding-left: 18px; }
.subnav-section .title::before { content: ''; position: absolute; left: 0; top: 50%; transform: translateY(-50%); width: 3px; height: 24px; background: var(--c-accent); }
.subnav-tabs { background: var(--c-primary-light); }
.subnav-tabs .wrap { display: flex; gap: 0; }
.subnav-tabs a { padding: 14px 32px; font-size: 15px; color: rgba(255,255,255,.85); position: relative; transition: .25s; text-decoration: none; }
.subnav-tabs a:hover { color: #fff; background: rgba(255,255,255,.08); }
.subnav-tabs a.on { color: #fff; font-weight: 600; background: rgba(255,255,255,.12); }
.subnav-tabs a.on::after { content: ''; position: absolute; left: 32px; right: 32px; bottom: 0; height: 3px; background: var(--c-accent); }
.content-list { list-style: none; padding: 0; margin: 0; }
.list-item { display: flex; gap: 32px; padding: 28px 0; border-bottom: 1px solid var(--c-line); transition: .3s; }
.list-item:hover { background: var(--c-bg-soft); margin: 0 -20px; padding: 28px 20px; }
.thumb { flex: 0 0 260px; height: 170px; border-radius: 4px; overflow: hidden; display: block; }
.thumb img { width: 100%; height: 100%; object-fit: cover; transition: .4s; }
.list-item:hover .thumb img { transform: scale(1.05); }
.info { flex: 1; display: flex; flex-direction: column; justify-content: center; }
.info h4 { font-size: 19px; color: var(--c-primary); margin-bottom: 10px; font-weight: 600; }
.info h4 a { color: inherit; text-decoration: none; }
.info h4 a:hover { color: var(--c-accent); }
.meta { font-size: 13px; color: var(--c-text-light); margin-bottom: 10px; }
.meta .cat { color: var(--c-accent); font-weight: 600; }
.info p { font-size: 14px; color: var(--c-text-light); line-height: 1.7; }
.pagination-row { display: flex; justify-content: center; margin-top: 32px; }
@media (max-width: 1000px) {
  .list-item { flex-direction: column; gap: 16px; }
  .thumb { flex: none; width: 100%; height: 200px; }
}
</style>
