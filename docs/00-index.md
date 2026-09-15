# 安徽宁商科技集团官方网站 — 开发蓝图

> 项目代号: NINGSHOW / 版本: 1.0 / 生成日期: 2026-01-18 / 基于 ai.md 蓝图规范

## 目录（Table of Contents）

| 编号 | 文件 | 说明 |
|------|------|------|
| 01 | [PRD](01-prd.md) | 产品需求文档 |
| 02 | [项目总体分析](02-overview.md) | 定位/目标/角色/风险 |
| 03 | [系统架构设计](03-architecture.md) | 架构图、ADR、模块依赖 |
| 04 | [数据库设计](04-database.md) | ER 图、表结构、索引 |
| 05 | [API 设计](05-api.md) | 接口列表、OpenAPI 规范 |
| 06 | [前端设计 - 官网](06-frontend-web.md) | 页面/组件/路由/状态 |
| 07 | [前端设计 - 后台](07-frontend-admin.md) | 管理后台设计 |
| 08 | [后端设计](08-backend.md) | 分层、中间件、缓存 |
| 09 | [核心业务流程](09-business-flow.md) | 流程图、时序图 |
| 10 | [安全设计](10-security.md) | RBAC、注入防护、限流 |
| 11 | [性能设计](11-performance.md) | 缓存策略、容量规划 |
| 12 | [测试设计](12-test.md) | 单元/E2E/压测/覆盖率 |
| 13 | [开发路线图](13-roadmap.md) | Epic/Phase/里程碑 |
| 14 | [编码规范](14-standards.md) | 目录/命名/Git Flow |
| 15 | [DevOps](15-devops.md) | Docker/CI CD/监控 |
| 16 | [蓝图自检](16-self-check.md) | 完整性报告 |

## 阅读顺序

1. PRD → 2. 总体分析 → 3. 架构 → 4. 数据库 → 5. API
2. 前端设计 → 7. 后端设计 → 8. 业务流程
3. 安全/性能/测试 → 11. 路线图 → 12. 规范
4. DevOps → 14. 自检

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 2.7 + JPA + Thymeleaf |
| 前端 | Vue 3 + Element Plus + Vite + Vue Router |
| 数据库 | MySQL 8.0 |
| 缓存 | Redis (预留) |
| 部署 | Docker + Docker Compose |

## 交叉引用

- 数据库表结构 → [04-database.md](04-database.md)
- REST API 列表 → [05-api.md](05-api.md)
- 前端路由 → [06-frontend-web.md](06-frontend-web.md)
- 后端分层 → [08-backend.md](08-backend.md)
- 安全 RBAC → [10-security.md](10-security.md)

## 变更记录

| 版本 | 日期 | 变更人 | 变更说明 |
|------|------|--------|----------|
| 1.0 | 2026-01-18 | AI | 初版蓝图 |
