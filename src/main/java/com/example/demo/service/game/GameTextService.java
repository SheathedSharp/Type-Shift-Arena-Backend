/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:18:27
 */
package com.example.demo.service.game;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.GameText;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.dto.CustomGameTextDTO;
import com.example.demo.model.dto.game.GameCategoryDTO;
import com.example.demo.model.dto.game.GameTextMetadataDTO;
import com.example.demo.repository.GameTextRepository;

@Service
public class GameTextService {
    @Autowired
    private GameTextRepository gameTextRepository;
    
    public GameText createCustomText(CustomGameTextDTO request) {
        GameText gameText = new GameText();
        gameText.setTitle(request.getTitle());
        gameText.setContent(request.getContent());
        gameText.setSourceTitle(request.getSourceTitle());
        gameText.setSourceAuthor(request.getSourceAuthor());
        gameText.setLanguage(request.getLanguage());
        gameText.setCategory(request.getCategory());
        gameText.setDifficulty(request.getDifficulty());
        gameText.setCreatedAt(LocalDateTime.now());
        gameText.setIsCustom(true);
        
        return gameTextRepository.save(gameText);
    }


    public GameText addGameText(String content, TextLanguage language, TextCategory category, String difficulty) {
        GameText gameText = new GameText();
        gameText.setContent(content);
        gameText.setLanguage(language);
        gameText.setCategory(category);
        gameText.setDifficulty(difficulty);
        gameText.setCreatedAt(LocalDateTime.now());
        return gameTextRepository.save(gameText);
    }

    // 基础随机文本获取
    public GameText getRandomText(TextLanguage language, TextCategory category, 
                                String difficulty, boolean includeCustom) {
        return gameTextRepository.findRandomText(
            language.toString(), 
            category.toString(), 
            difficulty,
            includeCustom
        );
    }
    
    // 使用动态过滤条件获取随机文本
    public GameText getRandomTextWithFilters(TextLanguage language, 
                                           TextCategory category, 
                                           String difficulty, 
                                           String author, 
                                           String sourceTitle,
                                           boolean includeCustom) {
        return gameTextRepository.findRandomTextWithFilters(
            language.toString(),
            category.toString(),
            difficulty,
            author,
            sourceTitle,
            includeCustom
        );
    }

    public boolean hasTextsForLanguage(TextLanguage language) {
        return gameTextRepository.existsByLanguage(language);
    }

    public boolean hasTextsForCategory(TextCategory category) {
        return gameTextRepository.existsByCategory(category);
    }

    @Cacheable(value = "categories", key = "#language")
    public List<GameCategoryDTO> getAvailableCategoriesByLanguage(TextLanguage language) {
        return Arrays.stream(TextCategory.values())
            .filter(category -> gameTextRepository.existsByLanguageAndCategory(language, category))
            .map(category -> new GameCategoryDTO(
                category.name(),
                category.getDescription(),
                true
            ))
            .collect(Collectors.toList());
    }

    @Cacheable(value = "difficulties", key = "#language + '-' + #category")
    public List<String> getAvailableDifficulties(TextLanguage language, TextCategory category) {
        return gameTextRepository.findDistinctDifficultiesByLanguageAndCategory(language, category);
    }

    @Cacheable(value = "metadata", key = "#language + '-' + #category + '-' + #difficulty")
    public GameTextMetadataDTO getAvailableMetadata(
            TextLanguage language, 
            TextCategory category, 
            String difficulty) {
        return new GameTextMetadataDTO(
            gameTextRepository.findDistinctAuthorsByFilters(language, category, difficulty),
            gameTextRepository.findDistinctSourceTitlesByFilters(language, category, difficulty),
            gameTextRepository.countByFilters(language, category, difficulty)
        );
    }
}