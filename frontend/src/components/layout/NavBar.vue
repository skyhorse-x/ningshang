<template>
  <div class="site-nav" :class="{ 'is-overlay': overlay }">
    <div class="wrap">
      <router-link to="/" class="brand">
        <img src="/images/logo.png" alt="安徽宁商科技集团" width="72" height="54">
        <span class="bt"><b>宁商科技集团</b><small>ANHUI NINGSHANG TECH GROUP</small></span>
      </router-link>
      <div class="menu-btn" @click="menuOpen = !menuOpen">
        <span></span><span></span><span></span>
      </div>
      <ul class="nav" :class="{ open: menuOpen }">
        <li :class="{ on: route.path === '/' }"><router-link to="/" @click="menuOpen=false">首页<span class="en">HOME</span></router-link></li>
        <li :class="{ on: route.path.startsWith('/about') }">
          <router-link to="/about/intro" class="nav-link" @click="menuOpen=false">集团概况<span class="en">ABOUT US</span></router-link>
          <ul class="subnav">
            <li><router-link to="/about/intro" @click="menuOpen=false">集团简介</router-link></li>
            <li><router-link to="/about/speech" @click="menuOpen=false">董事长致词</router-link></li>
            <li><router-link to="/about/events" @click="menuOpen=false">发展大事记</router-link></li>
            <li><router-link to="/about/team" @click="menuOpen=false">管理团队</router-link></li>
            <li><router-link to="/about/honor" @click="menuOpen=false">企业荣誉</router-link></li>
            <li><router-link to="/about/party" @click="menuOpen=false">党建工作</router-link></li>
            <li><router-link to="/about/culture" @click="menuOpen=false">企业文化</router-link></li>
          </ul>
        </li>
        <li :class="{ on: route.path.startsWith('/news') }">
          <router-link to="/news" class="nav-link" @click="menuOpen=false">新闻中心<span class="en">NEWS</span></router-link>
          <ul class="subnav">
            <li><router-link to="/news?category=group" @click="menuOpen=false">集团新闻</router-link></li>
            <li><router-link to="/news?category=industry" @click="menuOpen=false">产业动态</router-link></li>
            <li><router-link to="/news?category=trend" @click="menuOpen=false">行业资讯</router-link></li>
            <li><router-link to="/news?category=staff" @click="menuOpen=false">员工风采</router-link></li>
          </ul>
        </li>
        <li :class="{ on: route.path.startsWith('/industry') }">
          <router-link to="/industry" class="nav-link" @click="menuOpen=false">集团产业<span class="en">INDUSTRY</span></router-link>
        </li>
        <li :class="{ on: route.path.startsWith('/contact') || route.path.startsWith('/recruit') }">
          <router-link to="/contact" class="nav-link" @click="menuOpen=false">联系宁商<span class="en">CONTACT</span></router-link>
          <ul class="subnav">
            <li><router-link to="/recruit" @click="menuOpen=false">人才理念</router-link></li>
            <li><router-link to="/recruit/jobs" @click="menuOpen=false">招聘岗位</router-link></li>
            <li><router-link to="/contact" @click="menuOpen=false">联系方式</router-link></li>
            <li><router-link to="/contact/message" @click="menuOpen=false">在线留言</router-link></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'

defineProps({
  overlay: {
    type: Boolean,
    default: false
  }
})

const route = useRoute()
const menuOpen = ref(false)
</script>

