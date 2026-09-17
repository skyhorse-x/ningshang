<template>
  <div class="topbar">
    <div class="wrap">
      <div class="tl">
        <span><Phone theme="outline" :size="14" fill="#c8a45c" class="topbar-icon" />服务热线：{{ c('contact_phone', '13365745652') }}</span>
        <span><Mail theme="outline" :size="14" fill="#c7d6ec" class="topbar-icon" />{{ c('contact_email', 'NStechnology@163.com') }}</span>
      </div>
      <div class="tr">
        <router-link to="/recruit/jobs">人才招聘</router-link>
        <router-link to="/contact">联系宁商</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Phone, Mail } from '@icon-park/vue-next'
import { loadContent, pick } from '@/utils/content'

const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.topbar {
  background: var(--c-primary-dark);
  color: #c7d6ec;
  font-size: 13px;
  height: 38px;
  line-height: 38px;
}
.wrap {
  width: 1200px;
  max-width: 94%;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.tl span {
  margin-right: 22px;
}
.topbar-icon { vertical-align: middle; margin-right: 4px; }
.tr a {
  margin-left: 18px;
  color: #c7d6ec;
  text-decoration: none;
}
.tr a:hover {
  color: #fff;
}
@media (max-width: 1000px) {
  .topbar { display: none; }
}
</style>
