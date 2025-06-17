/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 15:00:00
 */
package com.example.demo.controller.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.config.CacheConstants;
import com.example.demo.service.config.GameConfigCacheWarmupService;
import com.example.demo.service.config.GameConfigRelationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/game-config/cache")
@Tag(name = "游戏配置缓存管理", description = "管理游戏配置的Redis缓存")
public class GameConfigCacheController {

    private static final Logger logger = LoggerFactory.getLogger(GameConfigCacheController.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GameConfigCacheWarmupService cacheWarmupService;

    @Autowired
    private GameConfigRelationService relationService;

    @Operation(summary = "手动触发缓存预热", description = "清空现有缓存并重新预热所有游戏配置")
    @PostMapping("/warmup")
    public ResponseEntity<Map<String, Object>> warmupCache() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.info("开始手动触发缓存预热");
            
            // 先清空缓存
            clearAllGameConfigCache();
            
            // 手动预热
            cacheWarmupService.manualWarmup();
            
            result.put("success", true);
            result.put("message", "缓存预热成功");
            result.put("timestamp", System.currentTimeMillis());
            
            logger.info("缓存预热完成");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("缓存预热失败", e);
            result.put("success", false);
            result.put("message", "缓存预热失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "清空所有配置缓存", description = "清空所有游戏配置相关的Redis缓存")
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCache() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.info("开始清空所有游戏配置缓存");
            
            int clearedCount = clearAllGameConfigCache();
            
            result.put("success", true);
            result.put("message", "缓存清空成功");
            result.put("clearedCacheCount", clearedCount);
            result.put("timestamp", System.currentTimeMillis());
            
            logger.info("缓存清空完成，清理了 {} 个缓存项", clearedCount);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("清空缓存失败", e);
            result.put("success", false);
            result.put("message", "清空缓存失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "获取缓存统计信息", description = "获取当前缓存的统计信息")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取各类缓存的键数量
            Set<String> gameConfigKeys = redisTemplate.keys("game_config_::" + CacheConstants.GAME_CONFIG + "::*");
            Set<String> gameConfigListKeys = redisTemplate.keys("game_config_::" + CacheConstants.GAME_CONFIG_LIST + "::*");
            Set<String> gameConfigCombinationKeys = redisTemplate.keys("game_config_::" + CacheConstants.GAME_CONFIG_COMBINATION + "::*");
            
            stats.put("gameConfigCount", gameConfigKeys != null ? gameConfigKeys.size() : 0);
            stats.put("gameConfigListCount", gameConfigListKeys != null ? gameConfigListKeys.size() : 0);
            stats.put("gameConfigCombinationCount", gameConfigCombinationKeys != null ? gameConfigCombinationKeys.size() : 0);
            stats.put("totalCacheCount", 
                    (gameConfigKeys != null ? gameConfigKeys.size() : 0) +
                    (gameConfigListKeys != null ? gameConfigListKeys.size() : 0) +
                    (gameConfigCombinationKeys != null ? gameConfigCombinationKeys.size() : 0));
            
            result.put("success", true);
            result.put("stats", stats);
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("获取缓存统计失败", e);
            result.put("success", false);
            result.put("message", "获取缓存统计失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "查看指定缓存内容", description = "查看指定类型和键的缓存内容")
    @GetMapping("/content/{type}/{key}")
    public ResponseEntity<Map<String, Object>> getCacheContent(
            @PathVariable String type,
            @PathVariable String key) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String fullKey = "game_config_::" + type + "::" + key;
            Object cacheContent = redisTemplate.opsForValue().get(fullKey);
            
            result.put("success", true);
            result.put("key", fullKey);
            result.put("exists", cacheContent != null);
            result.put("content", cacheContent);
            
            if (cacheContent != null) {
                Long ttl = redisTemplate.getExpire(fullKey);
                result.put("ttlSeconds", ttl);
            }
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("获取缓存内容失败", e);
            result.put("success", false);
            result.put("message", "获取缓存内容失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "刷新所有缓存", description = "清空缓存并重新预热")
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshAllCache() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            logger.info("开始刷新所有游戏配置缓存");
            
            // 使用关系服务的刷新方法
            relationService.refreshAllCache();
            
            result.put("success", true);
            result.put("message", "缓存刷新成功");
            result.put("timestamp", System.currentTimeMillis());
            
            logger.info("缓存刷新完成");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            logger.error("刷新缓存失败", e);
            result.put("success", false);
            result.put("message", "刷新缓存失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 清空所有游戏配置相关的缓存
     */
    private int clearAllGameConfigCache() {
        int clearedCount = 0;
        
        try {
            // 清空Spring Cache管理的缓存
            if (cacheManager.getCache(CacheConstants.GAME_CONFIG) != null) {
                cacheManager.getCache(CacheConstants.GAME_CONFIG).clear();
                clearedCount++;
            }
            
            if (cacheManager.getCache(CacheConstants.GAME_CONFIG_LIST) != null) {
                cacheManager.getCache(CacheConstants.GAME_CONFIG_LIST).clear();
                clearedCount++;
            }
            
            if (cacheManager.getCache(CacheConstants.GAME_CONFIG_COMBINATION) != null) {
                cacheManager.getCache(CacheConstants.GAME_CONFIG_COMBINATION).clear();
                clearedCount++;
            }
            
            // 清空Redis中的游戏配置缓存键
            Set<String> allGameConfigKeys = redisTemplate.keys("game_config_*");
            if (allGameConfigKeys != null && !allGameConfigKeys.isEmpty()) {
                redisTemplate.delete(allGameConfigKeys);
                clearedCount += allGameConfigKeys.size();
            }
            
        } catch (Exception e) {
            logger.error("清空缓存时发生错误", e);
            throw e;
        }
        
        return clearedCount;
    }
} 