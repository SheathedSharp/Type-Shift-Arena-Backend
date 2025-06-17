/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.controller.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.config.*;
import com.example.demo.service.config.GameConfigService;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
public class GameConfigController {

    @Autowired
    private GameConfigService gameConfigService;

    // =========================== 游戏模式API ===========================
    
    @GetMapping("/modes")
    public ResponseEntity<List<GameMode>> getAllModes(
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive) {
        List<GameMode> modes = includeInactive ? 
            gameConfigService.getAllModes() : 
            gameConfigService.getAllActiveModes();
        return ResponseEntity.ok(modes);
    }
    
    @GetMapping("/modes/{name}")
    public ResponseEntity<GameMode> getModeByName(@PathVariable String name) {
        Optional<GameMode> mode = gameConfigService.getModeByName(name);
        return mode.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/modes")
    public ResponseEntity<GameMode> createMode(@RequestBody GameMode mode) {
        GameMode createdMode = gameConfigService.createMode(mode);
        return ResponseEntity.ok(createdMode);
    }
    
    @PutMapping("/modes/{id}")
    public ResponseEntity<GameMode> updateMode(@PathVariable String id, @RequestBody GameMode mode) {
        mode.setId(id);
        GameMode updatedMode = gameConfigService.updateMode(mode);
        return ResponseEntity.ok(updatedMode);
    }
    
    @DeleteMapping("/modes/{id}")
    public ResponseEntity<Void> deleteMode(@PathVariable String id) {
        gameConfigService.deleteMode(id);
        return ResponseEntity.ok().build();
    }

    // =========================== 游戏语言API ===========================
    
    @GetMapping("/languages")
    public ResponseEntity<List<GameLanguage>> getAllLanguages(
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive) {
        List<GameLanguage> languages = includeInactive ? 
            gameConfigService.getAllLanguages() : 
            gameConfigService.getAllActiveLanguages();
        return ResponseEntity.ok(languages);
    }
    
    @GetMapping("/languages/{name}")
    public ResponseEntity<GameLanguage> getLanguageByName(@PathVariable String name) {
        Optional<GameLanguage> language = gameConfigService.getLanguageByName(name);
        return language.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/languages/code/{code}")
    public ResponseEntity<GameLanguage> getLanguageByCode(@PathVariable String code) {
        Optional<GameLanguage> language = gameConfigService.getLanguageByCode(code);
        return language.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/languages")
    public ResponseEntity<GameLanguage> createLanguage(@RequestBody GameLanguage language) {
        GameLanguage createdLanguage = gameConfigService.createLanguage(language);
        return ResponseEntity.ok(createdLanguage);
    }
    
    @PutMapping("/languages/{id}")
    public ResponseEntity<GameLanguage> updateLanguage(@PathVariable String id, @RequestBody GameLanguage language) {
        language.setId(id);
        GameLanguage updatedLanguage = gameConfigService.updateLanguage(language);
        return ResponseEntity.ok(updatedLanguage);
    }
    
    @DeleteMapping("/languages/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String id) {
        gameConfigService.deleteLanguage(id);
        return ResponseEntity.ok().build();
    }

    // =========================== 游戏类型API ===========================
    
    @GetMapping("/categories")
    public ResponseEntity<List<GameCategory>> getAllCategories(
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive) {
        List<GameCategory> categories = includeInactive ? 
            gameConfigService.getAllCategories() : 
            gameConfigService.getAllActiveCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/{name}")
    public ResponseEntity<GameCategory> getCategoryByName(@PathVariable String name) {
        Optional<GameCategory> category = gameConfigService.getCategoryByName(name);
        return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/categories")
    public ResponseEntity<GameCategory> createCategory(@RequestBody GameCategory category) {
        GameCategory createdCategory = gameConfigService.createCategory(category);
        return ResponseEntity.ok(createdCategory);
    }
    
    @PutMapping("/categories/{id}")
    public ResponseEntity<GameCategory> updateCategory(@PathVariable String id, @RequestBody GameCategory category) {
        category.setId(id);
        GameCategory updatedCategory = gameConfigService.updateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }
    
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        gameConfigService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // =========================== 游戏难度API ===========================
    
    @GetMapping("/difficulties")
    public ResponseEntity<List<GameDifficulty>> getAllDifficulties(
            @RequestParam(value = "includeInactive", defaultValue = "false") boolean includeInactive) {
        List<GameDifficulty> difficulties = includeInactive ? 
            gameConfigService.getAllDifficulties() : 
            gameConfigService.getAllActiveDifficulties();
        return ResponseEntity.ok(difficulties);
    }
    
    @GetMapping("/difficulties/{name}")
    public ResponseEntity<GameDifficulty> getDifficultyByName(@PathVariable String name) {
        Optional<GameDifficulty> difficulty = gameConfigService.getDifficultyByName(name);
        return difficulty.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/difficulties")
    public ResponseEntity<GameDifficulty> createDifficulty(@RequestBody GameDifficulty difficulty) {
        GameDifficulty createdDifficulty = gameConfigService.createDifficulty(difficulty);
        return ResponseEntity.ok(createdDifficulty);
    }
    
    @PutMapping("/difficulties/{id}")
    public ResponseEntity<GameDifficulty> updateDifficulty(@PathVariable String id, @RequestBody GameDifficulty difficulty) {
        difficulty.setId(id);
        GameDifficulty updatedDifficulty = gameConfigService.updateDifficulty(difficulty);
        return ResponseEntity.ok(updatedDifficulty);
    }
    
    @DeleteMapping("/difficulties/{id}")
    public ResponseEntity<Void> deleteDifficulty(@PathVariable String id) {
        gameConfigService.deleteDifficulty(id);
        return ResponseEntity.ok().build();
    }

    // =========================== 配置组合API ===========================
    
    @GetMapping("/combinations")
    public ResponseEntity<List<GameConfigCombination>> getAllCombinations() {
        List<GameConfigCombination> combinations = gameConfigService.getAllActiveCombinations();
        return ResponseEntity.ok(combinations);
    }
    
    @GetMapping("/combinations/validate")
    public ResponseEntity<Map<String, Boolean>> validateConfiguration(
            @RequestParam String mode,
            @RequestParam String language,
            @RequestParam String category,
            @RequestParam String difficulty) {
        boolean isValid = gameConfigService.isConfigurationValid(mode, language, category, difficulty);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    // =========================== 层级查询API ===========================
    
    @GetMapping("/modes/{modeName}/languages")
    public ResponseEntity<List<GameLanguage>> getLanguagesByMode(@PathVariable String modeName) {
        List<GameLanguage> languages = gameConfigService.getLanguagesByMode(modeName);
        return ResponseEntity.ok(languages);
    }
    
    @GetMapping("/languages/{languageName}/categories")
    public ResponseEntity<List<GameCategory>> getCategoriesByLanguage(@PathVariable String languageName) {
        List<GameCategory> categories = gameConfigService.getCategoriesByLanguage(languageName);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/categories/{categoryName}/difficulties")
    public ResponseEntity<List<GameDifficulty>> getDifficultiesByCategory(@PathVariable String categoryName) {
        List<GameDifficulty> difficulties = gameConfigService.getDifficultiesByCategory(categoryName);
        return ResponseEntity.ok(difficulties);
    }
    
    @GetMapping("/combinations/mode/{modeName}/language/{languageName}")
    public ResponseEntity<List<GameConfigCombination>> getCombinationsByModeAndLanguage(
            @PathVariable String modeName, @PathVariable String languageName) {
        List<GameConfigCombination> combinations = gameConfigService.getCombinationsByModeAndLanguage(modeName, languageName);
        return ResponseEntity.ok(combinations);
    }
    
    @GetMapping("/combinations/language/{languageName}/category/{categoryName}")
    public ResponseEntity<List<GameConfigCombination>> getCombinationsByLanguageAndCategory(
            @PathVariable String languageName, @PathVariable String categoryName) {
        List<GameConfigCombination> combinations = gameConfigService.getCombinationsByLanguageAndCategory(languageName, categoryName);
        return ResponseEntity.ok(combinations);
    }
    
    @GetMapping("/combinations/category/{categoryName}/difficulty/{difficultyName}")
    public ResponseEntity<List<GameConfigCombination>> getCombinationsByCategoryAndDifficulty(
            @PathVariable String categoryName, @PathVariable String difficultyName) {
        List<GameConfigCombination> combinations = gameConfigService.getCombinationsByCategoryAndDifficulty(categoryName, difficultyName);
        return ResponseEntity.ok(combinations);
    }
} 