<template>
  <div class="subnav-tabs"><div class="wrap"><slot /></div></div>
</template>

<script setup>
defineProps({ title: { type: String, default: '' } })
</script>

<style scoped>
.wrap {
  width: 1200px;
  max-width: 94%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 16px;
}
.subnav-tabs {
  background: var(--c-primary-light);
}
.subnav-tabs .wrap {
  display: flex;
  gap: 0;
}
.subnav-tabs ::v-deep(a) {
  padding: 14px 32px;
  font-size: 15px;
  color: rgba(255,255,255,.85);
  position: relative;
  transition: .25s;
  text-decoration: none;
}
.subnav-tabs ::v-deep(a:hover) {
  color: #fff;
  background: rgba(255,255,255,.08);
}
.subnav-tabs ::v-deep(a.on) {
  color: #fff;
  font-weight: 600;
  background: rgba(255,255,255,.12);
}
.subnav-tabs ::v-deep(a.on::after) {
  content: "";
  position: absolute;
  left: 32px;
  right: 32px;
  bottom: 0;
  height: 3px;
  background: var(--c-accent);
}
@media (max-width: 1000px) {
  .subnav-tabs .wrap { overflow-x: auto; flex-wrap: nowrap; }
  .subnav-tabs ::v-deep(a) { padding: 10px 20px; font-size: 13px; white-space: nowrap; }
}
</style>
