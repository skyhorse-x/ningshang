<template>
  <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
    <el-row :gutter="18">
      <el-col :span="12">
        <el-form-item label="您的姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="18">
      <el-col :span="12">
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="留言类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
            <el-option label="合作咨询" value="cooperation" />
            <el-option label="招聘求职" value="recruit" />
            <el-option label="业务洽谈" value="business" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="留言内容" prop="content">
      <el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入留言内容" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit" :loading="submitting">提交留言</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import api from '@/api'
const formRef = ref(null)
const submitting = ref(false)
const form = reactive({ name: '', phone: '', email: '', type: '', content: '' })
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
  content: [{ required: true, message: '请输入留言内容', trigger: 'blur' }]
}
const onSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      await api.submitMessage(form)
      ElMessage.success('感谢您的留言，我们已收到信息，将尽快与您联系！')
      formRef.value.resetFields()
    } catch (e) {
      ElMessage.error('提交失败，请稍后重试')
    } finally {
      submitting.value = false
    }
  })
}
</script>
