# DevOps

## 1. Docker 部署

### 1.1 Dockerfile（后端）

后端使用 OpenJDK 11 基础镜像，暴露 8080 端口，通过 Maven 打包后复制 JAR 文件到容器中启动。

### 1.2 Dockerfile（前端）

前端使用 Node 16 基础镜像构建 Nginx 服务，将构建产物复制到 Nginx 静态资源目录。

### 1.3 Docker Compose

使用 Docker Compose 编排三个服务：
- ningshang-app：后端 Spring Boot 应用
- ningshang-web：前端 Nginx 静态服务
- ningshang-db：MySQL 8.0 数据库

## 2. CI/CD

### 2.1 持续集成

代码提交到 develop 分支时自动触发：
- 代码检查（Checkstyle / ESLint）
- 单元测试
- 构建镜像

### 2.2 持续部署

合并到 main 分支时自动触发：
- 运行全部测试
- 构建生产镜像
- 部署到生产环境

## 3. 环境配置

### 3.1 开发环境

- 后端：localhost:8080
- 前端：localhost:5173
- 数据库：localhost:3306

### 3.2 生产环境

- 通过 Docker Compose 统一部署
- 使用 Nginx 反向代理
- 配置 HTTPS

## 4. 监控与告警

### 4.1 应用监控

使用 Spring Boot Actuator 暴露健康检查端点：
- /actuator/health：健康检查
- /actuator/metrics：应用指标

### 4.2 日志收集

- 后端日志输出到 stdout
- Docker 日志驱动收集
- 生产环境使用 ELK 集中管理

## 5. 备份恢复

### 5.1 数据库备份

- 每日自动备份
- 保留 7 天备份
- 备份文件存储到对象存储

### 5.2 恢复流程

- 停止应用服务
- 执行恢复脚本
- 验证数据完整性
- 重启应用服务
