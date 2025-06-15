/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-31 10:00:00
 */
package com.example.demo.model.dto.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameCategoryDTO extends GameConfigDTO {
    private List<String> supportedLanguageNames; // 支持的语言名称列表
    private List<String> supportedDifficultyNames; // 支持的难度名称列表
} 