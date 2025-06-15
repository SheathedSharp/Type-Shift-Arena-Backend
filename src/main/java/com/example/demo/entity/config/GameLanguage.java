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
@Table(name = "game_languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameLanguage {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code; // zh, en, ja, ko

    @Column(name = "name", nullable = false, length = 50)
    private String name; // CHINESE, ENGLISH, JAPANESE, KOREAN

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName; // 简体中文, English, 日本語, 한국어

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 多对多关系 - 支持该语言的模式
    @JsonIgnore
    @ManyToMany(mappedBy = "supportedLanguages", fetch = FetchType.LAZY)
    private Set<GameMode> supportedModes;

    // 多对多关系 - 该语言支持的类型
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "game_language_categories",
        joinColumns = @JoinColumn(name = "language_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
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