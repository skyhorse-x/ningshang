import axios from 'axios'

const request = axios.create({ baseURL: '/api', timeout: 10000 })

// 请求拦截：自动附带管理端 JWT Token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('admin_token')
  if (token) config.headers.Authorization = 'Bearer ' + token
  return config
})

request.interceptors.response.use(
  response => {
    const payload = response.data
    if (payload && payload.code !== undefined && payload.code !== 200) {
      const error = new Error(payload.message || '请求失败')
      error.response = response
      return Promise.reject(error)
    }
    return payload
  },
  error => {
    // 登录过期：清除 token 并跳转登录页
    if (error.response && error.response.status === 401 && window.location.pathname.startsWith('/ningshang-admin')) {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_user')
      if (!window.location.pathname.endsWith('/login')) {
        window.location.href = '/ningshang-admin/login'
      }
    }
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default {
  // Home
  getHome: () => request.get("/home", { params: { _t: Date.now() } }),

  // News
  getNews: () => request.get("/news", { params: { _t: Date.now() } }),
  getNewsDetail: (id) => request.get("/news/" + encodeURIComponent(id), { params: { _t: Date.now() } }),

  // About
  getTeam: () => request.get("/team", { params: { _t: Date.now() } }),
  getHonors: () => request.get("/honors", { params: { _t: Date.now() } }),
  getMilestones: () => request.get("/milestones", { params: { _t: Date.now() } }),
  getContent: () => request.get("/content", { params: { _t: Date.now() } }),

  // Contact
  getJobs: () => request.get("/jobs", { params: { _t: Date.now() } }),
  getSubsidiaries: () => request.get("/subsidiaries", { params: { _t: Date.now() } }),
  getCoreBusinesses: () => request.get("/core-businesses", { params: { _t: Date.now() } }),
  submitMessage: (data) => request.post("/messages", data),

  // Admin Auth（用户名由后端从 JWT 解析，前端不传）
  adminLogin: (data) => request.post('/admin/login', data),
  adminLogout: () => request.post('/admin/logout'),
  adminChangePassword: (data) => request.post('/admin/password', data),

  // Admin CRUD（r: news / jobs / messages / team / honors / milestones / content）
  adminList: (r, params) => request.get(`/admin/${r}`, { params }),
  adminNewsPage: (params) => request.get('/admin/news', { params }),
  adminCreate: (r, data) => request.post(`/admin/${r}`, data),
  adminUpdate: (r, id, data) => request.put(`/admin/${r}/${id}`, data),
  adminDelete: (r, id) => request.delete(`/admin/${r}/${id}`),
  adminBatchDelete: (r, ids) => request.post(`/admin/${r}/batch-delete`, ids),

  // 图片上传（multipart，返回 { url: '/uploads/xxx.png' }）
  adminUpload: (file) => {
    const fd = new FormData()
    fd.append('file', file)
    return request.post('/admin/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
  },

  // 管理员账号管理（仅 SUPER_ADMIN）
  adminListAdmins: () => request.get('/admin/admins'),
  adminCreateAdmin: (data) => request.post('/admin/admins', data),
  adminUpdateAdmin: (id, data) => request.put(`/admin/admins/${id}`, data),
  adminDeleteAdmin: (id) => request.delete(`/admin/admins/${id}`),

  // 富文本图片上传（与 adminUpload 同一接口）
  adminUploadImage: (file) => {
    const fd = new FormData()
    fd.append('file', file)
    return request.post('/admin/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
  },

  // ==================== 菜单管理（仅超级管理员） ====================
  adminMenuTree: () => request.get('/admin/menus'),
  adminMenuFlat: () => request.get('/admin/menus/flat'),
  adminMenuMine: () => request.get('/admin/menus/mine'),
  adminMenuCreate: (data) => request.post('/admin/menus', data),
  adminMenuUpdate: (id, data) => request.put(`/admin/menus/${id}`, data),
  adminMenuDelete: (id) => request.delete(`/admin/menus/${id}`),
  adminMenuBatchDelete: (ids) => request.post('/admin/menus/batch-delete', ids),

  // ==================== 权限组管理（仅超级管理员） ====================
  adminGroupList: () => request.get('/admin/groups'),
  adminGroupCreate: (data) => request.post('/admin/groups', data),
  adminGroupUpdate: (id, data) => request.put(`/admin/groups/${id}`, data),
  adminGroupDelete: (id) => request.delete(`/admin/groups/${id}`),
  adminGroupMenus: (id) => request.get(`/admin/groups/${id}/menus`),
  adminGroupSetMenus: (id, menuIds) => request.put(`/admin/groups/${id}/menus`, menuIds),
  adminPermissionList: () => request.get('/admin/permissions'),
  adminPermissionMine: () => request.get('/admin/permissions/mine'),
  adminGroupPermissions: (id) => request.get(`/admin/groups/${id}/permissions`),
  adminGroupSetPermissions: (id, ids) => request.put(`/admin/groups/${id}/permissions`, ids),
}
