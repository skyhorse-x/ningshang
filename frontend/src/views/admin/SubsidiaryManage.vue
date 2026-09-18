<template>
  <div>
    <div class="page-header"><h3>子公司管理</h3><div><el-button v-if="hasPermission('subsidiaries:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button><el-button v-if="hasPermission('subsidiaries:create')" type="primary" @click="openDialog()">+ 新建子公司</el-button></div></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="名称"><el-input v-model="searchForm.name" placeholder="搜索公司名称" clearable /></el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange"><el-table-column v-if="hasPermission('subsidiaries:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="公司名称" min-width="180" />
      <el-table-column prop="category" label="子公司类型" width="140" />
      <el-table-column label="内容介绍" min-width="260" show-overflow-tooltip>
        <template #default="{ row }">{{ richTextPreview(row.description) }}</template>
      </el-table-column>
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('subsidiaries:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('subsidiaries:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑子公司' : '新建子公司'" width="min(1100px, 96vw)" destroy-on-close>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基础信息" name="basic">
          <el-form :model="form" label-width="100px">
            <el-form-item label="公司名称"><el-input v-model="form.name" /></el-form-item>
            <el-form-item label="英文名"><el-input v-model="form.englishName" /></el-form-item>
            <el-form-item label="子公司类型"><el-input v-model="form.category" placeholder="如 建设工程、智能科技、信息咨询" /></el-form-item>
            <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="1" /></el-form-item>
            <el-form-item label="Logo图"><ImageUpload v-model="form.logo" /></el-form-item>
            <el-form-item label="背景图"><ImageUpload v-model="form.background" /></el-form-item>
            <el-form-item label="内容介绍"><RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.description" height="380px" /></el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="核心业务领域" name="business">
          <div class="business-tab">
            <div class="business-header">
              <span class="tip">选择该子公司关联的核心业务领域（可多选）</span>
              <el-button v-if="hasPermission('core-businesses:create')" size="small" type="primary" @click="openBusinessDialog()">+ 添加</el-button>
            </div>
            <el-table v-if="businessList.length" :data="businessList" stripe size="small" class="biz-table">
              <el-table-column type="selection" width="44">
                <template #default="{ row }">
                  <el-checkbox :model-value="(form.coreBusinessIds || []).includes(row.id)" @change="val => onToggleBiz(row.id, val)" />
                </template>
              </el-table-column>
              <el-table-column label="封面图片" width="100">
                <template #default="{ row }">
                  <el-image v-if="row.coverImage" :src="row.coverImage" class="biz-cover" fit="cover" />
                  <span v-else class="biz-no-cover">无封面</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="名称" min-width="140" />
              <el-table-column label="介绍" min-width="220" show-overflow-tooltip>
                <template #default="{ row }">{{ richTextPreview(row.description) }}</template>
              </el-table-column>
              <el-table-column prop="sortOrder" label="排序" width="70" />
              <el-table-column label="操作" width="160">
                <template #default="{ row }">
                  <el-button v-if="hasPermission('core-businesses:update')" size="small" type="primary" @click="openBusinessDialog(row)">编辑</el-button>
                  <el-button v-if="hasPermission('core-businesses:delete')" size="small" type="danger" @click="onBusinessDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-empty v-else description="暂无核心业务领域，请先添加" />
          </div>
        </el-tab-pane>
      </el-tabs>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>

    <el-dialog v-model="businessDialogVisible" :title="businessForm.id ? '编辑核心业务' : '新增核心业务'" width="min(960px, 94vw)" destroy-on-close>
      <el-form :model="businessForm" label-width="90px">
        <el-form-item label="名称"><el-input v-model="businessForm.name" placeholder="如 建设工程" /></el-form-item>
        <el-form-item label="封面图片"><ImageUpload v-model="businessForm.coverImage" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="businessForm.sortOrder" :min="1" /></el-form-item>
        <el-form-item label="介绍"><RichEditor v-if="businessDialogVisible" :key="businessForm.id || 'business-new'" v-model="businessForm.description" height="320px" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="businessDialogVisible = false">取消</el-button><el-button type="primary" :loading="businessSaving" @click="onBusinessSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import RichEditor from '@/components/admin/RichEditor.vue'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { richTextPreview } from '@/utils/richText'

const list = ref([])
const searchForm = ref({ name: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({})
const activeTab = ref('basic')
const businessList = ref([])
const businessDialogVisible = ref(false)
const businessSaving = ref(false)
const businessForm = ref({})

const emptyForm = () => ({ id: null, name: '', englishName: '', category: '', description: '', logo: '', background: '', sortOrder: list.value.length + 1, coreBusinessIds: [] })

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
  const res = await api.adminList('subsidiaries')
  if (res.code === 200) list.value = res.data
}
const loadBusinesses = async () => {
  const res = await api.adminList('core-businesses')
  if (res.code === 200) businessList.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('subsidiaries', load)
const { selectedIds: businessSelectedIds, onSelectionChange: onBusinessSelectionChange, onBatchDelete: onBusinessBatchDelete } = useBatchDelete('core-businesses', loadBusinesses)
onMounted(() => { load(); loadBusinesses() })

const onSearch = () => {}
const onReset = () => { searchForm.value = { name: '' } }

const openDialog = (row) => {
  form.value = row ? { ...row, coreBusinessIds: row.coreBusinessIds || [] } : emptyForm()
  activeTab.value = 'basic'
  dialogVisible.value = true
}
const onToggleBiz = (id, checked) => {
  if (!form.value.coreBusinessIds) form.value.coreBusinessIds = []
  const idx = form.value.coreBusinessIds.indexOf(id)
  if (checked && idx === -1) form.value.coreBusinessIds.push(id)
  if (!checked && idx !== -1) form.value.coreBusinessIds.splice(idx, 1)
}
const onSave = async () => {
  if (!form.value.name) { ElMessage.warning('请输入公司名称'); return }
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('subsidiaries', form.value.id, form.value)
      : await api.adminCreate('subsidiaries', form.value)
    if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}
const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该子公司吗？', '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('subsidiaries', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}

const emptyBusinessForm = () => ({ id: null, name: '', description: '', coverImage: '', sortOrder: businessList.value.length + 1 })
const openBusinessDialog = (row) => {
  businessForm.value = row ? { ...row } : emptyBusinessForm()
  businessDialogVisible.value = true
}
const onBusinessSave = async () => {
  if (!businessForm.value.name) { ElMessage.warning('请输入业务名称'); return }
  businessSaving.value = true
  try {
    const res = businessForm.value.id
      ? await api.adminUpdate('core-businesses', businessForm.value.id, businessForm.value)
      : await api.adminCreate('core-businesses', businessForm.value)
    if (res.code === 200) { ElMessage.success('保存成功'); businessDialogVisible.value = false; loadBusinesses() }
    else ElMessage.error(res.message || '保存失败')
  } catch (e) { ElMessage.error('保存失败') } finally { businessSaving.value = false }
}
const onBusinessDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除「${row.name}」吗？`, '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('core-businesses', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); loadBusinesses() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.page-header.compact { margin-top: 24px; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
.section-card { margin-top: 24px; }
.cover { width: 82px; height: 48px; border-radius: 4px; display: block; }
.muted { color: #909399; font-size: 13px; }
.business-tab .business-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.business-tab .tip { font-size: 13px; color: #909399; }
.biz-table { margin-top: 4px; }
.biz-cover { width: 56px; height: 36px; border-radius: 4px; display: block; }
.biz-no-cover { display: flex; align-items: center; justify-content: center; width: 56px; height: 36px; border-radius: 4px; background: #f0f0f0; font-size: 11px; color: #999; }
</style>
