# 游戏配置Redis缓存集成指南

## 概述

本文档描述了游戏配置系统中Redis缓存的集成方案，用于提高配置数据的查询性能。

## 架构设计

### 缓存策略

1. **缓存层级**
   - 一级缓存：Redis内存缓存
   - 二级缓存：MySQL数据库

2. **缓存分类**
   - `gameConfig`：单个配置项缓存（2小时TTL）
   - `gameConfigList`：配置列表缓存（30分钟TTL）
   - `gameConfigCombination`：配置组合缓存（1小时TTL）

3. **缓存键设计**
   ```
   game_config_::mode:CLASSIC
   game_config_::language:name:CHINESE
   game_config_list::all_modes
   game_config_combination::combination:CLASSIC:CHINESE:LITERATURE:EASY
   ```

## 配置说明

### Redis连接配置

```properties
# Redis基础配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0
spring.redis.timeout=5000ms

# 连接池配置
spring.redis.lettuce.pool.max-active=200
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.max-idle=10
spring.redis.lettuce.pool.min-idle=0

# 缓存配置
spring.cache.type=redis
spring.cache.redis.time-to-live=3600000
spring.cache.redis.key-prefix=game_config_
spring.cache.redis.use-key-prefix=true
```

### 缓存注解使用

1. **@Cacheable**：查询时缓存结果
   ```java
   @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'")
   public List<GameMode> getAllActiveModes() {
       return gameModeRepository.findAllActiveOrderBySortOrder();
   }
   ```

2. **@CacheEvict**：更新/删除时清除缓存
   ```java
   @Caching(evict = {
       @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'"),
       @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
   })
   public GameMode createMode(GameMode mode) {
       return gameModeRepository.save(mode);
   }
   ```

## 缓存管理

### 自动预热

应用启动时自动预热常用配置：

1. **基础配置列表**
   - 所有游戏模式
   - 所有游戏语言
   - 所有游戏类型
   - 所有游戏难度
   - 所有配置组合

2. **层级关系查询**
   - 模式 → 语言映射
   - 语言 → 类型映射
   - 类型 → 难度映射

3. **常用配置组合**
   - 热门配置验证
   - 配置组合查询

### 手动管理API

| API | 方法 | 描述 |
|-----|------|------|
| `/api/game-config/cache/warmup` | POST | 手动触发缓存预热 |
| `/api/game-config/cache/clear` | DELETE | 清空所有配置缓存 |
| `/api/game-config/cache/stats` | GET | 获取缓存统计信息 |
| `/api/game-config/cache/content/{type}/{key}` | GET | 查看指定缓存内容 |

## 性能优化

### 缓存命中率优化

1. **预热策略**
   - 启动时预加载热点数据
   - 定期刷新缓存内容

2. **TTL策略**
   - 基础配置：2小时（变更频率低）
   - 列表查询：30分钟（需要及时更新）
   - 组合查询：1小时（平衡性能和一致性）

3. **缓存更新策略**
   - 写入时清除相关缓存
   - 级联清除相关缓存键

### 内存使用优化

1. **序列化优化**
   - 使用JSON序列化减少内存占用
   - 配置类型信息保持反序列化正确性

2. **键值设计**
   - 使用有意义的键前缀
   - 避免键冲突和过长键名

## 监控和调试

### 缓存监控

1. **性能指标**
   - 缓存命中率
   - 平均响应时间
   - 内存使用情况

2. **业务指标**
   - 各类配置查询频率
   - 热点配置识别
   - 缓存失效频率

### 调试工具

1. **缓存统计API**
   ```bash
   # 查看缓存统计
   curl -X GET "http://localhost:8080/api/game-config/cache/stats"
   
   # 查看具体缓存内容
   curl -X GET "http://localhost:8080/api/game-config/cache/content/gameConfigList/all_modes"
   ```

2. **Redis命令行工具**
   ```bash
   # 连接Redis
   redis-cli
   
   # 查看所有游戏配置键
   keys "game_config_*"
   
   # 查看缓存内容
   get "game_config_::gameConfigList::all_modes"
   
   # 查看缓存过期时间
   ttl "game_config_::gameConfigList::all_modes"
   ```

## 故障处理

### 常见问题

1. **Redis连接失败**
   - 检查Redis服务状态
   - 验证连接配置
   - 查看防火墙设置

2. **缓存不一致**
   - 手动清除缓存
   - 重新触发预热
   - 检查缓存更新逻辑

3. **内存不足**
   - 优化TTL设置
   - 清理无用缓存
   - 调整Redis内存配置

### 故障恢复

1. **自动降级**
   - Redis不可用时自动查询数据库
   - 保证服务可用性

2. **手动恢复**
   ```bash
   # 清空缓存重新开始
   curl -X DELETE "http://localhost:8080/api/game-config/cache/clear"
   
   # 重新预热缓存
   curl -X POST "http://localhost:8080/api/game-config/cache/warmup"
   ```

## 部署注意事项

### 开发环境

```properties
# 开发环境Redis配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.database=0
```

### 生产环境

```properties
# 生产环境Redis配置
spring.redis.host=${REDIS_HOST:localhost}
spring.redis.port=${REDIS_PORT:6379}
spring.redis.password=${REDIS_PASSWORD:}
spring.redis.database=${REDIS_DATABASE:0}

# 连接池优化
spring.redis.lettuce.pool.max-active=200
spring.redis.lettuce.pool.max-idle=50
spring.redis.lettuce.pool.min-idle=10
```

### 集群部署

对于Redis集群部署，需要额外配置：

```properties
# Redis集群配置
spring.redis.cluster.nodes=${REDIS_CLUSTER_NODES}
spring.redis.cluster.max-redirects=3
```

## 最佳实践

1. **缓存键命名**
   - 使用有意义的前缀
   - 包含版本信息
   - 避免键冲突

2. **TTL设置**
   - 根据数据变更频率设置
   - 避免缓存雪崩
   - 合理设置过期时间

3. **缓存更新**
   - 及时清除相关缓存
   - 避免缓存穿透
   - 实现缓存预热

4. **监控告警**
   - 设置缓存命中率阈值
   - 监控Redis内存使用
   - 关注缓存异常情况

## 总结

Redis缓存集成大大提升了游戏配置查询的性能，通过合理的缓存策略和管理机制，能够保证数据的一致性和系统的高可用性。在实际使用中，需要根据业务特点调整缓存配置，并建立完善的监控和故障处理机制。 