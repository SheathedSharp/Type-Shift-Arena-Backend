/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 11:00:00
 */
package com.example.demo.config;

/**
 * 缓存常量类
 */
public class CacheConstants {
    
    // 缓存名称
    public static final String GAME_CONFIG = "gameConfig";
    public static final String GAME_CONFIG_LIST = "gameConfigList";
    public static final String GAME_CONFIG_COMBINATION = "gameConfigCombination";
    
    // 缓存键前缀
    public static final String MODE_PREFIX = "mode:";
    public static final String LANGUAGE_PREFIX = "language:";
    public static final String CATEGORY_PREFIX = "category:";
    public static final String DIFFICULTY_PREFIX = "difficulty:";
    public static final String COMBINATION_PREFIX = "combination:";
    
    // 列表缓存键
    public static final String ALL_MODES = "all_modes";
    public static final String ALL_LANGUAGES = "all_languages";
    public static final String ALL_CATEGORIES = "all_categories";
    public static final String ALL_DIFFICULTIES = "all_difficulties";
    public static final String ALL_COMBINATIONS = "all_combinations";
    
    // 关系查询缓存键模板
    public static final String LANGUAGES_BY_MODE = "languages_by_mode:";
    public static final String CATEGORIES_BY_LANGUAGE = "categories_by_language:";
    public static final String DIFFICULTIES_BY_CATEGORY = "difficulties_by_category:";
    public static final String COMBINATIONS_BY_MODE_LANGUAGE = "combinations_by_mode_language:";
    public static final String COMBINATIONS_BY_LANGUAGE_CATEGORY = "combinations_by_language_category:";
    public static final String COMBINATIONS_BY_CATEGORY_DIFFICULTY = "combinations_by_category_difficulty:";
    
    // 配置验证缓存键模板
    public static final String CONFIG_VALIDATION = "config_validation:";
} 