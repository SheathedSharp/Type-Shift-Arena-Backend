/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:17:09
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 21:58:09
 */
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;

@Entity
@Table(name = "game_texts")
@Getter
@Setter
public class GameText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    private TextLanguage language;
    
    @Enumerated(EnumType.STRING)
    private TextCategory category;
    
    @Column(nullable = false)
    private String difficulty;
    
    @Column(name = "word_count")
    private Integer wordCount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}