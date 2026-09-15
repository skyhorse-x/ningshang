# 前端设计 - 后台管理

## 1. 概述

后台管理用于管理官网内容（新闻、留言、招聘信息等），使用 Vue 3 + Element Plus 构建。

> **注意**：后台管理为 Phase 2 实施内容，Phase 1 仅预留接口和页面框架。

## 2. 页面清单

| 路由 | 页面组件 | 说明 |
|------|----------|------|
| /admin | AdminLayout | 后台布局 |
| /admin/dashboard | Dashboard | 控制台 |
| /admin/news | NewsManage | 新闻管理 |
| /admin/news/create | NewsCreate | 新建新闻 |
| /admin/news/:id/edit | NewsEdit | 编辑新闻 |
| /admin/messages | MessageManage | 留言管理 |
| /admin/jobs | JobManage | 招聘管理 |
| /admin/subsidiaries | SubsidiaryManage | 子公司管理 |

## 3. 布局设计

```
+--------------------------------------------------+
| Admin Header（顶部 Logo + 用户信息）              |
+--------+-----------------------------------------+
|        |                                         |
| Side   |        Main Content Area                |
| Bar    |                                         |
|        |                                         |
+--------+-----------------------------------------+
```

## 4. 功能设计

### 4.1 新闻管理
- 列表展示（分页、搜索、分类筛选）
- 新建/编辑/删除
- 富文本编辑器（Quill）
- 封面图上传

### 4.2 留言管理
- 列表展示
- 标记已读/未读
- 删除

### 4.3 招聘管理
- 列表展示
- 新建/编辑/删除
- 状态控制（启用/禁用）

## 5. 权限设计

| 角色 | 新闻管理 | 留言管理 | 招聘管理 | 子公司管理 |
|------|----------|----------|----------|------------|
| 超级管理员 | ✅ | ✅ | ✅ | ✅ |
| 编辑 | ✅ | ✅ | ✅ | ❌ |
| 访客 | 只读 | 只读 | 只读 | 只读 |

## 6. API 设计（后台专用）

| API | 方法 | 说明 |
|-----|------|------|
| /api/admin/news | GET | 新闻列表（分页） |
| /api/admin/news | POST | 创建新闻 |
| /api/admin/news/{id} | PUT | 更新新闻 |
| /api/admin/news/{id} | DELETE | 删除新闻 |
| /api/admin/messages | GET | 留言列表 |
| /api/admin/messages/{id} | DELETE | 删除留言 |
| /api/admin/jobs | GET | 岗位列表 |
| /api/admin/jobs | POST | 创建岗位 |
| /api/admin/jobs/{id} | PUT | 更新岗位 |
| /api/admin/jobs/{id} | DELETE | 删除岗位 |

## 7. 待确认事项

- [ ] 是否需要富文本编辑器？
- [ ] 是否需要图片上传至对象存储？
- [ ] 是否需要操作日志？
