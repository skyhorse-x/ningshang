<template>
  <AppLayout v-if="!isAdminRoute" />
  <router-view v-else />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppLayout from '@/components/layout/AppLayout.vue'

const route = useRoute()
const isAdminRoute = computed(() => route.path.startsWith('/ningshang-admin'))
</script>
