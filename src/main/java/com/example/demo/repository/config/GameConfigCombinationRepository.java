/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.repository.config;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.config.GameConfigCombination;

@Repository
public interface GameConfigCombinationRepository extends JpaRepository<GameConfigCombination, String> {
    
    // 查找所有激活的配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc WHERE gcc.isActive = true")
    List<GameConfigCombination> findAllActive();
    
    // 根据模式查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameMode.name = :modeName AND gcc.isActive = true")
    List<GameConfigCombination> findByModeName(@Param("modeName") String modeName);
    
    // 根据语言查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameLanguage.name = :languageName AND gcc.isActive = true")
    List<GameConfigCombination> findByLanguageName(@Param("languageName") String languageName);
    
    // 根据类型查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameCategory.name = :categoryName AND gcc.isActive = true")
    List<GameConfigCombination> findByCategoryName(@Param("categoryName") String categoryName);
    
    // 根据难度查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameDifficulty.name = :difficultyName AND gcc.isActive = true")
    List<GameConfigCombination> findByDifficultyName(@Param("difficultyName") String difficultyName);
    
    // 根据模式和语言查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameMode.name = :modeName AND gcc.gameLanguage.name = :languageName " +
           "AND gcc.isActive = true")
    List<GameConfigCombination> findByModeNameAndLanguageName(
            @Param("modeName") String modeName,
            @Param("languageName") String languageName);
    
    // 根据语言和类型查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameLanguage.name = :languageName AND gcc.gameCategory.name = :categoryName " +
           "AND gcc.isActive = true")
    List<GameConfigCombination> findByLanguageNameAndCategoryName(
            @Param("languageName") String languageName,
            @Param("categoryName") String categoryName);
    
    // 根据类型和难度查找配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameCategory.name = :categoryName AND gcc.gameDifficulty.name = :difficultyName " +
           "AND gcc.isActive = true")
    List<GameConfigCombination> findByCategoryNameAndDifficultyName(
            @Param("categoryName") String categoryName,
            @Param("difficultyName") String difficultyName);
    
    // 查找特定配置组合
    @Query("SELECT gcc FROM GameConfigCombination gcc " +
           "WHERE gcc.gameMode.name = :modeName AND gcc.gameLanguage.name = :languageName " +
           "AND gcc.gameCategory.name = :categoryName AND gcc.gameDifficulty.name = :difficultyName " +
           "AND gcc.isActive = true")
    Optional<GameConfigCombination> findByConfiguration(
            @Param("modeName") String modeName,
            @Param("languageName") String languageName,
            @Param("categoryName") String categoryName,
            @Param("difficultyName") String difficultyName);
    
    // 检查配置组合是否存在
    @Query("SELECT COUNT(gcc) > 0 FROM GameConfigCombination gcc " +
           "WHERE gcc.gameMode.name = :modeName AND gcc.gameLanguage.name = :languageName " +
           "AND gcc.gameCategory.name = :categoryName AND gcc.gameDifficulty.name = :difficultyName " +
           "AND gcc.isActive = true")
    boolean existsByConfiguration(
            @Param("modeName") String modeName,
            @Param("languageName") String languageName,
            @Param("categoryName") String categoryName,
            @Param("difficultyName") String difficultyName);
} 