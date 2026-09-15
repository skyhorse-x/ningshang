export function hasPermission(code) {
  try {
    const user = JSON.parse(localStorage.getItem('admin_user') || '{}')
    return Number(user.groupId) === 1 || (user.permissions || []).includes(code)
  } catch (_) { return false }
}
