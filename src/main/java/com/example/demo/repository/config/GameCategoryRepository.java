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

import com.example.demo.entity.config.GameCategory;

@Repository
public interface GameCategoryRepository extends JpaRepository<GameCategory, String> {
    
    // 根据名称查找类型
    Optional<GameCategory> findByName(String name);
    
    // 查找所有激活的类型，按排序顺序
    @Query("SELECT gc FROM GameCategory gc WHERE gc.isActive = true ORDER BY gc.sortOrder, gc.displayName")
    List<GameCategory> findAllActiveOrderBySortOrder();
    
    // 根据激活状态查找
    List<GameCategory> findByIsActiveOrderBySortOrder(Boolean isActive);
    
    // 检查名称是否存在
    boolean existsByName(String name);
    
    // 查找支持特定语言的类型
    @Query("SELECT DISTINCT gc FROM GameCategory gc " +
           "JOIN gc.supportedLanguages gl " +
           "WHERE gl.name = :languageName AND gc.isActive = true " +
           "ORDER BY gc.sortOrder")
    List<GameCategory> findByLanguageName(@Param("languageName") String languageName);
    
    // 查找支持特定难度的类型
    @Query("SELECT DISTINCT gc FROM GameCategory gc " +
           "JOIN gc.supportedDifficulties gd " +
           "WHERE gd.name = :difficultyName AND gc.isActive = true " +
           "ORDER BY gc.sortOrder")
    List<GameCategory> findByDifficultyName(@Param("difficultyName") String difficultyName);
    
    // 查找同时支持特定语言和难度的类型
    @Query("SELECT DISTINCT gc FROM GameCategory gc " +
           "JOIN gc.supportedLanguages gl " +
           "JOIN gc.supportedDifficulties gd " +
           "WHERE gl.name = :languageName AND gd.name = :difficultyName " +
           "AND gc.isActive = true ORDER BY gc.sortOrder")
    List<GameCategory> findByLanguageNameAndDifficultyName(
            @Param("languageName") String languageName,
            @Param("difficultyName") String difficultyName);
} 