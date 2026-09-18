<template>
  <div>
    <div class="page-header">
      <h3>合作伙伴</h3>
      <div>
        <el-button v-if="hasPermission('partners:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button>
        <el-button v-if="hasPermission('partners:create')" type="primary" @click="openDialog()">+ 新增伙伴</el-button>
      </div>
    </div>

    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="名称"><el-input v-model="searchForm.name" placeholder="搜索伙伴名称" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
          <el-option label="启用" :value="1" />
          <el-option label="关闭" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange">
      <el-table-column v-if="hasPermission('partners:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="Logo" width="110">
        <template #default="{ row }"><img v-if="row.logo" class="partner-logo" :src="row.logo" :alt="row.name"><span v-else>-</span></template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="160" />
      <el-table-column prop="link" label="链接" min-width="220" show-overflow-tooltip />
      <el-table-column label="状态" width="100"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '关闭' }}</el-tag></template></el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="90" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('partners:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('partners:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑伙伴' : '新增伙伴'" width="min(720px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="名称"><el-input v-model="form.name" maxlength="100" /></el-form-item>
        <el-form-item label="Logo"><ImageUpload v-model="form.logo" /></el-form-item>
        <el-form-item label="链接"><el-input v-model="form.link" placeholder="例如：https://example.com，可留空" maxlength="300" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="关闭" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const list = ref([])
const searchForm = ref({ name: '', status: null })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})
const emptyForm = () => ({ id: null, name: '', logo: '', link: '', status: 1, sortOrder: list.value.length + 1 })

const filteredList = computed(() => list.value.filter(item => {
  const matchName = !searchForm.value.name || item.name?.includes(searchForm.value.name)
  const matchStatus = searchForm.value.status === null || searchForm.value.status === undefined || searchForm.value.status === '' || item.status === searchForm.value.status
  return matchName && matchStatus
}))

const load = async () => {
  const res = await api.adminList('partners')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('partners', load)
onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { name: '', status: null } }
const openDialog = row => { form.value = row ? { ...row } : emptyForm(); dialogVisible.value = true }
const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入伙伴名称'); return }
  saving.value = true
  try {
    const res = form.value.id ? await api.adminUpdate('partners', form.value.id, form.value) : await api.adminCreate('partners', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}
const onDelete = async row => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('partners', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
.partner-logo { width: 72px; height: 38px; object-fit: contain; background: #f7f9fc; border-radius: 4px; padding: 4px; }
</style>
