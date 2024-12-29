package com.example.demo.service;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.entity.GameText;
import com.example.demo.service.game.GameTextService;
import com.example.demo.repository.GameTextRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {GameTextService.class})
public class GameTextServiceTest {

    @Autowired
    private GameTextService myService;

    @Mock
    private GameTextRepository gameTextRepository;

    @InjectMocks
    private GameTextService mockService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void cleanup() {
        gameTextRepository.deleteAll();
    }

    @Test
    public void shouldInitializeService() {
        assertNotNull(myService);
    }

    @Test
    public void shouldGetRandomTextWithCorrectParameters() {
        String text = myService.getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        assertNotNull(text);
        if (text.equals("The quick brown fox jumps over the lazy dog.")) {
            System.out.println("Default text returned, no matching text found in database.");
        } else {
            System.out.println("Text returned from database: " + text);
        }
    }

    @Test
    public void shouldAddGameTextWithCorrectProperties() {
        String content = "Sample text";
        TextLanguage language = TextLanguage.ENGLISH;
        TextCategory category = TextCategory.DAILY_CHAT;
        String difficulty = "EASY";

        GameText gameText = myService.addGameText(content, language, category, difficulty);
        assertNotNull(gameText);
        assertEquals(content, gameText.getContent());
        assertEquals(language, gameText.getLanguage());
        assertEquals(category, gameText.getCategory());
        assertEquals(difficulty, gameText.getDifficulty());
        assertEquals(content.split("\\s+").length, gameText.getWordCount());
        assertNotNull(gameText.getCreatedAt());
    }

    @Test
    public void shouldThrowExceptionWhenAddingGameTextWithNullContent() {
        assertThrows(IllegalArgumentException.class, () -> {
            myService.addGameText(null, TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        });
    }

    @Test
    public void shouldThrowExceptionWhenGettingRandomTextWithNullParameters() {
        assertThrows(IllegalArgumentException.class, () -> {
            myService.getRandomText(null, null, null);
        });
    }

    @Test
    public void shouldGetRandomTextFromMockRepository() {
        GameText mockGameText = new GameText();
        mockGameText.setContent("Mock text");
        when(gameTextRepository.findRandomText(anyString(), anyString(), anyString())).thenReturn(mockGameText);

        String text = mockService.getRandomText(TextLanguage.ENGLISH, TextCategory.DAILY_CHAT, "EASY");
        assertNotNull(text);
        assertEquals("Mock text", text);
    }
}

