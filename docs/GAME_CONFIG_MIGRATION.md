# 游戏配置动态管理系统迁移指南

## 概述

本项目已成功将游戏核心配置参数从硬编码枚举迁移到动态数据库管理系统，实现了以下四个配置项的动态化管理：

1. **游戏模式** (Game Mode) - 新增配置项
2. **游戏语言** (Game Language) - 从 `TextLanguage` 枚举迁移
3. **游戏类型** (Game Category) - 从 `TextCategory` 枚举迁移  
4. **游戏难度** (Game Difficulty) - 从硬编码字符串迁移

## 数据库结构

### 核心配置表
- `game_modes` - 游戏模式配置表
- `game_languages` - 游戏语言配置表
- `game_categories` - 游戏类型配置表
- `game_difficulties` - 游戏难度配置表

### 关系表
- `game_mode_languages` - 模式-语言关联表
- `game_language_categories` - 语言-类型关联表
- `game_category_difficulties` - 类型-难度关联表
- `game_config_combinations` - 完整配置组合表

### 层级关系
```
游戏模式 (Game Mode)
    ↓ 多对多关系
游戏语言 (Game Language)
    ↓ 多对多关系
游戏类型 (Game Category)
    ↓ 多对多关系
游戏难度 (Game Difficulty)
```

## 数据库初始化

### 1. 创建表结构
```bash
mysql -u root -p myappdb < sql/create_game_config_tables.sql
```

### 2. 初始化数据
```bash
mysql -u root -p myappdb < sql/init_game_config_data.sql
```

## API 接口

### 配置管理API

#### 游戏模式管理
- `GET /api/config/modes` - 获取所有激活的游戏模式
- `GET /api/config/modes/{name}` - 根据名称获取游戏模式
- `POST /api/config/modes` - 创建新的游戏模式
- `PUT /api/config/modes/{id}` - 更新游戏模式
- `DELETE /api/config/modes/{id}` - 删除游戏模式

#### 游戏语言管理
- `GET /api/config/languages` - 获取所有激活的游戏语言
- `GET /api/config/languages/{name}` - 根据名称获取游戏语言
- `GET /api/config/languages/code/{code}` - 根据代码获取游戏语言
- `POST /api/config/languages` - 创建新的游戏语言
- `PUT /api/config/languages/{id}` - 更新游戏语言
- `DELETE /api/config/languages/{id}` - 删除游戏语言

#### 游戏类型管理
- `GET /api/config/categories` - 获取所有激活的游戏类型
- `GET /api/config/categories/{name}` - 根据名称获取游戏类型
- `POST /api/config/categories` - 创建新的游戏类型
- `PUT /api/config/categories/{id}` - 更新游戏类型
- `DELETE /api/config/categories/{id}` - 删除游戏类型

#### 游戏难度管理
- `GET /api/config/difficulties` - 获取所有激活的游戏难度
- `GET /api/config/difficulties/{name}` - 根据名称获取游戏难度
- `POST /api/config/difficulties` - 创建新的游戏难度
- `PUT /api/config/difficulties/{id}` - 更新游戏难度
- `DELETE /api/config/difficulties/{id}` - 删除游戏难度

### 层级查询API

#### 配置组合验证
- `GET /api/config/combinations/validate?mode={mode}&language={language}&category={category}&difficulty={difficulty}` - 验证配置组合是否有效

#### 层级关系查询
- `GET /api/config/modes/{modeName}/languages` - 获取指定模式支持的语言
- `GET /api/config/languages/{languageName}/categories` - 获取指定语言支持的类型
- `GET /api/config/categories/{categoryName}/difficulties` - 获取指定类型支持的难度

## 代码迁移

### 原有枚举类保留
为了保持向后兼容，原有的枚举类仍然保留：
- `TextLanguage` 枚举
- `TextCategory` 枚举

### 新增实体类
- `GameMode` - 游戏模式实体
- `GameLanguage` - 游戏语言实体
- `GameCategory` - 游戏类型实体
- `GameDifficulty` - 游戏难度实体
- `GameConfigCombination` - 配置组合实体

### 服务层
- `GameConfigService` - 配置管理核心服务
- `GameConfigAdapter` - 配置适配服务（兼容旧代码）

### 控制器层
- `GameConfigController` - 配置管理API控制器

## 使用示例

