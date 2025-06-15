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

import com.example.demo.entity.config.GameDifficulty;

@Repository
public interface GameDifficultyRepository extends JpaRepository<GameDifficulty, String> {
    
    // 根据名称查找难度
    Optional<GameDifficulty> findByName(String name);
    
    // 查找所有激活的难度，按难度值排序
    @Query("SELECT gd FROM GameDifficulty gd WHERE gd.isActive = true ORDER BY gd.levelValue, gd.sortOrder")
    List<GameDifficulty> findAllActiveOrderByLevelValue();
    
    // 查找所有激活的难度，按排序顺序
    @Query("SELECT gd FROM GameDifficulty gd WHERE gd.isActive = true ORDER BY gd.sortOrder, gd.displayName")
    List<GameDifficulty> findAllActiveOrderBySortOrder();
    
    // 根据激活状态查找
    List<GameDifficulty> findByIsActiveOrderByLevelValue(Boolean isActive);
    
    // 检查名称是否存在
    boolean existsByName(String name);
    
    // 根据难度值范围查找
    @Query("SELECT gd FROM GameDifficulty gd WHERE gd.isActive = true " +
           "AND gd.levelValue >= :minLevel AND gd.levelValue <= :maxLevel " +
           "ORDER BY gd.levelValue")
    List<GameDifficulty> findByLevelValueRange(@Param("minLevel") Integer minLevel, 
                                               @Param("maxLevel") Integer maxLevel);
    
    // 查找支持特定类型的难度
    @Query("SELECT DISTINCT gd FROM GameDifficulty gd " +
           "JOIN gd.supportedCategories gc " +
           "WHERE gc.name = :categoryName AND gd.isActive = true " +
           "ORDER BY gd.levelValue")
    List<GameDifficulty> findByCategoryName(@Param("categoryName") String categoryName);
    
    // 查找特定难度值的难度
    Optional<GameDifficulty> findByLevelValueAndIsActive(Integer levelValue, Boolean isActive);
} 