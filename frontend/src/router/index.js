import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', component: () => import('@/views/HomeIndex.vue'), meta: { title: '安徽宁商科技集团有限公司' } },

  { path: '/about-intro', redirect: '/about/intro' },
  { path: '/about-speech', redirect: '/about/speech' },
  { path: '/about-events', redirect: '/about/events' },
  { path: '/about-team', redirect: '/about/team' },
  { path: '/about-honor', redirect: '/about/honor' },
  { path: '/about-party', redirect: '/about/party' },
  { path: '/about-culture', redirect: '/about/culture' },
  { path: '/recruit-job', redirect: '/recruit/jobs' },
  { path: '/news-detail', redirect: to => ({ path: '/news/' + (to.query.id || '') }) },
  { path: '/about/intro', component: () => import('@/views/about/AboutIntro.vue'), meta: { title: '集团简介' } },
  { path: '/about/speech', component: () => import('@/views/about/AboutSpeech.vue'), meta: { title: '董事长致词' } },
  { path: '/about/events', component: () => import('@/views/about/AboutEvents.vue'), meta: { title: '发展大事记' } },
  { path: '/about/team', component: () => import('@/views/about/AboutTeam.vue'), meta: { title: '管理团队' } },
  { path: '/about/honor', component: () => import('@/views/about/AboutHonor.vue'), meta: { title: '企业荣誉' } },
  { path: '/about/party', component: () => import('@/views/about/AboutParty.vue'), meta: { title: '党建工作' } },
  { path: '/about/culture', component: () => import('@/views/about/AboutCulture.vue'), meta: { title: '企业文化' } },
  { path: '/news', component: () => import('@/views/news/NewsList.vue'), meta: { title: '新闻中心' } },
  { path: '/news/:id', component: () => import('@/views/news/NewsDetail.vue'), meta: { title: '新闻详情' }, props: true },
  { path: '/industry', component: () => import('@/views/industry/IndustryOverview.vue'), meta: { title: '集团产业' } },
  { path: '/industry/:id(\\d+)', component: () => import('@/views/industry/IndustryDetail.vue'), meta: { title: '成员企业' }, props: true },
  { path: '/industry/construction', redirect: '/industry' },
  { path: '/industry/software', redirect: '/industry' },
  { path: '/contact', component: () => import('@/views/contact/ContactInfo.vue'), meta: { title: '联系方式' } },
  { path: '/contact/message', component: () => import('@/views/contact/ContactMessage.vue'), meta: { title: '在线留言' } },
  { path: '/contact-message', redirect: '/contact/message' },
  { path: '/recruit', component: () => import('@/views/contact/RecruitConcept.vue'), meta: { title: '人才理念' } },
  { path: '/recruit/jobs', component: () => import('@/views/contact/RecruitJobs.vue'), meta: { title: '招聘岗位' } },
  // Admin routes
  { path: '/ningshang-admin/login', component: () => import('@/views/admin/Login.vue'), meta: { title: '管理员登录', layout: 'blank' } },
  { path: '/ningshang-admin', component: () => import('@/views/admin/AdminLayout.vue'), meta: { requiresAuth: true, layout: 'admin' },
    children: [
      { path: '', redirect: '/ningshang-admin/dashboard' },
      { path: 'dashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '控制台' } },
      { path: 'news', component: () => import('@/views/admin/NewsManage.vue'), meta: { title: '新闻管理' } },
      { path: 'team', component: () => import('@/views/admin/TeamManage.vue'), meta: { title: '团队管理' } },
      { path: 'honors', component: () => import('@/views/admin/HonorManage.vue'), meta: { title: '荣誉管理' } },
      { path: 'partners', component: () => import('@/views/admin/PartnerManage.vue'), meta: { title: '合作伙伴' } },
      { path: 'milestones', component: () => import('@/views/admin/MilestoneManage.vue'), meta: { title: '大事记管理' } },
      { path: 'jobs', component: () => import('@/views/admin/JobManage.vue'), meta: { title: '招聘管理' } },
      { path: 'messages', component: () => import('@/views/admin/MessageManage.vue'), meta: { title: '留言管理' } },
      { path: 'content', redirect: '/ningshang-admin/content/intro' },
      { path: 'content/intro', component: () => import('@/views/admin/ContentManage.vue'), props: { section: 'intro' }, meta: { title: '集团简介' } },
      { path: 'content/speech', component: () => import('@/views/admin/ContentManage.vue'), props: { section: 'speech' }, meta: { title: '董事长致词' } },
      { path: 'content/culture', component: () => import('@/views/admin/ContentManage.vue'), props: { section: 'culture' }, meta: { title: '企业文化' } },
      { path: 'content/party', component: () => import('@/views/admin/ContentManage.vue'), props: { section: 'party' }, meta: { title: '党建工作' } },
      { path: 'content/recruit', component: () => import('@/views/admin/ContentManage.vue'), props: { section: 'recruit' }, meta: { title: '人才理念' } },
      { path: 'subsidiaries', component: () => import('@/views/admin/SubsidiaryManage.vue'), meta: { title: '子公司管理' } },
      { path: 'chatline', component: () => import('@/views/admin/ContentManage.vue'), props: { mode: 'settings' }, meta: { title: '联系方式' } },
      { path: 'site-settings', component: () => import('@/views/admin/ContentManage.vue'), props: { mode: 'settings' }, meta: { title: '网站设置' } },
      { path: 'menus', component: () => import('@/views/admin/MenuManage.vue'), meta: { title: '菜单管理' } },
      { path: 'groups', component: () => import('@/views/admin/GroupManage.vue'), meta: { title: '管理员分组' } },
      { path: 'admins', component: () => import('@/views/admin/AdminManage.vue'), meta: { title: '管理员账号' } },
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() { return { top: 0 } }
})

// Auth guard — 仅检查登录态，菜单权限由动态侧边栏 + 后端拦截器控制
router.beforeEach((to, from, next) => {
  document.title = to.meta?.title ? to.meta.title + ' - 安徽宁商科技集团有限公司' : '安徽宁商科技集团有限公司'
  if (to.meta?.requiresAuth) {
    const token = localStorage.getItem('admin_token')
    if (!token) {
      next('/ningshang-admin/login')
      return
    }
  }
  next()
})

export default router
