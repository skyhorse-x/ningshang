<template>
  <div>
    <div class="page-header"><h3>新闻管理</h3><div><el-button v-if="hasPermission('news:batch_delete')" type="danger" plain :disabled="!selectedIds.length" @click="onBatchDelete">批量删除</el-button><el-button v-if="hasPermission('news:create')" type="primary" @click="openDialog()">+ 新建新闻</el-button></div></div>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form" @submit.prevent="onSearch">
      <el-form-item label="标题"><el-input v-model="searchForm.title" placeholder="搜索标题" clearable /></el-form-item>
      <el-form-item label="分类">
        <el-select v-model="searchForm.category" placeholder="全部分类" clearable style="width:140px">
          <el-option label="全部分类" value="" />
          <el-option label="集团新闻" value="group" />
          <el-option label="产业动态" value="industry" />
          <el-option label="行业资讯" value="trend" />
          <el-option label="员工风采" value="staff" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSearch">搜索</el-button>
        <el-button @click="onReset">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" stripe v-loading="loading" @selection-change="onSelectionChange">
      <el-table-column v-if="hasPermission('news:batch_delete')" type="selection" width="44" />
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column label="创建时间" width="180">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button v-if="hasPermission('news:update')" size="small" @click="openDialog(row)">编辑</el-button>
          <el-button v-if="hasPermission('news:delete')" size="small" type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-row">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="onSizeChange"
        @current-change="load"
      />
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑新闻' : '新建新闻'" width="min(960px, 94vw)" top="5vh" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="分类"><el-select v-model="form.category" placeholder="请选择" @change="onCategoryChange"><el-option label="集团新闻" value="group" /><el-option label="产业动态" value="industry" /><el-option label="行业资讯" value="trend" /><el-option label="员工风采" value="staff" /></el-select></el-form-item>
        <el-form-item label="作者"><el-input v-model="form.author" /></el-form-item>
        <el-form-item label="封面图"><ImageUpload v-model="form.image" /></el-form-item>
        <el-form-item label="摘要"><el-input v-model="form.summary" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="正文"><RichEditor v-if="dialogVisible" :key="form.id || 'new'" v-model="form.body" height="380px" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="saving" @click="onSave">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'
import RichEditor from '@/components/admin/RichEditor.vue'
import ImageUpload from '@/components/admin/ImageUpload.vue'
import { hasPermission } from '@/utils/permission'
import { useBatchDelete } from '@/utils/batchDelete'

const CATS = { group: '集团新闻', industry: '产业动态', trend: '行业资讯', staff: '员工风采' }
const list = ref([])
const searchForm = ref({ title: '', category: '' })
const dialogVisible = ref(false)
const saving = ref(false)
const loading = ref(false)
const form = ref({})
const pagination = reactive({ page: 1, size: 10, total: 0 })

const emptyForm = () => ({ id: null, title: '', category: 'group', categoryName: '集团新闻', date: '', author: '', source: '内部资料', image: '', summary: '', body: '', newsId: '' })

const formatTime = (t) => {
  if (!t) return '-'
  const d = new Date(t)
  if (isNaN(d.getTime())) return t
  return d.toLocaleString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' }).replace(/\//g, '-')
}

const load = async () => {
  loading.value = true
  try {
    const res = await api.adminNewsPage({
      page: pagination.page, size: pagination.size,
      title: searchForm.value.title || undefined,
      category: searchForm.value.category || undefined
    })
    if (res.code === 200) {
      list.value = res.data.list || []
      pagination.total = res.data.total || 0
    }
  } finally { loading.value = false }
}
const { selectedIds, onSelectionChange, onBatchDelete } = useBatchDelete('news', load)
onMounted(load)

const onSearch = () => { pagination.page = 1; load() }
const onReset = () => { searchForm.value = { title: '', category: '' }; pagination.page = 1; load() }
const onSizeChange = () => { pagination.page = 1; load() }

const onCategoryChange = (val) => { form.value.categoryName = CATS[val] || val }

const openDialog = (row) => {
  form.value = row ? { ...row } : emptyForm()
  dialogVisible.value = true
}

const onSave = async () => {
  if (!form.value.title) { ElMessage.warning('请输入标题'); return }
  if (!form.value.newsId) form.value.newsId = 'news-' + Date.now()
  saving.value = true
  try {
    const res = form.value.id
      ? await api.adminUpdate('news', form.value.id, form.value)
      : await api.adminCreate('news', form.value)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogVisible.value = false
      pagination.page = 1
      load()
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch (e) { ElMessage.error('保存失败') } finally { saving.value = false }
}

const onDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该新闻吗？', '提示', { type: 'warning', confirmButtonText: '确认', cancelButtonText: '取消' })
  try {
    const res = await api.adminDelete('news', row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      if (list.value.length === 1 && pagination.page > 1) pagination.page--
      load()
    }
    else ElMessage.error(res.message || '删除失败')
  } catch (e) { ElMessage.error('删除失败') }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-header h3 { font-size: 20px; color: #0d3a72; margin: 0; }
.search-form { background: #fff; padding: 16px 16px 0; border-radius: 8px; margin-bottom: 16px; }
.pagination-row { display: flex; justify-content: flex-end; padding: 20px 0; }
</style>
