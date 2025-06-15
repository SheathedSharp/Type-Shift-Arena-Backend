/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.service.config;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.CacheConstants;
import com.example.demo.entity.config.*;
import com.example.demo.model.dto.config.*;
import com.example.demo.repository.config.*;

@Service
@Transactional
public class GameConfigService {

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

    // =========================== 游戏模式管理 ===========================
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'")
    public List<GameMode> getAllActiveModes() {
        return gameModeRepository.findAllActiveOrderBySortOrder();
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.MODE_PREFIX + "' + #name")
    public Optional<GameMode> getModeByName(String name) {
        return gameModeRepository.findByName(name);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameMode createMode(GameMode mode) {
        return gameModeRepository.save(mode);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.MODE_PREFIX + "' + #mode.name"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameMode updateMode(GameMode mode) {
        return gameModeRepository.save(mode);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_MODES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public void deleteMode(String id) {
        gameModeRepository.deleteById(id);
    }

    // =========================== 游戏语言管理 ===========================
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_LANGUAGES + "'")
    public List<GameLanguage> getAllActiveLanguages() {
        return gameLanguageRepository.findAllActiveOrderBySortOrder();
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.LANGUAGE_PREFIX + "name:' + #name")
    public Optional<GameLanguage> getLanguageByName(String name) {
        return gameLanguageRepository.findByName(name);
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.LANGUAGE_PREFIX + "code:' + #code")
    public Optional<GameLanguage> getLanguageByCode(String code) {
        return gameLanguageRepository.findByCode(code);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_LANGUAGES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameLanguage createLanguage(GameLanguage language) {
        return gameLanguageRepository.save(language);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.LANGUAGE_PREFIX + "name:' + #language.name"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.LANGUAGE_PREFIX + "code:' + #language.code"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_LANGUAGES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameLanguage updateLanguage(GameLanguage language) {
        return gameLanguageRepository.save(language);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_LANGUAGES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public void deleteLanguage(String id) {
        gameLanguageRepository.deleteById(id);
    }

    // =========================== 游戏类型管理 ===========================
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_CATEGORIES + "'")
    public List<GameCategory> getAllActiveCategories() {
        return gameCategoryRepository.findAllActiveOrderBySortOrder();
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.CATEGORY_PREFIX + "' + #name")
    public Optional<GameCategory> getCategoryByName(String name) {
        return gameCategoryRepository.findByName(name);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_CATEGORIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameCategory createCategory(GameCategory category) {
        return gameCategoryRepository.save(category);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.CATEGORY_PREFIX + "' + #category.name"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_CATEGORIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameCategory updateCategory(GameCategory category) {
        return gameCategoryRepository.save(category);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_CATEGORIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public void deleteCategory(String id) {
        gameCategoryRepository.deleteById(id);
    }

    // =========================== 游戏难度管理 ===========================
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_DIFFICULTIES + "'")
    public List<GameDifficulty> getAllActiveDifficulties() {
        return gameDifficultyRepository.findAllActiveOrderByLevelValue();
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.DIFFICULTY_PREFIX + "' + #name")
    public Optional<GameDifficulty> getDifficultyByName(String name) {
        return gameDifficultyRepository.findByName(name);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_DIFFICULTIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameDifficulty createDifficulty(GameDifficulty difficulty) {
        return gameDifficultyRepository.save(difficulty);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, key = "'" + CacheConstants.DIFFICULTY_PREFIX + "' + #difficulty.name"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_DIFFICULTIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public GameDifficulty updateDifficulty(GameDifficulty difficulty) {
        return gameDifficultyRepository.save(difficulty);
    }
    
    @Caching(evict = {
        @CacheEvict(value = CacheConstants.GAME_CONFIG, allEntries = true),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_DIFFICULTIES + "'"),
        @CacheEvict(value = CacheConstants.GAME_CONFIG_COMBINATION, allEntries = true)
    })
    public void deleteDifficulty(String id) {
        gameDifficultyRepository.deleteById(id);
    }

    // =========================== 配置组合管理 ===========================
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.ALL_COMBINATIONS + "'")
    public List<GameConfigCombination> getAllActiveCombinations() {
        return gameConfigCombinationRepository.findAllActive();
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_COMBINATION, 
               key = "'" + CacheConstants.COMBINATION_PREFIX + "' + #modeName + ':' + #languageName + ':' + #categoryName + ':' + #difficultyName")
    public Optional<GameConfigCombination> getCombinationByConfig(String modeName, String languageName, 
                                                                  String categoryName, String difficultyName) {
        return gameConfigCombinationRepository.findByConfiguration(modeName, languageName, categoryName, difficultyName);
    }
    
    @Cacheable(value = CacheConstants.GAME_CONFIG_COMBINATION, 
               key = "'" + CacheConstants.CONFIG_VALIDATION + "' + #modeName + ':' + #languageName + ':' + #categoryName + ':' + #difficultyName")
    public boolean isConfigurationValid(String modeName, String languageName, 
                                       String categoryName, String difficultyName) {
        return gameConfigCombinationRepository.existsByConfiguration(modeName, languageName, categoryName, difficultyName);
    }
    
    // =========================== 层级查询方法 ===========================
    
    // 根据模式获取支持的语言
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.LANGUAGES_BY_MODE + "' + #modeName")
    public List<GameLanguage> getLanguagesByMode(String modeName) {
        return gameLanguageRepository.findByModeName(modeName);
    }
    
    // 根据语言获取支持的类型
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.CATEGORIES_BY_LANGUAGE + "' + #languageName")
    public List<GameCategory> getCategoriesByLanguage(String languageName) {
        return gameCategoryRepository.findByLanguageName(languageName);
    }
    
    // 根据类型获取支持的难度
    @Cacheable(value = CacheConstants.GAME_CONFIG_LIST, key = "'" + CacheConstants.DIFFICULTIES_BY_CATEGORY + "' + #categoryName")
    public List<GameDifficulty> getDifficultiesByCategory(String categoryName) {
        return gameDifficultyRepository.findByCategoryName(categoryName);
    }
    
    // 根据模式和语言获取配置组合
    @Cacheable(value = CacheConstants.GAME_CONFIG_COMBINATION, key = "'" + CacheConstants.COMBINATIONS_BY_MODE_LANGUAGE + "' + #modeName + ':' + #languageName")
    public List<GameConfigCombination> getCombinationsByModeAndLanguage(String modeName, String languageName) {
        return gameConfigCombinationRepository.findByModeNameAndLanguageName(modeName, languageName);
    }
    
    // 根据语言和类型获取配置组合
    @Cacheable(value = CacheConstants.GAME_CONFIG_COMBINATION, key = "'" + CacheConstants.COMBINATIONS_BY_LANGUAGE_CATEGORY + "' + #languageName + ':' + #categoryName")
    public List<GameConfigCombination> getCombinationsByLanguageAndCategory(String languageName, String categoryName) {
        return gameConfigCombinationRepository.findByLanguageNameAndCategoryName(languageName, categoryName);
    }
    
    // 根据类型和难度获取配置组合
    @Cacheable(value = CacheConstants.GAME_CONFIG_COMBINATION, key = "'" + CacheConstants.COMBINATIONS_BY_CATEGORY_DIFFICULTY + "' + #categoryName + ':' + #difficultyName")
    public List<GameConfigCombination> getCombinationsByCategoryAndDifficulty(String categoryName, String difficultyName) {
        return gameConfigCombinationRepository.findByCategoryNameAndDifficultyName(categoryName, difficultyName);
    }
} 