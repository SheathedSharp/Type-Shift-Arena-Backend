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

import com.example.demo.entity.config.GameLanguage;

@Repository
public interface GameLanguageRepository extends JpaRepository<GameLanguage, String> {
    
    // 根据代码查找语言
    Optional<GameLanguage> findByCode(String code);
    
    // 根据名称查找语言
    Optional<GameLanguage> findByName(String name);
    
    // 查找所有激活的语言，按排序顺序
    @Query("SELECT gl FROM GameLanguage gl WHERE gl.isActive = true ORDER BY gl.sortOrder, gl.displayName")
    List<GameLanguage> findAllActiveOrderBySortOrder();
    
    // 根据激活状态查找
    List<GameLanguage> findByIsActiveOrderBySortOrder(Boolean isActive);
    
    // 检查代码是否存在
    boolean existsByCode(String code);
    
    // 检查名称是否存在
    boolean existsByName(String name);
    
    // 查找支持特定模式的语言
    @Query("SELECT DISTINCT gl FROM GameLanguage gl " +
           "JOIN gl.supportedModes gm " +
           "WHERE gm.name = :modeName AND gl.isActive = true " +
           "ORDER BY gl.sortOrder")
    List<GameLanguage> findByModeName(@Param("modeName") String modeName);
    
    // 查找支持特定类型的语言
    @Query("SELECT DISTINCT gl FROM GameLanguage gl " +
           "JOIN gl.supportedCategories gc " +
           "WHERE gc.name = :categoryName AND gl.isActive = true " +
           "ORDER BY gl.sortOrder")
    List<GameLanguage> findByCategoryName(@Param("categoryName") String categoryName);
} 