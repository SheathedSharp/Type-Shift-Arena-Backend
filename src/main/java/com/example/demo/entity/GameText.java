/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:17:09
 */
package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "game_texts")
@Getter
@Setter
public class GameText {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title; // 标题
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 内容
    
    @Column(name = "source_title")
    private String sourceTitle; // 来源标题
    
    @Column(name = "source_author")
    private String sourceAuthor; // 来源作者
    
    
    @Enumerated(EnumType.STRING)
    private TextLanguage language; // 语言
    
    @Enumerated(EnumType.STRING)
    private TextCategory category; // 分类
    
    @Column(nullable = false)
    private String difficulty; // 难度
    
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 创建时间
    
    @Column(name = "is_custom", nullable = false)
    private Boolean isCustom = false;
}