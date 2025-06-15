/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.entity.config;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "game_difficulties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDifficulty {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name; // EASY, MEDIUM, HARD, EXPERT, MASTER

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName; // 简单, 中等, 困难, 专家, 大师

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "level_value", nullable = false)
    private Integer levelValue; // 难度数值，用于排序和比较

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 多对多关系 - 支持该难度的类型
    @JsonIgnore
    @ManyToMany(mappedBy = "supportedDifficulties", fetch = FetchType.LAZY)
    private Set<GameCategory> supportedCategories;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 