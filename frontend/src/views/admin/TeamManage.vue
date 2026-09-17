<template>
  <div>
    <div class="page-header"><h3>管理团队</h3><div><el-button v-if="hasPermission('team:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button><el-button v-if="hasPermission('team:create')" type="primary" @click="openDialog()">+ 新增成员</el-button></div></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="姓名"><el-input v-model="searchForm.name" placeholder="搜索姓名" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="pagedList" stripe @selection-change="onSelectionChange">
      <el-table-column v-if="hasPermission('team:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="头像" width="80">
        <template #default="{ row }"><img :src="row.avatar" style="width:44px;height:44px;border-radius:50%;object-fit:cover" v-if="row.avatar"></template>
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="position" label="职位" />
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('team:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('team:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-row" v-if="filteredList.length > pagination.size">
      <el-pagination background layout="total, sizes, prev, pager, next" :page-sizes="[10, 20, 50, 100]" :total="filteredList.length" v-model:current-page="pagination.page" v-model:page-size="pagination.size" />
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑成员' : '新增成员'" width="min(960px, 94vw)" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="职位"><el-input v-model="form.position" placeholder="创始人 · 董事长 · 总工程师" /></el-form-item>
        <el-form-item label="头像"><ImageUpload v-model="form.avatar" /></el-form-item>
        <el-form-item label="渐变色"><el-input v-model="form.gradient" placeholder="linear-gradient(135deg,#1a365d 0%,#2c5282 100%)" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="简介">
          <RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.description" height="380px" />
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import RichEditor from '@/components/admin/RichEditor.vue'
import { ref, onMounted, computed, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const list = ref([])
const searchForm = ref({ name: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})
const pagination = reactive({ page: 1, size: 10 })

const emptyForm = () => ({ id: null, name: '', position: '', description: '', avatar: '', gradient: 'linear-gradient(135deg,#1a365d 0%,#2c5282 100%)', sortOrder: list.value.length + 1 })

const filteredList = computed(() => {
  let result = list.value
  if (searchForm.value.name) result = result.filter(item => item.name && item.name.includes(searchForm.value.name))
  return result
})
const pagedList = computed(() => filteredList.value.slice((pagination.page - 1) * pagination.size, pagination.page * pagination.size))

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  const res = await api.adminList('team')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('team', load)
onMounted(load)

const onSearch = () => { pagination.page = 1 }
const onReset = () => { searchForm.value = { name: '' }; pagination.page = 1 }

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}
const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入姓名'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('team', form.value.id, form.value)
      : await api.adminCreate('team', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}
const onDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除成员「${row.name}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('team', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
.pagination-row { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>
