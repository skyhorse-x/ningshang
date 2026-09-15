# 前端设计 - 官网

## 1. 页面清单

| 路由 | 页面组件 | 标题 | 说明 |
|------|----------|------|------|
| / | HomeIndex | 安徽宁商科技集团有限公司 | 首页 |
| /about/intro | AboutIntro | 集团简介 | 集团概况子页 |
| /about/speech | AboutSpeech | 董事长致词 | 集团概况子页 |
| /about/events | AboutEvents | 发展大事记 | 集团概况子页 |
| /about/team | AboutTeam | 管理团队 | 集团概况子页 |
| /about/honor | AboutHonor | 企业荣誉 | 集团概况子页 |
| /about/party | AboutParty | 党建工作 | 集团概况子页 |
| /about/culture | AboutCulture | 企业文化 | 集团概况子页 |
| /news | NewsList | 新闻中心 | 新闻列表 |
| /news/:id | NewsDetail | 新闻详情 | 新闻详情 |
| /industry | IndustryOverview | 集团产业 | 产业概览 |
| /industry/construction | IndustryConstruction | 建筑工程 | 产业子页 |
| /industry/software | IndustrySoftware | 软件科技 | 产业子页 |
| /contact | ContactInfo | 联系方式 | 联系方式 |
| /contact/message | ContactMessage | 在线留言 | 留言表单 |
| /recruit | RecruitConcept | 人才理念 | 人才招聘子页 |
| /recruit/jobs | RecruitJobs | 招聘岗位 | 招聘岗位列表 |

## 2. 菜单结构

```
首页 (HOME)
集团概况 (ABOUT US)
  ├── 集团简介
  ├── 董事长致词
  ├── 发展大事记
  ├── 管理团队
  ├── 企业荣誉
  ├── 党建工作
  └── 企业文化
新闻中心 (NEWS)
  └── 全部新闻
集团产业 (INDUSTRY)
  ├── 建筑工程
  └── 软件科技
联系宁商 (CONTACT)
  ├── 人才理念
  ├── 招聘岗位
  ├── 联系方式
  └── 在线留言
```

## 3. 布局设计

### 3.1 整体布局

```
+--------------------------------------------------+
| TopBar（顶部联系信息条）                          |
+--------------------------------------------------+
| Logo | Nav Menu                        | 导航高亮 |
+--------------------------------------------------+
|                                                  |
|              Page Content（页面内容）             |
|                                                  |
+--------------------------------------------------+
| Footer（页脚）                                    |
+--------------------------------------------------+
```

### 3.2 子页面布局

```
+--------------------------------------------------+
| TopBar                                          |
+--------------------------------------------------+
| Nav Bar                                         |
+--------------------------------------------------+
| Page Banner（页面横幅 + 背景图）                  |
+--------------------------------------------------+
| Sub Nav（二级子导航）                            |
+--------------------------------------------------+
|                                                  |
|              Page Content                        |
|                                                  |
+--------------------------------------------------+
| Footer                                          |
+--------------------------------------------------+
```

## 4. 组件设计

### 4.1 布局组件

| 组件名 | 说明 | Element Plus 组件 |
|--------|------|-------------------|
| AppLayout | 整体布局容器 | el-container |
| TopBar | 顶部联系信息条 | 自定义 div |
| NavBar | 主导航（含下拉） | el-menu (horizontal) |
| PageBanner | 页面横幅 | 自定义 div + bg-image |
| SubNav | 二级子导航 | el-menu (horizontal) |
| SiteFooter | 页脚 | el-row, el-col |

### 4.2 业务组件

| 组件名 | 说明 | Element Plus 组件 |
|--------|------|-------------------|
| HeroCarousel | 首页轮播 | el-carousel |
| NewsSection | 新闻区块 | el-card, el-tabs |
| IndustryGrid | 产业卡片 | el-row, el-col, el-card |
| TeamGrid | 团队展示 | el-row, el-col, el-card |
| HonorGrid | 荣誉展示 | el-row, el-col, el-card |
| TimelineSection | 时间轴 | el-timeline |
| JobList | 招聘列表 | el-card, el-tag |
| ContactForm | 联系表单 | el-form, el-input, el-button |
| MessageForm | 留言表单 | el-form, el-input, el-button |

