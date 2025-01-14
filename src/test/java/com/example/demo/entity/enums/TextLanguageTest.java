/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 18:37:03
 */
package com.example.demo.entity.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TextLanguageTest {

    @ParameterizedTest
    @CsvSource({
        "zh,CHINESE,简体中文",
        "en,ENGLISH,English"
    })
    void shouldHaveCorrectCodeAndDisplayName(String code, TextLanguage language, String displayName) {
        assertThat(language.getCode()).isEqualTo(code);
        assertThat(language.getDisplayName()).isEqualTo(displayName);
    }

    @Test
    void fromCode_WithValidCode_ShouldReturnCorrectLanguage() {
        assertThat(TextLanguage.fromCode("zh")).isEqualTo(TextLanguage.CHINESE);
        assertThat(TextLanguage.fromCode("en")).isEqualTo(TextLanguage.ENGLISH);
    }

    @Test
    void fromCode_WithInvalidCode_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, 
            () -> TextLanguage.fromCode("invalid"));
    }
}