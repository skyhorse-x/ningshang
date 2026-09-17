<template>
  <div>
    <div class="page-header">
      <h3>核心业务领域</h3>
      <div>
        <el-button v-if="hasPermission('core-businesses:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button>
        <el-button v-if="hasPermission('core-businesses:create')" type="primary" @click="openDialog()">+ 新增业务</el-button>
      </div>
    </div>

    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="名称"><el-input v-model="searchForm.name" placeholder="搜索业务名称" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange">
      <el-table-column v-if="hasPermission('core-businesses:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="封面图片" width="120">
        <template #default="{ row }">
          <el-image v-if="row.coverImage" class="cover" :src="row.coverImage" fit="cover" />
          <span v-else class="muted">未设置</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="160" />
      <el-table-column label="介绍" min-width="260" show-overflow-tooltip>
        <template #default="{ row }">{{ richTextPreview(row.description) }}</template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('core-businesses:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('core-businesses:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑核心业务' : '新增核心业务'" width="min(960px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="名称"><el-input v-model="form.name" placeholder="如 建设工程" /></el-form-item>
        <el-form-item label="封面图片"><ImageUpload v-model="form.coverImage" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="介绍"><RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.description" height="320px" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="onSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import RichEditor from '@/components/admin/RichEditor.vue'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'
import { richTextPreview } from '@/utils/richText'

const list = ref([])
const searchForm = ref({ name: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

const emptyForm = () => ({ id: null, name: '', description: '', coverImage: '', sortOrder: list.value.length + 1 })

const filteredList = computed(() => {
  let result = list.value
  if (searchForm.value.name) result = result.filter(item => item.name && item.name.includes(searchForm.value.name))
  return result
})

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  const res = await api.adminList('core-businesses')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('core-businesses', load)
onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { name: '' } }

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}

const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入业务名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('core-businesses', form.value.id, form.value)
      : await api.adminCreate('core-businesses', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning' })
  try {
    const res = await api.adminDelete('core-businesses', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
.cover { width: 82px; height: 48px; border-radius: 4px; display: block; }
.muted { color: #909399; font-size: 13px; }
</style>
