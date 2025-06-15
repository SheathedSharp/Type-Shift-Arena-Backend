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
@Table(name = "game_modes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameMode {
    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name; // RANKED, CASUAL, CUSTOM, TUTORIAL, CHALLENGE

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName; // 排位赛, 休闲赛, 自定义, 教程模式, 挑战模式

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

    // 多对多关系 - 该模式支持的语言
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "game_mode_languages",
        joinColumns = @JoinColumn(name = "mode_id"),
        inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<GameLanguage> supportedLanguages;

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