## 5. 路由配置

```javascript
const routes = [
  { path: '/', component: HomeIndex, meta: { title: '安徽宁商科技集团有限公司' } },
  { path: '/about/intro', component: AboutIntro, meta: { title: '集团简介' } },
  { path: '/about/speech', component: AboutSpeech, meta: { title: '董事长致词' } },
  { path: '/about/events', component: AboutEvents, meta: { title: '发展大事记' } },
  { path: '/about/team', component: AboutTeam, meta: { title: '管理团队' } },
  { path: '/about/honor', component: AboutHonor, meta: { title: '企业荣誉' } },
  { path: '/about/party', component: AboutParty, meta: { title: '党建工作' } },
  { path: '/about/culture', component: AboutCulture, meta: { title: '企业文化' } },
  { path: '/news', component: NewsList, meta: { title: '新闻中心' } },
  { path: '/news/:id', component: NewsDetail, meta: { title: '新闻详情' } },
  { path: '/industry', component: IndustryOverview, meta: { title: '集团产业' } },
  { path: '/industry/construction', component: IndustryConstruction, meta: { title: '建筑工程' } },
  { path: '/industry/software', component: IndustrySoftware, meta: { title: '软件科技' } },
  { path: '/contact', component: ContactInfo, meta: { title: '联系方式' } },
  { path: '/contact/message', component: ContactMessage, meta: { title: '在线留言' } },
  { path: '/recruit', component: RecruitConcept, meta: { title: '人才理念' } },
  { path: '/recruit/jobs', component: RecruitJobs, meta: { title: '招聘岗位' } }
];
```

## 6. 状态管理

使用 Pinia 管理全局状态：

| Store | 说明 | State |
|-------|------|-------|
| navStore | 导航状态 | currentPage, activeSubNav |
| newsStore | 新闻数据 | newsList, currentNews |

## 7. 主题设计

### 7.1 Element Plus 主题覆盖

```css
:root {
  --el-color-primary: #0d3a72;
  --el-color-primary-light-3: #1e5aa8;
  --el-color-primary-light-5: #3a7fd5;
  --el-color-primary-light-7: #6aa3e8;
  --el-color-primary-light-9: #b5d4f5;
  --el-color-primary-dark-2: #0a2c57;
}
```

### 7.2 品牌色板

| 变量 | 色值 | 用途 |
|------|------|------|
| --c-primary | #0d3a72 | 主色深蓝 |
| --c-primary-dark | #0a2c57 | 深蓝（顶栏/页脚） |
| --c-primary-light | #1e5aa8 | 浅蓝（子导航） |
| --c-accent | #c8a45c | 金色点缀 |
| --c-accent-dark | #a9863c | 金色深（悬停） |
| --c-red | #b3241f | 党建红 |

## 8. 响应式设计

| 断点 | 设备 | 宽度 |
|------|------|------|
| xs | 手机 | < 576px |
| sm | 平板 | >= 576px |
| md | 桌面 | >= 768px |
| lg | 大桌面 | >= 992px |
| xl | 超大屏 | >= 1200px |

关键响应式处理：
- 导航栏在移动端折叠为汉堡菜单
- 产业卡片网格 5列 -> 2列 -> 1列
- 轮播图高度自适应
- 字体大小按比例缩放

## 9. API 调用关系

| 页面 | API | 说明 |
|------|-----|------|
| HomeIndex | GET /api/home | 首页聚合数据 |
| NewsList | GET /api/news | 新闻列表 |
| NewsDetail | GET /api/news/{id} | 新闻详情 |
| AboutTeam | GET /api/team | 团队数据 |
| AboutHonor | GET /api/honors | 荣誉数据 |
| AboutEvents | GET /api/milestones | 里程碑数据 |
| IndustryOverview | GET /api/subsidiaries | 子公司数据 |
| RecruitJobs | GET /api/jobs | 招聘岗位 |
| ContactMessage | POST /api/messages | 提交留言 |
