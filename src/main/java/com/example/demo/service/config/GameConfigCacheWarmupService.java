/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 11:00:00
 */
package com.example.demo.service.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.example.demo.config.CacheConstants;
import com.example.demo.entity.config.*;
import com.example.demo.repository.config.*;
import com.example.demo.repository.*;

/**
 * 游戏配置缓存预热服务
 * 在应用启动时预加载常用配置到Redis缓存
 */
@Service
public class GameConfigCacheWarmupService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(GameConfigCacheWarmupService.class);

    @Autowired
    private GameConfigService gameConfigService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private GameModeRepository gameModeRepository;
    
    @Autowired
    private GameLanguageRepository gameLanguageRepository;
    
    @Autowired
    private GameCategoryRepository gameCategoryRepository;
    
    @Autowired
    private GameDifficultyRepository gameDifficultyRepository;
    
    @Autowired
    private GameConfigCombinationRepository gameConfigCombinationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始游戏配置缓存预热...");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 预热所有基础配置列表
            warmupBasicConfigs();
            
            // 预热层级关系查询
            warmupHierarchicalQueries();
            
            long endTime = System.currentTimeMillis();
            logger.info("游戏配置缓存预热完成，耗时: {}ms", endTime - startTime);
            
        } catch (Exception e) {
            logger.error("游戏配置缓存预热失败", e);
        }
    }

    /**
     * 预热基础配置列表
     */
    private void warmupBasicConfigs() {
        logger.info("预热基础配置列表...");
        
        // 预热所有模式（包括非激活的）
        List<GameMode> modes = gameConfigService.getAllModes();
        logger.info("预热游戏模式（全量）: {} 个", modes.size());
        
        // 预热激活的模式
        List<GameMode> activeModes = gameConfigService.getAllActiveModes();
        logger.info("预热游戏模式（激活）: {} 个", activeModes.size());
        
        // 预热所有语言（包括非激活的）
        List<GameLanguage> languages = gameConfigService.getAllLanguages();
        logger.info("预热游戏语言（全量）: {} 个", languages.size());
        
        // 预热激活的语言
        List<GameLanguage> activeLanguages = gameConfigService.getAllActiveLanguages();
        logger.info("预热游戏语言（激活）: {} 个", activeLanguages.size());
        
        // 预热所有类型（包括非激活的）
        List<GameCategory> categories = gameConfigService.getAllCategories();
        logger.info("预热游戏类型（全量）: {} 个", categories.size());
        
        // 预热激活的类型
        List<GameCategory> activeCategories = gameConfigService.getAllActiveCategories();
        logger.info("预热游戏类型（激活）: {} 个", activeCategories.size());
        
        // 预热所有难度（包括非激活的）
        List<GameDifficulty> difficulties = gameConfigService.getAllDifficulties();
        logger.info("预热游戏难度（全量）: {} 个", difficulties.size());
        
        // 预热激活的难度
        List<GameDifficulty> activeDifficulties = gameConfigService.getAllActiveDifficulties();
        logger.info("预热游戏难度（激活）: {} 个", activeDifficulties.size());
        
        // 预热所有配置组合（包括非激活的）
        List<GameConfigCombination> combinations = gameConfigService.getAllCombinations();
        logger.info("预热配置组合（全量）: {} 个", combinations.size());
        
        // 预热激活的配置组合
        List<GameConfigCombination> activeCombinations = gameConfigService.getAllActiveCombinations();
        logger.info("预热配置组合（激活）: {} 个", activeCombinations.size());
    }

    /**
     * 预热层级关系查询
     */
    private void warmupHierarchicalQueries() {
        logger.info("预热层级关系查询...");
        
        // 使用全量配置进行层级关系预热，确保覆盖所有可能的查询
        List<GameMode> modes = gameConfigService.getAllModes();
        List<GameLanguage> languages = gameConfigService.getAllLanguages();
        List<GameCategory> categories = gameConfigService.getAllCategories();
        
        // 预热 模式 -> 语言 查询
        for (GameMode mode : modes) {
            gameConfigService.getLanguagesByMode(mode.getName());
        }
        
        // 预热 语言 -> 类型 查询
        for (GameLanguage language : languages) {
            gameConfigService.getCategoriesByLanguage(language.getName());
        }
        
        // 预热 类型 -> 难度 查询
        for (GameCategory category : categories) {
            gameConfigService.getDifficultiesByCategory(category.getName());
        }
        
        logger.info("层级关系查询预热完成");
    }

    /**
     * 手动触发缓存预热（可用于运行时刷新缓存）
     */
    public void manualWarmup() {
        logger.info("手动触发缓存预热（包括非激活配置）...");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 直接操作缓存，避免AOP代理问题
            manualWarmupBasicConfigs();
            
            // 预热层级关系查询  
            warmupHierarchicalQueries();
            
            long endTime = System.currentTimeMillis();
            logger.info("手动缓存预热完成，耗时: {}ms", endTime - startTime);
            
        } catch (Exception e) {
            logger.error("手动缓存预热失败", e);
            throw new RuntimeException("缓存预热失败", e);
        }
    }
    
    /**
     * 手动预热基础配置列表（直接操作缓存）
     */
    private void manualWarmupBasicConfigs() {
        logger.info("手动预热基础配置列表...");
        
        Cache gameConfigListCache = cacheManager.getCache(CacheConstants.GAME_CONFIG_LIST);
        if (gameConfigListCache == null) {
            logger.error("缓存 {} 不存在，无法进行预热", CacheConstants.GAME_CONFIG_LIST);
            throw new RuntimeException("缓存管理器未正确配置");
        }
        
        // 游戏模式缓存预热
        try {
            List<GameMode> allModes = gameModeRepository.findAllByOrderBySortOrder();
            List<GameMode> activeModes = gameModeRepository.findAllActiveOrderBySortOrder();
            
            gameConfigListCache.put(CacheConstants.ALL_MODES, allModes);
            logger.info("成功设置全量游戏模式缓存: {} 个", allModes.size());
            
            gameConfigListCache.put(CacheConstants.ALL_MODES_ACTIVE, activeModes);
            logger.info("成功设置激活游戏模式缓存: {} 个", activeModes.size());
            
            logger.info("游戏模式缓存预热完成: 全量 {} 个, 激活 {} 个", allModes.size(), activeModes.size());
        } catch (Exception e) {
            logger.error("游戏模式缓存预热失败", e);
            throw new RuntimeException("游戏模式缓存预热失败", e);
        }
        
        // 游戏语言缓存预热
        try {
            List<GameLanguage> allLanguages = gameLanguageRepository.findAllByOrderBySortOrder();
            List<GameLanguage> activeLanguages = gameLanguageRepository.findAllActiveOrderBySortOrder();
            
            gameConfigListCache.put(CacheConstants.ALL_LANGUAGES, allLanguages);
            logger.info("成功设置全量游戏语言缓存: {} 个", allLanguages.size());
            
            gameConfigListCache.put(CacheConstants.ALL_LANGUAGES_ACTIVE, activeLanguages);
            logger.info("成功设置激活游戏语言缓存: {} 个", activeLanguages.size());
            
            logger.info("游戏语言缓存预热完成: 全量 {} 个, 激活 {} 个", allLanguages.size(), activeLanguages.size());
        } catch (Exception e) {
            logger.error("游戏语言缓存预热失败", e);
            throw new RuntimeException("游戏语言缓存预热失败", e);
        }
        
        // 游戏类型缓存预热
        try {
            List<GameCategory> allCategories = gameCategoryRepository.findAllByOrderBySortOrder();
            List<GameCategory> activeCategories = gameCategoryRepository.findAllActiveOrderBySortOrder();
            
            gameConfigListCache.put(CacheConstants.ALL_CATEGORIES, allCategories);
            logger.info("成功设置全量游戏类型缓存: {} 个", allCategories.size());
            
            gameConfigListCache.put(CacheConstants.ALL_CATEGORIES_ACTIVE, activeCategories);
            logger.info("成功设置激活游戏类型缓存: {} 个", activeCategories.size());
            
            logger.info("游戏类型缓存预热完成: 全量 {} 个, 激活 {} 个", allCategories.size(), activeCategories.size());
        } catch (Exception e) {
            logger.error("游戏类型缓存预热失败", e);
            throw new RuntimeException("游戏类型缓存预热失败", e);
        }
        
        // 游戏难度缓存预热
        try {
            List<GameDifficulty> allDifficulties = gameDifficultyRepository.findAllByOrderBySortOrder();
            List<GameDifficulty> activeDifficulties = gameDifficultyRepository.findAllActiveOrderByLevelValue();
            
            gameConfigListCache.put(CacheConstants.ALL_DIFFICULTIES, allDifficulties);
            logger.info("成功设置全量游戏难度缓存: {} 个", allDifficulties.size());
            
            gameConfigListCache.put(CacheConstants.ALL_DIFFICULTIES_ACTIVE, activeDifficulties);
            logger.info("成功设置激活游戏难度缓存: {} 个", activeDifficulties.size());
            
            logger.info("游戏难度缓存预热完成: 全量 {} 个, 激活 {} 个", allDifficulties.size(), activeDifficulties.size());
        } catch (Exception e) {
            logger.error("游戏难度缓存预热失败", e);
            throw new RuntimeException("游戏难度缓存预热失败", e);
        }
        
        // 配置组合缓存预热
        try {
            List<GameConfigCombination> allCombinations = gameConfigCombinationRepository.findAll();
            List<GameConfigCombination> activeCombinations = gameConfigCombinationRepository.findAllActive();
            
            gameConfigListCache.put(CacheConstants.ALL_COMBINATIONS, allCombinations);
            logger.info("成功设置全量配置组合缓存: {} 个", allCombinations.size());
            
            gameConfigListCache.put(CacheConstants.ALL_COMBINATIONS_ACTIVE, activeCombinations);
            logger.info("成功设置激活配置组合缓存: {} 个", activeCombinations.size());
            
            logger.info("配置组合缓存预热完成: 全量 {} 个, 激活 {} 个", allCombinations.size(), activeCombinations.size());
        } catch (Exception e) {
            logger.error("配置组合缓存预热失败", e);
            throw new RuntimeException("配置组合缓存预热失败", e);
        }
        
        logger.info("手动预热基础配置列表完成");
    }
} 