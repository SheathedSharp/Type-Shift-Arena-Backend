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
import org.springframework.stereotype.Service;

import com.example.demo.entity.config.*;

/**
 * 游戏配置缓存预热服务
 * 在应用启动时预加载常用配置到Redis缓存
 */
@Service
public class GameConfigCacheWarmupService implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(GameConfigCacheWarmupService.class);

    @Autowired
    private GameConfigService gameConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("开始游戏配置缓存预热...");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // 预热所有基础配置列表
            warmupBasicConfigs();
            
            // 预热层级关系查询
            warmupHierarchicalQueries();
            
            // 预热常用配置组合
            warmupCommonCombinations();
            
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
        
        // 预热所有模式
        List<GameMode> modes = gameConfigService.getAllActiveModes();
        logger.info("预热游戏模式: {} 个", modes.size());
        
        // 预热所有语言
        List<GameLanguage> languages = gameConfigService.getAllActiveLanguages();
        logger.info("预热游戏语言: {} 个", languages.size());
        
        // 预热所有类型
        List<GameCategory> categories = gameConfigService.getAllActiveCategories();
        logger.info("预热游戏类型: {} 个", categories.size());
        
        // 预热所有难度
        List<GameDifficulty> difficulties = gameConfigService.getAllActiveDifficulties();
        logger.info("预热游戏难度: {} 个", difficulties.size());
        
        // 暂时跳过配置组合的预热，避免懒加载问题
        // List<GameConfigCombination> combinations = gameConfigService.getAllActiveCombinations();
        // logger.info("预热配置组合: {} 个", combinations.size());
        logger.info("跳过配置组合预热（避免懒加载问题）");
    }

    /**
     * 预热层级关系查询
     */
    private void warmupHierarchicalQueries() {
        logger.info("预热层级关系查询...");
        
        List<GameMode> modes = gameConfigService.getAllActiveModes();
        List<GameLanguage> languages = gameConfigService.getAllActiveLanguages();
        List<GameCategory> categories = gameConfigService.getAllActiveCategories();
        
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
     * 预热常用配置组合
     */
    private void warmupCommonCombinations() {
        logger.info("预热常用配置组合...");
        
        // 预热一些常用的配置组合验证
        String[] commonModes = {"RANKED", "CASUAL"};
        String[] commonLanguages = {"CHINESE", "ENGLISH"};
        String[] commonCategories = {"LITERATURE", "DAILY_CHAT"};
        String[] commonDifficulties = {"EASY", "MEDIUM", "HARD"};
        
        int validationCount = 0;
        for (String mode : commonModes) {
            for (String language : commonLanguages) {
                for (String category : commonCategories) {
                    for (String difficulty : commonDifficulties) {
                        gameConfigService.isConfigurationValid(mode, language, category, difficulty);
                        validationCount++;
                    }
                }
            }
        }
        
        logger.info("预热配置验证: {} 个组合", validationCount);
        
        // 预热模式-语言组合查询
        for (String mode : commonModes) {
            for (String language : commonLanguages) {
                gameConfigService.getCombinationsByModeAndLanguage(mode, language);
            }
        }
        
        // 预热语言-类型组合查询
        for (String language : commonLanguages) {
            for (String category : commonCategories) {
                gameConfigService.getCombinationsByLanguageAndCategory(language, category);
            }
        }
        
        logger.info("常用配置组合预热完成");
    }

    /**
     * 手动触发缓存预热（可用于运行时刷新缓存）
     */
    public void manualWarmup() {
        logger.info("手动触发缓存预热...");
        try {
            run(null);
        } catch (Exception e) {
            logger.error("手动缓存预热失败", e);
        }
    }
} 