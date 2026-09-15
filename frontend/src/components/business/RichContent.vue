<template><div class="rich-content" v-html="html"></div></template>
<script setup>
import { computed } from 'vue'
import { richHtml } from '@/utils/richText'
const props = defineProps({ content: { type: String, default: '' } })
const html = computed(() => richHtml(props.content))
</script>
<style scoped>
.rich-content { min-width: 0; overflow-wrap: anywhere; line-height: 1.8; }
.rich-content :deep(p) { margin: 0 0 1em; }
.rich-content :deep(p:last-child) { margin-bottom: 0; }
.rich-content :deep(img) { max-width: 100%; height: auto; }
.rich-content :deep(h1), .rich-content :deep(h2), .rich-content :deep(h3), .rich-content :deep(h4) { margin: 1em 0 .5em; line-height: 1.4; }
.rich-content :deep(ul), .rich-content :deep(ol) { padding-left: 1.8em; margin: .6em 0; }
.rich-content :deep(ul) { list-style: disc; }
.rich-content :deep(ol) { list-style: decimal; }
.rich-content :deep(table) { border-collapse: collapse; max-width: 100%; display: block; overflow-x: auto; }
.rich-content :deep(td), .rich-content :deep(th) { border: 1px solid #dcdfe6; padding: 8px 12px; min-width: 40px; }
.rich-content :deep(blockquote) { margin: 1em 0; padding: .5em 1em; border-left: 4px solid #c8a45c; background: #f5f7fa; }
.rich-content :deep(a) { color: #1c67aa; text-decoration: underline; }
.rich-content :deep(pre) { overflow-x: auto; padding: 12px; background: #f5f7fa; white-space: pre; }
</style>
