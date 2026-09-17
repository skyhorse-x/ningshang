<template>
  <div>
    <div class="page-header">
      <h3>菜单管理</h3>
      <div>
        <el-button v-if="hasPermission('menus:batch_delete')" type="danger" plain :disabled="selectedIds.length === 0" @click="onBatchDelete">批量删除</el-button>
        <el-button v-if="hasPermission('menus:create')" type="primary" @click="openDialog()">+ 新增菜单</el-button>
      </div>
    </div>

    <el-table :data="treeList" stripe row-key="id" default-expand-all
              @selection-change="selectedIds = $event.map(r => r.id)">
      <el-table-column type="selection" width="42" :selectable="row => row.parentId > 0 && !row.isPermission" />
      <el-table-column label="菜单名称" prop="name" min-width="180">
        <template #default="{ row }">
          <el-icon v-if="row.parentId === 0" style="vertical-align:middle;margin-right:4px"><Folder /></el-icon>
          <el-icon v-else style="vertical-align:middle;margin-right:4px"><Document /></el-icon>
          <strong v-if="row.parentId === 0">{{ row.name }}</strong>
          <span v-else :class="{ 'perm-name': row.isPermission }">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="路径" prop="path" min-width="200" />
      <el-table-column label="图标" prop="icon" width="120">
        <template #default="{ row }">
          <span v-if="row.icon" class="icon-name">{{ row.icon }}</span>
          <span v-else class="muted">—</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sortOrder" width="70" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button v-if="hasPermission('menus:update') && !row.isPermission" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('menus:delete') && !row.isPermission" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑菜单' : '新增菜单'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类型">
          <el-radio-group v-model="isParent" :disabled="!!form.id">
            <el-radio :value="true">一级分组</el-radio>
            <el-radio :value="false">二级菜单</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="!isParent" label="所属分组">
          <el-select v-model="form.parentId" placeholder="请选择上级分组">
            <el-option v-for="g in parentList" :key="g.id" :label="g.name" :value="g.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" placeholder="如 新闻管理" /></el-form-item>
        <el-form-item v-if="!isParent" label="路径"><el-input v-model="form.path" placeholder="如 /ningshang-admin/news" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" placeholder="Element Plus 图标名，如 Document" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Folder, Document } from '@element-plus/icons-vue'
import api from '@/api'
import { hasPermission } from '@/utils/permission'

const list = ref([])
const permissions = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const isParent = ref(false)
const form = ref({})
const selectedIds = ref([])

const parentList = computed(() => list.value.filter(m => m.parentId === 0))
const moduleByPath = {
  '/ningshang-admin/news': 'news',
  '/ningshang-admin/content/intro': 'content',
  '/ningshang-admin/content/speech': 'content',
  '/ningshang-admin/content/culture': 'content',
  '/ningshang-admin/content/party': 'content',
  '/ningshang-admin/content/recruit': 'content',
  '/ningshang-admin/subsidiaries': 'subsidiaries',
  '/ningshang-admin/team': 'team',
  '/ningshang-admin/honors': 'honors',
  '/ningshang-admin/milestones': 'milestones',
  '/ningshang-admin/jobs': 'jobs',
  '/ningshang-admin/messages': 'messages',
  '/ningshang-admin/menus': 'menus',
  '/ningshang-admin/groups': 'groups',
  '/ningshang-admin/admins': 'admins',
  '/ningshang-admin/chatline': 'content',
  '/ningshang-admin/site-settings': 'content'
}
const actionName = {
  list: '列表',
  create: '添加',
  update: '编辑',
  delete: '删除',
  batch_delete: '批量删除'
}

// 构建树形数据用于表格展示
const treeList = computed(() => {
  const tree = []
  const parents = list.value.filter(m => m.parentId === 0).sort((a, b) => a.sortOrder - b.sortOrder)
  for (const p of parents) {
    tree.push(p)
    const children = list.value.filter(m => m.parentId === p.id).sort((a, b) => a.sortOrder - b.sortOrder)
    for (const child of children) {
      tree.push(child)
      tree.push(...permissionRows(child))
    }
  }
  return tree
})

const permissionRows = (menu) => {
  const module = moduleByPath[menu.path]
  if (!module) return []
  return permissions.value
    .filter(item => item.module === module)
    .sort((a, b) => a.sortOrder - b.sortOrder)
    .map(item => ({
      id: `perm-${menu.id}-${item.id}`,
      parentId: menu.id,
      name: `　${actionName[item.action] || item.name}`,
      path: item.code,
      icon: '接口权限',
      sortOrder: item.sortOrder,
      status: item.status,
      isPermission: true
    }))
}

const load = async () => {
  const [menuRes, permissionRes] = await Promise.all([api.adminMenuTree(), api.adminPermissionList()])
  if (menuRes.code === 200) list.value = flattenTree(menuRes.data)
  if (permissionRes.code === 200) permissions.value = permissionRes.data || []
}

// 将后端树形结构扁平化
const flattenTree = (tree) => {
  const result = []
  for (const node of tree) {
    const { children, ...rest } = node
    result.push(rest)
    if (children && children.length) {
      result.push(...flattenTree(children))
    }
  }
  return result
}

onMounted(load)

const emptyForm = () => ({
  id: null, parentId: 0, name: '', path: '', icon: '',
  sortOrder: list.value.length + 1, status: 1
})

const openDialog = (row) => {
  if (row) {
    form.value = { ...row }
    isParent.value = row.parentId === 0
  } else {
    form.value = emptyForm()
    isParent.value = false
  }
  dialogVisible.value = true
}

const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入菜单名称'); return }
  if (!isParent.value && !form.value.parentId) { ElMessage.warning('请选择所属分组'); return }
  if (!isParent.value && !form.value.path) { ElMessage.warning('请输入路由路径'); return }

  // 分组类型强制 parentId=0，清空 path
  if (isParent.value) {
    form.value.parentId = 0
    form.value.path = ''
  }

  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminMenuUpdate(form.value.id, form.value)
      : await api.adminMenuCreate(form.value)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      load()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}

const onDelete = async (row) => {
  const msg = row.parentId === 0 ? `删除分组「${row.name}」会同时删除其下所有菜单，确定吗？` : `确定删除菜单「${row.name}」吗？`
  await ElMessageBox.confirm(msg, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminMenuDelete(row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}

const onBatchDelete = async () => {
  if (selectedIds.value.length === 0) return
  await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 个菜单项吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminMenuBatchDelete(selectedIds.value)
    if (res.code === 200) { ElMessage.success('批量删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.muted { color: #c0c4cc; }
.perm-name { color: #606266; font-size: 13px; }
.icon-name { color: #606266; }
</style>
