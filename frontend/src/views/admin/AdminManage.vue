<template>
  <div>
    <div class="page-header">
      <h3>管理员账号</h3>
      <el-button type="primary" @click="openDialog()">+ 新增管理员</el-button>
    </div>
    <el-alert type="info" :closable="false" show-icon class="role-tip"
      title="权限组说明" description="管理员账号绑定到权限组，权限组决定可访问的菜单。超级管理员组拥有全部权限。" />
    <el-table :data="list" stripe style="margin-top: 16px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" width="160" />
      <el-table-column prop="nickname" label="昵称" width="180" />
      <el-table-column label="权限组" width="140">
        <template #default="{ row }">
          <el-tag :type="GROUP_TAGS[row.groupId] || 'info'" size="small">
            {{ groupMap[row.groupId] || row.role || '—' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="{ row }">{{ (row.createdAt || '').replace('T', ' ').slice(0, 19) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员' : '新增管理员'" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" :disabled="!!form.id" placeholder="登录用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="后台显示名称" />
        </el-form-item>
        <el-form-item label="权限组">
          <el-select v-model="form.groupId" style="width: 100%" placeholder="请选择权限组">
            <el-option v-for="g in groups" :key="g.id" :label="g.name" :value="g.id" />
          </el-select>
        </el-form-item>
        <el-form-item :label="form.id ? '重置密码' : '密码'">
          <el-input v-model="form.password" type="password" show-password
            :placeholder="form.id ? '留空表示不修改密码' : '不少于6位'" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const GROUP_TAGS = { 1: 'danger', 2: 'warning', 3: 'info' }

const list = ref([])
const groups = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

const groupMap = computed(() => {
  const m = {}
  for (const g of groups.value) m[g.id] = g.name
  return m
})

const emptyForm = () => ({ id: null, username: '', nickname: '', groupId: null, password: '' })

const load = async () => {
  try {
    const res = await api.adminListAdmins()
    if (res.code === 200) list.value = res.data
  } catch (e) { /* 无权限时忽略 */ }
}

const loadGroups = async () => {
  try {
    const res = await api.adminGroupList()
    if (res.code === 200) groups.value = res.data
  } catch (e) { /* ignore */ }
}

onMounted(async () => {
  await loadGroups()
  await load()
})

const openDialog = (row) => {
  form.value = row
    ? { id: row.id, username: row.username, nickname: row.nickname, groupId: row.groupId, password: '' }
    : emptyForm()
  dialogVisible.value = true
}

const onSave = async () => {
  const f = form.value
  if (!f.username) { ElMessage.warning('请输入用户名'); return }
  if (!f.groupId) { ElMessage.warning('请选择权限组'); return }
  if (!f.id && (!f.password || f.password.length < 6)) { ElMessage.warning('密码不能少于6位'); return }
  saving.value = true
  try {
    const res = f.id ? await api.adminUpdateAdmin(f.id, f) : await api.adminCreateAdmin(f)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '保存失败')
  } finally { saving.value = false }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除管理员「${row.nickname || row.username}」吗？`, '提示', { type: 'warning' })
  try {
    const res = await api.adminDeleteAdmin(row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '删除失败')
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.role-tip { margin-bottom: 8px; }
</style>
