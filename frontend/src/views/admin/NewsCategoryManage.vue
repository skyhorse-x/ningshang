<template>
  <div>
    <div class="page-header">
      <h3>新闻分类管理</h3>
      <div>
        <el-button v-if="hasPermission('news-categories:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button>
        <el-button v-if="hasPermission('news-categories:create')" type="primary" @click="openDialog()">+ 新增分类</el-button>
      </div>
    </div>

    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="分类名称"><el-input v-model="searchForm.name" placeholder="搜索分类名称" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.isActive" placeholder="全部" clearable style="width: 120px">
          <el-option label="启用" :value="true" />
          <el-option label="停用" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe v-loading="loading" @selection-change="onSelectionChange">
      <el-table-column v-if="hasPermission('news-categories:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="分类名称" min-width="160" />
      <el-table-column prop="key" label="分类标识" min-width="160" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isActive === false ? 'info' : 'success'">{{ row.isActive === false ? '停用' : '启用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="90" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('news-categories:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('news-categories:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑分类' : '新增分类'" width="min(640px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="分类名称"><el-input v-model="form.name" maxlength="50" placeholder="例如：集团新闻" /></el-form-item>
        <el-form-item label="分类标识"><el-input v-model="form.key" maxlength="50" placeholder="例如：group" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.isActive" active-text="启用" inactive-text="停用" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
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
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const list = ref([])
const loading = ref(false)
const searchForm = ref({ name: '', isActive: null })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})

const emptyForm = () => ({ id: null, name: '', key: '', isActive: true, sortOrder: list.value.length + 1 })

const filteredList = computed(() => list.value.filter(item => {
  const matchName = !searchForm.value.name || item.name?.includes(searchForm.value.name) || item.key?.includes(searchForm.value.name)
  const matchStatus = searchForm.value.isActive === null || searchForm.value.isActive === undefined || searchForm.value.isActive === '' || item.isActive === searchForm.value.isActive
  return matchName && matchStatus
}))

const load = async () => {
  loading.value = true
  try {
    const res = await api.adminList('news-categories')
    if (res.code === 200) list.value = res.data || []
  } finally {
    loading.value = false
  }
}

const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('news-categories', load)

onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { name: '', isActive: null } }
const openDialog = row => { form.value = row ? { ...row } : emptyForm(); dialogVisible.value = true }

const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入分类名称'); return }
  if (!form.value.key) { ElMessage.warning('请输入分类标识'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('news-categories', form.value.id, form.value)
      : await api.adminCreate('news-categories', form.value)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      await load()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || e.message || '保存失败')
  } finally {
    saving.value = false
  }
}

const onDelete = async row => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('news-categories', row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      await load()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error(e?.response?.data?.message || '删除失败')
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
</style>
