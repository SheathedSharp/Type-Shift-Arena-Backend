/*
 * @Author: SheathedSharp z404878860@163.com
 * @Date: 2024-12-26 18:35:12
 */
package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.entity.GameText;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.enums.TextCategory;
import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
class GameTextRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameTextRepository gameTextRepository;

    @Test
    void existsByLanguage_WhenTextsExist_ShouldReturnTrue() {
        // Given
        GameText gameText = new GameText();
        gameText.setLanguage(TextLanguage.CHINESE);
        gameText.setContent("测试文本");
        gameText.setTitle("测试标题");
        gameText.setDifficulty("EASY");
        gameText.setIsCustom(false);
        gameText.setCategory(TextCategory.LITERATURE);
        gameText.setCreatedAt(LocalDateTime.now());
        
        entityManager.persist(gameText);
        entityManager.flush();

        // When
        boolean exists = gameTextRepository.existsByLanguage(TextLanguage.CHINESE);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByLanguage_WhenNoTextsExist_ShouldReturnFalse() {
        // When
        boolean exists = gameTextRepository.existsByLanguage(TextLanguage.ENGLISH);

        // Then
        assertThat(exists).isFalse();
    }
}