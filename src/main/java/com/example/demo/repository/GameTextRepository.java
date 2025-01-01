/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:17:58
 */
package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.GameText;
import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;

@Repository
public interface GameTextRepository extends JpaRepository<GameText, Long> {
    boolean existsByLanguage(TextLanguage language);

    boolean existsByCategory(TextCategory category);

    boolean existsByLanguageAndCategory(TextLanguage language, TextCategory category);

    // 基础随机查询（包含/不包含自定义文本）
    @Query(value = "SELECT * FROM game_texts WHERE language = ?1 AND category = ?2 " +
           "AND difficulty = ?3 AND (?4 = true OR is_custom = false) " +
           "ORDER BY RAND() LIMIT 1", 
           nativeQuery = true)
    GameText findRandomText(String language, String category, String difficulty, 
                          boolean includeCustom);
    
    // 动态条件查询
    @Query("SELECT gt FROM GameText gt WHERE " +
           "gt.language = :language AND " +
           "gt.category = :category AND " +
           "gt.difficulty = :difficulty AND " +
           "(:sourceAuthor IS NULL OR gt.sourceAuthor = :sourceAuthor) AND " +
           "(:sourceTitle IS NULL OR gt.sourceTitle = :sourceTitle) AND " +
           "(:includeCustom = true OR gt.isCustom = false) " +
           "ORDER BY RAND()")
    GameText findRandomTextWithFilters(String language, 
                                     String category, 
                                     String difficulty, 
                                     String sourceAuthor, 
                                     String sourceTitle,
                                     boolean includeCustom);


    // 获取选中的类型
    @Query("SELECT DISTINCT gt.category FROM GameText gt " +
           "WHERE gt.language = :language")
    List<String> findDistinctCategoriesByLanguage(TextLanguage language);

    // 获取选中的难度
    @Query("SELECT DISTINCT gt.difficulty FROM GameText gt " +
           "WHERE gt.language = :language AND gt.category = :category")
    List<String> findDistinctDifficultiesByLanguageAndCategory(
            TextLanguage language, TextCategory category);
    
    // 获取选中的作者
    @Query("SELECT DISTINCT gt.sourceAuthor FROM GameText gt " +
           "WHERE gt.language = :language " +
           "AND gt.category = :category " +
           "AND gt.difficulty = :difficulty " +
           "AND gt.sourceAuthor IS NOT NULL")
    List<String> findDistinctAuthorsByFilters(
            TextLanguage language, TextCategory category, String difficulty);

    // 获取选中的来源标题
    @Query("SELECT DISTINCT gt.sourceTitle FROM GameText gt " +
           "WHERE gt.language = :language " +
           "AND gt.category = :category " +
           "AND gt.difficulty = :difficulty " +
           "AND gt.sourceTitle IS NOT NULL")
    List<String> findDistinctSourceTitlesByFilters(
            TextLanguage language, TextCategory category, String difficulty);
            
    // 获取选中的数量
    @Query("SELECT COUNT(gt) FROM GameText gt " +
           "WHERE gt.language = :language " +
           "AND gt.category = :category " +
           "AND gt.difficulty = :difficulty")
    int countByFilters(TextLanguage language, TextCategory category, String difficulty);
}