<!--
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-10-27 17:59:31
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

Type Shi(f)t Arena 是一个小型又友好的竞技打字项目，将实时游戏玩法与有趣的机制相结合。它包括使用 Vue.js 构建的前端和由 Spring Boot 驱动的后端，填充当前WEB网页市面上好的练习打字平台的空缺，为好友友好切磋，或爱好者提供无缝、强大的用户体验。**本仓库是项目的后端部分。** 前端仓库在[这里](https://github.com/SheathedSharp/Type-Shift-Arena-Frontend)

## 🎮 主要特性（后端）
- **游戏管理**：实时游戏房间、难度管理和基于类别的游戏内容。
- **认证和用户系统**：安全的JWT认证、玩家档案、好友系统、性能追踪和排名系统。
- **WebSocket集成**：多人会话的实时更新和游戏状态同步。
- **可扩展架构**：遵循领域驱动设计（DDD）的清晰分层结构，支持未来扩展。
- **性能优化**：使用Redis缓存实现快速响应，确保高并发环境下的稳定性。

## 🛠️ 技术栈（后端）
- **框架**：Spring Boot 3.2.3
- **数据库**：MySQL, Redis
- **安全**：JWT, Spring Security
- **实时通信**：Spring WebSocket
- **构建工具**：Maven

## 🌐 API和文档
后端使用Swagger UI提供全面的API文档，使前端开发人员能够轻松进行集成和测试。支持CORS、安全的JWT认证和详细的日志记录。

## 怎么部署使用

### 1. 添加环境变量
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

### 2. 数据库设置

要初始化数据库，请按照以下步骤操作：

1. 确保已安装并运行 MySQL。
2. 打开 MySQL 客户端（如 MySQL Workbench 或命令行客户端）。
3. 运行位于 `sql/init.sql` 的 SQL 脚本。这将创建数据库和必要的表。

   您可以在 MySQL 客户端中运行以下命令：
   ```
   source /path/to/your/project/sql/init.sql
   ```

4. 使用您的数据库凭据更新 `application-dev.properties` 和 `application-prod.properties` 文件。

### 3. 运行应用

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

### 4. API 文档和测试
访问 http://localhost:9090/swagger-ui.html 查看 API 文档并测试 API。

警告：由于安全配置，某些 API 可能无法使用。
