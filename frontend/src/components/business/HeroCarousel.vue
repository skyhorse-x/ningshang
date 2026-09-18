<template>
  <div class="hero">
    <el-carousel height="382px" :interval="5500" arrow="never" indicator-position="outside">
      <el-carousel-item v-for="(slide, idx) in slides" :key="idx">
        <div class="slide" :style="'background-image:url(' + slide.image + ')'">
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script setup>
const slides = [
  { image: '/images/hero-1.jpeg', tag: 'NINGSHANG', title: '安徽宁商科技集团', desc: '立足安徽本土，聚焦科创产业服务', link: '/about/intro' },
  { image: '/images/hero-2.jpg', tag: 'INNOVATION', title: '科创赋能产业', desc: '科技赋能产业，服务贯穿全程', link: '/industry' },
  { image: '/images/hero-3.jpg', tag: 'FUTURE', title: '城湖共生处，笃行向远方', desc: '传承徽商实业根脉，赋能数字产业升级', link: '/about/culture' }
]
</script>

<style scoped>
.hero { position: relative; height: 382px; overflow: hidden; background: #0a2c57; top: 0px; }
.slide {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
}
.slide::after { content: ""; position: absolute; inset: 0; background: linear-gradient(90deg, rgba(5,22,46,.82), rgba(13,58,114,.48), rgba(5,22,46,.32)); }
@media (max-width: 1000px) {
  .hero { height: 382px; }
}
</style>
