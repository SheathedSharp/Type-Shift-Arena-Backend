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
public class GameLanguageDTO extends GameConfigDTO {
    private String code; // 语言代码，如zh, en, ja, ko
    private List<String> supportedModeNames; // 支持的模式名称列表
    private List<String> supportedCategoryNames; // 支持的类型名称列表
} 