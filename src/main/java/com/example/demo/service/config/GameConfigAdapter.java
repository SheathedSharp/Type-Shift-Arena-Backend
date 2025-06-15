/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.service.config;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.config.*;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;

/**
 * 游戏配置适配服务
 * 用于在旧的枚举系统和新的动态配置系统之间提供兼容性
 */
@Service
public class GameConfigAdapter {

    @Autowired
    private GameConfigService gameConfigService;

    // =========================== 语言适配方法 ===========================
    
    /**
     * 将枚举语言转换为动态配置语言名称
     */
    public String getLanguageName(TextLanguage textLanguage) {
        if (textLanguage == null) return null;
        return textLanguage.name(); // CHINESE, ENGLISH, JAPANESE, KOREAN
    }
    
    /**
     * 将动态配置语言名称转换为枚举语言
     */
    public TextLanguage getTextLanguage(String languageName) {
        if (languageName == null) return null;
        try {
            return TextLanguage.valueOf(languageName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * 根据语言代码获取动态配置语言
     */
    public Optional<GameLanguage> getLanguageByCode(String code) {
        return gameConfigService.getLanguageByCode(code);
    }
    
    /**
     * 获取所有激活的语言并转换为枚举列表
     */
    public List<TextLanguage> getAllActiveTextLanguages() {
        return gameConfigService.getAllActiveLanguages().stream()
                .map(gl -> getTextLanguage(gl.getName()))
                .filter(tl -> tl != null)
                .collect(Collectors.toList());
    }

    // =========================== 类型适配方法 ===========================
    
    /**
     * 将枚举类型转换为动态配置类型名称
     */
    public String getCategoryName(TextCategory textCategory) {
        if (textCategory == null) return null;
        return textCategory.name(); // DAILY_CHAT, ACADEMIC_WRITING, etc.
    }
    
    /**
     * 将动态配置类型名称转换为枚举类型
     */
    public TextCategory getTextCategory(String categoryName) {
        if (categoryName == null) return null;
        try {
            return TextCategory.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * 获取所有激活的类型并转换为枚举列表
     */
    public List<TextCategory> getAllActiveTextCategories() {
        return gameConfigService.getAllActiveCategories().stream()
                .map(gc -> getTextCategory(gc.getName()))
                .filter(tc -> tc != null)
                .collect(Collectors.toList());
    }

    // =========================== 难度适配方法 ===========================
    
    /**
     * 根据难度名称获取动态配置难度
     */
    public Optional<GameDifficulty> getDifficultyByName(String difficultyName) {
        return gameConfigService.getDifficultyByName(difficultyName);
    }
    
    /**
     * 获取所有激活的难度名称
     */
    public List<String> getAllActiveDifficultyNames() {
        return gameConfigService.getAllActiveDifficulties().stream()
                .map(gd -> gd.getName())
                .collect(Collectors.toList());
    }

    // =========================== 配置验证方法 ===========================
    
    /**
     * 验证配置组合是否有效（兼容旧版本API）
     */
    public boolean isConfigurationValid(TextLanguage language, TextCategory category, String difficulty) {
        return isConfigurationValid("CASUAL", getLanguageName(language), getCategoryName(category), difficulty);
    }
    
    /**
     * 验证完整配置组合是否有效
     */
    public boolean isConfigurationValid(String modeName, String languageName, String categoryName, String difficultyName) {
        return gameConfigService.isConfigurationValid(modeName, languageName, categoryName, difficultyName);
    }

    // =========================== 层级查询适配方法 ===========================
    
    /**
     * 根据语言获取支持的类型（兼容旧版本）
     */
    public List<TextCategory> getCategoriesByLanguage(TextLanguage language) {
        String languageName = getLanguageName(language);
        if (languageName == null) return List.of();
        
        return gameConfigService.getCategoriesByLanguage(languageName).stream()
                .map(gc -> getTextCategory(gc.getName()))
                .filter(tc -> tc != null)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据类型获取支持的难度
     */
    public List<String> getDifficultiesByCategory(TextCategory category) {
        String categoryName = getCategoryName(category);
        if (categoryName == null) return List.of();
        
        return gameConfigService.getDifficultiesByCategory(categoryName).stream()
                .map(gd -> gd.getName())
                .collect(Collectors.toList());
    }
    
    /**
     * 根据语言和类型获取支持的难度
     */
    public List<String> getDifficultiesByLanguageAndCategory(TextLanguage language, TextCategory category) {
        String languageName = getLanguageName(language);
        String categoryName = getCategoryName(category);
        
        if (languageName == null || categoryName == null) return List.of();
        
        return gameConfigService.getCombinationsByLanguageAndCategory(languageName, categoryName).stream()
                .map(gcc -> gcc.getGameDifficulty().getName())
                .distinct()
                .collect(Collectors.toList());
    }

    // =========================== 模式相关方法 ===========================
    
    /**
     * 获取默认游戏模式（用于兼容不需要模式的旧版本API）
     */
    public String getDefaultModeName() {
        return "CASUAL"; // 默认使用休闲模式
    }
    
    /**
     * 根据排位状态获取对应模式
     */
    public String getModeByRanked(boolean isRanked) {
        return isRanked ? "RANKED" : "CASUAL";
    }
    
    /**
     * 获取所有激活的模式名称
     */
    public List<String> getAllActiveModeNames() {
        return gameConfigService.getAllActiveModes().stream()
                .map(gm -> gm.getName())
                .collect(Collectors.toList());
    }
} 