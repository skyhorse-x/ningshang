<template>
  <div class="admin-login">
    <div class="login-box">
      <div class="login-header">
        <img src="/images/logo.png" alt="宁商科技集团" class="login-logo">
        <h2>管理后台</h2>
        <p>NINGSHANG ADMIN</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" size="large">
            <template #prefix><User theme="outline" :size="18" fill="#909399" /></template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password>
            <template #prefix><Lock theme="outline" :size="18" fill="#909399" /></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="onLogin">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@icon-park/vue-next'
import api from '@/api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const onLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await api.adminLogin(form)
      if (res.code === 200) {
        localStorage.setItem('admin_token', res.data.token)
        const permissionRes = await api.adminPermissionMine()
        localStorage.setItem('admin_user', JSON.stringify({ username: res.data.username, nickname: res.data.nickname, role: res.data.role, groupId: res.data.groupId, permissions: permissionRes.data || [] }))
        ElMessage.success('登录成功')
        router.push('/ningshang-admin/dashboard')
      } else {
        ElMessage.error(res.message || '登录失败')
      }
    } catch (e) {
      // 后端登录失败返回真实 HTTP 401，错误信息在 error.response.data 中
      ElMessage.error(e?.response?.data?.message || '登录失败，请检查用户名和密码')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.admin-login {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0d3a72 0%, #0a2c57 100%);
}
.login-box {
  width: 420px;
  padding: 48px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0,0,0,.3);
}
.login-header { text-align: center; margin-bottom: 36px; }
.login-logo { height: 56px; margin: 0 auto 16px; display: block; }
.login-header h2 { font-size: 24px; color: #0d3a72; font-weight: 700; }
.login-header p { font-size: 12px; color: #6b6b6b; letter-spacing: 3px; margin-top: 4px; }
.login-btn { width: 100%; height: 44px; font-size: 16px; letter-spacing: 4px; }
.login-form :deep(.el-input__prefix) { display: flex; align-items: center; }
</style>
