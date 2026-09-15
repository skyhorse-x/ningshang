<template>
  <div>
    <div class="page-header"><h3>招聘岗位</h3><div><el-button v-if="hasPermission('jobs:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button><el-button v-if="hasPermission('jobs:create')" type="primary" @click="openDialog()">+ 新建岗位</el-button></div></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="岗位"><el-input v-model="searchForm.title" placeholder="搜索岗位名称" clearable /></el-form-item>
      <el-form-item label="部门"><el-input v-model="searchForm.department" placeholder="搜索部门" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange"><el-table-column v-if="hasPermission('jobs:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="岗位名称" />
      <el-table-column prop="department" label="部门" width="100" />
      <el-table-column prop="headcount" label="人数" width="80" />
      <el-table-column prop="location" label="地点" width="80" />
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('jobs:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('jobs:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑岗位' : '新建岗位'" width="min(960px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="岗位名称"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="部门"><el-input v-model="form.department" /></el-form-item>
        <el-form-item label="招聘人数"><el-input v-model="form.headcount" /></el-form-item>
        <el-form-item label="工作地点"><el-input v-model="form.location" /></el-form-item>
        <el-form-item label="学历要求"><el-input v-model="form.education" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="描述"><RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.description" height="380px" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import RichEditor from '@/components/admin/RichEditor.vue'
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const list = ref([])
const searchForm = ref({ title: '', department: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

const emptyForm = () => ({ id: null, title: '', department: '', headcount: '', location: '合肥', education: '', description: '', sortOrder: list.value.length + 1 })

const filteredList = computed(() => {
  let result = list.value
  if (searchForm.value.title) result = result.filter(item => item.title && item.title.includes(searchForm.value.title))
  if (searchForm.value.department) result = result.filter(item => item.department && item.department.includes(searchForm.value.department))
  return result
})

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  const res = await api.adminList('jobs')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('jobs', load)
onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { title: '', department: '' } }

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}
const onSave = async () => {
  if (!form.value.title) { ElMessage.warning('请输入岗位名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('jobs', form.value.id, form.value)
      : await api.adminCreate('jobs', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}
const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该岗位吗？', '提示', { type: 'warning' })
  try {
    const res = await api.adminDelete('jobs', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
</style>
