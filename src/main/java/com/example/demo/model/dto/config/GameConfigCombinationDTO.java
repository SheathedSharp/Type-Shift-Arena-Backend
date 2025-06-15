/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.model.dto.config;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameConfigCombinationDTO {
    private String id;
    private String modeName;
    private String modeDisplayName;
    private String languageName;
    private String languageDisplayName;
    private String languageCode;
    private String categoryName;
    private String categoryDisplayName;
    private String difficultyName;
    private String difficultyDisplayName;
    private Integer difficultyLevel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 便捷方法
    public String getConfigKey() {
        return String.format("%s_%s_%s_%s", modeName, languageName, categoryName, difficultyName);
    }
} 