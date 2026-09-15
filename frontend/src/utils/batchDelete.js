import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '@/api'

export function useBatchDelete(module, reload) {
  const selectedIds = ref([])
  const onSelectionChange = rows => { selectedIds.value = rows.map(row => row.id) }
  const onBatchDelete = async () => {
    if (!selectedIds.value.length) return
    await ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条数据吗？`, '批量删除', { type: 'warning' })
    await api.adminBatchDelete(module, selectedIds.value)
    ElMessage.success('批量删除成功')
    selectedIds.value = []
    await reload()
  }
  return { selectedIds, onSelectionChange, onBatchDelete }
}
