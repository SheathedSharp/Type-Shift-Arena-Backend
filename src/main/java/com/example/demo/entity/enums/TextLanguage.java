/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:56:18
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-13 21:57:46
 */
package com.example.demo.entity.enums;

public enum TextLanguage {
    CHINESE("zh"),
    ENGLISH("en");
    
    private final String code;
    
    TextLanguage(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}