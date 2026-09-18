<template>
  <div>
    <PageBanner :image="bg('bg_contact_banner', '/images/news-staff-training.jpeg')" title="人才理念" />
    <SubNav title="联系宁商">
      <router-link to="/recruit" class="on">人才理念</router-link>
      <router-link to="/recruit/jobs">招聘岗位</router-link>
      <router-link to="/contact">联系方式</router-link>
      <router-link to="/contact/message">在线留言</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>人才理念</h1></div>
        <div class="detail-body" v-if="false">
          <p style="text-align:center;font-size:22px;color:var(--c-primary);font-weight:600;text-indent:0;margin-bottom:8px;">务实笃行 · 创新赋能 · 诚信致远</p>
          <p style="text-align:center;color:var(--c-text-light);text-indent:0;margin-bottom:40px;">以人为本，聚才兴企，与志同道合者共赴远方</p>
          <h3>人才价值观</h3>
          <p><b>01 务实笃行</b><br>不尚空谈、不务虚功，以脚踏实地的作风深耕业务。</p>
          <p><b>02 创新赋能</b><br>以持续创新的能力响应需求，鼓励探索突破。</p>
          <p><b>03 诚信致远</b><br>以诚信合规的经营赢得市场信赖，重信守诺。</p>
          <p><b>04 开放共赢</b><br>秉持开放共赢、兼容并蓄的经营理念。</p>
          <h3>加入宁商</h3>
          <p>乘势而上，生生不息，期待与您同心同行</p>
          <el-image src="/images/news-staff-training.jpeg" style="width:100%;border-radius:6px;margin:20px 0;" />
          <p><router-link to="/contact" style="border-bottom:2px solid var(--c-accent);padding-bottom:4px;color:var(--c-primary);font-weight:600;text-decoration:none;">联系我们应聘 →</router-link></p>
        </div>
        <RichContent class="detail-body" :content="body" />
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import RichContent from '@/components/business/RichContent.vue'
import { loadContent, pick } from '@/utils/content'
const content = ref({})
const bg = (key, fallback) => pick(content.value, key, fallback)
const defaultBody = '<p style="text-align:center;font-size:22px;font-weight:bold">务实笃行 · 创新赋能 · 诚信致远</p><p style="text-align:center">以人为本，聚才兴企，与志同道合者共赴远方</p><h3>人才价值观</h3><p><strong>01 务实笃行</strong><br>不尚空谈、不务虚功，以脚踏实地的作风深耕业务。</p><p><strong>02 创新赋能</strong><br>以持续创新的能力响应需求，鼓励探索突破。</p><p><strong>03 诚信致远</strong><br>以诚信合规的经营赢得市场信赖，重信守诺。</p><p><strong>04 开放共赢</strong><br>秉持开放共赢、兼容并蓄的经营理念。</p><h3>加入宁商</h3><p>乘势而上，生生不息，期待与您同心同行</p><p><a href="/contact">联系我们应聘 →</a></p>'
const body = computed(() => pick(content.value, 'recruit_body', defaultBody))
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.detail-body { font-size: 16px; color: var(--c-text); line-height: 2; }
.detail-body p { margin-bottom: 22px; text-indent: 2em; }
.detail-body h3 { font-size: 22px; color: var(--c-primary); margin: 32px 0 16px; font-weight: 600; }
</style>
