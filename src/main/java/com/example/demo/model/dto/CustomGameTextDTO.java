/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 15:54:52
 */
package com.example.demo.model.dto;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomGameTextDTO {
    private String title;
    private String content;
    private String sourceTitle;
    private String sourceAuthor;
    private TextLanguage language;
    private TextCategory category;
    private String difficulty;
}