<template>
  <div class="hero">
    <el-carousel height="320px" :interval="5500" arrow="never" indicator-position="outside">
      <el-carousel-item v-for="(slide, idx) in slides" :key="idx">
        <div class="slide" :style="'background-image:url(' + slide.image + ')'">
          <div class="cap">
            <span class="tag">{{ slide.tag }}</span>
            <h2>{{ slide.title }}</h2>
            <p>{{ slide.desc }}</p>
            <router-link v-if="slide.link" :to="slide.link" class="more">了解更多</router-link>
          </div>
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
.hero { position: relative; height: 320px; overflow: hidden; background: #0a2c57; }
.slide {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
}
.cap {
  position: relative;
  z-index: 2;
  color: #fff;
  max-width: 680px;
  margin: 0 auto;
  padding: 0 20px;
}
.tag {
  display: inline-block;
  font-size: 12px;
  letter-spacing: 3px;
  color: var(--c-accent);
  border-left: 3px solid var(--c-accent);
  padding-left: 10px;
  margin-bottom: 12px;
}
.cap h2 { font-size: 32px; font-weight: 700; line-height: 1.3; margin-bottom: 12px; text-shadow: 0 2px 12px rgba(0,0,0,.3); }
.cap p { font-size: 15px; color: #dbe6f5; margin-bottom: 20px; }
.more {
  display: inline-block;
  padding: 10px 28px;
  border: 1px solid rgba(255,255,255,.5);
  color: #fff;
  font-size: 13px;
  letter-spacing: 2px;
  text-decoration: none;
  transition: .3s;
}
.more:hover { background: var(--c-accent); border-color: var(--c-accent); }
@media (max-width: 1000px) {
  .hero { height: 260px; }
  .cap h2 { font-size: 24px; }
  .cap p { font-size: 13px; }
}
</style>
