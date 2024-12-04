# NodeGuard

NodeGuard 是一个基于 Spring Boot 和 Vue3 的代理节点管理系统。本项目不提供节点创建功能，主要用于：
1. 管理已有的代理节点（如Vless节点）
2. 为节点生成配置文件（如Clash配置）
3. 管理节点访问令牌（Token）
4. 维护分流规则

## 主要功能

- 节点管理
  - Vless节点的导入与管理
  - 支持从节点链接导入（vless://...）
  - 节点批量管理
  - 节点状态监控
- Token管理
  - 生成访问令牌
  - Token与节点绑定
  - Token权限控制
- 配置生成
  - 支持生成Clash配置文件
  - 基于Token的配置访问控制
- 规则管理
  - 分流规则维护
  - 规则分类管理
  - 规则导入导出
- 系统管理
  - 用户管理
  - 数据字典
  - 密码修改

## 使用场景

1. 已有代理节点需要统一管理
2. 需要为不同用户分发不同的节点配置
3. 需要维护统一的分流规则
4. 需要监控节点状态和使用情况

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

## 开发计划

### 近期计划
1. Docker支持
   - 提供Dockerfile
   - 提供docker-compose配置
   - 支持一键部署

2. 协议支持
   - 支持Hysteria2协议
   - 支持Tuic协议
   - 支持Reality配置

3. 配置生成
   - 支持Clash Meta配置
   - 支持sing-box配置
   - 支持V2rayN配置

4. 节点管理
   - 节点状态检测
   - 节点延迟测试
   - 节点流量统计
   - 节点分组管理

### 长期计划

1. 系统功能
   - 完善的权限管理
   - 操作日志记录
   - 支持更多协议
   - 支持更多配置格式

## 参与贡献

1. Fork 本仓库
2. 新建 feature/xxx 分支
3. 提交代码
4. 新建 Pull Request
