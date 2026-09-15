# 数据库设计

## 1. ER 关系图

系统包含 7 张核心表：

- **news**（新闻表）— 新闻中心文章
- **subsidiary**（子公司表）— 集团成员企业
- **team_member**（团队成员表）— 管理团队
- **honor**（荣誉表）— 企业荣誉
- **milestone**（里程碑表）— 发展大事记
- **job**（岗位表）— 招聘信息
- **message**（留言表）— 在线留言

## 2. 表结构设计

### 2.1 news（新闻表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| title | VARCHAR(200) | NOT NULL | 新闻标题 |
| summary | VARCHAR(500) | | 摘要 |
| body | TEXT | | 正文（HTML） |
| category | VARCHAR(50) | INDEX | 分类标识 |
| category_name | VARCHAR(50) | | 分类名称 |
| date | VARCHAR(50) | | 发布日期 |
| author | VARCHAR(100) | | 作者 |
| source | VARCHAR(100) | | 来源 |
| image | VARCHAR(200) | | 封面图路径 |
| news_id | VARCHAR(50) | UNIQUE | 业务唯一标识 |
| created_at | DATETIME | DEFAULT NOW() | 创建时间 |

**索引：**
- PRIMARY KEY (id)
- UNIQUE INDEX uk_news_id (news_id)
- INDEX idx_category (category)
- INDEX idx_created_at (created_at)

### 2.2 subsidiary（子公司表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 公司名称 |
| english_name | VARCHAR(100) | | 英文名称 |
| category | VARCHAR(50) | | 业务类别 |
| description | VARCHAR(500) | | 公司简介 |
| logo | VARCHAR(200) | | Logo 图片路径 |
| background | VARCHAR(200) | | 背景图片路径 |
| sort_order | INT | DEFAULT 0 | 排序权重 |

**索引：**
- PRIMARY KEY (id)
- INDEX idx_sort_order (sort_order)

### 2.3 team_member（团队成员表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| position | VARCHAR(100) | | 职位 |
| description | VARCHAR(500) | | 个人简介 |
| avatar | VARCHAR(200) | | 头像路径 |
| gradient | VARCHAR(50) | | 渐变背景色 |
| sort_order | INT | DEFAULT 0 | 排序权重 |

### 2.4 honor（企业荣誉表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| title | VARCHAR(100) | NOT NULL | 荣誉名称 |
| description | VARCHAR(500) | | 描述 |
| icon | VARCHAR(50) | | 图标（emoji） |
| image | VARCHAR(200) | | 背景图片路径 |
| sort_order | INT | DEFAULT 0 | 排序权重 |

### 2.5 milestone（里程碑表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| year | VARCHAR(20) | NOT NULL | 发生时间 |
| title | VARCHAR(100) | NOT NULL | 事件标题 |
| description | VARCHAR(500) | | 事件描述 |
| sort_order | INT | DEFAULT 0 | 排序权重 |

### 2.6 job（招聘岗位表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| title | VARCHAR(100) | NOT NULL | 岗位名称 |
| department | VARCHAR(50) | | 所属部门 |
| headcount | VARCHAR(50) | | 招聘人数 |
| location | VARCHAR(50) | | 工作地点 |
| education | VARCHAR(50) | | 学历要求 |
| description | VARCHAR(500) | | 岗位描述 |
| sort_order | INT | DEFAULT 0 | 排序权重 |

### 2.7 message（留言表）

| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| name | VARCHAR(50) | NOT NULL | 姓名 |
| phone | VARCHAR(20) | NOT NULL | 联系电话 |
| email | VARCHAR(100) | | 电子邮箱 |
| type | VARCHAR(50) | | 留言类型 |
| content | TEXT | NOT NULL | 留言内容 |
| created_at | DATETIME | DEFAULT NOW() | 创建时间 |

## 3. 数据库规范

- 字符集：utf8mb4
- 排序规则：utf8mb4_unicode_ci
- 存储引擎：InnoDB
- 所有表包含 created_at 字段
- 所有 VARCHAR 字段需指定合理长度
- 索引命名：idx_字段名
- 唯一索引命名：uk_字段名

## 4. 初始化脚本

```sql
CREATE DATABASE IF NOT EXISTS ningshang
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ningshang;
```

完整初始化脚本见 `sql/init.sql`
