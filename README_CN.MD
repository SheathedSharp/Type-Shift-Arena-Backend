<!--
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 17:59:31
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-02 23:55:31
-->
<div align="center">
  
# Type Shi(f)t Arena 后端

<div>
  <a href="README.MD">
    <img src="https://img.shields.io/badge/English-README-blue.svg?style=for-the-badge" height="22px" />
  </a>
  <a href="README_CN.MD">
    <img src="https://img.shields.io/badge/中文-说明文档-red.svg?style=for-the-badge" height="22px" />
  </a>
</div>

<div style="margin-top: 5px">
  <a href="CONTRIBUTING.MD">
    <img src="https://img.shields.io/badge/English-Contributing-green.svg?style=for-the-badge" height="22px" />
  </a>
  <a href="CONTRIBUTING_CN.MD">
    <img src="https://img.shields.io/badge/中文-贡献指南-yellow.svg?style=for-the-badge" height="22px" />
  </a>
</div>

---

</div>

这是 Type Shi(f)t Arena 的后端项目。该项目使用 Spring Boot 和 Spring Data JPA 来管理 MySQL 数据库。

## 依赖
- Spring Boot 3.2.3
- Maven 3.9.9
- MySQL 9.0.1
- JDK 17
- JWT
- Spring Security
- Spring WebSocket

## 包结构
com.example.demo.controller: 包含所有应用控制器

com.example.demo.service: 包含所有应用服务

com.example.demo.repository: 包含所有应用数据仓库

com.example.demo.model: 包含所有应用模型

com.example.demo.config: 包含所有应用配置

com.example.demo.security: 包含所有应用安全相关类

## 数据库设置

要初始化数据库，请按照以下步骤操作：

1. 确保已安装并运行 MySQL。
2. 打开 MySQL 客户端（如 MySQL Workbench 或命令行客户端）。
3. 运行位于 `sql/init.sql` 的 SQL 脚本。这将创建数据库和必要的表。

   您可以在 MySQL 客户端中运行以下命令：
   ```
   source /path/to/your/project/sql/init.sql
   ```

4. 使用您的数据库凭据更新 `application-dev.properties` 和 `application-prod.properties` 文件。

## 运行应用

使用以下命令来选择运行环境。（默认是开发环境）

开发环境：
```bash
mvn spring-boot:run
```

生产环境：
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

访问 http://localhost:9090/hello 查看结果。

## API 文档和测试
访问 http://localhost:9090/swagger-ui.html 查看 API 文档并测试 API。

警告：由于安全配置，某些 API 可能无法使用。
