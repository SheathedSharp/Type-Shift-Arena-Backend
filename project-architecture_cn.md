<!--
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-11-11 16:07:35
 * @LastEditors: SheathedSharp z404878860@163.com
 * @LastEditTime: 2024-11-11 17:08:30
-->
# Type Shi(f)t Arena 后端项目架构文档
这个项目采用了更现代的多层架构设计模式，具体来说是 领域驱动设计(DDD) 的简化版本。
```
Controller (表现层)
    ↓
Service (业务逻辑层)
    ↓
Repository (数据访问层)
    ↓
Entity/Model (领域模型层)
```
## 1. 项目整体架构
```
src/main/java/com/example/demo/
├── controller/           # 控制器层
│   ├── auth/            # 认证相关控制器
│   │   └── AuthController.java         # 处理登录注册
│   ├── game/            # 游戏相关控制器  
│   │   ├── GameController.java         # 基础游戏控制
│   │   └── GameRoomController.java     # 游戏房间管理
│   └── user/            # 用户相关控制器
│       ├── UserController.java         # 用户基础操作
│       ├── FriendController.java       # 好友系统
│       └── PlayerProfileController.java # 玩家数据
├── service/             # 服务层
│   ├── auth/           # 认证服务
│   ├── game/           # 游戏服务
│   │   └── GameRoomService.java        # 房间管理逻辑
│   └── user/           # 用户服务
│       ├── UserService.java            # 用户管理
│       ├── FriendService.java          # 好友关系
│       └── PlayerProfileService.java    # 玩家数据
├── repository/          # 数据访问层
│   ├── UserRepository.java             # 用户数据访问
│   └── PlayerProfileRepository.java     # 玩家数据访问
├── entity/             # 实体类
│   ├── User.java                       # 用户实体
│   └── PlayerProfile.java              # 玩家数据实体
├── model/              # 模型类
│   ├── dto/            # 数据传输对象
│   │   ├── FriendDTO.java             # 好友信息传输
│   │   └── PlayerDTO.java              # 玩家信息传输
│   └── game/           # 游戏相关模型
│       ├── GameMessage.java            # 游戏消息
│       ├── GameProgress.java           # 游戏进度
│       ├── GameRoom.java               # 游戏房间
│       └── GameStatus.java             # 游戏状态
├── config/             # 配置类
│   ├── SecurityConfig.java             # 安全配置
│   ├── WebSocketConfig.java            # WebSocket配置
│   └── OpenApiConfig.java              # API文档配置
└── core/               # 核心工具类
    └── ApiResponse.java                # 统一响应格式
```

## 2. 数据流转过程
一个典型的请求处理流程如下:

1. 客户端（WEB）发送请求到控制器(Controller)层
2. 控制器调用相应的服务(Service)层方法
3. 服务层实现业务逻辑,通过数据访问层(Repository)操作数据
4. 数据访问层与数据库交互,执行 SQL 操作
5. 结果沿原路返回到客户端
```
Client Request 
  → UserController.createUser()
    → UserService.saveUser()
      → UserRepository.save()
        → Database
```

## 3. 整体架构说明
### 3.1 表现层(Controller)
每个控制器都使用了Spring框架的相关注解(@RestController、@Controller)来标注其功能,并通过@RequestMapping指定请求路径。同时使用Swagger注解(@Operation等)提供API文档。
- 位置: controller/
- 功能: 处理 HTTP 请求和响应,接收前端数据并返回处理结果
- 主要组件:
    1. 认证控制器 (auth/)
        - AuthController: 处理用户登录注册认证
    2. 游戏控制器 (game/)
        - GameController: 处理游戏进度更新等基础操作
        - GameRoomController: 管理游戏房间(创建、加入、退出、开始游戏等)
    3. 用户控制器 (user/)
        - UserController: 用户CRUD操作(创建、查询、更新、删除)
        - FriendController: 好友系统管理(添加、删除、查询好友)
        - PlayerProfileController: 玩家数据管理(查询和更新游戏统计数据)
    4. 测试控制器 (test/)
        - HelloController: 用于测试服务器连接状态

