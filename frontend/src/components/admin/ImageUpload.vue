<template>
  <div class="image-upload">
    <el-upload
      class="uploader"
      drag
      action="#"
      :show-file-list="false"
      :http-request="doUpload"
      accept="image/jpeg,image/png,image/gif,image/webp"
    >
      <img v-if="modelValue" :src="modelValue" class="preview" alt="预览图">
      <div v-else class="empty">
        <el-icon class="up-icon"><UploadFilled /></el-icon>
        <div class="up-text">将图片拖到此处，或 <em>点击上传</em></div>
        <div class="up-tip">支持 jpg / png / gif / webp，不超过 5MB</div>
      </div>
      <div v-if="uploading" class="mask"><el-icon class="loading" :size="26"><Loading /></el-icon></div>
    </el-upload>
    <el-input
      v-model="urlInput"
      size="small"
      class="url-input"
      placeholder="或直接粘贴图片地址，如 /images/news-1.jpeg"
      clearable
      @change="onUrlChange"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Loading } from '@element-plus/icons-vue'
import api from '@/api'

const props = defineProps({
  modelValue: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])

const uploading = ref(false)
const urlInput = ref(props.modelValue)

// 外部值变化时同步手动输入框
watch(() => props.modelValue, (v) => { urlInput.value = v || '' })

const onUrlChange = (v) => emit('update:modelValue', (v || '').trim())

const doUpload = async ({ file }) => {
  if (!/^image\//.test(file.type)) { ElMessage.warning('请选择图片文件'); return }
  if (file.size > 5 * 1024 * 1024) { ElMessage.warning('图片不能超过 5MB'); return }
  uploading.value = true
  try {
    const res = await api.adminUpload(file)
    if (res.code === 200 && res.data?.url) {
      emit('update:modelValue', res.data.url)
      ElMessage.success('上传成功')
    } else {
      ElMessage.error(res.message || '上传失败')
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || '上传失败，请检查后端服务')
  } finally {
    uploading.value = false
  }
}
</script>

<style scoped>
.image-upload { width: 100%; }
.uploader :deep(.el-upload-dragger) {
  width: 100%;
  height: 130px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
}
.uploader :deep(.el-upload) { width: 100%; }
.uploader :deep(.el-upload-dragger .el-upload-dragger) { padding: 0; }
.preview { max-height: 110px; max-width: 100%; border-radius: 4px; }
.empty { display: flex; flex-direction: column; align-items: center; gap: 4px; color: #8a94a6; }
.up-icon { font-size: 30px; color: #a8abb2; margin: 0; }
.up-text { font-size: 13px; }
.up-text em { color: #409eff; font-style: normal; }
.up-tip { font-size: 12px; color: #b0b6c0; }
.mask {
  position: absolute; inset: 0; display: flex; align-items: center; justify-content: center;
  background: rgba(255,255,255,.7); border-radius: 6px;
}
.loading { color: #409eff; }
.url-input { margin-top: 8px; }
</style>
