# SmartElderCare

> 基于 Spring Boot 3 + Vue 3 + MySQL + Redis 的智慧养老综合管理平台

SmartElderCare 是一个面向养老机构日常管理场景设计的综合管理平台，采用前后端分离架构，实现老人档案、家属、床位、护理计划、护理任务等核心业务的数字化管理。

项目以 **Java / Spring Boot** 为后端核心，结合 **MyBatis-Plus、MySQL、Redis、JWT** 等技术，构建完整的业务管理与权限认证体系；前端采用 **Vue 3** 实现管理后台，并提供家属端应用。

---

## ✨ 项目特点

- **前后端分离**：Spring Boot + Vue 3，前后端通过 RESTful API 进行数据交互
- **权限认证**：基于 JWT 实现用户身份认证与接口访问控制
- **业务模块化**：围绕老人、家属、床位、护理计划和护理任务构建完整业务流程
- **数据持久化**：MySQL + MyBatis-Plus 实现业务数据管理与分页查询
- **缓存支持**：Redis 用于缓存及相关临时数据处理
- **统一接口规范**：统一响应结构与全局异常处理
- **管理后台**：基于 Vue 3 构建后台管理界面
- **家属端**：提供面向老人家属的业务访问入口

---

## 🏗️ 系统架构

```text
┌──────────────────────────────────────────────┐
│                  用户端                      │
├──────────────────────┬───────────────────────┤
│   管理员 Web 后台     │     家属端应用        │
│     Vue 3             │      uni-app          │
└──────────┬───────────┴───────────┬───────────┘
           │                       │
           └──────── REST API ─────┘
                       │
                       ▼
┌──────────────────────────────────────────────┐
│              Spring Boot 后端                │
│                                              │
│ Controller → Service → Mapper                │
│      │          │                            │
│      └── JWT / 权限 / 全局异常处理            │
└───────────────┬──────────────────────────────┘
                │
        ┌───────┴────────┐
        ▼                ▼
   MySQL 数据库       Redis
```

---

## 📦 功能模块

### 1. 老人管理

实现养老机构老人基础档案的统一管理。

- 老人信息新增、修改、删除
- 老人信息查询
- 分页查询
- 老人状态管理
- 基础信息维护

### 2. 家属管理

用于维护老人家属及相关关联关系。

- 家属信息管理
- 家属账号管理
- 家属与老人关联
- 关联关系维护

### 3. 床位管理

围绕养老机构床位资源进行管理。

- 床位信息维护
- 床位状态管理
- 老人与床位关联
- 床位占用状态查询

### 4. 护理计划

用于制定和维护老人的护理服务计划。

- 护理计划创建
- 护理计划修改
- 护理计划查询
- 护理周期与执行信息维护
- 护理计划状态管理

### 5. 护理任务

将护理计划进一步落实到具体执行任务。

- 护理任务生成
- 待执行任务查询
- 任务执行状态维护
- 护理任务完成记录

### 6. 数据统计

对系统中的核心业务数据进行汇总展示，为管理人员提供数据查询与统计能力。

---

## 🛠️ 技术栈

### 后端

| 技术 | 用途 |
|---|---|
| Java | 核心开发语言 |
| Spring Boot 3 | 后端应用框架 |
| MyBatis-Plus | ORM 与数据库访问 |
| MySQL | 业务数据持久化 |
| Redis | 缓存及临时数据 |
| JWT | 身份认证 |
| Lombok | 简化 Java 代码 |
| Maven | 项目构建与依赖管理 |

### 前端

| 技术 | 用途 |
|---|---|
| Vue 3 | 前端框架 |
| Vite | 前端构建工具 |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求 |
| Vue Router | 前端路由 |
| ECharts | 数据可视化 |
| uni-app | 家属端应用开发 |

---

## 📁 项目结构

```text
SmartElderCare/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/elder/
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/elder/
│
├── ui/
│   ├── ui-admin/        # Vue 3 管理后台
│   └── ui-app/          # 家属端应用
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 🔐 权限与安全

项目采用 JWT 进行身份认证，并通过后端接口权限控制保证不同用户只能访问对应业务资源。

核心安全措施包括：

- JWT 身份认证
- 接口访问权限控制
- 后端参数校验
- 全局异常处理
- 统一接口响应
- 敏感配置外部化
- 密码安全存储

> 项目公开仓库不会保存真实数据库密码、JWT 密钥及其他第三方服务凭证。

---

## 🚀 本地运行

### 环境要求

建议使用：

```text
JDK 17+
Maven 3.8+
MySQL 8.0+
Redis 6.0+
Node.js 18+
npm
```

### 1. 克隆项目

```bash
git clone https://github.com/Ludeviix1/SmartElderCare.git

cd SmartElderCare
```

### 2. 配置数据库

创建 MySQL 数据库，并根据项目提供的 SQL 文件完成数据库初始化。

随后修改后端配置文件：

```text
src/main/resources/application.yml
```

配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
```

实际配置项请以项目当前 `application.yml` 为准。

数据库配置见`SmartElderCareDatabase.sql`

### 3. 启动后端

```bash
./mvnw spring-boot:run
```

Windows：

```bash
mvnw.cmd spring-boot:run
```

### 4. 启动管理后台

```bash
cd ui/ui-admin

npm install
npm run dev
```

启动后访问终端输出的 Vite 地址。

### 5. 启动家属端

```bash
cd ui/ui-app
```

使用对应的 uni-app / 微信开发工具进行运行和调试。

---

## 🔌 API

后端提供 RESTful API 接口，主要业务模块包括：

```text
老人管理
家属管理
床位管理
护理计划
护理任务
数据统计
用户认证
```

如项目启用了 OpenAPI / Swagger，可在后端启动后通过对应 Swagger UI 地址查看完整接口定义。

---

## 🧪 测试

项目测试代码位于：

```text
src/test/java/com/elder
```

测试重点覆盖核心业务逻辑，包括：

- 用户认证
- 核心业务 Service
- 护理计划
- 护理任务
- 业务状态流转

执行测试：

```bash
./mvnw test
```

Windows：

```bash
mvnw.cmd test
```

---

## 📌 项目定位

SmartElderCare 主要用于实践和展示以下工程能力：

- Java 后端开发
- Spring Boot 项目开发
- MyBatis-Plus 数据访问
- MySQL 数据库设计
- Redis 缓存应用
- JWT 身份认证
- RESTful API 设计
- Vue 3 前端开发
- 前后端分离架构
- 综合业务系统设计

项目重点不在于堆叠大量功能，而是通过完整的业务链路实践企业级 Web 应用开发中的常见技术与工程方法。

---

## 📄 License

本项目仅用于学习、实践与个人项目展示。