package com.example.demo.controller.game;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.entity.enums.TextCategory;
import com.example.demo.entity.enums.TextLanguage;
import com.example.demo.model.dto.game.GameCategoryDTO;
import com.example.demo.model.dto.game.GameTextMetadataDTO;
import com.example.demo.service.game.GameTextService;
import com.example.demo.util.TestOutputUtil;

@SpringBootTest
@AutoConfigureMockMvc
class GameMetadataControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(GameMetadataControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameTextService gameTextService;

    @Test
    @WithMockUser
    void getAllLanguages_ShouldReturnAllLanguages() throws Exception {
        String testName = "getAllLanguagesTest";
        TestOutputUtil.printTestStart(logger, testName);
        
        when(gameTextService.hasTextsForLanguage(TextLanguage.CHINESE)).thenReturn(true);
        when(gameTextService.hasTextsForLanguage(TextLanguage.ENGLISH)).thenReturn(true);
        when(gameTextService.hasTextsForLanguage(TextLanguage.JAPANESE)).thenReturn(false);
        when(gameTextService.hasTextsForLanguage(TextLanguage.KOREAN)).thenReturn(false);

        TestOutputUtil.printTestInput(logger, "GET", "/api/game/metadata/languages");
        TestOutputUtil.printExpectedOutput(logger, "return all languages and their availability");

        MvcResult result = mockMvc.perform(get("/api/game/metadata/languages")
                .characterEncoding(StandardCharsets.UTF_8.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].code").exists())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].displayName").exists())
            .andExpect(jsonPath("$[0].available").exists())
            .andReturn();

        TestOutputUtil.printActualOutput(logger, result);
        TestOutputUtil.printTestEnd(logger, testName);
    }

        @Test
    @WithMockUser
    void getAllLanguages_ShouldReturnAvailableLanguages() throws Exception {
        String testName = "getAllAvailableLanguagesTest";
        TestOutputUtil.printTestStart(logger, testName);

        // Mock service to return true only for CHINESE and ENGLISH
        when(gameTextService.hasTextsForLanguage(TextLanguage.CHINESE)).thenReturn(true);
        when(gameTextService.hasTextsForLanguage(TextLanguage.ENGLISH)).thenReturn(true);

        TestOutputUtil.printTestInput(logger, "GET", "/api/game/metadata/languages/available");
        TestOutputUtil.printExpectedOutput(logger, "return only languages that have texts in database");

        MvcResult result = mockMvc.perform(get("/api/game/metadata/languages/available")
                .characterEncoding(StandardCharsets.UTF_8.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andReturn();

        TestOutputUtil.printActualOutput(logger, result);
        TestOutputUtil.printTestEnd(logger, testName);
    }

    @Test
    @WithMockUser
    void getCategoriesByLanguage_ShouldReturnAvailableCategories() throws Exception {
        String testName = "getCategoriesByLanguageTest";
        TestOutputUtil.printTestStart(logger, testName);

        List<GameCategoryDTO> mockCategories = Arrays.asList(
            new GameCategoryDTO("LITERATURE", "Literature", true),
            new GameCategoryDTO("DAILY_CHAT", "Daily Chat", true)
        );

        when(gameTextService.getAvailableCategoriesByLanguage(TextLanguage.CHINESE))
            .thenReturn(mockCategories);

        TestOutputUtil.printTestInput(logger, "GET", "/api/game/metadata/categories/CHINESE");
        TestOutputUtil.printExpectedOutput(logger, "return available categories for Chinese language");

        MvcResult result = mockMvc.perform(get("/api/game/metadata/categories/CHINESE")
                .characterEncoding(StandardCharsets.UTF_8.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].code").value("LITERATURE"))
            .andExpect(jsonPath("$[1].code").value("DAILY_CHAT"))
            .andReturn();

        TestOutputUtil.printActualOutput(logger, result);
        TestOutputUtil.printTestEnd(logger, testName);
    }

    @Test
    @WithMockUser
    void getDifficulties_ShouldReturnAvailableDifficulties() throws Exception {
        String testName = "getDifficultiesTest";
        TestOutputUtil.printTestStart(logger, testName);

        List<String> mockDifficulties = Arrays.asList("EASY", "MEDIUM", "HARD");

        when(gameTextService.getAvailableDifficulties(TextLanguage.CHINESE, TextCategory.LITERATURE))
            .thenReturn(mockDifficulties);

        TestOutputUtil.printTestInput(logger, "GET", "/api/game/metadata/difficulties/CHINESE/LITERATURE");
        TestOutputUtil.printExpectedOutput(logger, "return available difficulties for Chinese literature");

        MvcResult result = mockMvc.perform(get("/api/game/metadata/difficulties/CHINESE/LITERATURE")
                .characterEncoding(StandardCharsets.UTF_8.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").value("EASY"))
            .andExpect(jsonPath("$[1]").value("MEDIUM"))
            .andExpect(jsonPath("$[2]").value("HARD"))
            .andReturn();

        TestOutputUtil.printActualOutput(logger, result);
        TestOutputUtil.printTestEnd(logger, testName);
    }

    @Test
    @WithMockUser
    void getAvailableMetadata_ShouldReturnMetadata() throws Exception {
        String testName = "getAvailableMetadataTest";
        TestOutputUtil.printTestStart(logger, testName);

        GameTextMetadataDTO mockMetadata = new GameTextMetadataDTO(
            Arrays.asList("曹雪芹", "罗贯中", "施耐庵", "吴承恩", "蒲松龄", "吴敬梓", "巴金", "老舍", "沈从文", "曹禺"),
            Arrays.asList("红楼梦", "三国演义", "水浒传", "西游记", "聊斋志异", "儒林外史", "家", "骆驼祥子", "边城", "雷雨"),
            10
        );

        when(gameTextService.getAvailableMetadata(
            TextLanguage.CHINESE, TextCategory.LITERATURE, "EASY"))
            .thenReturn(mockMetadata);

        TestOutputUtil.printTestInput(logger, "GET", 
            "/api/game/metadata/available-filters/CHINESE/LITERATURE/EASY");
        TestOutputUtil.printExpectedOutput(logger, 
            "return available authors and source titles for Chinese easy literature");

        MvcResult result = mockMvc.perform(get(
                "/api/game/metadata/available-filters/CHINESE/LITERATURE/EASY")
                .characterEncoding(StandardCharsets.UTF_8.name()))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.authors").isArray())
            .andExpect(jsonPath("$.sourceTitles").isArray())
            .andExpect(jsonPath("$.totalTexts").value(10))
            .andReturn();

        TestOutputUtil.printActualOutput(logger, result);
        TestOutputUtil.printTestEnd(logger, testName);
    }
} 