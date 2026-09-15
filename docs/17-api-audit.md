# 接口检查与修复记录（2026-09-15）

## 后台信息架构与富文本（后续补充）

- 左侧导航调整为：内容运营（新闻、页面内容）、企业资料（子公司、团队、荣誉、大事记）、人才与互动（招聘、留言）、系统管理（网站设置、菜单、角色权限、管理员）。
- 页面内容与网站设置使用同一套 `site_content` 数据接口，但按用途分成两个后台入口；增加新页面时可先添加内容键，再在页面通过 `loadContent/pick` 使用。
- 新闻正文、页面正文、招聘描述、团队简介、子公司介绍、荣誉说明、大事记说明均接入统一富文本编辑、服务端清洗和前端安全渲染。
- 富文本数据库列统一为 `LONGTEXT`；手工迁移脚本见 `sql/migrate_richtext.sql`，应用启动也会执行幂等字段升级。
- 建筑工程、软件科技、人才理念三个原硬编码正文已纳入页面内容管理。

## 验证结果

- 61 个 API 路由；61 个已被实际 HTTP 回归覆盖。
- 242 项断言通过，包括数据形状、增删改查、权限、异常状态和持久化结果。
- 17 个后端 HTML 路由通过；29 个前端页面逐页打开，无 JavaScript 异常或失败网络响应。
- 真实浏览器从 http://127.0.0.1:5173 提交登录：HTTP 200，成功进入控制台；跨域预检及错误密码 HTTP 401 也验证通过。
- Maven 打包和 Vite 构建通过。Vite 仍有大文件体积提示，未做性能优化。

## 已修复

1. CORS 白名单缺少 127.0.0.1，导致浏览器登录 POST 被拒绝为 403；补齐本地来源并增加带 Origin 的回归。
2. 留言管理 Vue 插槽多余括号导致构建失败。
3. 业务异常与参数错误曾返回 HTTP 200；现返回对应 400/401/403/404/409/413/415/429/500，前端同时拒绝业务失败响应。
4. 留言及 CRUD 字段缺少有效校验；新增与修改混用客户端 ID、未知 ID 更新可意外创建记录；现保护 ID/创建时间并检查更新目标存在。
5. 菜单权限校验存在普通用户可创建菜单、用菜单路径推导超管的错误；现以数据库权限组判断系统权限，并限制路径边界，检查删除账号与禁用组。
6. 独立网站设置菜单、空菜单分组被过滤；现保留管理树中的分组和独立菜单，处理启用状态及排序。
7. 权限组菜单替换缺少事务、重复 ID 和无效 ID 处理；现去重并在写入前验证。菜单删除同时清理权限关联。
8. 重启强制重置管理员密码；已移除该调用。禁止禁用超管组及降级最后一个超管。
9. 排序字段保存后不生效；相关模块按 sortOrder、ID 排序。
10. 新闻不存在时返回成功；现返回 404。新闻正文清洗保留上传图片相对路径，并防止重复新闻标识。
11. 官网文案并发缓存导致部分组件拿到空配置；失败结果不再永久缓存。新闻详情随路由参数变化重新加载并区分加载失败。
12. 缺少 Thymeleaf head 片段导致后端页面 500；已补齐。图片路径规范化不再破坏外部 URL。
13. 登录未保存 groupId，角色显示错误；现保存。退出调用后端并撤销当前令牌。
14. Maven 声明 Java 8，但代码依赖 Java 11 的 API；已将编译目标改为 Java 11，本机用 JDK 17 构建。

## 当前环境与验证边界

- 前端 http://127.0.0.1:5173；后端 http://127.0.0.1:8080。
- 使用独立 MySQL 127.0.0.1:3307 中的 ningshang 数据副本。原 3306 实例未导入或修改，仍需要其可用密码才能完成原环境联调。
- 原 data.sql 为 UTF-16，导入时生成 .local/data-utf8.sql；未改写原始备份。
- 副本已执行 sql/migrate_transactional_tables.sql，将权限相关表改为 InnoDB，支持事务。原库部署修复时也需在备份后执行该迁移。
- 回归仅删除测试本次创建的记录；测试上传图片保存在 .local/test-uploads。
- 本次不是穷尽安全审计、并发压力测试或生产部署验证。注销撤销表目前在内存中，服务重启会清空。
- 源码修改前备份：.local/audit-before.zip。日志与测试结果在 .local/。

## 复测

使用独立测试数据库启动后端，然后执行（密码由环境变量提供）：

```powershell
$env:API_PASSWORD = '<测试管理员密码>'
node scripts/api-regression.mjs
```

