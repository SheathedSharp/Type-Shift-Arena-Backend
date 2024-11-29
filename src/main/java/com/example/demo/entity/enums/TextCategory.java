/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:56:32
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 21:56:55
 */
package com.example.demo.entity.enums;

public enum TextCategory {
    DAILY_CHAT("日常聊天"),
    ACADEMIC_WRITING("学术写作"),
    LATEX_MATH("LaTeX数学"),
    PROGRAMMING("编程解题"),
    LITERATURE("文学欣赏"),
    BUSINESS("商务写作");
    
    private final String description;
    
    TextCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}