<template>
  <section class="section coverage-section">
    <div class="wrap">
      <div class="sec-head light">
        <span class="en">NATIONWIDE COVERAGE</span>
        <h3>业务辐射全国</h3>
        <p>以安徽为中心，连接重点城市与产业节点，形成全国化服务网络</p>
      </div>
      <div class="coverage-legend">
        <span class="legend-item"><span class="dot dot-center"></span>总部·合肥</span>
        <span class="legend-item"><span class="dot dot-city"></span>覆盖城市</span>
        <span class="legend-item"><span class="line"></span>业务辐射</span>
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
  合肥: [117.27, 31.86], 北京: [116.40, 39.90], 上海: [121.47, 31.23], 广州: [113.26, 23.13], 深圳: [114.06, 22.54],
  成都: [104.06, 30.67], 武汉: [114.31, 30.52], 西安: [108.94, 34.34], 杭州: [120.15, 30.28], 南京: [118.78, 32.04],
  重庆: [106.55, 29.56], 长沙: [112.94, 28.23], 郑州: [113.65, 34.76], 济南: [117.00, 36.67], 福州: [119.30, 26.08],
  沈阳: [123.43, 41.80], 哈尔滨: [126.53, 45.80], 昆明: [102.83, 24.88], 南宁: [108.33, 22.84], 贵阳: [106.71, 26.57],
  兰州: [103.83, 36.06], 乌鲁木齐: [87.62, 43.80], 呼和浩特: [111.75, 40.84], 长春: [125.32, 43.90], 太原: [112.55, 37.87]
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
.coverage-legend { display: flex; justify-content: center; gap: 28px; margin-bottom: 18px; flex-wrap: wrap; }
.legend-item { display: flex; align-items: center; gap: 6px; font-size: 13px; color: rgba(255,255,255,.75); }
.legend-item .dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; }
.dot-center { background: #f5c76a; box-shadow: 0 0 8px #f5c76a; }
.dot-city { background: rgba(255,255,255,.7); }
.legend-item .line { display: inline-block; width: 24px; height: 2px; background: linear-gradient(90deg, #f5c76a, rgba(245,199,106,.3)); }
.coverage-chart { width: 100%; height: 460px; border: 1px solid rgba(255,255,255,.12); border-radius: 16px; background: linear-gradient(135deg, rgba(255,255,255,.08), rgba(255,255,255,.02)); box-shadow: 0 24px 60px rgba(0,0,0,.18); }
@media (max-width: 900px) { .coverage-chart { height: 320px; } .coverage-legend { gap: 14px; } }
</style>
