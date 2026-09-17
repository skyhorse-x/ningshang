<template>
  <section class="section coverage-section">
    <div class="wrap">
      <div class="sec-head light">
        <span class="en">NATIONWIDE COVERAGE</span>
        <h3>业务辐射全国</h3>
        <p>以安徽为中心，连接重点城市与产业节点，形成全国化服务网络</p>
      </div>
      <div ref="chartRef" class="coverage-chart" aria-label="中国地图业务辐射动态图"></div>
    </div>
  </section>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import * as echarts from 'echarts'

const chartRef = ref(null)
let chart
const chinaGeo = {
  type: 'FeatureCollection',
  features: [{
    type: 'Feature',
    properties: { name: '中国' },
    geometry: { type: 'Polygon', coordinates: [[[73,18],[81,30],[88,39],[96,49],[108,53],[121,48],[135,43],[129,31],[123,22],[113,18],[101,21],[90,25],[80,20],[73,18]]] }
  }]
}
const cities = {
  合肥: [117.27, 31.86], 北京: [116.40, 39.90], 上海: [121.47, 31.23], 广州: [113.26, 23.13], 深圳: [114.06, 22.54], 成都: [104.06, 30.67], 武汉: [114.31, 30.52], 西安: [108.94, 34.34], 杭州: [120.15, 30.28]
}
const lines = Object.entries(cities).filter(([name]) => name !== '合肥').map(([, coord]) => ({ coords: [cities.合肥, coord] }))

const render = () => {
  if (!chartRef.value) return
  echarts.registerMap('china-coverage', chinaGeo)
  chart = echarts.init(chartRef.value)
  chart.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'item' },
    geo: { map: 'china-coverage', roam: false, zoom: 1.05, itemStyle: { areaColor: 'rgba(255,255,255,.08)', borderColor: 'rgba(255,255,255,.5)' }, emphasis: { itemStyle: { areaColor: 'rgba(200,164,92,.25)' }, label: { show: false } } },
    series: [
      { type: 'effectScatter', coordinateSystem: 'geo', zlevel: 2, symbolSize: val => val[2] || 10, rippleEffect: { brushType: 'stroke' }, itemStyle: { color: '#f5c76a' }, data: Object.entries(cities).map(([name, coord]) => ({ name, value: [...coord, name === '合肥' ? 18 : 10] })) },
      { type: 'lines', coordinateSystem: 'geo', zlevel: 1, effect: { show: true, period: 4, trailLength: .25, symbol: 'arrow', symbolSize: 8 }, lineStyle: { color: '#f5c76a', width: 1.5, opacity: .6, curveness: .2 }, data: lines }
    ]
  })
}
const resize = () => chart?.resize()
onMounted(() => { render(); window.addEventListener('resize', resize) })
onBeforeUnmount(() => { window.removeEventListener('resize', resize); chart?.dispose() })
</script>

<style scoped>
.coverage-section { background: radial-gradient(circle at center, #1e5aa8 0%, #0d3a72 48%, #071f3f 100%); color: #fff; overflow: hidden; }
.sec-head.light h3 { color: #fff; }
.sec-head.light p { color: rgba(255,255,255,.72); }
.coverage-chart { width: 100%; height: 460px; border: 1px solid rgba(255,255,255,.12); border-radius: 16px; background: linear-gradient(135deg, rgba(255,255,255,.08), rgba(255,255,255,.02)); box-shadow: 0 24px 60px rgba(0,0,0,.18); }
@media (max-width: 900px) { .coverage-chart { height: 320px; } }
</style>
