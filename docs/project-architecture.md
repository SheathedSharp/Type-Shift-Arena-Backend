# Type Shift Arena Backend Project Architecture Document
This project adopts a modern multi-layered architecture design pattern, specifically a simplified version of Domain-Driven Design (DDD).
```
Controller (Presentation Layer)
    ↓
Service (Business Logic Layer)
    ↓
Repository (Data Access Layer)
    ↓
Entity/Model (Domain Model Layer)
```

## 1. Project Structure Overview
```
src/main/java/com/example/demo/
├── controller/           # Controller Layer
│   ├── auth/             # Authentication Controllers
│   │   └── AuthController.java         # Handles login and registration
│   ├── game/             # Game Controllers  
│   │   ├── GameController.java         # Basic game control
│   │   └── GameRoomController.java     # Game room management
│   └── user/             # User Controllers
│       ├── UserController.java         # Basic user operations
│       ├── FriendController.java       # Friend system
│       └── PlayerProfileController.java # Player data
├── service/              # Service Layer
│   ├── auth/             # Authentication Services
│   ├── game/             # Game Services
│   │   └── GameRoomService.java        # Room management logic
│   └── user/             # User Services
│       ├── UserService.java            # User management
│       ├── FriendService.java          # Friend relationships
│       └── PlayerProfileService.java   # Player data
├── repository/           # Data Access Layer
│   ├── UserRepository.java             # User data access
│   └── PlayerProfileRepository.java    # Player data access
├── entity/               # Entity Classes
│   ├── User.java                         # User entity
│   └── PlayerProfile.java                # Player data entity
├── model/                # Model Classes
│   ├── dto/              # Data Transfer Objects
│   │   ├── FriendDTO.java               # Friend data transfer
│   │   └── PlayerDTO.java               # Player data transfer
│   └── game/             # Game-related Models
│       ├── GameMessage.java             # Game messages
│       ├── GameProgress.java            # Game progress
│       ├── GameRoom.java                # Game room
│       └── GameStatus.java              # Game status
├── config/               # Configuration Classes
│   ├── SecurityConfig.java              # Security configuration
│   ├── WebSocketConfig.java             # WebSocket configuration
│   └── OpenApiConfig.java               # API documentation configuration
└── core/                 # Core Utility Classes
    └── ApiResponse.java                  # Unified response format
```

## 2. Data Flow Process
A typical request handling process follows:

1. Client (WEB) sends a request to the Controller layer.
2. The Controller calls the appropriate Service layer method.
3. The Service layer implements business logic and interacts with the Data Access layer (Repository).
4. The Repository interacts with the database and executes SQL operations.
5. The result is returned back to the client in reverse order.
```
Client Request 
  → UserController.createUser()
    → UserService.saveUser()
      → UserRepository.save()
        → Database
```

## 3. Architecture Description
### 3.1 Presentation Layer (Controller)
Each controller is annotated with Spring framework annotations (@RestController, @Controller) to define its functionality, and uses @RequestMapping to specify request paths. Swagger annotations (@Operation, etc.) are used for API documentation.
- Location: controller/
- Function: Handles HTTP requests and responses, receives data from the front end, and returns processing results.
- Main Components:
    1. Authentication Controller (auth/)
        - AuthController: Manages user login, registration, and authentication.
    2. Game Controller (game/)
        - GameController: Handles basic game operations and updates.
        - GameRoomController: Manages game rooms (creation, joining, exiting, starting games, etc.).
    3. User Controller (user/)
        - UserController: User CRUD operations (create, read, update, delete).
        - FriendController: Manages the friend system (add, remove, query friends).
        - PlayerProfileController: Manages player data (query and update game statistics).
    4. Test Controller (test/)
        - HelloController: Tests server connection status.

