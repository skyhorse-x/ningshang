# 系统架构设计

## 1. 整体架构

系统采用经典的前后端分离架构：

- **前端**：Vue 3 SPA，通过 REST API 与后端交互
- **后端**：Spring Boot Monolith，提供 REST API + 页面渲染
- **数据库**：MySQL 8.0 持久化存储
- **缓存**：Redis（预留，Phase 2 实现）

## 2. 技术栈选型

### 2.1 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.7.x | 后端框架 |
| Spring Data JPA | 2.7.x | ORM 数据访问 |
| Spring Web | 2.7.x | Web MVC |
| Thymeleaf | 2.7.x | 服务端模板（预留） |
| MySQL Connector | 8.0 | 数据库驱动 |
| Lombok | 1.18.x | 代码简化 |

### 2.2 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | 3.x | 前端框架 |
| Element Plus | 2.x | UI 组件库 |
| Vite | 4.x | 构建工具 |
| Vue Router | 4.x | 路由管理 |
| Axios | 1.x | HTTP 请求 |
| Pinia | 2.x | 状态管理 |

### 2.3 数据库

| 技术 | 版本 | 用途 |
|------|------|------|
| MySQL | 8.0 | 关系型数据库 |

## 3. 架构决策记录（ADR）

### ADR-001：为什么选择 Spring Boot + JPA？
- **决策**：使用 Spring Boot + Spring Data JPA 作为后端 ORM
- **理由**：快速开发、生态成熟、与 MySQL 集成良好
- **替代方案**：MyBatis（灵活性更高但开发效率低）

### ADR-002：为什么选择 Vue 3 + Element Plus？
- **决策**：前端使用 Vue 3 + Element Plus
- **理由**：组件丰富、主题定制方便、中文社区活跃
- **替代方案**：React + Ant Design（生态更大但学习曲线陡）

### ADR-003：为什么选择 Monolith 而非 Microservice？
- **决策**：采用单体架构
- **理由**：官网功能相对简单，单体部署维护成本低
- **替代方案**：Microservice（适用于复杂业务场景）

### ADR-004：为什么使用 REST API？
- **决策**：前后端通过 REST API 交互
- **理由**：标准化、易调试、前后端可并行开发
- **替代方案**：GraphQL（灵活性更高但复杂度高）

## 4. 模块依赖

| 模块 | 依赖 | 说明 |
|------|------|------|
| 前端 Vue 应用 | 后端 REST API | 数据获取 |
| 后端 Controller | Service 层 | 业务逻辑 |
| Service 层 | Repository 层 | 数据访问 |
| Repository 层 | MySQL 数据库 | 持久化 |

## 5. 目录结构

### 5.1 后端目录

```
src/main/java/com/ningshang/
├── NingshangApplication.java
├── config/          # 配置类
├── controller/      # 控制器
├── service/         # 服务层
├── repository/      # 数据访问
├── entity/          # 实体类
├── dto/             # 数据传输
└── exception/       # 异常处理
```

### 5.2 前端目录

```
frontend/
├── src/
│   ├── main.js          # 入口
│   ├── App.vue          # 根组件
│   ├── router/          # 路由
│   ├── api/             # API 封装
│   ├── views/           # 页面
│   ├── components/      # 组件
│   ├── styles/          # 样式
│   └── stores/          # 状态管理
└── public/
    └── images/          # 图片资源
```

## 6. 高可用方案

- **Phase 1**：单体部署，Docker Compose
- **Phase 2**：Nginx 反向代理 + 负载均衡
- **Phase 3**：K8s 集群部署
