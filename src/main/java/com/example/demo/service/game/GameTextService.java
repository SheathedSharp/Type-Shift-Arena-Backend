/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:18:27
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 22:16:30
 */
package com.example.demo.service.game;

import com.example.demo.entity.GameText;
import com.example.demo.repository.GameTextRepository;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GameTextService {
    @Autowired
    private GameTextRepository gameTextRepository;
    
    // public String getRandomText(String difficulty, String category) {
    //     GameText gameText = gameTextRepository.findRandomText(difficulty, category);
    //     return gameText != null ? gameText.getContent() : "The quick brown fox jumps over the lazy dog.";
    // }
    public String getRandomText(TextLanguage language, TextCategory category, String difficulty) {
        GameText gameText = gameTextRepository.findRandomText(
            language.toString(), 
            category.toString(), 
            difficulty
        );
        return gameText != null ? gameText.getContent() : "The quick brown fox jumps over the lazy dog.";
    }

    // public GameText addGameText(String content, String difficulty, String category) {
    //     GameText gameText = new GameText();
    //     gameText.setContent(content);
    //     gameText.setDifficulty(difficulty);
    //     gameText.setCategory(category);
    //     gameText.setWordCount(content.split("\\s+").length);
    //     return gameTextRepository.save(gameText);
    // }
    public GameText addGameText(String content, TextLanguage language, TextCategory category, String difficulty) {
        GameText gameText = new GameText();
        gameText.setContent(content);
        gameText.setLanguage(language);
        gameText.setCategory(category);
        gameText.setDifficulty(difficulty);
        gameText.setWordCount(content.split("\\s+").length);
        gameText.setCreatedAt(LocalDateTime.now());
        return gameTextRepository.save(gameText);
    }
}