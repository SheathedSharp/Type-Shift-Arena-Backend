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
public class GameDifficultyDTO extends GameConfigDTO {
    private Integer levelValue; // 难度数值，用于排序和比较
    private List<String> supportedCategoryNames; // 支持的类型名称列表
} 