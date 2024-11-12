<!--
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 17:59:31
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-11 20:06:30
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

<div style="margin-top: 5px">
  <a href="project-architecture.md">
    <img src="https://img.shields.io/badge/English-PROJUCT ARCHITECTURE-brown.svg?style=for-the-badge" height="22px" />
  </a>
  <a href="project-architecture_cn.md">
    <img src="https://img.shields.io/badge/中文-项目结构-black.svg?style=for-the-badge" height="22px" />
  </a>
  
</div>

---

</div>

这是 Type Shi(f)t Arena 的后端项目。该项目使用 Spring Boot 和 Spring Data JPA 来管理 MySQL 数据库。

## 添加环境变量
在开发环境中有关数据库和JWT的配置均使用环境变量的方法进行配置，以防隐私泄露

具体的代码如下：
```
# application-dev.properties
......
spring.datasource.url=${MYSQL_URL:jdbc:mysql://localhost:3306/myappdb}
spring.datasource.username=${MYSQL_USERNAME}
spring.datasource.password=${MYSQL_PASSWORD}
.....
.....
jwt.secret=${JWT_SECRET:your_jwt_secret_key_should_be_at_least_256_bits_long_for_security}
jwt.expiration=${JWT_EXPIRATION:86400}
```

对于Windows系统：
1. 右键"此电脑" -> 属性
2. 点击"高级系统设置"
3. 点击"环境变量"按钮
4. 在"系统变量"部分点击"新建"
5. 添加以下变量：
```
MYSQL_USERNAME=你的数据库用户名
MYSQL_PASSWORD=你的密码
JWT_SECRET=你的密钥（非必要）
JWT_EXPIRATION=你期望的到期时间（非必要）
```

对于Mac系统：
1. 打开配置文件`nano ~/.zshrc`
2. 添加以下内容
```
export MYSQL_USERNAME=你的数据库用户名
export MYSQL_PASSWORD=你的密码
export JWT_SECRET=你的密钥（非必要）
export JWT_EXPIRATION=你期望到期的时间（非必要）
```
3. 保存文件并退出
4. 重新加载配置`source ~/.zshrc`

## 依赖
- Spring Boot 3.2.3
- Maven 3.9.9
- MySQL 9.0.1
- JDK 17
- JWT
- Spring Security
- Spring WebSocket

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
