<!--
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-10-27 17:59:31
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-05 23:50:47
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

---

</div>

This is a backend project for Type Shi(f)t Arena. It is a simple project that uses Spring Boot and Spring Data JPA to manage a MySQL database.

## Adding Environment Variables
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

## DEPENDENCIES
- Spring Boot 3.2.3
- Maven 3.9.9
- MySQL 9.0.1
- JDK 17
- JWT
- Spring Security
- Spring WebSocket

## PACKAGE STRUCTURE
com.example.demo.controller: The folder contains all application controllers

com.example.demo.service: The folder contains all application services

com.example.demo.repository: The folder contains all application repositories

com.example.demo.model: The folder contains all application models

com.example.demo.config: The folder contains all application configurations

com.example.demo.security: The folder contains all application security related classes

## Database Setup

To initialize the database, follow these steps:

1. Ensure you have MySQL installed and running.
2. Open a MySQL client (e.g., MySQL Workbench or command-line client).
3. Run the SQL script located at `sql/init.sql`. This will create the database and necessary tables.

   You can do this by running the following command in your MySQL client:
   ```
   source /path/to/your/project/sql/init.sql
   ```

4. Update the `application-dev.properties` and `application-prod.properties` files with your database credentials.

## Running the Application

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

## API Documentation AND TEST
visit http://localhost:9090/swagger-ui.html to see the API documentation and test the API.

waring: some API may not work because of the security configuration.