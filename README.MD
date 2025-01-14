<!--
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-30 00:17:44
-->
<div align="center">
  
# Type Shi(f)t Arena Backend

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

Type Shi(f)t Arena is an engaging, competitive typing platform combining real-time gameplay with fun mechanics. It includes both a frontend built with Vue.js and a backend powered by Spring Boot, offering a seamless, robust user experience for typing enthusiasts.**This repo is the backend part of the project.** frontend repo is
[here](https://github.com/SheathedSharp/Type-Shift-Arena-Frontend)

## 🎮 Key Features (Backend)
- **Game Management**: Real-time game rooms, difficulty management, and category-based game content.
- **Authentication & User System**: Secure JWT-based authentication, player profiles, friends, performance tracking, and ranking systems.
- **WebSocket Integration**: Real-time updates and game state synchronization for multiplayer sessions.
- **Scalable Architecture**: Follows Domain-Driven Design (DDD) with a clean, layered structure, supporting future expansions.
- **Performance Optimization**: Redis caching for fast responses, ensuring stability in high-concurrency environments.

## 🛠️ Technical Stack (Backend)
- **Framework**: Spring Boot 3.2.3
- **Database**: MySQL, Redis
- **Security**: JWT, Spring Security
- **Real-Time Communication**: Spring WebSocket
- **Build Tool**: Maven

## 🌐 API & Documentation
The backend includes extensive API documentation using Swagger UI, making integration and testing straightforward for frontend developers. It supports CORS, secure JWT-based authentication, and detailed logging.

**Note**: Postman is not supported, please use Swagger UI for API testing.

## How to use
### 1. Adding Environment Variables
In the development environment, database and JWT configurations use environment variables to prevent privacy leaks.

The specific code is as follows:
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

For Windows:

1. Right-click "This PC" -> Properties
2. Click "Advanced system settings"
3. Click "Environment Variables" button
4. Click "New" in the "System Variables" section
5. Add the following variables:
```
MYSQL_USERNAME=your_database_username
MYSQL_PASSWORD=your_password
JWT_SECRET=your_secret_key (optional)
JWT_EXPIRATION=your_desired_expiration_time (optional)
```

For Mac:

1. Open configuration file `nano ~/.zshrc`
2. Add the following content:
```
export MYSQL_USERNAME=your_database_username
export MYSQL_PASSWORD=your_password
export JWT_SECRET=your_secret_key (optional)
export JWT_EXPIRATION=your_desired_expiration_time (optional)
```

3. Save and exit the file
4. Reload configuration with source ~/.zshrc

### 2. Database Setup

To initialize the database, follow these steps:

1. Ensure you have MySQL installed and running.
2. Open a MySQL client (e.g., MySQL Workbench or command-line client).
3. Run the SQL script located at `sql/init.sql`. This will create the database and necessary tables.

   You can do this by running the following command in your MySQL client:
   ```
   source /path/to/your/project/sql/init.sql
   ```

4. Update the `application-dev.properties` and `application-prod.properties` files with your database credentials.

### 3. Running the Application

To run the application, use the following command. (Default is development environment)

for development:
```bash
mvn spring-boot:run
```

for production:
```bash
mvn spring-boot:run -Dspring.profiles.active=prod
```

visit http://localhost:9090/hello to see the result.

### 4. API Documentation AND TEST
visit http://localhost:9090/swagger-ui.html to see the API documentation and test the API.

waring: some API may not work because of the security configuration.