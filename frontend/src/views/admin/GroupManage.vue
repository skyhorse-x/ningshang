<template>
  <div class="group-manage">
    <!-- 左侧：管理员分组 -->
    <div class="group-list">
      <div class="panel-header">
        <h3>管理员分组</h3>
        <el-button type="primary" size="small" @click="openDialog()">+ 新增</el-button>
      </div>
      <el-table :data="groups" stripe highlight-current-row @current-change="onSelectGroup">
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="状态" width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="openDialog(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="onDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 右侧：权限配置 -->
    <div class="group-perm">
      <div class="panel-header">
        <h3>权限配置 — {{ selectedGroup ? selectedGroup.name : '请选择分组' }}</h3>
        <el-button type="primary" size="small" :disabled="!selectedGroup" @click="onSavePermissions">保存权限</el-button>
      </div>
      <div v-if="selectedGroup" class="perm-tree">
        <h4>菜单访问权限</h4>
        <el-tree ref="menuTreeRef" :data="menuTree" show-checkbox node-key="id"
                 :props="{ label: 'name', children: 'children' }"
                 :default-checked-keys="checkedMenuIds" default-expand-all />
        <h4>页面操作权限</h4>
        <el-table :data="permissionModules" border size="small">
          <el-table-column prop="name" label="功能模块" width="130" />
          <el-table-column label="允许的操作">
            <template #default="{ row }">
              <el-checkbox-group v-model="checkedPermissionIds">
                <el-checkbox v-for="item in row.items" :key="item.id" :value="item.id">{{ item.name }}</el-checkbox>
              </el-checkbox-group>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-empty v-else description="请在左侧选择一个分组" />
    </div>

  </div>

  <!-- 新增/编辑分组对话框 -->
  <el-dialog v-model="dialogVisible" :title="form.id ? '编辑管理员分组' : '新增管理员分组'" width="460px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="名称"><el-input v-model="form.name" placeholder="如 内容编辑" /></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" placeholder="分组说明" /></el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const groups = ref([])
const menuTree = ref([])
const checkedMenuIds = ref([])
const selectedGroup = ref(null)
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})
const menuTreeRef = ref(null)
const permissions = ref([])
const checkedPermissionIds = ref([])
const permissionModules = computed(() => {
  const map = new Map()
  permissions.value.forEach(p => { if (!map.has(p.module)) map.set(p.module, { name: p.moduleName, items: [] }); map.get(p.module).items.push(p) })
  return [...map.values()]
})

const loadGroups = async () => {
  const res = await api.adminGroupList()
  if (res.code === 200) groups.value = res.data
}

const loadMenuTree = async () => {
  const res = await api.adminMenuTree()
  if (res.code === 200) menuTree.value = res.data
}

onMounted(async () => {
  await loadMenuTree()
  permissions.value = (await api.adminPermissionList()).data || []
  await loadGroups()
})

const onSelectGroup = async (row) => {
  if (!row) return
  selectedGroup.value = row
  const [res, permissionRes] = await Promise.all([api.adminGroupMenus(row.id), api.adminGroupPermissions(row.id)])
  if (res.code === 200) {
    // 只勾选叶子节点（子菜单），分组节点由父级自动关联
    checkedMenuIds.value = res.data
    await nextTick()
    menuTreeRef.value?.setCheckedKeys(res.data)
  }
  checkedPermissionIds.value = permissionRes.data || []
}

const openDialog = (row) => {
  form.value = row ? { ...row } : { id: null, name: '', description: '', status: 1 }
  dialogVisible.value = true
}

const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入分组名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminGroupUpdate(form.value.id, form.value)
      : await api.adminGroupCreate(form.value)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      loadGroups()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除分组「${row.name}」吗？`, '提示', { type: 'warning' })
  try {
    const res = await api.adminGroupDelete(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      if (selectedGroup.value?.id === row.id) selectedGroup.value = null
      loadGroups()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) { ElMessage.error(e?.response?.data?.message || '删除失败') }
}

const onSavePermissions = async () => {
  if (!selectedGroup.value) return
  // 获取所有勾选的节点（包括半选的父节点）
  const checked = menuTreeRef.value?.getCheckedKeys() || []
  const halfChecked = menuTreeRef.value?.getHalfCheckedKeys() || []
  const allIds = [...checked, ...halfChecked]
  try {
    const [res] = await Promise.all([
      api.adminGroupSetMenus(selectedGroup.value.id, allIds),
      api.adminGroupSetPermissions(selectedGroup.value.id, checkedPermissionIds.value)
    ])
    if (res.code === 200) ElMessage.success('菜单与操作权限保存成功')
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') }
}
</script>

<style scoped>
.group-manage { display: flex; gap: 20px; height: calc(100vh - 160px); }
.group-list { width: 380px; flex-shrink: 0; background: #fff; border-radius: 8px; padding: 16px; display: flex; flex-direction: column; }
.group-perm { flex: 1; background: #fff; border-radius: 8px; padding: 16px; display: flex; flex-direction: column; }
.panel-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.panel-header h3 { font-size: 16px; color: #0d3a72; margin: 0; }
.perm-tree { flex: 1; overflow-y: auto; }
</style>
