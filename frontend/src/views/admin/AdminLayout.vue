<template>
  <div class="admin-layout">
    <aside class="admin-sidebar" :class="{ collapsed: isCollapsed }">
      <div class="admin-logo">
        <img src="/images/logo.png" alt="宁商">
        <span v-show="!isCollapsed">管理后台</span>
      </div>
      <el-menu :default-active="activeMenu" router class="admin-menu" :collapse="isCollapsed" :default-openeds="defaultOpeneds">
        <!-- 首页 -->
        <el-menu-item index="/ningshang-admin/dashboard">
          <i class="fas fa-home menu-icon"></i><span>控制台</span>
        </el-menu-item>
        <!-- 动态菜单 -->
        <template v-for="group in myMenus" :key="group.id">
          <el-sub-menu v-if="group.children && group.children.length" :index="'menu-' + group.id">
            <template #title>
              <component :is="group.icon || 'Folder'" class="menu-icon ep-icon" />
              <span>{{ group.name }}</span>
            </template>
            <el-menu-item v-for="item in group.children" :key="item.id" :index="item.path">
              <component :is="item.icon || 'Document'" class="menu-icon ep-icon" />
              <span>{{ item.name }}</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item v-else :index="group.path">
            <component :is="group.icon || 'Document'" class="menu-icon ep-icon" />
            <span>{{ group.name }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </aside>
    <div class="admin-main">
      <header class="admin-header">
        <div class="header-left">
          <el-tooltip :content="isCollapsed ? '展开导航' : '收起导航'" placement="bottom">
            <button class="collapse-btn" type="button" @click="toggleSidebar" :aria-label="isCollapsed ? '展开导航' : '收起导航'">
              <el-icon><Expand v-if="isCollapsed" /><Fold v-else /></el-icon>
            </button>
          </el-tooltip>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/ningshang-admin/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ route.meta?.title || '' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <el-dropdown @command="handleCommand">
          <span class="admin-user">
            <i class="fas fa-user user-icon"></i> {{ adminName }}
            <el-tag size="small" :type="roleTagType" class="role-tag">{{ roleLabel }}</el-tag>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </header>
      <main class="admin-content">
        <router-view />
      </main>
    </div>
    <el-dialog v-model="pwdDialogVisible" title="修改密码" width="420px">
      <el-form :model="pwdForm" label-width="90px">
        <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
        <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="不少于6位" /></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="pwdForm.confirmPassword" type="password" show-password /></el-form-item>
      </el-form>
      <template #footer><el-button @click="pwdDialogVisible = false">取消</el-button><el-button type="primary" @click="onChangePassword">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Expand, Fold } from '@element-plus/icons-vue'
import api from '@/api'

const router = useRouter()
const route = useRoute()

const adminUser = (() => {
  try { return JSON.parse(localStorage.getItem('admin_user') || '{}') } catch (e) { return {} }
})()

const adminName = computed(() => adminUser.nickname || adminUser.username || '管理员')

// 根据 groupId 显示角色名
const ROLE_LABELS = { 1: '超级管理员', 2: '管理员', 3: '编辑' }
const ROLE_TAGS = { 1: 'danger', 2: 'warning', 3: 'info' }
const roleLabel = computed(() => ROLE_LABELS[adminUser.groupId] || adminUser.role || '管理员')
const roleTagType = computed(() => ROLE_TAGS[adminUser.groupId] || 'info')

// 动态菜单
const myMenus = ref([])
const activeMenu = computed(() => route.path)
const defaultOpeneds = ref([])
const isCollapsed = ref(localStorage.getItem('admin_sidebar_collapsed') === '1')
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
  localStorage.setItem('admin_sidebar_collapsed', isCollapsed.value ? '1' : '0')
}

onMounted(async () => {
  try {
    const res = await api.adminMenuMine()
    if (res.code === 200) {
      myMenus.value = res.data || []
      // 展开所有分组
      defaultOpeneds.value = myMenus.value
        .filter(g => g.children && g.children.length)
        .map(g => 'menu-' + g.id)
    }
  } catch (e) {
    console.error('Failed to load menu:', e)
  }
})

const pwdDialogVisible = ref(false)
const pwdForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })

