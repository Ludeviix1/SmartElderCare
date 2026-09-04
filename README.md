# SmartElderCare

> 基于 Spring Boot 3 + Vue 3 的智慧养老综合管理平台

SmartElderCare 是一个面向养老机构日常管理场景开发的前后端分离管理系统。

项目以 **Java / Spring Boot** 为后端核心，结合 **MyBatis-Plus、MySQL、JWT** 等技术，实现老人、家属、床位、护理计划及护理任务等核心业务管理，并通过 Vue 3 管理后台提供统一的可视化操作界面。

本项目主要用于实践和展示 **Java 后端业务开发、数据库设计、权限认证、业务流程设计以及前后端协作开发能力**。

---

## ✨ 项目特点

- **前后端分离**：Spring Boot 提供 RESTful API，Vue 3 提供管理后台
- **模块化业务设计**：按照 Controller / Service / Mapper 分层组织后端代码
- **JWT 身份认证**：实现用户登录及接口访问控制
- **统一异常处理**：集中处理参数异常、业务异常及系统异常
- **统一接口响应**：规范后端 API 返回结构
- **MySQL + MyBatis-Plus**：完成核心业务数据持久化
- **前后端参数校验**：降低非法参数进入业务层的风险
- **业务状态管理**：对老人、床位、护理计划、护理任务和体检预约等业务状态进行管理

---

## 🏥 核心功能

### 1. 老人管理

提供养老机构老人基础信息管理功能。

- 老人信息新增、修改、删除
- 老人信息查询
- 老人状态管理
- 老人基本资料维护
- 老人与家属关系关联

### 2. 家属管理

实现老人家属信息及关联关系管理。

- 家属信息维护
- 家属账号管理
- 家属与老人绑定
- 家属与老人解绑
- 关联关系查询

### 3. 床位管理

实现养老机构床位资源的基本管理。

- 床位信息维护
- 床位状态管理
- 老人入住床位分配
- 床位释放
- 床位使用状态查询

### 4. 护理计划

用于维护老人日常护理计划。

- 护理计划创建
- 护理计划修改
- 护理计划查询
- 护理计划状态管理
- 护理计划与老人关联

### 5. 护理任务

根据护理计划生成并管理实际护理任务。

- 护理任务生成
- 待执行任务查询
- 护理任务状态流转
- 护理任务执行记录
- 老人与护理任务关联

### 6. 数据统计

对系统中的核心业务数据进行汇总，为管理人员提供基础的数据展示能力。

### 7. 体检管理

- 体检项目与套餐维护
- 管理端和家属端发起体检预约
- 手动或按任务量自动分配护工
- 体检执行、报告填写与报告查看

### 8. 权限管理

- 用户、角色与权限维护
- 菜单及操作权限按角色授权
- 护工仅查看本人护理与体检任务

### 9. 家属端与 AI 问答

- 家属端查看体检套餐、提交预约和查看体检报告
- 基于可选 AI 服务的健康问答

---

## 🏗️ 系统架构

```text
┌──────────────────────────────────────────────┐
│                  用户访问层                   │
│                                              │
│       Vue 3 管理后台       家属端             │
│             │                │               │
└─────────────┼────────────────┼───────────────┘
              │    HTTP/REST API
              ▼
┌──────────────────────────────────────────────┐
│              Spring Boot 后端                │
│                                              │
│  Controller                                  │
│      ↓                                       │
│  Service                                     │
│      ↓                                       │
│  Mapper                                      │
│      ↓                                       │
│  MySQL                                       │
│                                              │
│  JWT 鉴权 · 参数校验 · 全局异常处理 · 统一响应  │
└──────────────────────────────────────────────┘
```

后端采用经典的分层架构，将接口处理、业务逻辑和数据访问进行解耦：

```text
Controller
    ↓
Service
    ↓
Mapper
    ↓
MySQL
```

公共配置、异常处理、认证及工具类等功能集中放置于对应基础模块中。

---

## 🛠️ 技术栈

### 后端

| 技术 | 用途 |
| --- | --- |
| Java 21 | 后端开发语言 |
| Spring Boot 3 | 后端核心框架 |
| MyBatis-Plus | ORM / 数据访问 |
| MySQL 8 | 关系型数据库 |
| JWT | 身份认证 |
| Lombok | 简化 Java 代码 |
| Maven | 项目构建与依赖管理 |

### 前端

| 技术 | 用途 |
| --- | --- |
| Vue 3 | 前端框架 |
| Vite | 前端构建工具 |
| Element Plus | UI 组件库 |
| Axios | HTTP 请求 |
| Vue Router | 路由管理 |
| ECharts | 数据可视化 |
| Vant | 家属端 UI 组件库 |

---

## 📁 项目结构

```text
SmartElderCare/
├── src/
│   ├── main/
│   │   ├── java/com/elder/
│   │   │   ├── controller/       # 控制层
│   │   │   ├── service/          # 业务逻辑层
│   │   │   ├── mapper/           # 数据访问层
│   │   │   ├── pojo/             # 实体、DTO、VO 等对象
│   │   │   ├── config/           # 系统配置
│   │   │   ├── exception/        # 异常处理
│   │   │   └── util/             # 工具类
│   │   │
│   │   └── resources/
│   │       ├── mapper/            # MyBatis 映射文件
│   │       └── application.yml   # 应用配置
│   │
│   └── test/
│       └── java/com/elder/        # 测试代码
│
├── ui/
│   ├── ui-admin/                  # Vue 3 管理后台
│   └── ui-app/                    # 家属端
│
├── SmartElderCareDatabase.sql      # 数据库初始化脚本
├── .env.example                    # 本地配置示例
├── pom.xml                        # Maven 配置
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

> 实际目录结构可能会随着项目版本调整，以仓库当前代码为准。

---

## 🔐 安全与工程实践

项目在基础业务功能之外，对后端工程中的一些通用问题进行了处理。

### 身份认证

采用 JWT 实现用户登录认证。

```text
用户登录
   ↓
