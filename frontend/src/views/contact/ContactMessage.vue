<template>
  <div>
    <PageBanner image="/images/contact-banner.jpeg" title="在线留言" />
    <SubNav title="联系宁商">
      <router-link to="/recruit">人才理念</router-link>
      <router-link to="/recruit/jobs">招聘岗位</router-link>
      <router-link to="/contact">联系方式</router-link>
      <router-link to="/contact/message" class="on">在线留言</router-link>
    </SubNav>
    <section class="section"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>在线留言</h1></div>
        <div class="detail-body">
          <p>欢迎留下您的需求与建议，我们将尽快与您联系</p>
          <MessageForm />
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import MessageForm from '@/components/business/MessageForm.vue'
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.detail-body { font-size: 15px; color: var(--c-text); }
.detail-body p { margin: 0 0 24px; color: var(--c-text-light); }
</style>
