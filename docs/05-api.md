# API 设计

## 1. API 规范

### 1.1 基础信息
- Base URL: `/api`
- 数据格式：JSON
- 字符编码：UTF-8
- 时间格式：YYYY-MM-DD

### 1.2 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

### 1.3 错误码定义

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 2. API 列表

### 2.1 首页相关

#### GET /api/home
获取首页聚合数据

**Response:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "news": [ ],
    "subsidiaries": [ ]
  }
}
```

### 2.2 新闻相关

#### GET /api/news
获取新闻列表

**Query Parameters:**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| category | String | 否 | 分类过滤 |
| page | Integer | 否 | 页码，默认 1 |
| size | Integer | 否 | 每页条数，默认 10 |

**Response:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [ ],
    "total": 0,
    "page": 1,
    "size": 10
  }
}
```

#### GET /api/news/{newsId}
获取新闻详情

**Response:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "新闻标题",
    "body": "<p>HTML内容</p>",
    "categoryName": "集团新闻",
    "date": "2026-07",
    "author": "品牌策划部",
    "source": "内部资料",
    "image": "images/news-1.jpg"
  }
}
```

### 2.3 集团概况相关

#### GET /api/team
获取管理团队列表

#### GET /api/honors
获取企业荣誉列表

#### GET /api/milestones
获取里程碑列表

### 2.4 集团产业相关

#### GET /api/subsidiaries
获取子公司列表

### 2.5 联系宁商相关

#### GET /api/jobs
获取招聘岗位列表

#### POST /api/messages
提交在线留言

**Request Body:**
```json
{
  "name": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "type": "cooperation",
  "content": "留言内容"
}
```

**Response:**
```json
{
  "code": 200,
  "message": "留言提交成功"
}
```

## 3. API 权限

| API | 匿名用户 | 管理员 |
|-----|----------|--------|
| GET /api/home | ✅ | ✅ |
| GET /api/news | ✅ | ✅ |
| GET /api/news/{id} | ✅ | ✅ |
| GET /api/team | ✅ | ✅ |
| GET /api/honors | ✅ | ✅ |
| GET /api/milestones | ✅ | ✅ |
| GET /api/subsidiaries | ✅ | ✅ |
| GET /api/jobs | ✅ | ✅ |
| POST /api/messages | ✅ | ✅ |