浏览器复测脚本：scripts/browser-login-regression.cjs，需要 playwright-core；可用 PLAYWRIGHT_MODULE 与 CHROME_PATH 指定本机安装。

## 路由覆盖清单

| 方法 | 路由 | 实测覆盖 |
| --- | --- | --- |
| GET | `/about-intro` | 通过 |
| GET | `/about-speech` | 通过 |
| GET | `/about-events` | 通过 |
| GET | `/about-team` | 通过 |
| GET | `/about-honor` | 通过 |
| GET | `/about-party` | 通过 |
| GET | `/about-culture` | 通过 |
| GET | `/api/team` | 通过 |
| GET | `/api/honors` | 通过 |
| GET | `/api/milestones` | 通过 |
| GET | `/api/content` | 通过 |
| GET | `/api/admin/news` | 通过 |
| POST | `/api/admin/news` | 通过 |
| PUT | `/api/admin/news/{id}` | 通过 |
| DELETE | `/api/admin/news/{id}` | 通过 |
| GET | `/api/admin/jobs` | 通过 |
| POST | `/api/admin/jobs` | 通过 |
| PUT | `/api/admin/jobs/{id}` | 通过 |
| DELETE | `/api/admin/jobs/{id}` | 通过 |
| GET | `/api/admin/messages` | 通过 |
| DELETE | `/api/admin/messages/{id}` | 通过 |
| GET | `/api/admin/subsidiaries` | 通过 |
| POST | `/api/admin/subsidiaries` | 通过 |
| PUT | `/api/admin/subsidiaries/{id}` | 通过 |
| DELETE | `/api/admin/subsidiaries/{id}` | 通过 |
| GET | `/api/admin/team` | 通过 |
| POST | `/api/admin/team` | 通过 |
| PUT | `/api/admin/team/{id}` | 通过 |
| DELETE | `/api/admin/team/{id}` | 通过 |
| GET | `/api/admin/honors` | 通过 |
| POST | `/api/admin/honors` | 通过 |
| PUT | `/api/admin/honors/{id}` | 通过 |
| DELETE | `/api/admin/honors/{id}` | 通过 |
| GET | `/api/admin/milestones` | 通过 |
| POST | `/api/admin/milestones` | 通过 |
| PUT | `/api/admin/milestones/{id}` | 通过 |
| DELETE | `/api/admin/milestones/{id}` | 通过 |
| GET | `/api/admin/content` | 通过 |
| POST | `/api/admin/content` | 通过 |
| PUT | `/api/admin/content/{id}` | 通过 |
| DELETE | `/api/admin/content/{id}` | 通过 |
| GET | `/api/admin/admins` | 通过 |
| POST | `/api/admin/admins` | 通过 |
| PUT | `/api/admin/admins/{id}` | 通过 |
| DELETE | `/api/admin/admins/{id}` | 通过 |
| POST | `/api/admin/password` | 通过 |
| POST | `/api/admin/login` | 通过 |
| POST | `/api/admin/logout` | 通过 |
| GET | `/api/admin/groups` | 通过 |
| POST | `/api/admin/groups` | 通过 |
| PUT | `/api/admin/groups/{id}` | 通过 |
| DELETE | `/api/admin/groups/{id}` | 通过 |
| GET | `/api/admin/groups/{id}/menus` | 通过 |
| PUT | `/api/admin/groups/{id}/menus` | 通过 |
| GET | `/api/admin/menus` | 通过 |
| GET | `/api/admin/menus/mine` | 通过 |
| POST | `/api/admin/menus` | 通过 |
| PUT | `/api/admin/menus/{id}` | 通过 |
| DELETE | `/api/admin/menus/{id}` | 通过 |
| POST | `/api/admin/menus/batch-delete` | 通过 |
| GET | `/api/admin/menus/flat` | 通过 |
| GET | `/contact` | 通过 |
| GET | `/contact-message` | 通过 |
| GET | `/recruit` | 通过 |
| GET | `/recruit-job` | 通过 |
| POST | `/api/messages` | 通过 |
| GET | `/api/jobs` | 通过 |
| POST | `/api/admin/upload` | 通过 |
| GET | `/` | 通过 |
| GET | `/api/home` | 通过 |
| GET | `/industry` | 通过 |
| GET | `/industry-construction` | 通过 |
| GET | `/industry-software` | 通过 |
| GET | `/api/subsidiaries` | 通过 |
| GET | `/news` | 通过 |
| GET | `/news-detail` | 通过 |
| GET | `/api/news` | 通过 |
| GET | `/api/news/{newsId}` | 通过 |
