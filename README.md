# Lxzrj 微服务项目

这是一个基于 Spring Cloud Alibaba Nacos 的微服务多模块 Maven 项目。

## 项目结构

```
lxzrj/
├── pom.xml                    # 父 POM 文件
├── gateway/                   # 网关模块
│   ├── pom.xml
│   └── src/
├── user-service/              # 用户服务模块
│   ├── pom.xml
│   └── src/
├── code-generator/            # 代码生成器模块
│   ├── pom.xml
│   ├── README.md
│   └── src/
└── common/                    # 公共模块
    ├── pom.xml
    └── src/
```

## 模块说明

### gateway 模块
API 网关模块，基于 Spring Cloud Gateway，提供统一的路由和负载均衡功能。
- 端口：8888
- 服务名：gateway

### user-service 模块
用户服务模块，提供用户相关的业务功能。
- 端口：8082
- 服务名：user-service

### code-generator 模块
独立的代码生成器模块，基于 MyBatis-Plus 代码生成器，可以快速生成 Entity、Mapper、Service、Controller 等代码。
- 端口：8081
- 服务名：code-generator

详细使用说明请参考：[code-generator/README.md](code-generator/README.md)

### common 模块
公共模块，存放公共的工具类、常量、统一响应结果等。
- 包含统一响应结果封装（Result）
- 包含公共常量类（CommonConstants）

## 环境要求

- JDK 8+
- Maven 3.6+
- Nacos Server 2.0+（需要先启动 Nacos）

## Nacos 配置

### 启动 Nacos

1. 下载 Nacos Server：https://github.com/alibaba/nacos/releases
2. 启动 Nacos：
```bash
# Windows
startup.cmd -m standalone

# Linux/Mac
sh startup.sh -m standalone
```

3. 访问 Nacos 控制台：http://localhost:8848/nacos
   - 默认用户名/密码：nacos/nacos

### 配置说明

所有服务的 Nacos 配置都在各自的 `application.yml` 文件中：
- 服务发现地址：localhost:8848
- 配置中心地址：localhost:8848
- 命名空间：public
- 分组：DEFAULT_GROUP

## 构建项目

在项目根目录执行：

```bash
mvn clean install
```

## 运行模块

### 1. 启动 Nacos Server

确保 Nacos Server 已启动并运行在 localhost:8848

### 2. 运行网关模块
```bash
cd gateway
mvn spring-boot:run
```

### 3. 运行用户服务模块
```bash
cd user-service
mvn spring-boot:run
```

### 4. 运行代码生成器模块
```bash
cd code-generator
mvn spring-boot:run
```

## 服务访问

### 通过网关访问

- 网关地址：http://localhost:8888
- 代码生成器服务：http://localhost:8888/api/generator/**
- 用户服务：http://localhost:8888/api/user/**

### 直接访问服务

- 用户服务：http://localhost:8082
- 代码生成器服务：http://localhost:8081

## 技术栈

- Spring Boot 2.7.18
- Spring Cloud 2021.0.8
- Spring Cloud Alibaba 2021.0.5.0
- Nacos（服务发现 + 配置中心）
- Spring Cloud Gateway（API 网关）
- MyBatis-Plus 3.5.3.1
- Maven 多模块项目

