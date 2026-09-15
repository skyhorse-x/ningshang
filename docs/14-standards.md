# 编码规范

## 1. 目录规范

### 1.1 后端目录规范

后端代码按以下结构组织：启动类放在根目录，配置类在 config 包下，控制器在 controller 包下，服务层在 service 包下，数据访问层在 repository 包下，实体类在 entity 包下，数据传输对象在 dto 包下，视图对象在 vo 包下，异常处理在 exception 包下，工具类在 util 包下。

### 1.2 前端目录规范

前端代码按以下结构组织：入口文件 main.js 和根组件 App.vue 放在 src 目录下，路由配置在 router 包下，API 封装在 api 包下，页面组件在 views 包下（按模块分子目录），组件在 components 包下（布局组件和业务组件分开），样式文件在 styles 包下，状态管理在 stores 包下，工具函数在 utils 包下。

## 2. 命名规范

### 2.1 Java 命名

- 类名使用大驼峰命名法，例如 NewsController
- 方法名使用小驼峰命名法，例如 findNewsById
- 常量使用全大写下划线命名法，例如 MAX_PAGE_SIZE
- 包名使用全小写，例如 com.ningshang.service

### 2.2 Vue 命名

- 组件文件使用大驼峰命名法，例如 HeroCarousel.vue
- 页面文件使用大驼峰命名法，例如 HomeIndex.vue
- 路由名使用小驼峰命名法，例如 homeIndex
- 事件名使用小驼峰命名法，例如 handleClick

### 2.3 数据库命名

- 表名使用小写下划线命名法，例如 news, team_member
- 字段名使用小写下划线命名法，例如 title, created_at
- 索引命名为 idx_字段名，例如 idx_category
- 唯一索引命名为 uk_字段名，例如 uk_news_id

## 3. Git Flow

### 3.1 分支模型

- main 分支：生产环境代码
- develop 分支：开发环境代码
- feature/* 分支：功能开发分支
- hotfix/* 分支：紧急修复分支
- release/* 分支：发布分支

### 3.2 Commit 规范

提交信息格式为：type(scope): subject

type 可选值：feat（新功能）、fix（修复）、docs（文档）、style（格式）、refactor（重构）、test（测试）、chore（构建/工具）

示例：
- feat(news): add news list API
- fix(contact): fix message form validation
- docs(readme): update installation guide

## 4. 注释规范

### 4.1 Java 注释

类注释包含作者和日期信息，方法注释说明参数和返回值。关键业务逻辑需要添加行内注释。

### 4.2 Vue 注释

组件顶部添加组件说明注释，复杂逻辑添加行内注释。

## 5. 异常处理规范

### 5.1 后端异常

使用自定义业务异常类 BusinessException，全局异常处理器捕获并返回统一错误响应。

### 5.2 前端异常

API 调用使用 try/catch 捕获异常，用户友好的错误提示通过 ElMessage 组件展示。

## 6. 日志规范

- 使用 SLF4J + Logback 日志框架
- 关键操作记录 INFO 级别日志
- 异常信息记录 ERROR 级别日志并包含堆栈
- 调试信息使用 DEBUG 级别
- 生产环境默认 INFO 级别，开发环境使用 DEBUG 级别