### 3.2 业务层(Service)
每个服务层组件都通过依赖注入使用相应的Repository接口访问数据库,并实现相关的业务规则和数据处理逻辑。服务层是连接控制器层和数据访问层的桥梁,确保业务规则的统一实现。
- 位置: service/
- 功能: 实现具体的业务逻辑,处理数据的加工和处理
- 主要组件:
    1. 认证服务 (auth/)
        - CustomUserDetailsService: 实现用户认证逻辑
    2. 游戏服务 (game/)
        - GameRoomService:
            - 管理游戏房间的创建、加入、退出
            - 处理游戏状态变更
            - 维护玩家在房间中的状态
    3. 用户服务 (user/)
        - UserService:
            - 用户账号管理(注册、查询、更新、删除)
            - 密码加密和验证
            - 创建用户时初始化玩家资料
        - FriendService:       
            - 好友关系管理
            - 好友添加和删除
            - 好友列表查询
        - PlayerProfileService:
            - 玩家游戏数据统计
            - 更新游戏成绩(速度、准确率、胜率)
            - 计算和更新玩家等级
            - 管理玩家排名分数

### 3.3 数据访问层(Repository)
这些Repository接口通过继承Spring Data JPA的JpaRepository接口,自动获得了基本的CRUD操作能力,同时也可以通过方法命名规则或@Query注解来定义自定义查询方法。它们是连接业务逻辑层和数据库的桥梁,确保了数据的持久化存储和访问。
- 位置: repository/
- 功能: 负责与数据库交互,实现数据的增删改查
- 主要组件:
    1. UserRepository:
        - 用户基础数据操作
        - 继承 JpaRepository<User, Long>
        - 主要方法:
            1. findAll(): 查询所有用户
            2. findById(Long id): 根据ID查询用户
            3. save(User user): 保存/更新用户
            4. delete(User user): 删除用户
            5. existsByUsername(String username): 检查用户名是否存在
            6. existsByEmail(String email): 检查邮箱是否存在
            7. findByUsername(String username): 根据用户名查询用户

    2. PlayerProfileRepository
        - 玩家数据统计操作
        - 继承 JpaRepository<PlayerProfile, Long>
        - 主要方法:
            - findById(Long id): 查询玩家数据
            - save(PlayerProfile profile): 保存/更新玩家数据
            - findAll(): 查询所有玩家数据
            - delete(PlayerProfile profile): 删除玩家数据
            - findByUser(User user): 根据用户查询玩家数据

### 3.4 实体层(Entity)
这些实体使用JPA注解进行对象关系映射
```
@Entity: 标记为实体类
@Table: 指定数据库表名
@Id: 标记主键
@Column: 定制字段映射
```
实体生命周期钩子:
```
@PrePersist: 数据保存前的处理
@PreUpdate: 数据更新前的处理
```
实体间关联:
```
@OneToOne: 一对一关系
@ManyToMany: 多对多关系
@JoinTable: 定义关联表
```
使用Lombok简化代码:
```
@Getter: 自动生成getter方法
@Setter: 自动生成setter方法
```
字段验证:
```
@Column(nullable = false): 非空约束
@Column(unique = true): 唯一约束
```
- 位置: entity/
- 功能: 定义数据库表对应的 Java 对象
- 主要组件:
    - User: 用户实体
    - PlayerProfile: 玩家数据实体

### 3.5 模型层(Model)
> @非持久化

相比于entity层，model层存储的是非持久化的数据模型，如跟游戏信息相关的数据模型，这些数据将不会存入数据库表中。
- 位置: model/
- 功能: 定义数据传输对象(DTO)和游戏相关模型
- 主要组件:
    1. 数据传输对象(DTO)
    2. 游戏相关模型(game/)
    
### 3.6 配置层(Config)
- 位置: config/
- 功能: 提供系统配置和框架集成
- 主要组件:
    - SecurityConfig: 安全配置
    - WebSocketConfig: WebSocket配置
    - ...

## 4. 开发指南
1. 添加新功能时的一般流程:
    - 创建实体类(Entity)
    - 创建数据访问接口(Repository)
    - 实现业务逻辑(Service)
    - 添加控制器(Controller)

2. 代码规范:
    - 遵循 RESTful API 设计规范
    - 使用统一响应格式 ApiResponse
    - 添加 Swagger 文档注解
