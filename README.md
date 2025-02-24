# bigEvent_Admin-trainingOperation

## 项目概述

bigEvent_Admin-trainingOperation 是一个综合性的项目，旨在提供丰富的文章管理功能，包括文章的分类、列表展示、添加、删除、更新以及详情查看等。该项目前后端分离，后端使用 Java 开发，前端基于 Vue 框架构建。

## 技术栈

### 后端
- **Java**：作为主要的编程语言。
- **MyBatis**：用于数据库访问，通过 XML 映射文件实现 SQL 语句的管理。
- **Spring Boot**（假设使用，根据实际情况调整）：用于快速构建独立的、生产级别的 Spring 应用。
- **MySQL**：作为数据库管理系统，存储文章和分类等相关数据。

### 前端
- **Vue 3**：构建用户界面的 JavaScript 框架。
- **Vite**：作为构建工具，提供快速的开发服务器和优化的构建过程。
- **Element Plus**：一套基于 Vue 3 的组件库，用于快速构建美观的界面。
- **Vue Quill**：富文本编辑器，用于文章内容的编辑。

## 项目结构

### 后端
- `src/main/java`：Java 源代码目录。
  - `com/win/pojo`：包含数据模型类，如 `Result`、`PageBean` 等。
  - `com/win/mapper`：MyBatis 的 Mapper 接口和 XML 映射文件，负责数据库操作。
  - `com/win/utils`：工具类，如 `JwtUtil` 用于生成和解析 JWT 令牌。
- `src/main/resources`：资源文件目录，包含配置文件和 MyBatis 的 XML 映射文件。
- `src/test/java`：测试代码目录。

### 前端
- `src`：前端源代码目录。
  - `composables`：包含可复用的组合式函数，如 `useArticle.js` 用于文章相关操作。
  - `views`：Vue 组件目录，如 `ArticleManage.vue` 用于文章管理页面。
  - `api`：API 服务目录，包含与后端接口交互的服务函数。
  - `stores`：状态管理目录，如 `token.js` 用于管理用户令牌。

## 环境配置

### 后端
项目使用 Docker Compose 来管理 MySQL 数据库，确保在开发环境中可以快速启动和配置数据库。

#### Docker Compose 配置
```yaml
services:
  mysql:
    image: mysql:8.3.0
    command: mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci
    environment:
      - MYSQL_ROOT_PASSWORD=1234
      - MYSQL_LOWER_CASE_TABLE_NAMES=0
    ports:
      - "5104:3306"
    volumes:
      - ./data/mysql:/var/lib/mysql
