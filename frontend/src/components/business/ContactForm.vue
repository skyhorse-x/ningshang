<template>
  <div class="contact-info">
    <div class="ci-item">
      <div class="ico"><LocalTwo theme="outline" :size="24" fill="#fff" /></div>
      <div><h5>公司地址</h5><p>{{ c('contact_address', '安徽省合肥市蜀山区24号创新工场') }}</p></div>
    </div>
    <div class="ci-item">
      <div class="ico"><Phone theme="outline" :size="24" fill="#fff" /></div>
      <div><h5>服务热线</h5><p>{{ c('contact_phone', '13365745652') }}</p></div>
    </div>
    <div class="ci-item">
      <div class="ico"><Mail theme="outline" :size="24" fill="#fff" /></div>
      <div><h5>电子邮箱</h5><p>{{ c('contact_email', 'NStechnology@163.com') }}</p></div>
    </div>
    <div class="ci-item">
      <div class="ico"><Time theme="outline" :size="24" fill="#fff" /></div>
      <div><h5>办公时间</h5><p>{{ c('office_hours', '周一至周五　09:00 - 18:00') }}</p></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { LocalTwo, Phone, Mail, Time } from '@icon-park/vue-next'
import { loadContent, pick } from '@/utils/content'
const content = ref({})
const c = (key, fallback) => pick(content.value, key, fallback)
onMounted(async () => { content.value = await loadContent() })
</script>

<style scoped>
.contact-info { margin-bottom: 30px; }
.ci-item { display: flex; gap: 18px; padding: 22px 0; border-bottom: 1px dashed var(--c-line); }
.ico { flex-shrink: 0; width: 52px; height: 52px; border-radius: 8px; background: var(--c-primary); color: #fff; display: flex; align-items: center; justify-content: center; font-size: 22px; }
.ci-item h5 { font-size: 16px; color: var(--c-primary); margin-bottom: 4px; }
.ci-item p { font-size: 15px; color: var(--c-text); }
</style>
