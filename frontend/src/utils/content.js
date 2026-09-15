import api from '@/api'

// 官网页面文案缓存：key -> content
let cache = null
let pending = null
export function clearContentCache() { cache = null }

export async function loadContent(force = false) {
  if (cache && !force) return cache
  if (pending) return pending
  pending = (async () => {
    try {
    const res = await api.getContent()
    if (res.code === 200 && Array.isArray(res.data)) {
      cache = Object.fromEntries(res.data.map(item => [item.contentKey, item.content]))
      return cache
    }
    } catch (e) {
      // 失败不缓存，后续页面仍可重新请求。
    }
    return {}
  })()
  try { return await pending } finally { pending = null }
}

// 取值：优先后台配置，其次默认值
export function pick(content, key, fallback) {
  const v = content && content[key]
  return (v === undefined || v === null || v === '') ? fallback : v
}