const handleCommand = async (cmd) => {
  if (cmd === "logout") {
    await ElMessageBox.confirm("确定要退出登录吗？", "提示", { type: "warning" })
    try { await api.adminLogout() } catch (e) {
      if (e.response?.status !== 401) { ElMessage.error('退出失败，请重试'); return }
    }
    localStorage.removeItem("admin_token")
    localStorage.removeItem("admin_user")
    ElMessage.success("已退出登录")
    router.push("/ningshang-admin/login")
  } else if (cmd === "password") {
    pwdForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
    pwdDialogVisible.value = true
  }
}

const onChangePassword = async () => {
  const f = pwdForm.value
  if (!f.oldPassword || !f.newPassword) { ElMessage.warning('请填写完整'); return }
  if (f.newPassword.length < 6) { ElMessage.warning('新密码不能少于6位'); return }
  if (f.newPassword !== f.confirmPassword) { ElMessage.warning('两次输入的新密码不一致'); return }
  try {
    const res = await api.adminChangePassword({ oldPassword: f.oldPassword, newPassword: f.newPassword })
    if (res.code === 200) { ElMessage.success('密码修改成功'); pwdDialogVisible.value = false }
    else ElMessage.error(res.message || '修改失败')
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '修改失败')
  }
}
</script>

<style scoped>
.admin-layout { display: flex; min-height: 100vh; }
.admin-sidebar { width: 232px; background: #0d3a72; color: #fff; flex-shrink: 0; overflow-x: hidden; overflow-y: auto; transition: width .2s ease; }
.admin-sidebar.collapsed { width: 64px; }
.admin-logo { display: flex; align-items: center; gap: 10px; padding: 20px; border-bottom: 1px solid rgba(255,255,255,.1); }
.admin-sidebar.collapsed .admin-logo { justify-content: center; padding: 20px 0; }
.admin-logo img { height: 36px; }
.admin-logo span { font-size: 16px; font-weight: 600; }
.admin-menu { border-right: none; background: transparent; width: 100%; }
.admin-menu:not(.el-menu--collapse) { width: 232px; }
.admin-menu :deep(.el-menu-item), .admin-menu :deep(.el-sub-menu__title) { height: 48px; line-height: 48px; color: rgba(255,255,255,.76); }
.admin-menu :deep(.el-menu-item:hover), .admin-menu :deep(.el-menu-item.is-active) { color: #fff; background: rgba(255,255,255,.1); }
.admin-menu :deep(.el-sub-menu__title:hover), .admin-menu :deep(.el-sub-menu.is-opened > .el-sub-menu__title) { color: #fff; background: rgba(255,255,255,.06); }
.admin-menu :deep(.el-sub-menu .el-menu) { background: rgba(0,0,0,.18); }
/* IconPark 图标对齐 */
.admin-menu svg { width: 16px; height: 16px; flex: 0 0 16px; vertical-align: middle; margin-right: 8px; }
.user-icon { vertical-align: middle; margin-right: 4px; }
.role-tag { margin-left: 8px; }
.admin-main { flex: 1; display: flex; flex-direction: column; background: #f5f7fa; }
.admin-header { display: flex; justify-content: space-between; align-items: center; padding: 0 24px; height: 56px; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.06); }
.header-left { display: flex; align-items: center; gap: 14px; min-width: 0; }
.collapse-btn { width: 34px; height: 34px; border: 0; border-radius: 6px; background: transparent; color: #4f5f72; cursor: pointer; display: inline-flex; align-items: center; justify-content: center; transition: background .18s ease, color .18s ease; }
.collapse-btn:hover { background: #eef3f8; color: #0d3a72; }
.collapse-btn :deep(.el-icon) { font-size: 20px; }
.admin-user { display: flex; align-items: center; gap: 6px; cursor: pointer; color: #6b6b6b; }
.admin-content { flex: 1; padding: 20px 24px 28px; overflow-y: auto; min-width: 0; }
.admin-content > :deep(*) { max-width: 1600px; margin-left: auto; margin-right: auto; }
.admin-content :deep(.page-header) { min-height: 36px; margin-bottom: 16px; }
.admin-content :deep(.page-header h3) { line-height: 1.4; }
.admin-content :deep(.search-form) { margin-bottom: 16px; border-radius: 8px; }
.admin-content :deep(.el-table) { border-radius: 8px; overflow: hidden; }
@media (max-width: 900px) {
  .admin-sidebar { width: 200px; }
  .admin-sidebar.collapsed { width: 64px; }
  .admin-menu:not(.el-menu--collapse) { width: 200px; }
  .admin-content { padding: 16px; }
}
</style>
