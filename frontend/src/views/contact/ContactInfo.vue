<template>
  <div>
    <PageBanner :image="bg('bg_contact_banner', '/images/contact-banner.jpeg')" title="联系方式" />
    <SubNav title="联系宁商">
      <router-link to="/recruit">人才理念</router-link>
      <router-link to="/recruit/jobs">招聘岗位</router-link>
      <router-link to="/contact" class="on">联系方式</router-link>
    </SubNav>
    <section class="section text-bg-contact" :style="sectionBg('bg_contact_page', '/images/b.jpg')"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>联系方式</h1></div>
        <div class="detail-body">
          <ContactForm />
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import ContactForm from '@/components/business/ContactForm.vue'
import { loadContent, pick } from '@/utils/content'
const content = ref({})
const bg = (key, fallback) => pick(content.value, key, fallback)
const sectionBg = (key, fallback) => ({ backgroundImage: `url(${bg(key, fallback)})` })
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-contact { background-image: url(/images/b.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.detail-body { font-size: 15px; color: var(--c-text); }
</style>
