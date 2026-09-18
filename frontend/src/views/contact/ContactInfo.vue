<template>
  <div>
    <PageBanner :image="bg('bg_contact_banner', '/images/contact-banner.jpeg')" title="联系方式" />
    <SubNav title="联系宁商">
      <router-link to="/recruit">人才理念</router-link>
      <router-link to="/recruit/jobs">招聘岗位</router-link>
      <router-link to="/contact" class="on">联系方式</router-link>
      <router-link to="/contact/message">在线留言</router-link>
    </SubNav>
    <section class="section text-bg-contact" :style="sectionBg('bg_contact_page', '/images/b.jpg')"><div class="wrap">
      <div class="content-detail">
        <div class="detail-head"><h1>联系方式</h1></div>
        <div class="detail-body">
          <ContactForm />
          <h3>在线留言</h3>
          <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
            <el-row :gutter="18">
              <el-col :span="12"><el-form-item label="您的姓名" prop="name"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" placeholder="请输入电话" /></el-form-item></el-col>
            </el-row>
            <el-row :gutter="18">
              <el-col :span="12"><el-form-item label="电子邮箱"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item></el-col>
              <el-col :span="12"><el-form-item label="咨询类别"><el-select v-model="form.type" placeholder="请选择" style="width:100%"><el-option label="业务合作" value="cooperation" /><el-option label="人才应聘" value="recruit" /><el-option label="媒体联系" value="media" /><el-option label="其他咨询" value="other" /></el-select></el-form-item></el-col>
            </el-row>
            <el-form-item label="留言内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="5" placeholder="请简要描述您的需求" /></el-form-item>
            <el-form-item><el-button type="primary" @click="onSubmit">提交留言</el-button></el-form-item>
          </el-form>
        </div>
      </div>
    </div></section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import PageBanner from '@/components/layout/PageBanner.vue'
import SubNav from '@/components/layout/SubNav.vue'
import ContactForm from '@/components/business/ContactForm.vue'
import api from '@/api'
import { loadContent, pick } from '@/utils/content'
const content = ref({})
const bg = (key, fallback) => pick(content.value, key, fallback)
const sectionBg = (key, fallback) => ({ backgroundImage: `url(${bg(key, fallback)})` })
const formRef = ref(null)
const form = reactive({ name: '', phone: '', email: '', type: '', content: '' })
onMounted(async () => { content.value = await loadContent() })
const rules = { name: [{ required: true, message: '请输入姓名', trigger: 'blur' }], phone: [{ required: true, message: '请输入电话', trigger: 'blur' }], content: [{ required: true, message: '请输入留言内容', trigger: 'blur' }] }
const onSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      await api.submitMessage(form)
      ElMessage.success('感谢您的留言，我们已收到信息，将尽快与您联系！')
      formRef.value.resetFields()
    } catch (e) {
      ElMessage.error('提交失败，请稍后重试')
    }
  })
}
</script>

<style scoped>
.section { padding: 80px 0; }
.wrap { width: 1200px; max-width: 94%; margin: 0 auto; }
.text-bg-contact { background-image: url(/images/b.jpg); background-size: cover; background-position: center top; background-attachment: fixed; }
.content-detail { max-width: 900px; margin: 0 auto; }
.detail-head { margin-bottom: 32px; padding-bottom: 24px; border-bottom: 1px solid var(--c-line); }
.detail-head h1 { font-size: 32px; color: var(--c-primary); font-weight: 700; }
.detail-body { font-size: 15px; color: var(--c-text); }
.detail-body h3 { font-size: 22px; color: var(--c-primary); margin: 32px 0 16px; font-weight: 600; }
</style>