### 1. 获取所有游戏模式
```java
@Autowired
private GameConfigService gameConfigService;

public List<GameMode> getAllModes() {
    return gameConfigService.getAllActiveModes();
}
```

### 2. 验证配置组合
```java
@Autowired
private GameConfigService gameConfigService;

public boolean validateConfig(String mode, String language, String category, String difficulty) {
    return gameConfigService.isConfigurationValid(mode, language, category, difficulty);
}
```

### 3. 层级查询
```java
@Autowired
private GameConfigService gameConfigService;

// 根据模式获取支持的语言
public List<GameLanguage> getLanguagesByMode(String modeName) {
    return gameConfigService.getLanguagesByMode(modeName);
}

// 根据语言获取支持的类型
public List<GameCategory> getCategoriesByLanguage(String languageName) {
    return gameConfigService.getCategoriesByLanguage(languageName);
}

// 根据类型获取支持的难度
public List<GameDifficulty> getDifficultiesByCategory(String categoryName) {
    return gameConfigService.getDifficultiesByCategory(categoryName);
}
```

### 4. 兼容旧代码
```java
@Autowired
private GameConfigAdapter gameConfigAdapter;

// 将枚举转换为配置名称
public String convertLanguage(TextLanguage language) {
    return gameConfigAdapter.getLanguageName(language);
}

// 将配置名称转换为枚举
public TextLanguage convertLanguageName(String languageName) {
    return gameConfigAdapter.getTextLanguage(languageName);
}

// 验证旧版本配置
public boolean validateOldConfig(TextLanguage language, TextCategory category, String difficulty) {
    return gameConfigAdapter.isConfigurationValid(language, category, difficulty);
}
```

## 配置管理

### 1. 添加新的游戏模式
```json
POST /api/config/modes
{
    "name": "TOURNAMENT",
    "displayName": "锦标赛模式",
    "description": "竞技锦标赛模式，用于大型赛事",
    "isActive": true,
    "sortOrder": 6
}
```

### 2. 添加新的游戏语言
```json
POST /api/config/languages
{
    "code": "fr",
    "name": "FRENCH",
    "displayName": "Français",
    "isActive": true,
    "sortOrder": 5
}
```

### 3. 添加新的游戏类型
```json
POST /api/config/categories
{
    "name": "TECHNICAL_WRITING",
    "displayName": "技术写作",
    "description": "技术文档和API文档，适合技术人员练习",
    "isActive": true,
    "sortOrder": 7
}
```

### 4. 添加新的游戏难度
```json
POST /api/config/difficulties
{
    "name": "NIGHTMARE",
    "displayName": "噩梦",
    "description": "噩梦难度，终极挑战",
    "levelValue": 6,
    "isActive": true,
    "sortOrder": 6
}
```

## 前端集成

### 1. 获取配置选项
```javascript
// 获取所有游戏模式
const modes = await fetch('/api/config/modes').then(res => res.json());

// 根据选择的模式获取支持的语言
const languages = await fetch(`/api/config/modes/${selectedMode}/languages`).then(res => res.json());

// 根据选择的语言获取支持的类型
const categories = await fetch(`/api/config/languages/${selectedLanguage}/categories`).then(res => res.json());

// 根据选择的类型获取支持的难度
const difficulties = await fetch(`/api/config/categories/${selectedCategory}/difficulties`).then(res => res.json());
```

### 2. 配置验证
```javascript
const validateConfig = async (mode, language, category, difficulty) => {
    const response = await fetch(`/api/config/combinations/validate?mode=${mode}&language=${language}&category=${category}&difficulty=${difficulty}`);
    const result = await response.json();
    return result.valid;
};
```

## 注意事项

1. **向后兼容**：所有现有的API接口都继续工作，不会破坏现有功能
2. **数据完整性**：配置组合表确保只有有效的配置组合被存储
3. **性能优化**：通过索引和预计算的组合表提高查询性能
4. **扩展性**：可以轻松添加新的配置项和关系
5. **管理便捷**：通过API可以在线管理所有配置，无需重启应用

## 总结

通过此次重构，我们成功实现了：
- ✅ 游戏核心配置的动态化管理
- ✅ 多对多层级关系的正确实现
- ✅ 完整的CRUD操作支持
- ✅ 向后兼容性保证
- ✅ 性能优化和扩展性设计

这个系统为游戏的持续发展和配置管理提供了强大的基础设施。 