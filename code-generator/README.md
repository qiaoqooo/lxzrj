# 代码生成器模块

这是一个独立的 MyBatis-Plus 代码生成器 Spring Boot 模块，可以快速生成 Entity、Mapper、Service、Controller 等代码。

## 功能特性

- ✅ 基于 MyBatis-Plus 3.5.3.1 代码生成器
- ✅ 支持 Freemarker 模板引擎
- ✅ 支持 Lombok 自动生成
- ✅ 支持 RESTful 风格 Controller
- ✅ 支持自动填充字段（create_time、update_time）
- ✅ 提供 REST API 接口和命令行两种使用方式

## 快速开始

### 1. 配置数据库连接

编辑 `src/main/resources/application.yml` 文件，修改数据库连接信息：

```yaml
code:
  generator:
    datasource:
      url: jdbc:mysql://localhost:3306/your_database?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: your_password
    author: your_name
    package:
      parent: org.example
```

### 2. 使用方式

#### 方式一：通过 REST API 调用

1. 启动代码生成器模块：
```bash
cd code-generator
mvn spring-boot:run
```

2. 调用生成接口：
```bash
curl -X POST "http://localhost:8081/api/generator/generate?moduleName=user&tableNames=user,role"
```

参数说明：
- `moduleName`: 模块名（如：user、order等）
- `tableNames`: 表名，多个用逗号分隔（如：user,role,permission）

#### 方式二：通过命令行交互式生成

1. 在 `application.yml` 中启用交互式生成：
```yaml
code:
  generator:
    enabled: true
```

2. 启动应用，系统会提示输入模块名和表名：
```bash
mvn spring-boot:run
```

#### 方式三：在代码中直接调用

```java
@Autowired
private CodeGenerator codeGenerator;

public void generateCode() {
    codeGenerator.execute("user", "user,role");
}
```

### 3. 生成的文件结构

代码生成器会在以下位置生成文件：

```
src/main/java/org/example/
  └── {moduleName}/
      ├── controller/
      │   └── {Table}Controller.java
      ├── entity/
      │   └── {Table}.java
      ├── mapper/
      │   └── {Table}Mapper.java
      └── service/
          ├── {Table}Service.java
          └── impl/
              └── {Table}ServiceImpl.java

src/main/resources/mapper/{moduleName}/
  └── {Table}Mapper.xml
```

## 配置说明

### 代码生成器配置项

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `code.generator.datasource.url` | 数据库连接URL | jdbc:mysql://localhost:3306/test |
| `code.generator.datasource.username` | 数据库用户名 | root |
| `code.generator.datasource.password` | 数据库密码 | 空 |
| `code.generator.author` | 作者名称 | code-generator |
| `code.generator.package.parent` | 父包名 | org.example |
| `code.generator.output.dir` | 输出目录 | src/main/java |
| `code.generator.enabled` | 是否启用交互式生成 | false |

### 生成策略

- **实体类**：自动启用 Lombok，支持自动填充字段
- **Controller**：生成 RESTful 风格的控制器
- **Service**：生成接口和实现类
- **Mapper**：生成 XML 映射文件

## 注意事项

1. 确保数据库连接信息正确
2. 确保数据库中存在要生成代码的表
3. 生成的文件会覆盖已存在的同名文件
4. 表名前缀会自动过滤（如：t_、c_）

## 参考文档

- [MyBatis-Plus 代码生成器官方文档](https://baomidou.com/guides/code-generator/)

