<template>
  <div>
    <div class="page-header">
      <h3>管理员分组</h3>
      <el-button type="primary" @click="openDialog()">+ 新增分组</el-button>
    </div>

    <el-table :data="groups" stripe>
      <el-table-column prop="name" label="分组名称" min-width="160" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑/权限</el-button>
          <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员分组' : '新增管理员分组'" width="min(860px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" placeholder="如 内容编辑" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" placeholder="分组说明" /></el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
        </el-form-item>
        <el-form-item label="权限列表">
          <el-tree
            ref="permissionTreeRef"
            class="permission-tree"
            :data="permissionTree"
            show-checkbox
            node-key="key"
            :props="{ label: 'label', children: 'children' }"
            default-expand-all
          />
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
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const groups = ref([])
const menus = ref([])
const permissions = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})
const permissionTreeRef = ref(null)

const actionLabel = { list: '列表', create: '添加', update: '修改', delete: '删除', batch_delete: '批量删除' }
const modulesByPath = {
  '/ningshang-admin/news': ['news'],
  '/ningshang-admin/content/intro': ['content'],
  '/ningshang-admin/content/speech': ['content'],
  '/ningshang-admin/content/culture': ['content'],
  '/ningshang-admin/content/party': ['content'],
  '/ningshang-admin/content/recruit': ['content'],
  '/ningshang-admin/subsidiaries': ['subsidiaries', 'core-businesses'],
  '/ningshang-admin/team': ['team'],
  '/ningshang-admin/honors': ['honors'],
  '/ningshang-admin/milestones': ['milestones'],
  '/ningshang-admin/jobs': ['jobs'],
  '/ningshang-admin/messages': ['messages'],
  '/ningshang-admin/menus': ['menus'],
  '/ningshang-admin/groups': ['groups'],
  '/ningshang-admin/admins': ['admins'],
  '/ningshang-admin/chatline': ['content'],
  '/ningshang-admin/site-settings': ['content']
}

const permissionTree = computed(() => menus.value.map(group => ({
  key: `menu:${group.id}`,
  label: group.name,
  children: (group.children || []).map(menu => ({
    key: `menu:${menu.id}`,
    label: menu.name,
    children: permissions.value
      .filter(item => (modulesByPath[menu.path] || []).includes(item.module))
      .map(item => ({ key: `perm:${item.id}`, label: actionLabel[item.action] || item.name }))
  }))
})))

const loadGroups = async () => {
  const res = await api.adminGroupList()
  if (res.code === 200) groups.value = res.data
}

const loadBase = async () => {
  const [menuRes, permissionRes] = await Promise.all([api.adminMenuTree(), api.adminPermissionList()])
  if (menuRes.code === 200) menus.value = menuRes.data || []
  if (permissionRes.code === 200) permissions.value = permissionRes.data || []
}

onMounted(async () => {
  await Promise.all([loadBase(), loadGroups()])
})

const openDialog = async (row) => {
  form.value = row ? { ...row } : { id: null, name: '', description: '', status: 1 }
  dialogVisible.value = true
  let menuIds = []
  let permissionIds = []
  if (row?.id) {
    const [menuRes, permissionRes] = await Promise.all([api.adminGroupMenus(row.id), api.adminGroupPermissions(row.id)])
    menuIds = [...new Set(menuRes.data || [])]
    permissionIds = [...new Set(permissionRes.data || [])]
  }
  await nextTick()
  permissionTreeRef.value?.setCheckedKeys([
    ...menuIds.map(id => `menu:${id}`),
    ...permissionIds.map(id => `perm:${id}`)
  ])
}

const splitChecked = () => {
  const checked = permissionTreeRef.value?.getCheckedKeys() || []
  const halfChecked = permissionTreeRef.value?.getHalfCheckedKeys() || []
  const toId = key => Number(String(key).slice(5))
  return {
    menuIds: [...new Set([...checked, ...halfChecked].filter(key => String(key).startsWith('menu:')).map(toId))],
    permissionIds: [...new Set(checked.filter(key => String(key).startsWith('perm:')).map(toId))]
  }
}

const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入分组名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminGroupUpdate(form.value.id, form.value)
      : await api.adminGroupCreate(form.value)
    if (res.code === 200) {
      const { menuIds, permissionIds } = splitChecked()
      await Promise.all([api.adminGroupSetMenus(res.data.id, menuIds), api.adminGroupSetPermissions(res.data.id, permissionIds)])
      ElMessage.success('保存成功')
      dialogVisible.value = false
      await loadGroups()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '保存失败')
  } finally {
    saving.value = false
  }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除分组「${row.name}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminGroupDelete(row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); loadGroups() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error(e?.response?.data?.message || '删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.permission-tree { width: 100%; max-height: 420px; overflow: auto; border: 1px solid #ebeef5; border-radius: 6px; padding: 10px 0; }
</style>

