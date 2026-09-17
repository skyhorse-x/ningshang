<template>
  <div>
    <PageBanner image="/images/1.jpg" title="董事长致词" />
    <SubNav title="集团概况">
      <router-link to="/about/intro">集团简介</router-link>
      <router-link to="/about/speech" class="on">董事长致词</router-link>
      <router-link to="/about/events">发展大事记</router-link>
      <router-link to="/about/team">管理团队</router-link>
      <router-link to="/about/honor">企业荣誉</router-link>
      <router-link to="/about/party">党建工作</router-link>
      <router-link to="/about/culture">企业文化</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="speech-layout">
        <div class="speech-header">
          <div class="chairman-photo">
            <img src="/images/chairman.png" alt="董事长王力">
            <h3>{{ c('speech_chairman_name', '王 力') }}</h3>
            <p class="title">{{ c('speech_chairman_title', '安徽宁商科技集团 董事长') }}</p>
          </div>
          <div class="chairman-info">
            <RichContent class="quote" :content="quoteHtml" />
          </div>
        </div>
        <div class="speech-body">
          <RichContent :content="bodyParas" />
          <div class="signature-block">
            <p class="sign-name">{{ c('speech_sign', '安徽宁商科技集团有限公司 董事长　王力') }}</p>
            <p class="sign-title">{{ c('speech_date', '2026年8月') }}</p>
          </div>
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, computed, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)

const quoteHtml = computed(() =>
  c('speech_quote', '徽商古训有云：\n"诚为本，义为先，贾而好儒，行稳致远。"')
)
const bodyParas = computed(() =>
  c('speech_body', '尊敬的社会各界友人、合作伙伴，全体宁商同仁：\n\n千载江淮文脉，沉淀出实业兴邦的厚重底色；长三角一体化浪潮，奔涌着数字赋能的蓬勃生机。\n\n展望未来，集团将始终践行"科创赋能城乡，数字服务实业"的企业使命，以诚信立品牌之基，以创新拓产业之局。')
)

onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.speech-layout { max-width: 900px; margin: 0 auto; }
.speech-header { display: flex; align-items: center; gap: 40px; padding: 40px 0; border-bottom: 2px solid var(--c-primary); margin-bottom: 40px; }
.chairman-photo img { width: 220px; height: 280px; object-fit: cover; border-radius: 8px; box-shadow: 0 10px 40px rgba(0,0,0,.15); }
.chairman-photo h3 { font-size: 28px; color: var(--c-primary); margin: 12px 0 4px; letter-spacing: 2px; text-align: center; }
.chairman-photo .title { font-size: 15px; color: var(--c-text-light); text-align: center; }
.chairman-info { flex: 1; }
.quote { font-size: 18px; color: var(--c-accent); line-height: 1.6; border-left: 3px solid var(--c-primary); padding-left: 16px; font-style: italic; }
.speech-body { font-size: 16px; line-height: 2; color: var(--c-text); }
.speech-body p { margin: 0 0 22px; text-indent: 2em; }
.signature-block { text-align: right; margin-top: 50px; padding-top: 20px; border-top: 1px solid var(--c-line); }
.signature-block p { text-indent: 0; margin: 4px 0; }
.sign-name { font-size: 18px; font-weight: 700; color: var(--c-primary); }
.sign-title { font-size: 14px; color: var(--c-text-light); }
@media (max-width: 1000px) {
  .speech-header { flex-direction: column; text-align: center; }
}
</style>
