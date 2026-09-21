<template>
  <div class="video-upload">
    <el-upload drag action="#" :show-file-list="false" :http-request="doUpload" accept="video/mp4,video/webm,video/ogg,video/quicktime">
      <div class="empty">
        <el-icon class="up-icon"><UploadFilled /></el-icon>
        <div>{{ uploading ? '正在上传，请稍候…' : '将视频拖到此处，或点击上传' }}</div>
        <small>支持 mp4 / webm / ogg / mov，不超过 100MB</small>
      </div>
    </el-upload>
    <video v-if="modelValue" class="preview" :src="modelValue" controls preload="metadata" />
    <el-input v-model="urlInput" class="url-input" placeholder="也可直接填写视频地址" clearable @change="onUrlChange" />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'
import api from '@/api'

const props = defineProps({ modelValue: { type: String, default: '' } })
const emit = defineEmits(['update:modelValue'])
const uploading = ref(false)
const urlInput = ref(props.modelValue)
watch(() => props.modelValue, value => { urlInput.value = value || '' })
const onUrlChange = value => emit('update:modelValue', (value || '').trim())

const doUpload = async ({ file }) => {
  if (!/^video\//.test(file.type)) return ElMessage.warning('请选择视频文件')
  if (file.size > 100 * 1024 * 1024) return ElMessage.warning('视频不能超过 100MB')
  uploading.value = true
  try {
    const res = await api.adminUploadVideo(file)
    if (res.code === 200 && res.data?.url) {
      emit('update:modelValue', res.data.url)
      ElMessage.success('视频上传成功，请点击保存基本设置')
    }
  } catch (e) {
    ElMessage.error(e.response?.data?.message || e.message || '视频上传失败')
  } finally {
    uploading.value = false
  }
}
</script>

<style scoped>
.video-upload { width: 100%; }
.empty { color: #8a94a6; line-height: 1.8; }
.up-icon { display: block; margin: 0 auto 6px; font-size: 32px; color: #409eff; }
.empty small { color: #b0b6c0; }
.preview { display: block; width: min(560px, 100%); max-height: 315px; margin-top: 12px; border-radius: 6px; background: #000; }
.url-input { margin-top: 10px; }
</style>