<style scoped>
.site-nav {
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 200;
  box-shadow: 0 2px 12px rgba(0,0,0,.06);
  transition: background-color .25s ease, box-shadow .25s ease;
}
.site-nav.is-overlay {
  position: absolute;
  top: 38px;
  left: 0;
  right: 0;
  background: rgb(4 4 4 / 0%);
  box-shadow: none;
  backdrop-filter: blur(8px);
}
.wrap {
  width: 1200px;
  max-width: 94%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 96px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 14px;
  flex-shrink: 0;
  text-decoration: none;
}
.brand img { height: 60px; width: auto; }
.brand .bt { display: flex; flex-direction: column; line-height: 1.25; }
.brand .bt b { font-size: 22px; color: var(--c-primary); letter-spacing: 1px; }
.brand .bt small { font-size: 11px; color: var(--c-text-light); letter-spacing: 2px; text-transform: uppercase; }
.site-nav.is-overlay .brand .bt b,
.site-nav.is-overlay .brand .bt small,
.site-nav.is-overlay .nav > li > a,
.site-nav.is-overlay .nav-link {
  color: #fff;
}
.site-nav.is-overlay .nav > li > a .en,
.site-nav.is-overlay .nav-link .en {
  color: rgba(255,255,255,.72);
}
.site-nav.is-overlay .nav > li:hover > a,
.site-nav.is-overlay .nav > li.on > a {
  color: #fff;
}
.nav {
  margin-left: auto;
  display: flex;
  align-items: center;
  height: 100%;
  list-style: none;
  padding: 0;
}
.nav > li {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
}
.nav > li > a, .nav-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 0 22px;
  font-size: 16px;
  color: var(--c-text);
  font-weight: 500;
  text-decoration: none;
  cursor: pointer;
  transition: color .25s ease, background-color .25s ease;
}
.nav > li > a .en, .nav-link .en {
  font-size: 10px;
  color: #b7b7b7;
  letter-spacing: 1px;
  font-weight: 400;
  margin-top: 2px;
}
.nav > li > a::after {
  content: "";
  position: absolute;
  left: 50%;
  bottom: 24px;
  width: 0;
  height: 2px;
  background: var(--c-accent);
  transition: width .25s ease;
  transform: translateX(-50%);
}
.nav > li:hover > a::after,
.nav > li.on > a::after { width: 24px; }
.nav > li:hover > a,
.nav > li.on > a { color: var(--c-primary); }
.subnav {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%) translateY(10px);
  min-width: 170px;
  background: #fff;
  box-shadow: 0 6px 24px rgba(13,58,114,.10);
  border-top: 3px solid var(--c-accent);
  opacity: 0;
  visibility: hidden;
  transition: .28s;
  padding: 8px 0;
  list-style: none;
  z-index: 100;
}
.nav > li:hover .subnav {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(0);
}
.subnav li a {
  display: block;
  padding: 11px 24px;
  font-size: 14px;
  color: var(--c-text);
  white-space: nowrap;
  text-decoration: none;
  transition: color .25s ease, background-color .25s ease;
}
.subnav li a:hover {
  background: var(--c-bg-soft);
  color: var(--c-primary);
}
.menu-btn { display: none; }

@media (max-width: 1000px) {
  .site-nav.is-overlay {
    top: 0;
    background: rgba(5, 22, 46, .72);
  }
  .site-nav.is-overlay .wrap { height: 78px; }
  .site-nav.is-overlay .brand img { height: 48px; }
  .site-nav.is-overlay .menu-btn span { background: #fff; }
  .menu-btn {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 5px;
    margin-left: auto;
    cursor: pointer;
    padding: 8px;
  }
  .menu-btn span { width: 26px; height: 2px; background: var(--c-primary); }
  .nav {
    position: fixed;
    top: 96px;
    left: 0;
    right: 0;
    flex-direction: column;
    background: #fff;
    height: auto;
    max-height: 0;
    overflow: hidden;
    transition: .4s;
    box-shadow: 0 8px 20px rgba(0,0,0,.12);
  }
  .site-nav.is-overlay .nav { top: 78px; }
  .nav.open { max-height: 600px; }
  .nav > li { width: 100%; height: auto; border-bottom: 1px solid var(--c-line); }
  .nav > li > a { padding: 14px 20px; height: auto; }
  .subnav {
    position: static;
    transform: none;
    opacity: 1;
    visibility: visible;
    box-shadow: none;
    border: none;
    background: var(--c-bg-soft);
    padding: 0;
    display: none;
  }
  .nav > li:hover .subnav { display: block; }
}
</style>
