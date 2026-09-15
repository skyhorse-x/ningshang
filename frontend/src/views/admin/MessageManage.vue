<template>
  <div>
    <div class="page-header"><h3>在线留言</h3><el-button v-if="hasPermission('messages:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="姓名"><el-input v-model="searchForm.name" placeholder="搜索姓名" clearable /></el-form-item>
      <el-form-item label="电话"><el-input v-model="searchForm.phone" placeholder="搜索电话" clearable /></el-form-item>
      <el-form-item label="类型">
        <el-select v-model="searchForm.type" placeholder="全部类型" clearable style="width:130px">
          <el-option label="全部类型" value="" />
          <el-option label="业务合作" value="cooperation" />
          <el-option label="人才应聘" value="recruit" />
          <el-option label="媒体联系" value="media" />
          <el-option label="其他咨询" value="other" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="filteredList" stripe @selection-change="onSelectionChange"><el-table-column v-if="hasPermission('messages:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="phone" label="电话" width="130" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column label="类型" width="100">
        <template #default="{ row }">{{ typeText(row.type) }}</template>
      </el-table-column>
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column label="留言时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="hasPermission('messages:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

const TYPES = { cooperation: '业务合作', recruit: '人才应聘', media: '媒体联系', other: '其他咨询' }
const list = ref([])
const searchForm = ref({ name: '', phone: '', type: '' })

const typeText = (t) => TYPES[t] || t || '-'

const filteredList = computed(() => {
  let result = list.value
  if (searchForm.value.name) result = result.filter(item => item.name && item.name.includes(searchForm.value.name))
  if (searchForm.value.phone) result = result.filter(item => item.phone && item.phone.includes(searchForm.value.phone))
  if (searchForm.value.type) result = result.filter(item => item.type === searchForm.value.type)
  return result
})

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  const res = await api.adminList('messages')
  if (res.code === 200) list.value = res.data
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('messages', load)
onMounted(load)

const onSearch = () => {}
const onReset = () => { searchForm.value = { name: '', phone: '', type: '' } }

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该留言吗？', '提示', { type: 'warning' })
  try {
    const res = await api.adminDelete('messages', row.id)
    if (res.code === 200) { ElMessage.success('删除成功'); load() }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
</style>
