/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 18:26:00
 */

package com.example.demo.service.game;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.repository.GameTextRepository;

@ExtendWith(MockitoExtension.class)
class GameTextServiceTest {

    @Mock
    private GameTextRepository gameTextRepository;

    @InjectMocks
    private GameTextService gameTextService;

    @Test
    void hasTextsForLanguage_WhenTextsExist_ShouldReturnTrue() {
        // Given
        when(gameTextRepository.existsByLanguage(TextLanguage.CHINESE))
            .thenReturn(true);

        // When
        boolean result = gameTextService.hasTextsForLanguage(TextLanguage.CHINESE);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void hasTextsForLanguage_WhenNoTextsExist_ShouldReturnFalse() {
        // Given
        when(gameTextRepository.existsByLanguage(TextLanguage.ENGLISH))
            .thenReturn(false);

        // When
        boolean result = gameTextService.hasTextsForLanguage(TextLanguage.ENGLISH);

        // Then
        assertThat(result).isFalse();
    }
}