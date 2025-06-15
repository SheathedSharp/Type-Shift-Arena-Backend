/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 12:00:00
 */
package com.example.demo.controller.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.config.GameCategory;
import com.example.demo.entity.config.GameDifficulty;
import com.example.demo.entity.config.GameLanguage;
import com.example.demo.entity.config.GameMode;
import com.example.demo.service.config.GameConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/game-config/relations")
@Tag(name = "游戏配置关系查询", description = "查看各种配置项之间的多对多关系")
public class GameConfigRelationController {

    @Autowired
    private GameConfigService gameConfigService;

    @Operation(summary = "获取所有模式-语言关系", description = "返回完整的模式与语言的多对多关系映射")
    @GetMapping("/mode-language")
    public ResponseEntity<Map<String, Object>> getModeLanguageRelations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<GameMode> modes = gameConfigService.getAllActiveModes();
            Map<String, Object> relations = new HashMap<>();
            
            for (GameMode mode : modes) {
                List<GameLanguage> languages = gameConfigService.getLanguagesByMode(mode.getName());
                
                Map<String, Object> modeInfo = new HashMap<>();
                modeInfo.put("id", mode.getId());
                modeInfo.put("name", mode.getName());
                modeInfo.put("displayName", mode.getDisplayName());
                modeInfo.put("supportedLanguages", languages);
                
                relations.put(mode.getName(), modeInfo);
            }
            
            result.put("success", true);
            result.put("relations", relations);
            result.put("totalModes", modes.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取模式-语言关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "获取所有语言-类型关系", description = "返回完整的语言与类型的多对多关系映射")
    @GetMapping("/language-category")
    public ResponseEntity<Map<String, Object>> getLanguageCategoryRelations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<GameLanguage> languages = gameConfigService.getAllActiveLanguages();
            Map<String, Object> relations = new HashMap<>();
            
            for (GameLanguage language : languages) {
                List<GameCategory> categories = gameConfigService.getCategoriesByLanguage(language.getName());
                
                Map<String, Object> languageInfo = new HashMap<>();
                languageInfo.put("id", language.getId());
                languageInfo.put("name", language.getName());
                languageInfo.put("displayName", language.getDisplayName());
                languageInfo.put("code", language.getCode());
                languageInfo.put("supportedCategories", categories);
                
                relations.put(language.getName(), languageInfo);
            }
            
            result.put("success", true);
            result.put("relations", relations);
            result.put("totalLanguages", languages.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取语言-类型关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "获取所有类型-难度关系", description = "返回完整的类型与难度的多对多关系映射")
    @GetMapping("/category-difficulty")
    public ResponseEntity<Map<String, Object>> getCategoryDifficultyRelations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<GameCategory> categories = gameConfigService.getAllActiveCategories();
            Map<String, Object> relations = new HashMap<>();
            
            for (GameCategory category : categories) {
                List<GameDifficulty> difficulties = gameConfigService.getDifficultiesByCategory(category.getName());
                
                Map<String, Object> categoryInfo = new HashMap<>();
                categoryInfo.put("id", category.getId());
                categoryInfo.put("name", category.getName());
                categoryInfo.put("displayName", category.getDisplayName());
                categoryInfo.put("supportedDifficulties", difficulties);
                
                relations.put(category.getName(), categoryInfo);
            }
            
            result.put("success", true);
            result.put("relations", relations);
            result.put("totalCategories", categories.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取类型-难度关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "获取完整的关系矩阵", description = "返回所有配置项的完整关系矩阵")
    @GetMapping("/matrix")
    public ResponseEntity<Map<String, Object>> getCompleteRelationMatrix() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取基础数据
            List<GameMode> modes = gameConfigService.getAllActiveModes();
            List<GameLanguage> languages = gameConfigService.getAllActiveLanguages();
            List<GameCategory> categories = gameConfigService.getAllActiveCategories();
            List<GameDifficulty> difficulties = gameConfigService.getAllActiveDifficulties();
            
            // 构建关系矩阵
            Map<String, Object> matrix = new HashMap<>();
            
