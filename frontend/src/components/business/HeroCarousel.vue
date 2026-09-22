<template>
  <div class="hero" @pointerdown="startSwipe" @pointerup="finishSwipe" @pointercancel="cancelSwipe">
    <el-carousel ref="carousel" height="500px" :interval="5500" arrow="never" trigger="click">
      <el-carousel-item v-for="(slide, idx) in slides" :key="idx">
        <div class="slide" :style="'background-image:url(' + slide.image + ')'">
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { loadContent, pick } from '@/utils/content'

// 默认轮播图；后台「系统设置 - 网站设置 - 基本设置 - 首页轮播图」配置后覆盖
const defaults = [
  { image: '/images/hero-1.jpeg', tag: 'NINGSHANG', title: '安徽宁商科技集团', desc: '立足安徽本土，聚焦科创产业服务', link: '/about/intro' },
  { image: '/images/hero-2.jpg', tag: 'INNOVATION', title: '科创赋能产业', desc: '科技赋能产业，服务贯穿全程', link: '/industry' },
  { image: '/images/hero-3.jpg', tag: 'FUTURE', title: '城湖共生处，笃行向远方', desc: '传承徽商实业根脉，赋能数字产业升级', link: '/about/culture' }
]
const slides = ref(defaults)
const carousel = ref(null)
const swipeStartX = ref(null)

function startSwipe(event) {
  if (event.pointerType === 'mouse' && event.button !== 0) return
  swipeStartX.value = event.clientX
}
function finishSwipe(event) {
  if (swipeStartX.value === null) return
  const distance = event.clientX - swipeStartX.value
  swipeStartX.value = null
  if (Math.abs(distance) < 45) return
  distance < 0 ? carousel.value?.next() : carousel.value?.prev()
}
function cancelSwipe() { swipeStartX.value = null }

onMounted(async () => {
  const content = await loadContent()
  slides.value = defaults.map((slide, idx) => ({
    ...slide,
    image: pick(content, 'bg_hero_' + (idx + 1), slide.image)
  }))
})
</script>

<style scoped>
.hero { position: relative; height: 500px; overflow: hidden; background: #0a2c57; top: 0px; touch-action: pan-y; cursor: grab; }
.hero:active { cursor: grabbing; }
.hero :deep(.el-carousel__indicators--horizontal) { bottom: 18px; }
.hero :deep(.el-carousel__button) { width: 28px; height: 4px; border-radius: 4px; }
.slide {
  width: 100%;
  height: 100%;
  background-size: cover;
  position: relative;
  display: flex;
  align-items: center;
}
@media (max-width: 1000px) {
  .hero { height: 500px; }
}
</style>
