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

import com.example.demo.entity.config.GameMode;

@Repository
public interface GameModeRepository extends JpaRepository<GameMode, String> {
    
    // 根据名称查找模式
    Optional<GameMode> findByName(String name);
    
    // 查找所有激活的模式，按排序顺序
    @Query("SELECT gm FROM GameMode gm WHERE gm.isActive = true ORDER BY gm.sortOrder, gm.displayName")
    List<GameMode> findAllActiveOrderBySortOrder();
    
    // 查找所有模式（包括禁用的），按排序顺序
    @Query("SELECT gm FROM GameMode gm ORDER BY gm.sortOrder, gm.displayName")
    List<GameMode> findAllByOrderBySortOrder();
    
    // 根据激活状态查找
    List<GameMode> findByIsActiveOrderBySortOrder(Boolean isActive);
    
    // 检查名称是否存在
    boolean existsByName(String name);
    
    // 查找支持特定语言的模式
    @Query("SELECT DISTINCT gm FROM GameMode gm " +
           "JOIN gm.supportedLanguages gl " +
           "WHERE gl.name = :languageName AND gm.isActive = true " +
           "ORDER BY gm.sortOrder")
    List<GameMode> findByLanguageName(@Param("languageName") String languageName);
} 