            // 模式-语言矩阵
            Map<String, Map<String, Boolean>> modeLanguageMatrix = new HashMap<>();
            for (GameMode mode : modes) {
                Map<String, Boolean> languageMap = new HashMap<>();
                List<GameLanguage> supportedLanguages = gameConfigService.getLanguagesByMode(mode.getName());
                
                for (GameLanguage language : languages) {
                    boolean isSupported = supportedLanguages.stream()
                        .anyMatch(sl -> sl.getName().equals(language.getName()));
                    languageMap.put(language.getName(), isSupported);
                }
                modeLanguageMatrix.put(mode.getName(), languageMap);
            }
            
            // 语言-类型矩阵
            Map<String, Map<String, Boolean>> languageCategoryMatrix = new HashMap<>();
            for (GameLanguage language : languages) {
                Map<String, Boolean> categoryMap = new HashMap<>();
                List<GameCategory> supportedCategories = gameConfigService.getCategoriesByLanguage(language.getName());
                
                for (GameCategory category : categories) {
                    boolean isSupported = supportedCategories.stream()
                        .anyMatch(sc -> sc.getName().equals(category.getName()));
                    categoryMap.put(category.getName(), isSupported);
                }
                languageCategoryMatrix.put(language.getName(), categoryMap);
            }
            
            // 类型-难度矩阵
            Map<String, Map<String, Boolean>> categoryDifficultyMatrix = new HashMap<>();
            for (GameCategory category : categories) {
                Map<String, Boolean> difficultyMap = new HashMap<>();
                List<GameDifficulty> supportedDifficulties = gameConfigService.getDifficultiesByCategory(category.getName());
                
                for (GameDifficulty difficulty : difficulties) {
                    boolean isSupported = supportedDifficulties.stream()
                        .anyMatch(sd -> sd.getName().equals(difficulty.getName()));
                    difficultyMap.put(difficulty.getName(), isSupported);
                }
                categoryDifficultyMatrix.put(category.getName(), difficultyMap);
            }
            
            matrix.put("modeLanguage", modeLanguageMatrix);
            matrix.put("languageCategory", languageCategoryMatrix);
            matrix.put("categoryDifficulty", categoryDifficultyMatrix);
            
            result.put("success", true);
            result.put("matrix", matrix);
            result.put("summary", Map.of(
                "totalModes", modes.size(),
                "totalLanguages", languages.size(),
                "totalCategories", categories.size(),
                "totalDifficulties", difficulties.size()
            ));
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取关系矩阵失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "获取指定模式的详细关系", description = "获取指定模式支持的所有语言及其后续关系")
    @GetMapping("/mode/{modeName}/details")
    public ResponseEntity<Map<String, Object>> getModeDetails(@PathVariable String modeName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取模式支持的语言
            List<GameLanguage> languages = gameConfigService.getLanguagesByMode(modeName);
            
            Map<String, Object> details = new HashMap<>();
            for (GameLanguage language : languages) {
                Map<String, Object> languageDetail = new HashMap<>();
                languageDetail.put("id", language.getId());
                languageDetail.put("name", language.getName());
                languageDetail.put("displayName", language.getDisplayName());
                languageDetail.put("code", language.getCode());
                
                // 获取该语言支持的类型
                List<GameCategory> categories = gameConfigService.getCategoriesByLanguage(language.getName());
                Map<String, Object> categoryDetails = new HashMap<>();
                
                for (GameCategory category : categories) {
                    Map<String, Object> categoryInfo = new HashMap<>();
                    categoryInfo.put("id", category.getId());
                    categoryInfo.put("name", category.getName());
                    categoryInfo.put("displayName", category.getDisplayName());
                    
                    // 获取该类型支持的难度
                    List<GameDifficulty> difficulties = gameConfigService.getDifficultiesByCategory(category.getName());
                    categoryInfo.put("supportedDifficulties", difficulties);
                    
                    categoryDetails.put(category.getName(), categoryInfo);
                }
                
                languageDetail.put("supportedCategories", categoryDetails);
                details.put(language.getName(), languageDetail);
            }
            
            result.put("success", true);
            result.put("modeName", modeName);
            result.put("details", details);
            result.put("totalLanguages", languages.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取模式详细关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
} 