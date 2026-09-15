<template>
  <div class="rich-editor">
    <Toolbar class="re-toolbar" :editor="editorRef" :defaultConfig="toolbarConfig" mode="default" />
    <Editor
      class="re-editor"
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :style="{ height: height }"
      mode="default"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup>
import { ref, shallowRef, computed, onBeforeUnmount } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import api from '@/api'
import { richHtml } from '@/utils/richText'

const props = defineProps({
  modelValue: { type: String, default: '' },
  height: { type: String, default: '380px' },
  placeholder: { type: String, default: '请输入正文内容...' }
})
const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef(null)

const valueHtml = computed({
  get: () => richHtml(props.modelValue),
  set: (v) => emit('update:modelValue', v || '')
})

const toolbarConfig = { excludeKeys: ['group-video'] }

// 图片上传到后端 /api/admin/upload，正文只保存 URL（避免 Base64 撑爆数据库字段）
const editorConfig = {
  placeholder: props.placeholder,
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 10 * 1024 * 1024,
      async customUpload(file, insertFn) {
        try {
          const res = await api.adminUploadImage(file)
          if (res.code === 200 && res.data && res.data.url) {
            insertFn(res.data.url, file.name, '')
          } else {
            console.error('图片上传失败:', res.message)
            alert(res.message || '图片上传失败')
          }
        } catch (e) {
          console.error('图片上传失败:', e)
          alert('图片上传失败，请重试')
        }
      }
    }
  }
}

const handleCreated = (editor) => { editorRef.value = editor }
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) editor.destroy()
})
</script>

<style scoped>
.rich-editor { width: 100%; min-width: 0; border: 1px solid #dcdfe6; border-radius: 4px; position: relative; z-index: 10; background: #fff; }
.re-toolbar { border-bottom: 1px solid #dcdfe6; }
.re-editor { overflow-y: hidden; }
</style>
