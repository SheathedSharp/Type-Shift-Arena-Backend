/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:17:58
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 22:07:34
 */
package com.example.demo.repository;

import com.example.demo.entity.GameText;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameTextRepository extends JpaRepository<GameText, Long> {
    @Query(value = "SELECT * FROM game_texts WHERE language = ?1 AND category = ?2 AND difficulty = ?3 ORDER BY RAND() LIMIT 1", nativeQuery = true)
    GameText findRandomText(String language, String category, String difficulty);
    
    List<GameText> findByLanguageAndCategory(TextLanguage language, TextCategory category);
}