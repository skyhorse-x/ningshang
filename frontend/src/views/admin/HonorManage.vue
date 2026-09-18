<template>
  <div>
    <div class="page-header"><h3>企业荣誉</h3><div><el-button v-if="hasPermission('honors:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button><el-button v-if="hasPermission('honors:create')" type="primary" @click="openDialog()">+ 新增荣誉</el-button></div></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="名称"><el-input v-model="searchForm.title" placeholder="搜索荣誉名称" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange"><el-table-column v-if="hasPermission('honors:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="icon" label="图标" width="70" />
      <el-table-column prop="title" label="荣誉名称" />
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('honors:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('honors:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑荣誉' : '新增荣誉'" width="min(960px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="荣誉名称"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图标"><el-input v-model="form.icon" placeholder="可填 emoji 或图标名，如 🏅 / medal" /></el-form-item>
        <el-form-item label="图片"><ImageUpload v-model="form.image" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="说明"><RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.description" height="380px" /></el-form-item>
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
import ImageUpload from '@/components/admin/ImageUpload.vue'

const list = ref([])
const searchForm = ref({ title: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

const emptyForm = () => ({ id: null, title: '', description: '', icon: '🏅', image: '', sortOrder: list.value.length + 1 })

const filteredList = computed(() => {
  let result = list.value
  if (searchForm.value.title) result = result.filter(item => item.title && item.title.includes(searchForm.value.title))
  return result
})

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  const res = await api.adminList('honors')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('honors', load)
onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { title: '' } }

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}
const onSave = async () => {
  if (!form.value.title) { ElMessage.warning('请输入荣誉名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('honors', form.value.id, form.value)
      : await api.adminCreate('honors', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}
const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.title}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('honors', row.id)
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