### 3.2 Business Logic Layer (Service)
Each service layer component uses dependency injection to access the corresponding Repository interface for database interaction and implements business rules and data processing logic. The Service layer bridges the Controller and Data Access layers, ensuring unified business logic.
- Location: service/
- Function: Implements specific business logic and processes data.
- Main Components:
    1. Authentication Service (auth/)
        - CustomUserDetailsService: Implements user authentication logic.
    2. Game Service (game/)
        - GameRoomService:
            - Manages the creation, joining, and exiting of game rooms.
            - Handles game state changes.
            - Maintains player states within rooms.
    3. User Service (user/)
        - UserService:
            - Manages user accounts (register, query, update, delete).
            - Password encryption and verification.
            - Initializes player profiles when creating a user.
        - FriendService:       
            - Manages friend relationships.
            - Adds and deletes friends.
            - Queries friend lists.
        - PlayerProfileService:
            - Manages player game data statistics.
            - Updates game performance metrics (speed, accuracy, win rate).
            - Calculates and updates player levels.
            - Manages player ranking scores.

### 3.3 Data Access Layer (Repository)
These Repository interfaces extend Spring Data JPA's JpaRepository interface, providing basic CRUD operations automatically. They also define custom query methods using method naming conventions or @Query annotations. They bridge the business logic layer and the database, ensuring data persistence and access.
- Location: repository/
- Function: Interacts with the database, implementing CRUD operations.
- Main Components:
    1. UserRepository:
        - Manages basic user data operations.
        - Extends JpaRepository<User, Long>.
        - Main Methods:
            1. findAll(): Queries all users.
            2. findById(Long id): Finds user by ID.
            3. save(User user): Saves/updates a user.
            4. delete(User user): Deletes a user.
            5. existsByUsername(String username): Checks if a username exists.
            6. existsByEmail(String email): Checks if an email exists.
            7. findByUsername(String username): Finds user by username.

    2. PlayerProfileRepository:
        - Manages player data statistics.
        - Extends JpaRepository<PlayerProfile, Long>.
        - Main Methods:
            - findById(Long id): Finds player data.
            - save(PlayerProfile profile): Saves/updates player data.
            - findAll(): Queries all player data.
            - delete(PlayerProfile profile): Deletes player data.
            - findByUser(User user): Finds player data by user.

### 3.4 Entity Layer (Entity)
These entities use JPA annotations for object-relational mapping.
```
@Entity: Marks a class as an entity.
@Table: Specifies database table name.
@Id: Marks the primary key.
@Column: Customizes field mapping.
```
Entity Lifecycle Hooks:
```
@PrePersist: Process data before saving.
@PreUpdate: Process data before updating.
```
Entity Relationships:
```
@OneToOne: One-to-one relationship.
@ManyToMany: Many-to-many relationship.
@JoinTable: Defines the join table.
```
Uses Lombok to simplify code:
```
@Getter: Auto-generates getter methods.
@Setter: Auto-generates setter methods.
```
Field Validations:
```
@Column(nullable = false): Non-null constraint.
@Column(unique = true): Unique constraint.
```
- Location: entity/
- Function: Defines Java objects corresponding to database tables.
- Main Components:
    - User: User entity.
    - PlayerProfile: Player data entity.

### 3.5 Model Layer (Model)
> @Non-persistent

Unlike the Entity layer, the Model layer contains non-persistent data models, such as game-related data models, which are not stored in database tables.
- Location: model/
- Function: Defines Data Transfer Objects (DTO) and game-related models.
- Main Components:
    1. Data Transfer Objects (DTO)
    2. Game-related models (game/)

### 3.6 Configuration Layer (Config)
- Location: config/
- Function: Provides system configuration and framework integration.
- Main Components:
    - SecurityConfig: Security configuration.
    - WebSocketConfig: WebSocket configuration.
    - ...

## 4. Development Guide
1. General procedure when adding a new feature:
    - Create an Entity class.
    - Create a Repository interface.
    - Implement business logic in the Service.
    - Add a Controller.

2. Code standards:
    - Follow RESTful API design principles.
    - Use unified response format ApiResponse.
    - Add Swagger documentation annotations.