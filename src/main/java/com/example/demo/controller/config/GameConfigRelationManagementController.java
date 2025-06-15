/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 12:30:00
 */
package com.example.demo.controller.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.config.GameConfigRelationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/game-config/relation-management")
@Tag(name = "游戏配置关系管理", description = "管理各种配置项之间的多对多关系")
public class GameConfigRelationManagementController {

    @Autowired
    private GameConfigRelationService relationService;

    // =========================== 模式-语言关系管理 ===========================

    @Operation(summary = "添加模式-语言关系", description = "为指定模式添加支持的语言")
    @PostMapping("/mode-language")
    public ResponseEntity<Map<String, Object>> addModeLanguageRelation(
            @RequestParam String modeName,
            @RequestParam String languageName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.addModeLanguageRelation(modeName, languageName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为模式 %s 添加语言 %s", modeName, languageName));
            } else {
                result.put("success", false);
                result.put("message", "关系已存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "删除模式-语言关系", description = "删除指定模式与语言的关系")
    @DeleteMapping("/mode-language")
    public ResponseEntity<Map<String, Object>> removeModeLanguageRelation(
            @RequestParam String modeName,
            @RequestParam String languageName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.removeModeLanguageRelation(modeName, languageName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功删除模式 %s 与语言 %s 的关系", modeName, languageName));
            } else {
                result.put("success", false);
                result.put("message", "关系不存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "批量设置模式支持的语言", description = "为指定模式批量设置支持的语言列表")
    @PutMapping("/mode/{modeName}/languages")
    public ResponseEntity<Map<String, Object>> setModeLanguages(
            @PathVariable String modeName,
            @RequestBody List<String> languageNames) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.setModeLanguages(modeName, languageNames);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为模式 %s 设置 %d 个支持的语言", modeName, languageNames.size()));
                result.put("languageCount", languageNames.size());
            } else {
                result.put("success", false);
                result.put("message", "设置失败，模式不存在或部分语言不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量设置关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // =========================== 语言-类型关系管理 ===========================

    @Operation(summary = "添加语言-类型关系", description = "为指定语言添加支持的类型")
    @PostMapping("/language-category")
    public ResponseEntity<Map<String, Object>> addLanguageCategoryRelation(
            @RequestParam String languageName,
            @RequestParam String categoryName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.addLanguageCategoryRelation(languageName, categoryName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为语言 %s 添加类型 %s", languageName, categoryName));
            } else {
                result.put("success", false);
                result.put("message", "关系已存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "删除语言-类型关系", description = "删除指定语言与类型的关系")
    @DeleteMapping("/language-category")
    public ResponseEntity<Map<String, Object>> removeLanguageCategoryRelation(
            @RequestParam String languageName,
            @RequestParam String categoryName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.removeLanguageCategoryRelation(languageName, categoryName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功删除语言 %s 与类型 %s 的关系", languageName, categoryName));
            } else {
                result.put("success", false);
                result.put("message", "关系不存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "批量设置语言支持的类型", description = "为指定语言批量设置支持的类型列表")
    @PutMapping("/language/{languageName}/categories")
    public ResponseEntity<Map<String, Object>> setLanguageCategories(
            @PathVariable String languageName,
            @RequestBody List<String> categoryNames) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.setLanguageCategories(languageName, categoryNames);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为语言 %s 设置 %d 个支持的类型", languageName, categoryNames.size()));
                result.put("categoryCount", categoryNames.size());
            } else {
                result.put("success", false);
                result.put("message", "设置失败，语言不存在或部分类型不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量设置关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // =========================== 类型-难度关系管理 ===========================

    @Operation(summary = "添加类型-难度关系", description = "为指定类型添加支持的难度")
    @PostMapping("/category-difficulty")
    public ResponseEntity<Map<String, Object>> addCategoryDifficultyRelation(
            @RequestParam String categoryName,
            @RequestParam String difficultyName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.addCategoryDifficultyRelation(categoryName, difficultyName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为类型 %s 添加难度 %s", categoryName, difficultyName));
            } else {
                result.put("success", false);
                result.put("message", "关系已存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "删除类型-难度关系", description = "删除指定类型与难度的关系")
    @DeleteMapping("/category-difficulty")
    public ResponseEntity<Map<String, Object>> removeCategoryDifficultyRelation(
            @RequestParam String categoryName,
            @RequestParam String difficultyName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.removeCategoryDifficultyRelation(categoryName, difficultyName);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功删除类型 %s 与难度 %s 的关系", categoryName, difficultyName));
            } else {
                result.put("success", false);
                result.put("message", "关系不存在或配置项不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "批量设置类型支持的难度", description = "为指定类型批量设置支持的难度列表")
    @PutMapping("/category/{categoryName}/difficulties")
    public ResponseEntity<Map<String, Object>> setCategoryDifficulties(
            @PathVariable String categoryName,
            @RequestBody List<String> difficultyNames) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = relationService.setCategoryDifficulties(categoryName, difficultyNames);
            
            if (success) {
                result.put("success", true);
                result.put("message", String.format("成功为类型 %s 设置 %d 个支持的难度", categoryName, difficultyNames.size()));
                result.put("difficultyCount", difficultyNames.size());
            } else {
                result.put("success", false);
                result.put("message", "设置失败，类型不存在或部分难度不存在");
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批量设置关系失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // =========================== 关系重建和同步 ===========================

    @Operation(summary = "重建配置组合表", description = "根据当前关系重新生成所有有效的配置组合")
    @PostMapping("/rebuild-combinations")
    public ResponseEntity<Map<String, Object>> rebuildCombinations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int count = relationService.rebuildConfigCombinations();
            
            result.put("success", true);
            result.put("message", "配置组合表重建成功");
            result.put("generatedCombinations", count);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "重建配置组合表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @Operation(summary = "清空缓存并重新预热", description = "清空所有相关缓存并重新预热")
    @PostMapping("/refresh-cache")
    public ResponseEntity<Map<String, Object>> refreshCache() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            relationService.refreshAllCache();
            
            result.put("success", true);
            result.put("message", "缓存刷新成功");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "刷新缓存失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
} 