身份验证
   ↓
生成 JWT
   ↓
客户端保存 Token
   ↓
后续请求携带 Token
   ↓
后端验证 Token
   ↓
访问业务接口
```

### 参数校验

在 Controller / DTO 层对用户输入进行基础校验，避免明显非法数据进入业务逻辑。

### 全局异常处理

通过统一异常处理机制集中处理：

- 参数校验异常
- 业务异常
- 权限异常
- 数据不存在
- 系统异常

避免在每个 Controller 中重复编写异常处理逻辑。

### 统一响应

后端接口使用统一的数据返回结构，使前端能够以一致的方式处理接口结果。

---

## 🗄️ 数据库

项目使用 MySQL 保存核心业务数据。

主要业务数据围绕老人、家属、床位、护理计划/任务、体检套餐/预约/报告、用户、角色和权限等实体展开。

```text
用户
 │
 ├── 家属
 │      │
 │      └── 老人
 │             │
 │             ├── 床位
 │             ├── 护理计划
 │             │       │
 │             │       └── 护理任务
 │             ├── 体检预约 / 报告
 │             │
 │             └── 其他业务信息
 │
 └── 权限 / 登录信息
```

数据库设计重点考虑实体之间的关联关系以及业务状态的一致性。

---

## 🚀 快速开始

### 环境要求

| 环境 | 版本 |
| --- | --- |
| JDK | 21+ |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Node.js | 22.18.x 或 24.12+ |

---

### 1. 克隆项目

```bash
git clone https://github.com/Ludeviix1/SmartElderCare.git

cd SmartElderCare
```

---

### 2. 配置数据库

创建 MySQL 数据库，并根据项目提供的 SQL 脚本完成数据库初始化。

```sql
CREATE DATABASE elder
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

随后将 `SmartElderCareDatabase.sql` 导入 `elder` 数据库。

> 该脚本会重建表并写入演示数据，仅用于新建数据库或允许覆盖数据的环境。

---

### 3. 配置后端

复制 `.env.example` 为 `.env`，再按本地环境填写配置：

```powershell
Copy-Item .env.example .env
```

至少需要配置 `DB_USERNAME`、`DB_PASSWORD` 和 `JWT_SECRET`。`JWT_SECRET` 长度不得少于 32 个字符。AI 问答仅在设置 `AI_ENABLED=true` 和 `DASHSCOPE_API_KEY` 后启用。

`.env` 会由应用启动时自动读取，且已被 Git 忽略。不要将真实密码、AccessKey、Token 等敏感信息提交到仓库。

---

### 4. 启动后端

使用 Maven Wrapper：

```bash
# Windows
.\mvnw.cmd spring-boot:run
```

或：

```bash
# Linux / macOS
./mvnw spring-boot:run
```

也可以直接使用 Maven：

```bash
mvn spring-boot:run
```

---

### 5. 启动管理后台

进入管理后台目录：

```bash
cd ui/ui-admin
```

安装依赖：

```bash
npm install
```

启动开发服务器：

```bash
npm run dev
```

启动后访问终端输出的 Vite 地址。

---

### 6. 启动家属端

```bash
cd ui/ui-app
npm install
npm run dev
```

启动后访问终端输出的 Vite 地址。

---

### 7. 构建前端

分别在 `ui/ui-admin` 和 `ui/ui-app` 目录执行：

```bash
npm run build
```

构建完成后生成生产环境静态资源。

---

## 🧪 测试

项目测试代码位于：

```text
src/test/java/com/elder
```

执行测试：

```bash
mvn test
```

或者使用 Maven Wrapper：

```bash
./mvnw test
```

Windows：

```bash
.\mvnw.cmd test
```

---

## 📌 项目定位

SmartElderCare 并非单纯的 CRUD 示例，而是围绕实际业务场景进行设计的综合管理系统。

项目开发过程中重点关注：

- 业务模型设计
- 数据库表结构设计
- REST API 设计
- Controller / Service / Mapper 分层
- JWT 身份认证
- 参数校验
- 全局异常处理
- 统一接口响应
- 前后端分离
- 核心业务状态流转
- 基础测试与工程规范

项目的主要目标是通过完整业务系统的开发，实践和验证 Java 后端开发中的常见工程问题。

---

## 📈 后续方向

当前版本以**核心业务完整性和工程稳定性**为主要目标。

后续如果继续扩展，可以围绕以下方向进行：

- 更完善的权限模型
- 更细粒度的操作日志
- 接口性能优化
- 更完整的自动化测试
- Docker 容器化部署
- CI/CD 自动化部署

这些能力不作为当前版本运行的必要条件。

---

## 📄 License

本项目主要用于：

- 学习
- Java 后端工程实践
- 项目展示
- 秋招求职作品集

未经许可，请勿直接将本项目用于商业用途。

---

## 👤 Author

**Ludeviix1**

GitHub：

https://github.com/Ludeviix1/SmartElderCare
