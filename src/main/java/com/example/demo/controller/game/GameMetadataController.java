/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-12-26 23:10:45
 */
package com.example.demo.controller.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.dto.game.GameCategoryDTO;
import com.example.demo.model.dto.game.GameLanguageDTO;
import com.example.demo.model.dto.game.GameTextMetadataDTO;
import com.example.demo.service.game.GameTextService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/game/metadata")
@Tag(name = "Game Metadata", description = "APIs for retrieving game text metadata")
public class GameMetadataController {

    @Autowired
    private GameTextService gameTextService;

    @Operation(summary = "Get all supported languages", 
              description = "Returns a list of all supported languages with their availability status")
    @GetMapping("/languages")
    public ResponseEntity<List<GameLanguageDTO>> getAllLanguages() {
        List<GameLanguageDTO> languages = Arrays.stream(TextLanguage.values())
            .map(lang -> new GameLanguageDTO(
                lang.getCode(),
                lang.name(),
                lang.getDisplayName(),
                gameTextService.hasTextsForLanguage(lang)
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(languages);
    }

    @Operation(summary = "Get available languages", 
              description = "Returns a list of languages that have available game texts")
    @GetMapping("/languages/available")
    public ResponseEntity<List<GameLanguageDTO>> getAvailableLanguages() {
        List<GameLanguageDTO> availableLanguages = Arrays.stream(TextLanguage.values())
            .filter(lang -> gameTextService.hasTextsForLanguage(lang))
            .map(lang -> new GameLanguageDTO(
                lang.getCode(),
                lang.name(),
                lang.getDisplayName(),
                true
            ))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(availableLanguages);
    }

    @Operation(summary = "Get available categories by language")
    @GetMapping("/categories/{language}")
    public ResponseEntity<List<GameCategoryDTO>> getCategoriesByLanguage(
            @PathVariable TextLanguage language) {
        return ResponseEntity.ok(gameTextService.getAvailableCategoriesByLanguage(language));
    }

    @Operation(summary = "Get available difficulties by language and category")
    @GetMapping("/difficulties/{language}/{category}")
    public ResponseEntity<List<String>> getDifficulties(
            @PathVariable TextLanguage language,
            @PathVariable TextCategory category) {
        return ResponseEntity.ok(gameTextService.getAvailableDifficulties(language, category));
    }

    @Operation(summary = "Get available metadata for texts")
    @GetMapping("/available-filters/{language}/{category}/{difficulty}")
    public ResponseEntity<GameTextMetadataDTO> getAvailableMetadata(
            @PathVariable TextLanguage language,
            @PathVariable TextCategory category,
            @PathVariable String difficulty) {
        return ResponseEntity.ok(gameTextService.getAvailableMetadata(language, category, difficulty));
    }

    // @Operation(summary = "Get all authors")
    // @GetMapping("/authors")
    // public ResponseEntity<List<String>> getAuthors() {
    //     return ResponseEntity.ok(gameTextService.getAllAuthors());
    // }

    // @Operation(summary = "Get all source titles")
    // @GetMapping("/sources")
    // public ResponseEntity<List<String>> getSourceTitles() {
    //     return ResponseEntity.ok(gameTextService.getAllSourceTitles());
    // }
} 