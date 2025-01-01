/*
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-13 21:56:18
 */
package com.example.demo.entity.enums;

public enum TextLanguage {
    CHINESE("zh", "简体中文"),
    ENGLISH("en", "English"),
    JAPANESE("ja", "日本語"),
    KOREAN("ko", "한국어");
    
    private final String code;
    private final String displayName;
    
    TextLanguage(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // 根据语言代码获取对应的枚举值
    public static TextLanguage fromCode(String code) {
        for (TextLanguage language : TextLanguage.values()) {
            if (language.getCode().equals(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unsupported language code: " + code);
    }
}