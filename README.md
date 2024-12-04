# NodeGuard

NodeGuard 是一个基于 Spring Boot 和 Vue3 的代理节点管理系统。

## 功能特性

- 节点管理
  - Vless节点的增删改查
  - 支持导入节点链接
  - 批量删除节点
  - 节点状态管理
- Token管理
  - Token的增删改查
  - Token与节点关联
- 规则管理
  - 规则的增删改查
  - 规则分类管理
- 系统管理
  - 用户管理
  - 数据字典
  - 密码修改

## 技术栈

### 后端

- Spring Boot 2.7.x
- Spring Security
- MyBatis-Plus
- MySQL 8.0
- JWT

### 前端

- Vue 3
- TypeScript
- Element Plus
- Vue Router
- Axios

## 快速开始

### 环境要求

- JDK 1.8+
- Node.js 16+
- MySQL 8.0+

### 后端启动

1. 创建数据库并执行SQL脚本 