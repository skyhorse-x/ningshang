<template>
  <div class="footer">
    <div class="wrap">
      <div class="grid">
        <div class="fbrand">
          <img :src="c('site_logo', '/images/logo.png')" alt="安徽宁商科技集团" width="72" height="54">
          <RichContent :content="c('footer_brand_desc', '安徽宁商科技集团有限公司，立足安徽本土，聚焦科创产业服务，秉持&quot;务实笃行、创新赋能、诚信致远&quot;的核心价值观。')" />
        </div>
        <div>
          <h6>集团概况</h6>
          <ul class="flink">
            <li><router-link to="/about/intro">集团简介</router-link></li>
            <li><router-link to="/about/speech">董事长致词</router-link></li>
            <li><router-link to="/about/events">发展大事记</router-link></li>
            <li><router-link to="/about/team">管理团队</router-link></li>
            <li><router-link to="/about/honor">企业荣誉</router-link></li>
            <li><router-link to="/about/party">党建工作</router-link></li>
            <li><router-link to="/about/culture">企业文化</router-link></li>
          </ul>
        </div>
        <div>
          <h6>快捷导航</h6>
          <ul class="flink">
            <li><router-link to="/news">新闻中心</router-link></li>
            <li><router-link to="/industry">集团产业</router-link></li>
            <li><router-link to="/recruit">人才理念</router-link></li>
            <li><router-link to="/contact">联系方式</router-link></li>
            <li><router-link to="/contact/message">在线留言</router-link></li>
          </ul>
        </div>
        <div class="fcontact">
          <h6>联系方式</h6>
          <ul>
            <li><LocalTwo theme="outline" :size="14" class="footer-icon" /> {{ c('contact_address', '安徽省合肥市蜀山区24号创新工场') }}</li>
            <li><Phone theme="outline" :size="14" class="footer-icon" /> {{ c('contact_phone', '13365745652') }}</li>
            <li><Mail theme="outline" :size="14" class="footer-icon" /> {{ c('contact_email', 'NStechnology@163.com') }}</li>
          </ul>
        </div>
      </div>
    </div>
    <div class="copy">Copyright © 2026 安徽宁商科技集团有限公司 {{ c('icp_number', '皖ICP备2026XXXXXX号-1') }}　技术支持：宁商科技</div>
  </div>
</template>

<script setup>
import RichContent from '@/components/business/RichContent.vue'
import { ref, onMounted } from 'vue'
import { LocalTwo, Phone, Mail } from '@icon-park/vue-next'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.footer {
  background: var(--c-primary-dark);
  color: #a9bdd6;
  padding: 60px 0 0;
}
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.grid {
  display: grid;
  grid-template-columns: 1.4fr 1fr 1fr 1fr;
  gap: 40px;
  padding-bottom: 40px;
  border-bottom: 1px solid rgba(255,255,255,.08);
}
.fbrand img { height: 54px; margin-bottom: 16px; }
.fbrand p { font-size: 13px; line-height: 1.9; color: #8da4c4; }
.footer h6 {
  color: #fff;
  font-size: 16px;
  margin-bottom: 18px;
  position: relative;
  padding-bottom: 10px;
}
.footer h6::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 24px;
  height: 2px;
  background: var(--c-accent);
}
.flink { list-style: none; padding: 0; margin: 0; }
.flink li { margin-bottom: 10px; font-size: 13px; }
.flink li a { color: #a9bdd6; text-decoration: none; }
.flink li a:hover { color: var(--c-accent); }
.fcontact ul { list-style: none; padding: 0; margin: 0; }
.fcontact li { font-size: 13px; margin-bottom: 10px; }
.footer-icon { vertical-align: middle; margin-right: 4px; }
.copy {
  text-align: center;
  padding: 22px 0;
  font-size: 13px;
  color: #6f88ad;
}
@media (max-width: 1000px) {
  .grid { grid-template-columns: 1fr; }
}
</style>
