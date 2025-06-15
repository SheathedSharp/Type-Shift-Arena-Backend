/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 12:30:00
 */
package com.example.demo.service.config;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.CacheConstants;
import com.example.demo.entity.config.*;
import com.example.demo.repository.config.*;
import com.example.demo.service.config.GameConfigCacheWarmupService;

@Service
@Transactional
public class GameConfigRelationService {

    private static final Logger logger = LoggerFactory.getLogger(GameConfigRelationService.class);

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
    
    @Autowired
    private GameConfigCacheWarmupService cacheWarmupService;

    // =========================== 模式-语言关系管理 ===========================

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean addModeLanguageRelation(String modeName, String languageName) {
        try {
            Optional<GameMode> modeOpt = gameModeRepository.findByName(modeName);
            Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
            
            if (modeOpt.isEmpty() || languageOpt.isEmpty()) {
                logger.warn("模式或语言不存在: {} - {}", modeName, languageName);
                return false;
            }
            
            GameMode mode = modeOpt.get();
            GameLanguage language = languageOpt.get();
            
            // 初始化懒加载集合
            if (mode.getSupportedLanguages() == null) {
                mode.setSupportedLanguages(new HashSet<>());
            }
            
            // 检查关系是否已存在
            if (mode.getSupportedLanguages().contains(language)) {
                logger.info("关系已存在: {} - {}", modeName, languageName);
                return false;
            }
            
            // 添加关系
            mode.getSupportedLanguages().add(language);
            gameModeRepository.save(mode);
            
            logger.info("成功添加模式-语言关系: {} - {}", modeName, languageName);
            return true;
            
        } catch (Exception e) {
            logger.error("添加模式-语言关系失败: {} - {}", modeName, languageName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean removeModeLanguageRelation(String modeName, String languageName) {
        try {
            Optional<GameMode> modeOpt = gameModeRepository.findByName(modeName);
            Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
            
            if (modeOpt.isEmpty() || languageOpt.isEmpty()) {
                logger.warn("模式或语言不存在: {} - {}", modeName, languageName);
                return false;
            }
            
            GameMode mode = modeOpt.get();
            GameLanguage language = languageOpt.get();
            
            if (mode.getSupportedLanguages() == null || !mode.getSupportedLanguages().contains(language)) {
                logger.info("关系不存在: {} - {}", modeName, languageName);
                return false;
            }
            
            // 删除关系
            mode.getSupportedLanguages().remove(language);
            gameModeRepository.save(mode);
            
            logger.info("成功删除模式-语言关系: {} - {}", modeName, languageName);
            return true;
            
        } catch (Exception e) {
            logger.error("删除模式-语言关系失败: {} - {}", modeName, languageName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean setModeLanguages(String modeName, List<String> languageNames) {
        try {
            Optional<GameMode> modeOpt = gameModeRepository.findByName(modeName);
            if (modeOpt.isEmpty()) {
                logger.warn("模式不存在: {}", modeName);
                return false;
            }
            
            GameMode mode = modeOpt.get();
            Set<GameLanguage> newLanguages = new HashSet<>();
            
            // 查找所有指定的语言
            for (String languageName : languageNames) {
                Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
                if (languageOpt.isEmpty()) {
                    logger.warn("语言不存在: {}", languageName);
                    return false;
                }
                newLanguages.add(languageOpt.get());
            }
            
            // 替换所有关系
            mode.setSupportedLanguages(newLanguages);
            gameModeRepository.save(mode);
            
            logger.info("成功为模式 {} 设置 {} 个支持的语言", modeName, languageNames.size());
            return true;
            
        } catch (Exception e) {
            logger.error("批量设置模式-语言关系失败: {}", modeName, e);
            return false;
        }
    }

    // =========================== 语言-类型关系管理 ===========================

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean addLanguageCategoryRelation(String languageName, String categoryName) {
        try {
            Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
            Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
            
            if (languageOpt.isEmpty() || categoryOpt.isEmpty()) {
                logger.warn("语言或类型不存在: {} - {}", languageName, categoryName);
                return false;
            }
            
            GameLanguage language = languageOpt.get();
            GameCategory category = categoryOpt.get();
            
            if (language.getSupportedCategories() == null) {
                language.setSupportedCategories(new HashSet<>());
            }
            
            if (language.getSupportedCategories().contains(category)) {
                logger.info("关系已存在: {} - {}", languageName, categoryName);
                return false;
            }
            
            language.getSupportedCategories().add(category);
            gameLanguageRepository.save(language);
            
            logger.info("成功添加语言-类型关系: {} - {}", languageName, categoryName);
            return true;
            
        } catch (Exception e) {
            logger.error("添加语言-类型关系失败: {} - {}", languageName, categoryName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean removeLanguageCategoryRelation(String languageName, String categoryName) {
        try {
            Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
            Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
            
            if (languageOpt.isEmpty() || categoryOpt.isEmpty()) {
                return false;
            }
            
            GameLanguage language = languageOpt.get();
            GameCategory category = categoryOpt.get();
            
            if (language.getSupportedCategories() == null || !language.getSupportedCategories().contains(category)) {
                return false;
            }
            
            language.getSupportedCategories().remove(category);
            gameLanguageRepository.save(language);
            
            logger.info("成功删除语言-类型关系: {} - {}", languageName, categoryName);
            return true;
            
        } catch (Exception e) {
            logger.error("删除语言-类型关系失败: {} - {}", languageName, categoryName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean setLanguageCategories(String languageName, List<String> categoryNames) {
        try {
            Optional<GameLanguage> languageOpt = gameLanguageRepository.findByName(languageName);
            if (languageOpt.isEmpty()) {
                return false;
            }
            
            GameLanguage language = languageOpt.get();
            Set<GameCategory> newCategories = new HashSet<>();
            
            for (String categoryName : categoryNames) {
                Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
                if (categoryOpt.isEmpty()) {
                    return false;
                }
                newCategories.add(categoryOpt.get());
            }
            
            language.setSupportedCategories(newCategories);
            gameLanguageRepository.save(language);
            
            logger.info("成功为语言 {} 设置 {} 个支持的类型", languageName, categoryNames.size());
            return true;
            
        } catch (Exception e) {
            logger.error("批量设置语言-类型关系失败: {}", languageName, e);
            return false;
        }
    }

    // =========================== 类型-难度关系管理 ===========================

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean addCategoryDifficultyRelation(String categoryName, String difficultyName) {
        try {
            Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
            Optional<GameDifficulty> difficultyOpt = gameDifficultyRepository.findByName(difficultyName);
            
            if (categoryOpt.isEmpty() || difficultyOpt.isEmpty()) {
                return false;
            }
            
            GameCategory category = categoryOpt.get();
            GameDifficulty difficulty = difficultyOpt.get();
            
            if (category.getSupportedDifficulties() == null) {
                category.setSupportedDifficulties(new HashSet<>());
            }
            
            if (category.getSupportedDifficulties().contains(difficulty)) {
                return false;
            }
            
            category.getSupportedDifficulties().add(difficulty);
            gameCategoryRepository.save(category);
            
            logger.info("成功添加类型-难度关系: {} - {}", categoryName, difficultyName);
            return true;
            
        } catch (Exception e) {
            logger.error("添加类型-难度关系失败: {} - {}", categoryName, difficultyName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean removeCategoryDifficultyRelation(String categoryName, String difficultyName) {
        try {
            Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
            Optional<GameDifficulty> difficultyOpt = gameDifficultyRepository.findByName(difficultyName);
            
            if (categoryOpt.isEmpty() || difficultyOpt.isEmpty()) {
                return false;
            }
            
            GameCategory category = categoryOpt.get();
            GameDifficulty difficulty = difficultyOpt.get();
            
            if (category.getSupportedDifficulties() == null || !category.getSupportedDifficulties().contains(difficulty)) {
                return false;
            }
            
            category.getSupportedDifficulties().remove(difficulty);
            gameCategoryRepository.save(category);
            
            logger.info("成功删除类型-难度关系: {} - {}", categoryName, difficultyName);
            return true;
            
        } catch (Exception e) {
            logger.error("删除类型-难度关系失败: {} - {}", categoryName, difficultyName, e);
            return false;
        }
    }

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public boolean setCategoryDifficulties(String categoryName, List<String> difficultyNames) {
        try {
            Optional<GameCategory> categoryOpt = gameCategoryRepository.findByName(categoryName);
            if (categoryOpt.isEmpty()) {
                return false;
            }
            
            GameCategory category = categoryOpt.get();
            Set<GameDifficulty> newDifficulties = new HashSet<>();
            
            for (String difficultyName : difficultyNames) {
                Optional<GameDifficulty> difficultyOpt = gameDifficultyRepository.findByName(difficultyName);
                if (difficultyOpt.isEmpty()) {
                    return false;
                }
                newDifficulties.add(difficultyOpt.get());
            }
            
            category.setSupportedDifficulties(newDifficulties);
            gameCategoryRepository.save(category);
            
            logger.info("成功为类型 {} 设置 {} 个支持的难度", categoryName, difficultyNames.size());
            return true;
            
        } catch (Exception e) {
            logger.error("批量设置类型-难度关系失败: {}", categoryName, e);
            return false;
        }
    }

    // =========================== 配置组合重建 ===========================

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public int rebuildConfigCombinations() {
        try {
            // 清空现有组合
            gameConfigCombinationRepository.deleteAll();
            
            int count = 0;
            List<GameMode> modes = gameModeRepository.findAllActiveOrderBySortOrder();
            
            for (GameMode mode : modes) {
                if (mode.getSupportedLanguages() != null) {
                    for (GameLanguage language : mode.getSupportedLanguages()) {
                        if (language.getSupportedCategories() != null) {
                            for (GameCategory category : language.getSupportedCategories()) {
                                if (category.getSupportedDifficulties() != null) {
                                    for (GameDifficulty difficulty : category.getSupportedDifficulties()) {
                                        GameConfigCombination combination = new GameConfigCombination();
                                        combination.setGameMode(mode);
                                        combination.setGameLanguage(language);
                                        combination.setGameCategory(category);
                                        combination.setGameDifficulty(difficulty);
                                        combination.setIsActive(true);
                                        
                                        gameConfigCombinationRepository.save(combination);
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            logger.info("重建配置组合表完成，生成 {} 个有效组合", count);
            return count;
            
        } catch (Exception e) {
            logger.error("重建配置组合表失败", e);
            throw new RuntimeException("重建配置组合表失败", e);
        }
    }

    // =========================== 缓存刷新 ===========================

    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public void refreshAllCache() {
        try {
            logger.info("开始刷新所有游戏配置缓存");
            
            // 手动触发缓存预热
            cacheWarmupService.manualWarmup();
            
            logger.info("游戏配置缓存刷新完成");
            
        } catch (Exception e) {
            logger.error("刷新缓存失败", e);
            throw new RuntimeException("刷新缓存失败", e);
        }
    }
} 