/**
 * Format date to YYYY-MM
 */
export function formatDate(date) {
  if (!date) return ''
  const d = new Date(date)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  return y + '-' + m
}

/**
 * Truncate text with ellipsis
 */
export function truncate(text, length = 100) {
  if (!text) return ''
  return text.length > length ? text.slice(0, length) + '...' : text
}

/**
 * Get asset URL
 */
export function asset(path) {
  return path.startsWith('http') ? path : '/' + path.replace(/^\//, '')
}
