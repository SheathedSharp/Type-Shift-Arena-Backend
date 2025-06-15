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
@Table(name = "game_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameCategory {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name; // DAILY_CHAT, ACADEMIC_WRITING, LATEX_MATH, PROGRAMMING, LITERATURE, BUSINESS

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName; // 日常聊天, 学术写作, LaTeX数学, 编程解题, 文学欣赏, 商务写作

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 多对多关系 - 支持该类型的语言
    @JsonIgnore
    @ManyToMany(mappedBy = "supportedCategories", fetch = FetchType.LAZY)
    private Set<GameLanguage> supportedLanguages;

    // 多对多关系 - 该类型支持的难度
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "game_category_difficulties",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "difficulty_id")
    )
    private Set<GameDifficulty> supportedDifficulties;

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